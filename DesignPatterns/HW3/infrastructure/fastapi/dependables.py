from typing import Annotated

from fastapi import Depends
from fastapi.requests import Request

from core.product import ProductRepository
from core.receipt import ReceiptRepository
from core.unit import UnitRepository


def get_unit_repository(request: Request) -> UnitRepository:
    return request.app.state.unit  # type: ignore


def get_product_repository(request: Request) -> ProductRepository:
    return request.app.state.product  # type: ignore


def get_receipt_repository(request: Request) -> ReceiptRepository:
    return request.app.state.receipt  # type: ignore


UnitRepositoryDependable = Annotated[UnitRepository, Depends(get_unit_repository)]

ProductRepositoryDependable = Annotated[
    ProductRepository, Depends(get_product_repository)
]

ReceiptRepositoryDependable = Annotated[
    ReceiptRepository, Depends(get_receipt_repository)
]
