import os

from fastapi import FastAPI

from infrastructure.fastapi.product import product_api
from infrastructure.fastapi.receipt import receipt_api
from infrastructure.fastapi.store import sales_api
from infrastructure.fastapi.unit import unit_api
from infrastructure.in_memory.product import ProductInMemory
from infrastructure.in_memory.receipt import ReceiptInMemory
from infrastructure.in_memory.unit import UnitInMemory
from infrastructure.sqlite.product_database import ProductInDatabase
from infrastructure.sqlite.receipt_database import ReceiptInDatabase
from infrastructure.sqlite.unit_database import UnitInDatabase


def init_app() -> FastAPI:
    app = FastAPI()
    app.include_router(product_api)
    app.include_router(unit_api)
    app.include_router(receipt_api)
    app.include_router(sales_api)
    os.environ["REPOSITORY_KIND"] = "sqlite"

    if os.getenv("REPOSITORY_KIND", "memory") == "sqlite":
        app.state.product = ProductInDatabase()
        app.state.unit = UnitInDatabase()
        app.state.receipt = ReceiptInDatabase()
    else:
        app.state.product = ProductInMemory()
        app.state.unit = UnitInMemory()
        app.state.receipt = ReceiptInMemory()

    return app
