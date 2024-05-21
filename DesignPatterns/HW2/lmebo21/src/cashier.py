from typing import Protocol

from src.logger import Logger
from src.product import IProduct
from src.receipt import IReceipt, IReceiptBuilder
from src.store import IStore


class ICashier(Protocol):
    def create_receipt(self, receipt: IReceiptBuilder) -> None:
        pass

    def add_product(self, product: IProduct) -> None:
        pass

    def get_receipt(self) -> IReceiptBuilder:
        pass

    def reset_receipt(self) -> None:
        pass

    def give_receipt(self, receipt: IReceipt, store: IStore) -> None:
        pass

    def make_Z_report(self, store: IStore) -> None:
        pass


class Cashier:
    receipt: IReceiptBuilder

    def create_receipt(self, receipt: IReceiptBuilder) -> None:
        self.receipt = receipt

    def add_product(self, product: IProduct) -> None:
        self.receipt.add_product(product)

    def get_receipt(self) -> IReceiptBuilder:
        return self.receipt

    def reset_receipt(self) -> None:
        self.receipt.reset()

    def give_receipt(self, receipt: IReceipt, store: IStore) -> None:
        Logger.print_receipt(receipt, store)

    def make_Z_report(self, store: IStore) -> None:
        store.reset_data()
