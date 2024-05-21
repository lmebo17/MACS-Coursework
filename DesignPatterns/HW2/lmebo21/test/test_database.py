# # Note: For simplicity in this example,
# I am using the same database as the production database.
# # In a real-world scenario, it is recommended
# to use a separate test database for testing purposes.
from constants import shift_payment_table_name, shift_sold_table_name
from src.store_database import ProductDatabase


def test_all_products() -> None:
    database = ProductDatabase()
    all_products = database.fetch_all_products()
    assert len(all_products) == 10


def test_all_discounts() -> None:
    database = ProductDatabase()
    all_products = database.fetch_all_discounts()
    assert len(all_products) == 5


def test_price() -> None:
    database = ProductDatabase()
    price = database.get_price("Milk")
    assert price == 4.99

    price = database.get_price("3 liters Milk")
    assert price == 0


def test_quantity() -> None:
    database = ProductDatabase()
    quantity = database.get_quantity("3 liters Milk")
    assert quantity == 3

    quantity = database.get_quantity("2 loaves Bread")
    assert quantity == 2

    quantity = database.get_quantity("4 Khinkali")
    assert quantity == 0


def test_single_product_name() -> None:
    database = ProductDatabase()
    name = database.get_single_product_name("3 liters Milk")
    assert name == "Milk"

    name = database.get_single_product_name("4 Khinkali")
    assert name == ""


def test_discount() -> None:
    database = ProductDatabase()
    discount = database.get_discount("3 liters Milk")
    assert discount == 0.3
    discount = database.get_discount("2 liters Milk")
    assert discount == 0
    discount = database.get_discount("4 Khinkali")
    assert discount == 0


def test_sold_items() -> None:
    database = ProductDatabase()
    database.reset_table(shift_sold_table_name)
    database.add_sold_item("Milk", shift_sold_table_name)
    database.add_sold_item("Milk", shift_sold_table_name)
    database.add_sold_item("Milk", shift_sold_table_name)
    sold_items = database.get_sold_items(shift_sold_table_name)
    assert sold_items[0][0] == "Milk"
    assert sold_items[0][1] == 3
    database.reset_table(shift_sold_table_name)


def test_payment_statistic() -> None:
    database = ProductDatabase()
    database.reset_table(shift_payment_table_name)
    database.add_payment("Cash", 10, shift_payment_table_name)
    database.add_payment("Card", 25, shift_payment_table_name)
    database.add_payment("Cash", 3, shift_payment_table_name)
    database.add_payment("Card", 7, shift_payment_table_name)
    assert database.get_money("Cash") == 13
    assert database.get_money("Card") == 32
    assert database.get_total_money() == 45
    database.reset_table(shift_payment_table_name)
