class Parser:
    def __init__(self, file_name: str) -> None:
        self.file = open(file_name, "r")

        self.file_lines = self.file.readlines()
        self.ind = -1
        self.current_instruction = ""

    def has_more_lines(self) -> bool:
        self.ind += 1
        return self.ind != len(self.file_lines)

    def advance(self) -> None:
        self.current_instruction = self.file_lines[self.ind].strip()

    def command_type(self) -> str | None:
        if not self.current_instruction.strip():
            return None

        command = self.current_instruction.split()[0]
        if command in ["add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not"]:
            return "C_ARITHMETIC"
        elif command == "push":
            return "C_PUSH"
        elif command == "pop":
            return "C_POP"
        elif command == "label":
            return "C_LABEL"
        elif command == "goto":
            return "C_GOTO"
        elif command == "if-goto":
            return "C_IF"
        elif command == "function":
            return "C_FUNCTION"
        elif command == "return":
            return "C_RETURN"
        elif command == "call":
            return "C_CALL"
        else:
            return None

    def arg_one(self) -> str:
        command = self.current_instruction.split()[0]
        if self.command_type() == "C_ARITHMETIC":
            return command
        else:
            return self.current_instruction.split()[1]

    def arg_two(self) -> int:
        return int(self.current_instruction.split()[2])
