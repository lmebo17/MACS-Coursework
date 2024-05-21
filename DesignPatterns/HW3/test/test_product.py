import os
from dataclasses import dataclass, field
from typing import Any, Dict
from unittest.mock import ANY
from uuid import uuid4

import pytest
from faker import Faker
from fastapi.testclient import TestClient

from infrastructure.sqlite.product_database import ProductInDatabase
from runner.setup import init_app


@dataclass
class ProductFake:
    faker: Faker = field(default_factory=Faker)

    def product(self) -> Dict[str, Any]:
        return {
            "unit_id": str(uuid4()),
            "name": self.faker.word(),
            "barcode": self.faker.uuid4(),
            "price": self.faker.random_number(),
        }

    def price(self) -> Dict[str, Any]:
        return {"price": self.faker.random_int()}


@pytest.fixture
def client() -> TestClient:
    return TestClient(init_app())


def clear_tables() -> None:
    if os.getenv("REPOSITORY_KIND", "memory") == "sqlite":
        ProductInDatabase().clear_tables()


def test_should_not_read_unknown_product(client: TestClient) -> None:
    clear_tables()
    unknown_id = uuid4()

    response = client.get(f"/products/{unknown_id}")

    assert response.status_code == 404
    assert response.json() == {
        "error": {"message": f"Product with id<{unknown_id}> does not exist."}
    }


def test_should_create_product(client: TestClient) -> None:
    clear_tables()
    product = ProductFake().product()

    response = client.post("/products", json=product)

    assert response.status_code == 201
    assert response.json() == {"product": {"id": ANY, **product}}


def test_should_persist_product(client: TestClient) -> None:
    clear_tables()
    product = ProductFake().product()

    response_create = client.post("/products", json=product)
    assert response_create.status_code == 201
    product_id = response_create.json()["product"]["id"]

    response_get = client.get(f"/products/{product_id}")

    assert response_get.status_code == 200
    assert response_get.json() == {"product": {"id": product_id, **product}}


def test_get_all_products_on_empty(client: TestClient) -> None:
    clear_tables()
    response = client.get("/products")

    assert response.status_code == 200
    assert response.json() == {"products": []}


def test_get_all_products(client: TestClient) -> None:
    clear_tables()
    product = ProductFake().product()

    response_create = client.post("/products", json=product)
    product_id = response_create.json()["product"]["id"]

    response_get = client.get("/products")

    assert response_get.status_code == 200
    assert response_get.json() == {"products": [{"id": product_id, **product}]}


def test_update_product(client: TestClient) -> None:
    clear_tables()
    product = ProductFake().product()
    price = ProductFake().price()

    response_create = client.post("/products", json=product)
    assert response_create.status_code == 201
    product_id = response_create.json()["product"]["id"]

    client.patch(f"/products/{product_id}", json=price)

    response_get = client.get(f"/products/{product_id}")

    assert response_get.status_code == 200
    assert response_get.json()["product"]["id"] == product_id
    assert response_get.json()["product"]["unit_id"] == product["unit_id"]
    assert response_get.json()["product"]["name"] == product["name"]
    assert response_get.json()["product"]["barcode"] == product["barcode"]
    assert response_get.json()["product"]["price"] == price["price"]
