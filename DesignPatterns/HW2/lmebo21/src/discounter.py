from dataclasses import dataclass
from typing import Any


@dataclass
class BaseDiscounter:
    price: float = 0

    def get_price(self) -> float:
        return self.price

    def set_price(self, price: float) -> None:
        self.price = price


@dataclass
class DiscountDecorator:
    creature: BaseDiscounter

    def get_price(self) -> float:
        return self.creature.get_price()

    def set_price(self, price: float) -> None:
        self.creature.set_price(price)


@dataclass
class CustomerDiscounter(DiscountDecorator):
    id: int
    modifier: float

    def get_price(self) -> float:
        if is_prime(self.id):
            return self.creature.get_price() * (1 - self.modifier)
        return self.creature.get_price()


@dataclass
class ProductDiscounter(DiscountDecorator):
    modifier: float

    def get_price(self) -> float:
        return self.creature.get_price() * (1 - self.modifier)


def is_prime(number: int) -> bool:
    if number < 2:
        return False
    for i in range(2, int(number**0.5) + 1):
        if number % i == 0:
            return False
    return True


def setup_customer_discounter(
    discounter: BaseDiscounter,
    price: float,
    discount_modifier: float,
    customer_id: int,
    DecoratorClass: Any,
) -> BaseDiscounter:
    discounter.set_price(price)
    discounter = DecoratorClass(
        DiscountDecorator(discounter), customer_id, discount_modifier
    )
    return discounter


def setup_product_discounter(
    discounter: BaseDiscounter,
    price: float,
    discounter_modifier: float,
    DecoratorClass: Any,
) -> BaseDiscounter:
    discounter.set_price(price)
    discounter = DecoratorClass(DiscountDecorator(discounter), discounter_modifier)
    return discounter
