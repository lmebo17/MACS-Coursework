from typing import Tuple

from src import constants


class MovementStrategy:
    def __init__(self, next_move: "MovementStrategy") -> None:
        self.next = next_move

    def move(self, legs: int, wings: int, stamina: int) -> Tuple[int, int]:
        if self.next:
            return self.next.move(legs, wings, stamina)
        return 0, 0


class LastMove(MovementStrategy):
    def __init__(self) -> None:
        pass

    def move(self, legs: int, wings: int, stamina: int) -> Tuple[int, int]:
        return 0, 0


class FlyingMovement(MovementStrategy):
    def move(self, legs: int, wings: int, stamina: int) -> Tuple[int, int]:
        if wings >= constants.WINGS_TO_FLY and stamina >= constants.STAMINA_TO_FLY:
            return constants.FLY_SPEED, constants.FLY_USED_STAMINA
        elif self.next:
            return self.next.move(legs, wings, stamina)
        return 0, 0


class RunningMovement(MovementStrategy):
    def move(self, legs: int, wings: int, stamina: int) -> Tuple[int, int]:
        if legs >= constants.LEGS_TO_RUN and stamina >= constants.STAMINA_TO_RUN:
            return constants.RUN_SPEED, constants.RUN_USED_STAMINA
        elif self.next:
            return self.next.move(legs, wings, stamina)
        return 0, 0


class WalkingMovement(MovementStrategy):
    def move(self, legs: int, wings: int, stamina: int) -> Tuple[int, int]:
        if legs >= constants.LEGS_TO_WALK and stamina >= constants.STAMINA_TO_WALK:
            return constants.WALK_SPEED, constants.WALK_USED_STAMINA
        elif self.next:
            return self.next.move(legs, wings, stamina)
        return 0, 0


class HoppingMovement(MovementStrategy):
    def move(self, legs: int, wings: int, stamina: int) -> Tuple[int, int]:
        if legs >= constants.LEGS_TO_HOP and stamina >= constants.STAMINA_TO_HOP:
            return constants.HOP_SPEED, constants.HOP_USED_STAMINA
        elif self.next:
            return self.next.move(legs, wings, stamina)
        return 0, 0


class CrawlingMovement(MovementStrategy):
    def move(self, legs: int, wings: int, stamina: int) -> Tuple[int, int]:
        if stamina > 0:
            return constants.CRAWLING_SPEED, constants.CRAWLING_USED_STAMINA
        elif self.next:
            return self.next.move(legs, wings, stamina)
        return 0, 0


class GreedyStrategy(MovementStrategy):
    def __init__(self) -> None:
        super().__init__(
            FlyingMovement(
                RunningMovement(
                    WalkingMovement(HoppingMovement(CrawlingMovement(LastMove())))
                )
            )
        )

    def move(self, legs: int, wings: int, stamina: int) -> Tuple[int, int]:
        return self.next.move(legs, wings, stamina) if self.next else (0, 0)
