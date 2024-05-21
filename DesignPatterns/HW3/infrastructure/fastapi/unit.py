from typing import Any
from uuid import UUID

from fastapi import APIRouter
from pydantic import BaseModel
from starlette.responses import JSONResponse

from core.errors import DoesNotExistError
from core.unit import Unit
from infrastructure.fastapi.dependables import UnitRepositoryDependable

unit_api = APIRouter(tags=["Units"])


class CreateUnitRequest(BaseModel):
    name: str


class UnitItem(BaseModel):
    id: UUID
    name: str


class UnitItemEnvelope(BaseModel):
    unit: UnitItem


class UnitListEnvelope(BaseModel):
    units: list[UnitItem]


@unit_api.post(
    "/units",
    status_code=201,
    response_model=UnitItemEnvelope,
)
def create_unit(
    request: CreateUnitRequest, units: UnitRepositoryDependable
) -> dict[str, Any]:
    unit = Unit(**request.model_dump())
    units.create(unit)

    return {"unit": unit}


@unit_api.get(
    "/units/{unit_id}",
    status_code=200,
    response_model=UnitItemEnvelope,
)
def get_one(
    unit_id: UUID, units: UnitRepositoryDependable
) -> dict[str, Any] | JSONResponse:
    try:
        return {"unit": units.read(unit_id)}
    except DoesNotExistError:
        return JSONResponse(
            status_code=404,
            content={"error": {"message": f"Unit with id<{unit_id}> does not exist."}},
        )


@unit_api.get("/units", response_model=UnitListEnvelope)
def fetch_all_units(units: UnitRepositoryDependable) -> dict[str, Any]:
    return {"units": units.get_all()}
