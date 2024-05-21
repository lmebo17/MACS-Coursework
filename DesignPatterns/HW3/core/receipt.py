from dataclasses import dataclass, field
from typing import Protocol
from uuid import UUID, uuid4


@dataclass
class Receipt:
    status: str
    products: dict[UUID, int]
    total_price: float
    id: UUID = field(default_factory=uuid4)


class ReceiptRepository(Protocol):
    def delete(self, receipt_id: UUID) -> None:
        pass

    def change_status(self, receipt: Receipt, status: str) -> None:
        pass

    def create_receipt(self, receipt: Receipt) -> None:
        pass

    def add_product(
        self, receipt_id: UUID, product_id: UUID, quantity: int, price: float
    ) -> None:
        pass

    def read(self, receipt_id: UUID) -> Receipt:
        pass

    def read_all(self) -> list[Receipt]:
        pass


@dataclass
class ReceiptService:
    receipts: ReceiptRepository

    def delete(self, receipt_id: UUID) -> None:
        self.receipts.delete(receipt_id)

    def change_status(self, receipt: Receipt, status: str) -> None:
        self.receipts.change_status(receipt, status)

    def create_receipt(self, receipt: Receipt) -> None:
        self.receipts.create_receipt(receipt)

    def add_product(
        self, receipt_id: UUID, product_id: UUID, quantity: int, price: float
    ) -> None:
        self.receipts.add_product(receipt_id, product_id, quantity, price)

    def read(self, receipt_id: UUID) -> Receipt:
        return self.receipts.read(receipt_id)

    def read_all(self) -> list[Receipt]:
        return self.receipts.read_all()
