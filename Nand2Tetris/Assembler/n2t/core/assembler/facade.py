from __future__ import annotations

from dataclasses import dataclass
from typing import Iterable

from n2t.core.assembler.code import Code
from n2t.core.assembler.parser import Parser
from n2t.core.assembler.symbol_table import SymbolTable


@dataclass
class Assembler:
    @classmethod
    def create(cls) -> Assembler:
        return cls()

    def assemble(self, assembly: Iterable[str]) -> Iterable[str]:
        symbol_table = SymbolTable()
        parsed_instructions = []
        current_address = 0

        for line in assembly:
            parser = Parser(line)
            instruction_type = parser.instruction_type()
            if instruction_type == "L_INSTRUCTION":
                symbol = parser.symbol()
                if not symbol_table.contains(symbol):
                    symbol_table.add_entry(symbol, current_address)

            elif instruction_type != "COMMENT":
                current_address += 1

        current_address = 16
        for line in assembly:
            parser = Parser(line)
            instruction_type = parser.instruction_type()

            if instruction_type == "A_INSTRUCTION":
                symbol = parser.symbol()
                if symbol.isdigit():
                    address = int(symbol)
                elif not symbol_table.contains(symbol):
                    address = current_address
                    symbol_table.add_entry(symbol, address)
                    current_address += 1
                else:
                    address = symbol_table.get_address(symbol)
                binary_address = format(address, '016b')
                parsed_instructions.append(binary_address + "\r")
            elif instruction_type == "C_INSTRUCTION":
                dest = parser.dest()
                comp = parser.comp()
                jump = parser.jump()
                c_instruction = ('111' + Code.comp(comp) +
                                 Code.dest(dest) + Code.jump(jump))
                parsed_instructions.append(c_instruction + "\r")
        return parsed_instructions
