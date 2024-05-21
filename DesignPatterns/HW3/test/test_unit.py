import os
from dataclasses import dataclass, field
from typing import Any
from unittest.mock import ANY
from uuid import uuid4

import pytest
from faker import Faker
from fastapi.testclient import TestClient

from infrastructure.sqlite.unit_database import UnitInDatabase
from runner.setup import init_app


@dataclass
class UnitFake:
    faker: Faker = field(default_factory=Faker)

    def unit(self) -> dict[str, Any]:
        return {
            "name": self.faker.word(),
        }


@pytest.fixture
def client() -> TestClient:
    return TestClient(init_app())


def clear_tables() -> None:
    if os.getenv("REPOSITORY_KIND", "memory") == "sqlite":
        UnitInDatabase().clear_tables()


def test_should_not_read_unknown_unit(client: TestClient) -> None:
    clear_tables()
    unknown_id = uuid4()

    response = client.get(f"/units/{unknown_id}")

    assert response.status_code == 404
    assert response.json() == {
        "error": {"message": f"Unit with id<{unknown_id}> does not exist."}
    }


def test_should_create_unit(client: TestClient) -> None:
    clear_tables()
    unit = UnitFake().unit()

    response = client.post("/units", json=unit)

    assert response.status_code == 201
    assert response.json() == {"unit": {"id": ANY, **unit}}


def test_should_persist_unit(client: TestClient) -> None:
    clear_tables()
    unit = UnitFake().unit()

    response = client.post("/units", json=unit)
    unit_id = response.json()["unit"]["id"]

    response = client.get(f"/units/{unit_id}")

    assert response.status_code == 200
    assert response.json() == {"unit": {"id": unit_id, **unit}}


def test_get_all_units_on_empty(client: TestClient) -> None:
    clear_tables()
    response = client.get("/units")

    assert response.status_code == 200
    assert response.json() == {"units": []}


def test_get_all_units(client: TestClient) -> None:
    clear_tables()
    unit = UnitFake().unit()

    response = client.post("/units", json=unit)
    unit_id = response.json()["unit"]["id"]

    response = client.get("/units")

    assert response.status_code == 200
    assert response.json() == {"units": [{"id": unit_id, **unit}]}
