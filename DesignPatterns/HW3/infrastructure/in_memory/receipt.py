from dataclasses import dataclass, field
from uuid import UUID

from core.errors import DoesNotExistError
from core.receipt import Receipt


@dataclass
class ReceiptInMemory:
    receipts: dict[UUID, Receipt] = field(default_factory=dict)

    def delete(self, receipt_id: UUID) -> None:
        self.receipts.pop(receipt_id)

    def change_status(self, receipt: Receipt, status: str) -> None:
        self.receipts[receipt.id].status = status

    def create_receipt(self, receipt: Receipt) -> None:
        self.receipts[receipt.id] = receipt

    def add_product(
        self, receipt_id: UUID, product_id: UUID, quantity: int, price: float
    ) -> None:
        self.receipts[receipt_id].products[product_id] = quantity
        self.receipts[receipt_id].total_price += quantity * price

    def read(self, receipt_id: UUID) -> Receipt:
        try:
            return self.receipts[receipt_id]
        except KeyError:
            raise DoesNotExistError(receipt_id)

    def read_all(self) -> list[Receipt]:
        return list(self.receipts.values())
