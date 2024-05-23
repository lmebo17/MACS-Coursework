from typing import Any


class Parser:

    def __init__(self, line: str) -> None:
        self.current_instruction = line

    def get_current_instruction(self) -> Any:
        return self.current_instruction.split("//")[0].strip()

    def instruction_type(self) -> str:
        instruction = self.get_current_instruction()
        if not instruction or instruction.startswith('/'):
            return "COMMENT"
        elif instruction.startswith('@'):
            return "A_INSTRUCTION"
        elif '(' in instruction and ')' in instruction:
            return "L_INSTRUCTION"
        else:
            return "C_INSTRUCTION"

    def symbol(self) -> Any:
        instruction_type = self.instruction_type()
        instruction = self.get_current_instruction()
        if instruction_type == "A_INSTRUCTION":
            return instruction[1:]
        elif instruction_type == "L_INSTRUCTION":
            return instruction[1:-1]
        else:
            return None

    def dest(self) -> Any:
        instruction_type = self.instruction_type()
        if instruction_type == "C_INSTRUCTION":
            instruction = self.get_current_instruction()
            if '=' in instruction:
                return instruction.split('=')[0]
            else:
                return None
        else:
            return None

    def comp(self) -> Any:
        instruction_type = self.instruction_type()
        if instruction_type == "C_INSTRUCTION":
            instruction = self.get_current_instruction()
            if '=' in instruction:
                return instruction.split('=')[1].split(';')[0]
            elif ';' in instruction:
                return instruction.split(';')[0]
            else:
                return instruction
        else:
            return None

    def jump(self) -> Any:
        instruction_type = self.instruction_type()
        if instruction_type == "C_INSTRUCTION":
            instruction = self.get_current_instruction()
            if ';' in instruction:
                return instruction.split(';')[1]
            else:
                return None
        else:
            return None
