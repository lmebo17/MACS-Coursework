from __future__ import annotations

from dataclasses import dataclass

from n2t.infra.code_writer import CodeWriter
from n2t.infra.parser import Parser


@dataclass
class VmProgram:
    file_name: str

    @classmethod
    def load_from(cls, file_or_directory_name: str) -> VmProgram:
        cls.file_name = file_or_directory_name
        return cls(file_or_directory_name)

    def translate(self) -> None:
        output_file = self.file_name.split(".vm")[0] + ".asm"
        parser = Parser(self.file_name)
        code_writer = CodeWriter(output_file)
        while parser.has_more_lines():
            parser.advance()
            command_type = parser.command_type()
            if command_type == "C_ARITHMETIC":
                code_writer.write_arithmetic(parser.arg_one())
            elif command_type in ["C_PUSH", "C_POP"]:
                code_writer.write_push_pop(
                    command_type, parser.arg_one(), parser.arg_two()
                )

        code_writer.close()
