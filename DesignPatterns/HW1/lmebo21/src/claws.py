from dataclasses import dataclass
from typing import Any, List

SMALL_CLAWS = 2
MEDIUM_CLAWS = 3
BIG_CLAWS = 4


@dataclass
class Claws:
    claws = {2: "SMALL_CLAWS", 3: "MEDIUM_CLAWS", 4: "LARGE_CLAWS"}

    @staticmethod
    def claws_list() -> List[Any]:
        return [SMALL_CLAWS, MEDIUM_CLAWS, BIG_CLAWS]
