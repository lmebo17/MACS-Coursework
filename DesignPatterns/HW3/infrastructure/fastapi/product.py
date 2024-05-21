from typing import Any
from uuid import UUID

from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from starlette.responses import JSONResponse

from core.errors import DoesNotExistError, ExistsError
from core.product import Product, ProductService
from infrastructure.fastapi.dependables import ProductRepositoryDependable

product_api = APIRouter(tags=["Products"])


class CreateProductRequest(BaseModel):
    unit_id: UUID
    name: str
    barcode: str
    price: float


class UpdateProductRequest(BaseModel):
    price: float


class ProductItem(BaseModel):
    id: UUID
    unit_id: UUID
    name: str
    barcode: str
    price: float


class ProductItemEnvelope(BaseModel):
    product: ProductItem


class ProductListEnvelope(BaseModel):
    products: list[ProductItem]


@product_api.post(
    "/products",
    status_code=201,
    response_model=ProductItemEnvelope,
)
def create_product(
    request: CreateProductRequest, products: ProductRepositoryDependable
) -> dict[str, Any] | JSONResponse:
    product = Product(**request.model_dump())
    try:
        products.create(product)
        return {"product": product}
    except ExistsError as e:
        raise HTTPException(status_code=409, detail={"error": {"message": str(e)}})


@product_api.get(
    "/products/{product_id}",
    status_code=200,
    response_model=ProductItemEnvelope,
)
def read_product(
    product_id: UUID, products: ProductRepositoryDependable
) -> dict[str, Any] | JSONResponse:
    try:
        return {"product": products.read(product_id)}
    except DoesNotExistError:
        return JSONResponse(
            status_code=404,
            content={
                "error": {"message": f"Product with id<{product_id}> does not exist."}
            },
        )


@product_api.get("/products", response_model=ProductListEnvelope)
def read_all_products(
    products: ProductRepositoryDependable,
) -> dict[str, Any] | JSONResponse:
    return {"products": products.read_all()}


class NoneProduct(BaseModel):
    pass


@product_api.patch(
    "/products/{product_id}",
    status_code=200,
    response_model=NoneProduct,
)
def update_product(
    product_id: UUID,
    request: UpdateProductRequest,
    products: ProductRepositoryDependable,
) -> dict[str, Any] | JSONResponse:
    try:
        ProductService(products).update(product_id, **request.model_dump())
        return {}
    except DoesNotExistError as e:
        raise HTTPException(status_code=404, detail={"error": {"message": str(e)}})
