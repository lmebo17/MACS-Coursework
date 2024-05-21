from typing import List

from constants import (
    life_time_payment_table_name,
    life_time_sold_table_name,
    shift_payment_table_name,
    shift_sold_table_name,
)
from src.cashier import ICashier
from src.creation_factory import CreationFactory, ICreationFactory
from src.manager import IManager
from src.product import IProduct
from src.receipt import IReceipt, ReceiptBuilder
from src.store import IStore

X_REPORT_COUNT: int = 20
Z_REPORT_COUNT: int = 100


class Simulation:
    manager: IManager
    cashier: ICashier
    factory: ICreationFactory
    store: IStore

    def __init__(self, store: IStore) -> None:
        self.factory = CreationFactory()
        self.manager = self.factory.create_manager()
        self.store = store
        self.store.reset_data()

    def shift_simulation(self) -> None:
        all_products = self.store.get_products_list()
        self.cashier = self.factory.create_cashier()

        end_of_shift = False
        while not end_of_shift:
            self.store.serving()
            self.serve_one_customer(all_products)
            end_of_shift = self.do_report()

    def serve_one_customer(self, all_products: List[IProduct]) -> None:
        customer = self.factory.create_customer(all_products=all_products)
        customer.choose_products()
        chosen_products = customer.get_chosen_products()

        self.cashier.create_receipt(
            receipt=ReceiptBuilder(chosen_products, store=self.store)
        )

        receipt = self.cashier.get_receipt().create()

        self.cashier.give_receipt(receipt, self.store)

        payment_strategy = customer.pay()

        self.cache_data(receipt, payment_strategy)

        self.cashier.reset_receipt()

    def cache_data(self, receipt: IReceipt, payment_strategy: str) -> None:
        for product in receipt.get_products():
            self.store.add_sold_item(product, shift_sold_table_name)
            self.store.add_sold_item(product, life_time_sold_table_name)

        total_price = receipt.calculate_total_price()
        self.store.add_payment(payment_strategy, total_price, shift_payment_table_name)
        self.store.add_payment(
            payment_strategy, total_price, life_time_payment_table_name
        )

    def do_report(self) -> bool:
        number_of_customers = self.store.customers_served()
        if (
            number_of_customers % X_REPORT_COUNT == 0
            and self.manager.answer_X_question()
        ):
            self.manager.make_report(
                self.store.get_sold_items(shift_sold_table_name),
                self.store.get_payment_statistic(shift_payment_table_name),
            )
        if (
            number_of_customers % Z_REPORT_COUNT == 0
            and self.manager.answer_Z_question()
        ):
            self.cashier.make_Z_report(self.store)
            return True

        return False
