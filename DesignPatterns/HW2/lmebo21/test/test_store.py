from constants import shift_payment_table_name, shift_sold_table_name
from src.product import Product
from src.store import Store


def test_available_products() -> None:
    store = Store()
    products = [
        ("Milk", 4.99),
        ("Bread", 2.5),
        ("Cheese", 6.99),
        ("Coffee", 8.99),
        ("Apples", 2.0),
        ("3 liters Milk", 0.0),
        ("2 loaves Bread", 0.0),
        ("Coffee Bundle", 0.0),
        ("Cheese Special", 0.0),
        ("Apple Bulk", 0.0),
    ]
    assert store.get_available_products() == products


def test_available_discounts() -> None:
    store = Store()
    discounts = [
        ("3 liters Milk", "Milk", 3, 0.3),
        ("2 loaves Bread", "Bread", 2, 0.15),
        ("Coffee Bundle", "Coffee", 5, 0.15),
        ("Cheese Special", "Cheese", 2, 0.2),
        ("Apple Bulk", "Apples", 10, 0.3),
    ]

    assert store.get_available_discounts() == discounts


def test_customers() -> None:
    store = Store()
    store.serving()
    store.serving()
    assert store.customers_served() == 2


def test_products() -> None:
    store = Store()

    assert store.get_price("Milk") == 4.99
    assert store.get_single_product_name("3 liters Milk") == "Milk"
    assert store.get_quantity("3 liters Milk") == 3
    assert store.get_discount("3 liters Milk") == 0.3


def test_sold_items() -> None:
    store = Store()
    store.reset_data()
    store.add_sold_item(Product("Milk"), shift_sold_table_name)
    store.add_sold_item(Product("Milk"), shift_sold_table_name)
    store.add_sold_item(Product("Cola"), shift_sold_table_name)
    assert store.get_sold_items(shift_sold_table_name) == [("Milk", 2), ("Cola", 1)]
    store.reset_data()


def test_payments() -> None:
    store = Store()
    store.reset_data()
    store.add_payment("Card", 10, shift_payment_table_name)
    store.add_payment("Card", 15, shift_payment_table_name)
    store.add_payment("Cash", 10, shift_payment_table_name)
    store.add_payment("Card", 12, shift_payment_table_name)
    store.add_payment("Cash", 3, shift_payment_table_name)

    assert store.get_payment_statistic(shift_payment_table_name) == [
        ("Card", 37),
        ("Cash", 13),
    ]
