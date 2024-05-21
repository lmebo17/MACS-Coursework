from src.product import OneBundle, Product


def test_name() -> None:
    product: Product = Product("Cola")
    assert product.get_name() == "Cola"


def test_count() -> None:
    product: Product = Product("Cola")
    assert product.get_count() == 1


def test_bundle_name() -> None:
    product: OneBundle = OneBundle("5 Cola", 5)
    assert product.get_name() == "5 Cola"


def test_bundle_quantity() -> None:
    product: OneBundle = OneBundle("5 Cola", 5)
    assert product.get_count() == 5
