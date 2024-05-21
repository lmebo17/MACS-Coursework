from typing import List

from src.creation_factory import CreationFactory
from src.customer import Customer
from src.product import IProduct, OneBundle, Product
from src.products_chooser import AllProductsGenerator


def test_payment() -> None:
    factory = CreationFactory()
    payment_strategies = ["Card", "Cash"]
    customer = factory.create_customer([])
    assert payment_strategies.count(customer.pay())


def test_choose_products() -> None:
    payment_strategies = ["Card", "Cash"]
    all_products: List[IProduct] = [
        Product("Apple"),
        Product("Cola"),
        OneBundle("Paper", 10),
    ]

    customer = Customer(
        payment_strategies=payment_strategies,
        all_products=all_products,
        products_chooser=AllProductsGenerator(),
    )

    assert customer.get_chosen_products() == []

    customer.choose_products()
    print(customer.get_chosen_products())
    chosen_product_names = [
        product.get_name() for product in customer.get_chosen_products()
    ]
    all_product_names = [product.get_name() for product in all_products]

    assert chosen_product_names == all_product_names
