def segment_to_address(segment: str) -> str | None:
    segment_map = {"local": "LCL", "argument": "ARG", "this": "THIS", "that": "THAT"}
    return segment_map.get(segment)


class CodeWriter:
    def __init__(self, output_file: str) -> None:
        self.file_name = ""
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
        self._write_decrement_sp()
        self.write_lines(["@SP", "A=M", "D=M"])
        self._write_decrement_sp()
        self.write_lines(["@SP", "A=M", "D=M{}D".format(operation)])
        self.write_lines(["@SP", "A=M", "M=D"])
        self._write_increment_sp()

    def write_unary_operation(self, operation: str) -> None:
        self._write_decrement_sp()
        self.write_lines(["@SP", "A=M", "D={}M".format(operation)])
        self.write_lines(["@SP", "A=M", "M=D"])
        self._write_increment_sp()

    def write_comparison_operation(self, jump_type: str) -> None:
        self._write_decrement_sp()
        self.write_lines(["@SP", "A=M", "D=M"])
        self._write_decrement_sp()
        self.write_lines(["@SP", "A=M", "D=M-D"])
        self.write_lines(
            [
                "@LABEL_TRUE{}".format(self.label_count),
                "D;{}".format(jump_type),
            ]
        )
        self.write_lines(["@SP", "A=M", "M=0"])
        self.write_lines(
            [
                "@LABEL_END{}".format(self.label_count),
                "0;JMP",
            ]
        )
        self.write_lines(
            [
                "(LABEL_TRUE{})".format(self.label_count),
                "@SP",
                "A=M",
                "M=-1",
            ]
        )
        self.write_lines(["(LABEL_END{})".format(self.label_count)])
        self._write_increment_sp()
        self.label_count += 1

    def write_push_pop(self, command: str, segment: str, index: int) -> None:
        if command == "C_PUSH":
            if segment == "constant":
                self.write_constant_push(index)
            elif segment in ["local", "argument", "this", "that"]:
                self.write_general_push(segment_to_address(segment), index)
            elif segment == "temp":
                self.write_push_temp(5, index)
            elif segment == "pointer":
                self.write_pointer_push("THIS" if not index else "THAT")
            elif segment == "static":
                self.write_push_static(index)
        elif command == "C_POP":
            if segment in ["local", "argument", "this", "that"]:
                self.write_general_pop(segment_to_address(segment), index)
            elif segment == "temp":
                self.write_pop_temp(5, index)
            elif segment == "pointer":
                self.write_pointer_pop("THIS" if not index else "THAT")
            elif segment == "static":
                self.write_static_pop(index)

    def write_pointer_push(self, address: str) -> None:
        self.write_lines(["@{}".format(address), "D=M"])
        self.write_lines(["@SP", "A=M", "M=D"])
        self._write_increment_sp()

    def write_pointer_pop(self, address: str) -> None:
        self._write_decrement_sp()
        self.write_lines(["@SP", "A=M", "D=M", "@{}".format(address), "M=D"])

    def write_push_d(self) -> None:
        self.write_lines(["@SP", "A=M", "M=D"])
        self._write_increment_sp()

    def close(self) -> None:
        self.output_file.close()

    def write_label(self, label: str) -> None:
        self.write_lines(["({})".format(label)])

    def write_if(self, label: str) -> None:
        self._write_decrement_sp()
        self.write_lines(["@SP", "A=M", "D=M"])
        self.write_lines(["@{}".format(label), "D;JNE"])

    def write_goto(self, label: str) -> None:
        self.write_lines(["@{}".format(label), "0;JMP"])

    def _write_increment_sp(self) -> None:
        self.write_lines(["@SP", "AM=M+1"])

    def _write_decrement_sp(self) -> None:
        self.write_lines(["@SP", "AM=M-1"])

    def write_function(self, function_name: str, local_count: int) -> None:
        self.label_count = 0
        self.write_line(f"({function_name})")
        for _ in range(local_count):
            self.write_push_pop("C_PUSH", "constant", 0)

    def write_function_return(self) -> None:
        self.write_lines(["@LCL", "D=M"])
        self.write_lines(["@BOX", "M=D"])

        self.write_lines(["@BOX", "D=M"])
        self.write_lines(["@5", "A=D-A", "D=M"])
        self.write_lines(["@RET", "M=D"])

        self.write_lines(
            ["@{}".format("0"), "D=A", "@{}".format("ARG"), "A=M", "A=A+D"]
        )
        self.write_lines(["D=A", "@TEMPORARY", "M=D"])
        self._write_decrement_sp()
        self.write_lines(["@SP", "A=M", "D=M"])
        self.write_lines(["@TEMPORARY", "A=M"])
        self.write_lines(["M=D"])

        self.write_lines(["@ARG", "D=M", "D=D+1", "@SP", "M=D"])

        table = {"THAT": 1, "THIS": 2, "ARG": 3, "LCL": 4}

        for symbol, offset in table.items():
            self.write_lines(["@BOX", "D=M"])
            self.write_lines(["@{}".format(offset), "A=D-A", "D=M"])
            self.write_lines(["@{}".format(symbol), "M=D"])

        self.write_lines(["@RET", "A=M", "0;JMP"])

    def write_function_call(self, function_name: str, arg_count: str) -> None:
        return_label = function_name + "_RET{}".format(self.label_count)
        self.label_count += 1

        self.write_lines(["@{}".format(return_label), "D=A"])
        self.write_push_d()

        for segment in ["LCL", "ARG", "THIS", "THAT"]:
            self.write_lines([f"@{segment}", "D=M"])
            self.write_push_d()

        self.write_lines(
            ["@SP", "D=M", "@{}".format(int(arg_count) + 5), "D=D-A", "@ARG", "M=D"]
        )

        self.write_lines(["@SP", "D=M", "@LCL", "M=D"])

        self.write_goto(function_name)

        self.write_lines(["({})".format(return_label)])

    def init_file(self) -> None:
        self.write_lines(["@256", "D=A", "@SP", "M=D"])
        self.write_function_call("Sys.init", "0")

    def write_lines(self, lines: list[str]) -> None:
        for line in lines:
            self.output_file.write(line + "\n")

    def write_line(self, param: str) -> None:
        self.output_file.write(param + "\n")

    def write_constant_push(self, index: int) -> None:
        self.write_lines(["@{}".format(index), "D=A"])
        self.write_push_d()

    def write_general_push(self, segment: str | None, index: int) -> None:
        self.write_lines(
            [
                "@{}".format(segment),
                "D=M",
                "@{}".format(index),
                "A=A+D",
            ]
        )
        self.write_lines(["D=M"])
        self.write_push_d()

    def write_push_temp(self, param: int, index: int) -> None:
        self.write_lines(["@{}".format(param + index), "D=M"])
        self.write_push_d()

    def write_push_static(self, index: int) -> None:
        self.write_lines(["@{}.{}".format(self.file_name, index), "D=M"])
        self.write_push_d()

    def write_general_pop(self, param: str | None, index: int) -> None:
        self.write_lines(
            [
                "@{}".format(index),
                "D=A",
                "@{}".format(param),
                "A=M",
                "A=A+D",
            ]
        )
        self.write_lines(["D=A", "@TEMPORARY", "M=D"])
        self._write_decrement_sp()
        self.write_lines(["@SP", "A=M", "D=M"])
        self.write_lines(["@TEMPORARY", "A=M"])
        self.write_lines(["M=D"])

    def write_pop_temp(self, param: int, index: int) -> None:
        self._write_decrement_sp()
        self.write_lines(["@SP", "A=M", "D=M"])
        self.write_lines(["@{}".format(param + index), "M=D"])

    def write_static_pop(self, index: int) -> None:
        self.write_lines(
            [
                "@SP",
                "M=M-1",
                "A=M",
                "D=M",
                "@{}.{}".format(self.file_name, index),
                "M=D",
            ]
        )

    def set_file_name(self, file_name: str) -> None:
        self.file_name = file_name
        pass
