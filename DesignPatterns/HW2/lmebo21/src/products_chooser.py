import random
from dataclasses import dataclass
from typing import Iterator, List, Protocol

from src.product import IProduct


class IProductsChooserStrategy(Protocol):
    def choose_product(self, all_products: List[IProduct]) -> IProduct:
        pass


@dataclass
class RandomProductsGenerator:
    def choose_product(self, all_products: List[IProduct]) -> IProduct:
        return random.choice(all_products)


class AllProductsGenerator:
    def __init__(self) -> None:
        self.flag: bool = False
        self.real_iterator: Iterator[IProduct] = iter([])

    def choose_product(self, all_products: List[IProduct]) -> IProduct:
        if not self.flag:
            self.real_iterator = iter(all_products)

        try:
            self.flag = True
            return next(self.real_iterator)
        except StopIteration:
            self.flag = False
            self.real_iterator = iter(all_products)
            return all_products[0]
