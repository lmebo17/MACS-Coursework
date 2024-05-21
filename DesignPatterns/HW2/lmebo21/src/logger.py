from typing import List

from constants import life_time_payment_table_name, life_time_sold_table_name
from src.receipt import IReceipt
from src.store import IStore


class Logger:
    @classmethod
    def print_life_time_sold_items(cls, store: IStore) -> None:
        cls.print_sold_items(store.get_sold_items(life_time_sold_table_name))

    @classmethod
    def print_life_time_revenue(cls, store: IStore) -> None:
        cls.print_revenue(store.get_payment_statistic(life_time_payment_table_name))

    @classmethod
    def print_catalogue(cls, store: IStore) -> None:
        all_items = store.get_products_list()
        header = "{:<15}".format("Products")
        divider = "-" * len(header)

        print(header)
        print(divider)

        for product in all_items:
            row = "{:<15}".format(product.get_name())
            print(row)

    @classmethod
    def print_discounts(cls, store: IStore) -> None:
        all_discounts = store.get_available_discounts()
        header = "{:<15} | {:<6} | {:<6}".format("Product", "Quantity", "Discount")
        divider = "-" * len(header)

        print(header)
        print(divider)

        for product in all_discounts:
            row = "{:<15} | {:<6} | {:<6}".format(product[0], product[2], product[3])
            print(row)

    @classmethod
    def print_sold_items(cls, sold_items: List[tuple[str, int]]) -> None:
        header = "{:<15} | {:<6}".format("Product", "Sales")
        divider = "-" * len(header)

        print(header)
        print(divider)

        for product_name, sales in sold_items:
            row = "{:<15} | {:<6}".format(product_name, sales)
            print(row)

    @classmethod
    def print_revenue(cls, revenue: List[tuple[str, float]]) -> None:
        header = "{:<10} | {:<8}".format("Payment", "Revenue")
        divider = "-" * len(header)

        print(header)
        print(divider)

        for payment, amount in revenue:
            row = "{:<10} | {:<8.2f}".format(payment, amount)
            print(row)

    @classmethod
    def print_receipt(cls, receipt: IReceipt, store: IStore) -> None:
        print("------------RECEIPT------------------")
        header = "{:<20} | {:<6} | {:<8} | {:<9}".format(
            "Product", "Units", "Price", "Total"
        )
        divider = "-" * len(header)

        print(header)
        print(divider)

        for product in receipt.get_products():
            product_name = product.get_name()
            quantity = product.get_count()
            single_product_name = product_name
            if quantity > 1:
                single_product_name = store.get_single_product_name(product_name)
            initial_price = store.get_price(single_product_name)

            discount = store.get_discount(product_name)
            price = receipt.calculate_single_product_price(
                product_name, quantity, discount
            )
            row = "{:<20} | {:<6} | {:<8.2f} | {:<9.2f}".format(
                product.get_name(), quantity, initial_price, price
            )
            print(row)

        print("------------RECEIPT END------------------")
