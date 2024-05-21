from typing import List

from src.cashier import Cashier
from src.product import IProduct, OneBundle, Product
from src.receipt import ReceiptBuilder
from src.store import Store


def test_cashier_using_receipt() -> None:
    cashier = Cashier()
    store = Store()
    products: List[IProduct] = [
        Product("Peach"),
        Product("Apple"),
        OneBundle("Paper", 10),
    ]
    receipt = ReceiptBuilder(products, store)
    cashier.create_receipt(ReceiptBuilder(products, store))
    assert cashier.get_receipt().create().get_products() == products

    receipt.add_product(Product("Cola"))
    cashier.add_product(Product("Cola"))
    assert cashier.get_receipt().create().get_products() == products


def test_cashier_reset_receipt() -> None:
    cashier = Cashier()
    store = Store()
    products: List[IProduct] = [Product("Peach")]
    cashier.create_receipt(ReceiptBuilder(products, store))
    cashier.reset_receipt()
    assert cashier.get_receipt().create().get_products() == []
