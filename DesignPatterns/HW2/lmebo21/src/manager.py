from typing import List, Protocol

from src.logger import Logger


class IManager(Protocol):
    def make_report(
        self, sold_items: List[tuple[str, int]], revenue: List[tuple[str, float]]
    ) -> None:
        pass

    def answer_Z_question(self) -> bool:
        pass

    def answer_X_question(self) -> bool:
        pass


class Manager:
    def make_report(
        self, sold_items: List[tuple[str, int]], revenue: List[tuple[str, float]]
    ) -> None:
        logger = Logger()
        logger.print_sold_items(sold_items)
        logger.print_revenue(revenue)

    def answer_Z_question(self) -> bool:
        answer = input("Do You Want To Make Z Report? Answer: y/n\n")
        return answer == "y"

    def answer_X_question(self) -> bool:
        answer = input("Do You Want To Make X Report? Answer: y/n\n")
        return answer == "y"
