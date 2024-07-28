from __future__ import annotations

from dataclasses import dataclass, field
from typing import Callable, Dict, Iterable, List

from n2t.core.executor.constants import comparison_map, destination_map, jump_map


@dataclass
class Executor:
    A: int = 0
    D: int = 0
    instruction_index: int = 0
    RAM: List[int] = field(default_factory=list)
    visited = [False for _ in range(2**15)]

    @classmethod
    def create(cls) -> Executor:
        return cls()

    def __init__(self) -> None:
        self.RAM = [int(0) for _ in range(2**15)]

    def execute(
        self, lines: Iterable[str], cycles: int
    ) -> tuple[list[int], list[bool]]:
        codes = list(lines)
        for _ in range(cycles):
            if self.instruction_index >= len(codes):
                break
            self.instruction_index += 1
            self.do_one_instruction(codes[self.instruction_index - 1])
        return self.RAM, self.visited

    def do_jump(self, instruction: str, bit: int) -> None:
        jump_conditions: Dict[str, Callable[[int], bool]] = {
            "JGT": lambda x: x > 0,
            "JLT": lambda x: x < 0,
            "JNE": lambda x: x != 0,
            "JEQ": lambda x: x == 0,
            "JLE": lambda x: x <= 0,
            "JGE": lambda x: x >= 0,
            "JMP": lambda x: True,
        }
        if instruction in jump_conditions and jump_conditions[instruction](bit):
            self.instruction_index = self.A

    def do_one_instruction(self, flag: str) -> None:
        if flag.startswith("0"):
            self.do_a_instruction(flag)
        else:
            self.do_c_instruction(flag[3:])

    def do_a_instruction(self, flag: str) -> None:
        self.A = int(flag[1:], 2)

    def do_c_instruction(self, flag: str) -> None:
        comparison_bit: int = self.evaluate(comparison_map[flag[:7]])
        destination: str = destination_map[flag[slice(7, 10)]]
        if "A" in destination:
            self.A = comparison_bit
        if "D" in destination:
            self.D = comparison_bit
        if "M" in destination:
            self.visited[self.A] = True
            self.RAM[self.A] = comparison_bit
        if jump_map[flag[10:]] != "":
            self.do_jump(jump_map[flag[10:]], int(comparison_bit))

    def evaluate(self, line: str) -> int:
        return self.pass_checkers(
            line,
            {"M": str(self.RAM[self.A]), "A": str(self.A), "D": str(self.D), "!": "~"},
        )

    def pass_checkers(self, line: str, replacements: Dict[str, str]) -> int:
        for key, value in replacements.items():
            line = line.replace(key, value)
        return int(eval(line))
