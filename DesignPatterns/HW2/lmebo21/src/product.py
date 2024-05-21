from abc import ABC, abstractmethod
from typing import Protocol


class IProduct(Protocol):
    def get_name(self) -> str:
        pass

    def get_count(self) -> int:
        pass


class Product:
    def __init__(self, name: str) -> None:
        self._name = name

    def get_name(self) -> str:
        return self._name

    def get_count(self) -> int:
        return 1


class Bundle(ABC):
    def __init__(self, name: str, quantity: int) -> None:
        self.name = name
        self.quantity = quantity
        self.products = []
        for _ in range(self.quantity):
            product = self.make_product()
            self.products.append(product)

    def get_name(self) -> str:
        return self.name

    def get_count(self) -> int:
        return self.quantity

    @abstractmethod
    def make_product(self) -> Product:
        pass


class OneBundle(Bundle):
    def make_product(self) -> Product:
        return Product(self.get_name())
