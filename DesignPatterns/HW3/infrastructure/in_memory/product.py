from dataclasses import dataclass, field
from uuid import UUID

from core.errors import DoesNotExistError
from core.product import Product


@dataclass
class ProductInMemory:
    products: dict[UUID, Product] = field(default_factory=dict)

    def create(self, product: Product) -> None:
        self.products[product.id] = product

    def read(self, product_id: UUID) -> Product:
        try:
            return self.products[product_id]
        except KeyError:
            raise DoesNotExistError()

    def read_all(self) -> list[Product]:
        return list(self.products.values())

    def update(self, product_id: UUID, price: float) -> None:
        self.products[product_id].price = price
