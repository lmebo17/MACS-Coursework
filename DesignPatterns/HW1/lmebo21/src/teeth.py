from dataclasses import dataclass
from typing import Any, List

SMALL_TEETH: int = 3
MEDIUM_TEETH: int = 6
BIG_TEETH: int = 9


@dataclass
class Teeth:
    teeth = {3: "SMALL_TEETH", 6: "MEDIUM_TEETH", 9: "LARGE_TEETH"}

    @staticmethod
    def teeth_list() -> List[Any]:
        return [SMALL_TEETH, MEDIUM_TEETH, BIG_TEETH]
