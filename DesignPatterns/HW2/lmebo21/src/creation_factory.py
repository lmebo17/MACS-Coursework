from typing import List, Protocol

from src.cashier import Cashier, ICashier
from src.customer import Customer, ICustomer
from src.manager import IManager, Manager
from src.product import IProduct


class ICreationFactory(Protocol):
    def create_manager(self) -> IManager:
        pass

    def create_cashier(self) -> ICashier:
        pass

    def create_customer(self, all_products: List[IProduct]) -> ICustomer:
        pass


class CreationFactory:
    def create_manager(self) -> IManager:
        return Manager()

    def create_cashier(self) -> ICashier:
        return Cashier()

    def create_customer(self, all_products: List[IProduct]) -> ICustomer:
        payment_strategies: List[str] = ["Cash", "Card"]
        return Customer(
            payment_strategies=payment_strategies, all_products=all_products
        )
