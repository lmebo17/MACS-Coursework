from typing import List

from src.product import IProduct, OneBundle, Product
from src.receipt import Receipt
from src.store import Store


def test_receipt() -> None:
    products: List[IProduct] = [
        Product("Apple"),
        Product("Orange"),
        OneBundle("Chocolates", 6),
        OneBundle("Coffee", 3),
    ]

    receipt: Receipt = Receipt(products, Store())
    assert products == receipt.get_products()


def test_receipt_total_price() -> None:
    products: List[IProduct] = [
        Product("Milk"),
        Product("Bread"),
    ]
    receipt: Receipt = Receipt(products, Store())
    assert receipt.calculate_total_price() == 2.5 + 4.99


def test_discounted_price() -> None:
    products: List[IProduct] = [
        OneBundle("3 liters Milk", 3),
        OneBundle("2 loaves Bread", 2),
    ]
    receipt: Receipt = Receipt(products, Store())
    assert receipt.calculate_total_price() == 4.99 * 3 * 0.7 + 2.5 * 2 * 0.85


def test_receipt_discounted_total_price() -> None:
    products: List[IProduct] = [
        Product("Milk"),
        Product("Bread"),
        Product("Cheese"),
        Product("Coffee"),
        Product("Apples"),
        OneBundle("3 liters Milk", 3),
        OneBundle("2 loaves Bread", 2),
        OneBundle("Coffee Bundle", 5),
        OneBundle("Cheese Special", 2),
        OneBundle("Apple Bulk", 10),
    ]

    receipt: Receipt = Receipt(products, Store())
    expected_price = 4.99 + 2.5 + 6.99 + 8.99 + 2
    expected_price += 3 * 4.99 * 0.7
    expected_price += 2 * 2.5 * 0.85
    expected_price += 5 * 8.99 * 0.85
    expected_price += 2 * 6.99 * 0.8
    expected_price += 10 * 2 * 0.7

    assert receipt.calculate_total_price() == expected_price
