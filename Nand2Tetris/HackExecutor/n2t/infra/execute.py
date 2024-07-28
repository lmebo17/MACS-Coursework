from __future__ import annotations

import json
from dataclasses import dataclass
from pathlib import Path
from typing import Iterable

from n2t.core import Assembler
from n2t.core.executor.facade import Executor
from n2t.infra.io import File, FileFormat


@dataclass
class ExecuteProgram:
    file: Path
    instructions: Iterable[str]

    @classmethod
    def load_from(cls, hack_or_asm_file: str) -> ExecuteProgram:
        return cls(Path(hack_or_asm_file), [])

    def execute(self, cycles: int) -> None:
        if self.file.suffix == ".hack":
            self.instructions = File(self.file).load()
        elif self.file.suffix == ".asm":
            file = open(self.file, "r")
            assembler = Assembler()
            self.instructions = assembler.assemble(file.readlines())

        self.instructions = [
            instruction.rstrip("\r") for instruction in self.instructions
        ]
        executor = Executor()
        RAM, visited = executor.execute(self.instructions, cycles)
        result = {}

        for index, value in enumerate(RAM):
            if visited[index]:
                result[str(index)] = int(value)

        output = {"RAM": result}
        json_output = json.dumps(output, indent=3)
        file_path = FileFormat.json.convert(self.file)
        with open(file_path, "w") as json_file:
            json_file.write(json_output)
