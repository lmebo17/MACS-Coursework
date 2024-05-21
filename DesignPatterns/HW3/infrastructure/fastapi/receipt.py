from typing import Any
from uuid import UUID

from fastapi import APIRouter
from fastapi.responses import JSONResponse
from pydantic import BaseModel

from core.errors import DoesNotExistError
from core.receipt import Receipt, ReceiptService
from infrastructure.fastapi.dependables import ReceiptRepositoryDependable

receipt_api = APIRouter(tags=["Receipts"])


class ProductAddRequest(BaseModel):
    id: UUID
    quantity: int
    price: float


class UpdateStatusRequest(BaseModel):
    status: str


class ReceiptItem(BaseModel):
    status: str
    products: dict[UUID, int]
    total_price: float
    id: UUID


class ReceiptItemEnvelope(BaseModel):
    receipt: ReceiptItem


class ReceiptListEnvelope(BaseModel):
    receipts: list[ReceiptItem]


class NoneReceipt(BaseModel):
    pass


@receipt_api.post(
    "/receipts",
    status_code=201,
    response_model=ReceiptItemEnvelope,
)
def create_receipt(receipts: ReceiptRepositoryDependable) -> dict[str, Any]:
    receipt = Receipt(status="open", products={}, total_price=0.0)
    ReceiptService(receipts).create_receipt(receipt)
    return {"receipt": receipt}


@receipt_api.post(
    "/receipts/{receipt_id}",
    status_code=201,
    response_model=NoneReceipt,
)
def add_product_to_receipt(
    receipt_id: UUID,
    request: ProductAddRequest,
    receipts: ReceiptRepositoryDependable,
) -> dict[str, Any] | JSONResponse:
    try:
        ReceiptService(receipts).add_product(
            receipt_id, request.id, request.quantity, request.price
        )
        return {}
    except DoesNotExistError:
        return JSONResponse(
            status_code=404,
            content={
                "error": {"message": f"Receipt with id<{receipt_id}> does not exist."}
            },
        )


@receipt_api.get(
    "/receipts/{receipt_id}",
    status_code=200,
    response_model=ReceiptItemEnvelope,
)
def read_receipt(
    receipt_id: UUID, receipts: ReceiptRepositoryDependable
) -> dict[str, Any] | JSONResponse:
    try:
        receipt = ReceiptService(receipts).read(receipt_id)
        return {"receipt": receipt}
    except DoesNotExistError:
        return JSONResponse(
            status_code=404,
            content={
                "error": {"message": f"Receipt with id<{receipt_id}> does not exist."}
            },
        )


@receipt_api.patch(
    "/receipts/{receipt_id}",
    status_code=200,
)
def close_receipt(
    receipt_id: UUID,
    request: UpdateStatusRequest,
    receipts: ReceiptRepositoryDependable,
) -> JSONResponse:
    status = request.status
    try:
        receipt = ReceiptService(receipts).read(receipt_id)
        ReceiptService(receipts).change_status(receipt, status)

        return JSONResponse(
            status_code=200,
            content={"message": f"Receipt with id<{receipt_id}> updated successfully."},
        )
    except DoesNotExistError:
        return JSONResponse(
            status_code=404,
            content={
                "error": {"message": f"Receipt with id<{receipt_id}> does not exist."}
            },
        )


@receipt_api.delete(
    "/receipts/{receipt_id}",
    status_code=200,
)
def delete_receipt(
    receipt_id: UUID, receipts: ReceiptRepositoryDependable
) -> JSONResponse:
    try:
        receipt = ReceiptService(receipts).read(receipt_id)
        if receipt.status == "closed":
            return JSONResponse(
                status_code=403,
                content={"message": f"Receipt with id<{receipt_id}> is closed."},
            )
        ReceiptService(receipts).delete(receipt_id)

        return JSONResponse(
            status_code=200,
            content={"message": f"Receipt with id<{receipt_id}> deleted successfully."},
        )
    except DoesNotExistError:
        return JSONResponse(
            status_code=404,
            content={
                "error": {"message": f"Receipt with id<{receipt_id}> does not exist."}
            },
        )
