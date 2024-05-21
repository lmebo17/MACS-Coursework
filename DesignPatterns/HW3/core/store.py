from dataclasses import dataclass
from typing import Any, List

from core.receipt import Receipt, ReceiptRepository


@dataclass
class StoreService:
    receipts: ReceiptRepository

    def sales_report(self) -> dict[Any, Any]:
        generated_receipts: List[Receipt] = self.receipts.read_all()
        closed_receipts = [
            receipt for receipt in generated_receipts if receipt.status == "closed"
        ]
        n_receipts, cash = (
            len(closed_receipts),
            sum(receipt.total_price for receipt in closed_receipts),
        )

        return {"n_receipts": n_receipts, "revenue": cash}
