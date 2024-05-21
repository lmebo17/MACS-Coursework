import random
from typing import List, Protocol

from constants import MAX_NUMBER_OF_PRODUCTS
from src.payment import IPaymentStrategy, RandomPaymentStrategy
from src.product import IProduct
from src.products_chooser import IProductsChooserStrategy, RandomProductsGenerator


class ICustomer(Protocol):
    def choose_products(self) -> None:
        pass

    def get_chosen_products(self) -> List[IProduct]:
        pass

    def pay(self) -> str:
        pass


# strategy pattern.
class Customer:
    products_chooser: IProductsChooserStrategy
    payment_chooser: IPaymentStrategy
    payment_strategies: List[str]
    chosen_products: List[IProduct]
    all_products: List[IProduct]

    def __init__(
        self,
        payment_strategies: List[str],
        all_products: List[IProduct],
        products_chooser: IProductsChooserStrategy = RandomProductsGenerator(),
        payment_chooser: IPaymentStrategy = RandomPaymentStrategy(),
    ):
        self.products_chooser = products_chooser
        self.payment_chooser = payment_chooser
        self.payment_strategies = payment_strategies
        self.all_products = all_products

        self.chosen_products = []

    def choose_products(self) -> None:
        number_of_products = len(self.all_products)

        if self.products_chooser == RandomProductsGenerator():
            number_of_products = random.randint(1, MAX_NUMBER_OF_PRODUCTS)
        for _ in range(number_of_products):
            product = self.products_chooser.choose_product(self.all_products)
            self.chosen_products.append(product)

    def get_chosen_products(self) -> List[IProduct]:
        return self.chosen_products.copy()

    def pay(self) -> str:
        payment_strategy = self.payment_chooser.choose_payment_strategy(
            self.payment_strategies
        )
        print("Customer paid with " + payment_strategy)
        return payment_strategy
