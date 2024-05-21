from src import discounter
from src.discounter import BaseDiscounter, CustomerDiscounter, ProductDiscounter


def test_product_discounter() -> None:
    base_discounter = discounter.setup_product_discounter(
        BaseDiscounter(), 10, 0.1, ProductDiscounter
    )
    assert base_discounter.get_price() == 9


def test_prime_customer_discounter() -> None:
    base_discounter = discounter.setup_customer_discounter(
        BaseDiscounter(), 10, 0.5, 17, CustomerDiscounter
    )
    assert base_discounter.get_price() == 5


def test_non_prime_customer_discounter() -> None:
    base_discounter = discounter.setup_customer_discounter(
        BaseDiscounter(), 10, 0.5, 4, CustomerDiscounter
    )
    assert base_discounter.get_price() == 10


def test_both() -> None:
    base_discounter = discounter.setup_product_discounter(
        BaseDiscounter(), 10, 0.1, ProductDiscounter
    )
    base_discounter = discounter.setup_customer_discounter(
        base_discounter, 10, 0.5, 7, CustomerDiscounter
    )
    assert base_discounter.get_price() == 4.5
