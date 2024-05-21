from typing import List, Protocol

from constants import shift_payment_table_name, shift_sold_table_name
from src.product import IProduct, OneBundle, Product
from src.store_database import IProductDatabase, ProductDatabase


class Store:
    database: IProductDatabase
    customers: int

    def __init__(self) -> None:
        self.database = ProductDatabase()
        self.customers = 0

    def get_products_list(self) -> List[IProduct]:
        products_list: List[IProduct] = []
        all_products = self.get_available_products()
        for product_name, price_of_single in all_products:
            if price_of_single == 0:
                quantity = self.get_quantity(product_name)
                products_list.append(OneBundle(product_name, quantity))
            else:
                products_list.append(Product(product_name))

        return products_list

    def get_available_products(self) -> List[tuple[str, int]]:
        all_products = self.database.fetch_all_products()
        return all_products

    def get_available_discounts(self) -> List[tuple[str, str, int, float]]:
        all_discounts = self.database.fetch_all_discounts()
        return all_discounts

    def get_price(self, product_name: str) -> float:
        return self.database.get_price(product_name)

    def get_discount(self, product_name: str) -> float:
        return self.database.get_discount(product_name)

    def get_single_product_name(self, product_name: str) -> str:
        return self.database.get_single_product_name(product_name)

    def get_quantity(self, product_name: str) -> int:
        return self.database.get_quantity(product_name)

    def customers_served(self) -> int:
        return self.customers

    def serving(self) -> None:
        self.customers += 1

    def calculate_revenue(self) -> float:
        money = self.database.get_total_money()
        return money

    def reset_data(self) -> None:
        self.database.reset_table(shift_payment_table_name)
        self.database.reset_table(shift_sold_table_name)
        self.customers = 0

    def add_sold_item(self, product: IProduct, table_name: str) -> None:
        self.database.add_sold_item(product.get_name(), table_name)

    def get_sold_items(self, table_name: str) -> List[tuple[str, int]]:
        return self.database.get_sold_items(table_name)

    def add_payment(self, payment: str, money: float, table_name: str) -> None:
        self.database.add_payment(payment, money, table_name)

    def get_payment_statistic(self, table_name: str) -> List[tuple[str, float]]:
        return self.database.get_payments(table_name)


class IStore(Protocol):
    def get_products_list(self) -> List[IProduct]:
        pass

    def get_available_products(self) -> List[tuple[str, int]]:
        pass

    def get_available_discounts(self) -> List[tuple[str, str, int, float]]:
        pass

    def customers_served(self) -> int:
        pass

    def get_price(self, product_name: str) -> float:
        pass

    def get_discount(self, product_name: str) -> float:
        pass

    def serving(self) -> None:
        pass

    def get_single_product_name(self, product_name: str) -> str:
        pass

    def get_quantity(self, product_name: str) -> int:
        pass

    def calculate_revenue(self) -> float:
        pass

    def reset_data(self) -> None:
        pass

    def add_sold_item(self, product: IProduct, table_name: str) -> None:
        pass

    def add_payment(self, payment: str, money: float, table_name: str) -> None:
        pass

    def get_sold_items(self, table_name: str) -> List[tuple[str, int]]:
        pass

    def get_payment_statistic(self, table_name: str) -> List[tuple[str, float]]:
        pass
