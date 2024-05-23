from __future__ import annotations

from dataclasses import dataclass

from n2t.infra.code_writer import CodeWriter
from n2t.infra.parser import Parser


@dataclass
class VmProgram:
    file_name: str
    directory_name: str

    @classmethod
    def load_from(cls, file_or_directory_name: str) -> VmProgram:
        cls.file_name = file_or_directory_name
        cls.directory_name = file_or_directory_name
        return cls(cls.file_name, cls.directory_name)

    def translate_single_file(self, code_writer: CodeWriter) -> None:
        parser = Parser(self.file_name)

        while parser.has_more_lines():
            parser.advance()
            command_type = parser.command_type()
            if command_type == "C_ARITHMETIC":
                code_writer.write_arithmetic(parser.arg_one())
            elif command_type in ["C_PUSH", "C_POP"]:
                code_writer.write_push_pop(
                    command_type, parser.arg_one(), parser.arg_two()
                )
            elif command_type == "C_LABEL":
                code_writer.write_label(parser.arg_one())
            elif command_type == "C_IF":
                code_writer.write_if(parser.arg_one())
            elif command_type == "C_GOTO":
                code_writer.write_goto(parser.arg_one())
            elif command_type == "C_FUNCTION":
                code_writer.write_function(parser.arg_one(), parser.arg_two())
            elif command_type == "C_RETURN":
                code_writer.write_function_return()
            elif command_type == "C_CALL":
                arg_count = str(parser.arg_two()) if parser.arg_two() else "0"
                code_writer.write_function_call(parser.arg_one(), arg_count)

    def translate(self) -> None:
        if self.file_name.endswith(".vm"):
            output_file = self.file_name.split(".vm")[0] + ".asm"
            code_writer = CodeWriter(output_file)
            self.translate_single_file(code_writer)

        else:
            import os

            path_components = self.directory_name.split("/")
            last_element = path_components[-2]
            output_file = str(self.directory_name) + last_element + ".asm"
            code_writer = CodeWriter(output_file)

            for file_name in os.listdir(self.directory_name):
                if file_name.endswith(".vm"):
                    if file_name.startswith("Sys"):
                        code_writer.init_file()
                    vm_file = os.path.join(self.directory_name, file_name)
                    code_writer.set_file_name(file_name)
                    self.file_name = vm_file
                    self.translate_single_file(code_writer)

            code_writer.close()
