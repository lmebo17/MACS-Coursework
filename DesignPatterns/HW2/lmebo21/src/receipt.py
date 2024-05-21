from typing import Iterator, List, Protocol

from constants import CUSTOMER_DISCOUNT
from src import discounter
from src.discounter import BaseDiscounter, CustomerDiscounter, ProductDiscounter
from src.product import IProduct
from src.store import IStore


class IReceipt(Protocol):
    def calculate_single_product_price(
        self, product_name: str, quantity: int, discount: float
    ) -> float:
        pass

    def calculate_total_price(self) -> float:
        pass

    def get_products(self) -> List[IProduct]:
        pass

    def __iter__(self) -> Iterator[IProduct]:
        pass


class IReceiptBuilder(Protocol):
    def add_product(self, product: IProduct) -> None:
        pass

    def create(self) -> IReceipt:
        pass

    def reset(self) -> None:
        pass


class ReceiptBuilder(IReceiptBuilder):
    def __init__(self, products: List[IProduct], store: IStore):
        self.products = products
        self.store = store

    def add_product(self, product: IProduct) -> None:
        self.products.append(product)

    def create(self) -> IReceipt:
        return Receipt(self.products, self.store)

    def reset(self) -> None:
        self.products = []


class Receipt(IReceipt):
    def __init__(self, products: List[IProduct], store: IStore):
        self.products = products
        self.store = store

    def calculate_total_price(self) -> float:
        total_price = 0.0
        for product in self.products:
            product_name = product.get_name()
            quantity = product.get_count()
            discount = self.store.get_discount(product_name)
            total_price += self.calculate_single_product_price(
                product_name, quantity, discount
            )
        return total_price

    def calculate_single_product_price(
        self, product_name: str, quantity: int, discount: float
    ) -> float:
        if not discount:
            return self.store.get_price(product_name)

        single_product_name = self.store.get_single_product_name(product_name)
        single_product_price = self.store.get_price(single_product_name)
        total_price = single_product_price * quantity
        initial_discount = self.store.get_discount(product_name)
        return self.get_discounted_price(total_price, initial_discount)

    def get_discounted_price(self, price: float, initial_discount: float) -> float:
        base_discounter = discounter.setup_product_discounter(
            BaseDiscounter(), price, initial_discount, ProductDiscounter
        )
        base_discounter = discounter.setup_customer_discounter(
            base_discounter,
            price,
            CUSTOMER_DISCOUNT,
            self.store.customers_served(),
            CustomerDiscounter,
        )

        return base_discounter.get_price()

    def get_products(self) -> List[IProduct]:
        return self.products.copy()

    def __iter__(self) -> Iterator[IProduct]:
        return iter(self.products)
