def segmentToAddress(segment: str) -> str | None:
    segment_map = {"local": "LCL", "argument": "ARG", "this": "THIS", "that": "THAT"}
    return segment_map.get(segment)


class CodeWriter:
    def __init__(self, output_file: str) -> None:
        self.output_file = open(output_file, "w")
        self.label_count = 0

    def write_arithmetic(self, command: str) -> None:
        if command == "add":
            self.write_binary_operation("+")
        elif command == "sub":
            self.write_binary_operation("-")
        elif command == "neg":
            self.write_unary_operation("-")
        elif command == "eq":
            self.write_comparison_operation("JEQ")
        elif command == "gt":
            self.write_comparison_operation("JGT")
        elif command == "lt":
            self.write_comparison_operation("JLT")
        elif command == "and":
            self.write_binary_operation("&")
        elif command == "or":
            self.write_binary_operation("|")
        elif command == "not":
            self.write_unary_operation("!")

    def write_binary_operation(self, operation: str) -> None:
        self.output_file.write("@SP\n")
        self.output_file.write("AM=M-1\n")
        self.output_file.write("D=M\n")
        self.output_file.write("A=A-1\n")
        self.output_file.write(f"M=M{operation}D\n")

    def write_unary_operation(self, operation: str) -> None:
        self.output_file.write("@SP\n")
        self.output_file.write("A=M-1\n")
        self.output_file.write(f"M={operation}M\n")

    def write_comparison_operation(self, jump_type: str) -> None:
        self.output_file.write("@SP\n")
        self.output_file.write("AM=M-1\n")
        self.output_file.write("D=M\n")
        self.output_file.write("A=A-1\n")
        self.output_file.write("D=M-D\n")
        self.output_file.write(f"@TRUE{self.label_count}\n")
        self.output_file.write(f"D;{jump_type}\n")
        self.output_file.write("@SP\n")
        self.output_file.write("A=M-1\n")
        self.output_file.write("M=0\n")
        self.output_file.write(f"@CONTINUE{self.label_count}\n")
        self.output_file.write("0;JMP\n")
        self.output_file.write(f"(TRUE{self.label_count})\n")
        self.output_file.write("@SP\n")
        self.output_file.write("A=M-1\n")
        self.output_file.write("M=-1\n")
        self.output_file.write(f"(CONTINUE{self.label_count})\n")
        self.label_count += 1

    def write_push_pop(self, command: str, segment: str, index: int) -> None:
        if command == "C_PUSH":
            if segment == "constant":
                self.output_file.write(f"@{index}\n")
                self.output_file.write("D=A\n")
                self.write_push_d()
            elif segment in ["local", "argument", "this", "that"]:
                self.output_file.write(f"@{segmentToAddress(segment)}\n")
                self.output_file.write("D=M\n")
                self.output_file.write(f"@{index}\n")
                self.output_file.write("A=D+A\n")
                self.output_file.write("D=M\n")
                self.write_push_d()
            elif segment == "temp":
                self.output_file.write(f"@{5 + int(index)}\n")
                self.output_file.write("D=M\n")
                self.write_push_d()
            elif segment == "pointer":
                self.write_pointer_push("THIS" if not index else "THAT")
            elif segment == "static":
                self.output_file.write(f"@{16 + int(index)}\n")
                self.output_file.write("D=M\n")
                self.write_push_d()

        elif command == "C_POP":
            if segment in ["local", "argument", "this", "that"]:
                self.output_file.write(f"@{segmentToAddress(segment)}\n")
                self.output_file.write("D=M\n")
                self.output_file.write(f"@{index}\n")
                self.output_file.write("D=D+A\n")
                self.output_file.write("@R13\n")
                self.output_file.write("M=D\n")
                self.write_pop_d()
                self.output_file.write("@R13\n")
                self.output_file.write("A=M\n")
                self.output_file.write("M=D\n")
            elif segment == "temp":
                self.write_pop_d()
                self.output_file.write(f"@{5 + int(index)}\n")
                self.output_file.write("M=D\n")
            elif segment == "pointer":
                self.write_pointer_pop("THIS" if not index else "THAT")
            elif segment == "static":
                self.write_pop_d()
                self.output_file.write(f"@{16 + int(index)}\n")
                self.output_file.write("M=D\n")

    def write_pointer_push(self, address: str) -> None:
        self.output_file.write("@{}\n".format(address))
        self.output_file.write("D=M\n")
        self.output_file.write("@SP\n")
        self.output_file.write("A=M\n")
        self.output_file.write("M=D\n")
        self.output_file.write("@SP\n")
        self.output_file.write("M=M+1\n")

    def write_pointer_pop(self, address: str) -> None:
        self.output_file.write("@SP\n")
        self.output_file.write("M=M-1\n")
        self.output_file.write("@SP\n")
        self.output_file.write("A=M\n")
        self.output_file.write("D=M\n")
        self.output_file.write("@{}\n".format(address))
        self.output_file.write("M=D\n")

    def write_push_d(self) -> None:
        self.output_file.write("@SP\n")
        self.output_file.write("A=M\n")
        self.output_file.write("M=D\n")
        self.output_file.write("@SP\n")
        self.output_file.write("M=M+1\n")

    def write_pop_d(self) -> None:
        self.output_file.write("@SP\n")
        self.output_file.write("AM=M-1\n")
        self.output_file.write("D=M\n")

    def close(self) -> None:
        self.output_file.close()
