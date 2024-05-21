from __future__ import annotations

from dataclasses import dataclass, field
from typing import Protocol
from uuid import UUID, uuid4


@dataclass
class ProductService:
    products: ProductRepository

    def create(self, product: Product) -> None:
        self.products.create(product)

    def read(self, product_id: UUID) -> Product:
        return self.products.read(product_id)

    def read_all(self) -> list[Product]:
        return self.products.read_all()

    def update(self, product_id: UUID, price: float) -> None:
        self.products.update(product_id, price)


class ProductRepository(Protocol):
    def create(self, product: Product) -> None:
        pass

    def read(self, product_id: UUID) -> Product:
        pass

    def read_all(self) -> list[Product]:
        pass

    def update(self, product_id: UUID, price: float) -> None:
        pass


@dataclass
class Product:
    unit_id: UUID
    name: str
    barcode: str
    price: float
    id: UUID = field(default_factory=uuid4)
