import os
from dataclasses import dataclass, field
from typing import Any, Dict
from unittest.mock import ANY
from uuid import UUID, uuid4

import pytest
from faker import Faker
from fastapi.testclient import TestClient

from infrastructure.sqlite.receipt_database import ReceiptInDatabase
from runner.setup import init_app


@pytest.fixture
def client() -> TestClient:
    return TestClient(init_app())


@dataclass
class ReceiptFake:
    faker: Faker = field(default_factory=Faker)

    @staticmethod
    def receipt() -> Dict[str, Any]:
        return {
            "status": "open",
            "products": {},
            "total_price": 0.0,
        }

    @staticmethod
    def get_status() -> Dict[str, Any]:
        return {"status": "closed"}

    @staticmethod
    def get_product(product_id: UUID) -> Dict[str, Any]:
        return {"id": str(product_id), "quantity": 1, "price": 1.0}

    def product(self) -> Dict[str, Any]:
        return {
            "unit_id": str(uuid4()),
            "name": self.faker.word(),
            "barcode": self.faker.uuid4(),
            "price": self.faker.random_number(),
        }


def clear_tables() -> None:
    if os.getenv("REPOSITORY_KIND", "memory") == "sqlite":
        ReceiptInDatabase().clear_table("receipts")
        ReceiptInDatabase().clear_table("products_in_receipts")


def test_should_not_read_unknown_product(client: TestClient) -> None:
    clear_tables()
    unknown_id = uuid4()

    response = client.get(f"/receipts/{unknown_id}")

    assert response.status_code == 404
    assert response.json() == {
        "error": {"message": f"Receipt with id<{unknown_id}> does not exist."}
    }


def test_create_receipt(client: TestClient) -> None:
    clear_tables()
    receipt = ReceiptFake().receipt()

    response = client.post("/receipts")

    assert response.status_code == 201

    assert response.json() == {"receipt": {"id": ANY, **receipt}}


def test_should_persist_receipt(client: TestClient) -> None:
    clear_tables()
    receipt = ReceiptFake().receipt()

    response_create = client.post("/receipts", json=receipt)
    assert response_create.status_code == 201
    receipt_id = response_create.json()["receipt"]["id"]

    response_get = client.get(f"/receipts/{receipt_id}")

    assert response_get.status_code == 200
    assert response_get.json() == {"receipt": {"id": receipt_id, **receipt}}


def test_update_receipt(client: TestClient) -> None:
    clear_tables()
    receipt = ReceiptFake().receipt()
    status = ReceiptFake().get_status()

    response = client.post("/receipts", json=receipt)
    receipt_id = response.json()["receipt"]["id"]

    client.patch(f"/receipts/{receipt_id}", json=status)

    response = client.get(f"/receipts/{receipt_id}")

    assert response.status_code == 200
    assert response.json()["receipt"]["status"] == status["status"]
    assert response.json()["receipt"]["id"] == receipt_id


def test_should_delete_receipt(client: TestClient) -> None:
    clear_tables()
    receipt = ReceiptFake().receipt()

    response = client.post("/receipts", json=receipt)
    receipt_id = response.json()["receipt"]["id"]

    response = client.delete(f"/receipts/{receipt_id}")

    assert response.status_code == 200

    response = client.get(f"/receipts/{receipt_id}")

    assert response.status_code == 404
    assert response.json() == {
        "error": {"message": f"Receipt with id<{receipt_id}> does not exist."}
    }


def test_should_not_delete_closed_receipt(client: TestClient) -> None:
    clear_tables()
    receipt = ReceiptFake().receipt()
    status = ReceiptFake().get_status()

    response = client.post("/receipts", json=receipt)
    receipt_id = response.json()["receipt"]["id"]

    client.patch(f"/receipts/{receipt_id}", json=status)

    response = client.delete(f"/receipts/{receipt_id}")

    assert response.status_code == 403

    response = client.get(f"/receipts/{receipt_id}")
    assert response.status_code == 200


def test_should_not_delete_unknown(client: TestClient) -> None:
    clear_tables()
    unknown_id = uuid4()

    response = client.delete(f"/receipts/{unknown_id}")

    assert response.status_code == 404
    assert response.json() == {
        "error": {"message": f"Receipt with id<{unknown_id}> does not exist."}
    }


def test_add_product(client: TestClient) -> None:
    clear_tables()
    receipt = ReceiptFake().receipt()

    response = client.post("/receipts", json=receipt)
    receipt_id = response.json()["receipt"]["id"]

    product = ReceiptFake().product()

    response_create = client.post("/products", json=product)
    assert response_create.status_code == 201

    product_id = response_create.json()["product"]["id"]

    product_request = ReceiptFake().get_product(UUID(product_id))

    client.post(f"/receipts/{receipt_id}", json=product_request)

    response = client.get(f"/receipts/{receipt_id}")

    assert response.status_code == 200

    assert len(response.json()["receipt"]["products"]) != 0
