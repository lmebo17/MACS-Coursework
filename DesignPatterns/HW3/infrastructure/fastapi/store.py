from typing import Any

from fastapi import APIRouter
from pydantic import BaseModel

from core.store import StoreService
from infrastructure.fastapi.dependables import ReceiptRepositoryDependable

sales_api = APIRouter(tags=["Sales"])


class SalesItem(BaseModel):
    n_receipts: int
    revenue: float


class SalesItemEnvelope(BaseModel):
    sales: SalesItem


@sales_api.get("/sales", status_code=200, response_model=SalesItemEnvelope)
def sales(receipts: ReceiptRepositoryDependable) -> dict[Any, dict[Any, Any]]:
    return {"sales": StoreService(receipts).sales_report()}
