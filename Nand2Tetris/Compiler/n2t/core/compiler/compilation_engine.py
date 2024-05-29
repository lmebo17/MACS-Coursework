from n2t.core.compiler.constants import Constants
from n2t.core.compiler.jack_tokenizer import JackTokenizer


class CompilationEngine:
    def __init__(self, input_path: str, output_path: str) -> None:
        self.tokenizer = JackTokenizer(input_file=input_path)
        self.indents = ""
        self.output_fd = open(output_path, "w")

    def write_into_file(self, line: str) -> None:
        self.output_fd.write(self.indents + line + "\n")

    def open_tag(self, tag_name: str) -> None:
        self.write_into_file("<{}>".format(tag_name))
        self.increase_padding()

    def close_tag(self, tag_name: str) -> None:
        self.decrease_padding()
        self.write_into_file("</{}>".format(tag_name))

    def increase_padding(self) -> None:
        self.indents += "  "

    def decrease_padding(self) -> None:
        self.indents = self.indents[:-2]

    def compile_class(self) -> None:
        self.open_tag("class")
        self.tokenizer.advance()

        self.add(self.tokenizer.keyword())
        self.add(self.tokenizer.identifier())
        self.add(self.tokenizer.symbol())

        while (
            self.tokenizer.token_type() == Constants.KEYWORD
            and self.tokenizer.keyword() in ["static", "field"]
        ):
            self.compile_class_var_dec()

        while (
            self.tokenizer.token_type() == Constants.KEYWORD
            and self.tokenizer.keyword() in ["constructor", "function", "method"]
        ):
            self.compile_subroutine()

        self.add(self.tokenizer.symbol())
        self.close_tag("class")

    def compile_class_var_dec(self) -> None:
        self.open_tag("classVarDec")
        self.add(self.tokenizer.keyword())
        self.wrapper()
        while not self.tokenizer.symbol() == ";":
            self.add(self.tokenizer.symbol())
            self.add(self.tokenizer.identifier())
        self.add(self.tokenizer.symbol())
        self.close_tag("classVarDec")

    def compile_subroutine(self) -> None:
        self.open_tag("subroutineDec")
        self.add(self.tokenizer.keyword())
        self.wrapper()
        self.add(self.tokenizer.symbol())
        self.compile_parameter_list()
        self.add(self.tokenizer.symbol())  # )
        self.compile_subroutine_body()
        self.close_tag("subroutineDec")
        self.tokenizer.advance()

    def compile_parameter_list(self) -> None:
        self.open_tag("parameterList")
        while not self.tokenizer.symbol() == ")":
            self.wrapper()
            if self.tokenizer.symbol() == ",":
                self.add(self.tokenizer.symbol())
        self.close_tag("parameterList")

    def wrapper(self) -> None:
        if self.tokenizer.token_type() == Constants.KEYWORD:
            self.add(self.tokenizer.keyword())
        elif self.tokenizer.token_type() == Constants.IDENTIFIER:
            self.add(self.tokenizer.identifier())
        self.add(self.tokenizer.identifier())

    def compile_subroutine_body(self) -> None:
        self.open_tag("subroutineBody")
        self.add(self.tokenizer.symbol())
        while self.tokenizer.keyword() == "var":
            self.compile_var_dec()
        self.compile_statements()
        self.add(self.tokenizer.symbol(), False)
        self.close_tag("subroutineBody")

    def compile_var_dec(self) -> None:
        self.open_tag("varDec")
        while not self.tokenizer.symbol() == ";":
            self.add(self.tokenizer.keyword())
            self.wrapper()
            if self.tokenizer.symbol() == ",":
                while not self.tokenizer.symbol() == ";":
                    self.add(self.tokenizer.symbol())
                    self.add(self.tokenizer.identifier())
        self.add(self.tokenizer.symbol())
        self.close_tag("varDec")

    def compile_statements(self) -> None:
        self.open_tag("statements")
        while self.tokenizer.token_type() == Constants.KEYWORD:
            if self.tokenizer.keyword() == "let":
                self.compile_let()
            elif self.tokenizer.keyword() == "if":
                self.compile_if()
            elif self.tokenizer.keyword() == "while":
                self.compile_while()
            elif self.tokenizer.keyword() == "do":
                self.compile_do()
            elif self.tokenizer.keyword() == "return":
                self.compile_return()
        self.close_tag("statements")

    def compile_let(self) -> None:
        self.open_tag("letStatement")
        self.add(self.tokenizer.keyword())
        self.add(self.tokenizer.identifier())
        if self.tokenizer.symbol() == "[":
            self.add(self.tokenizer.symbol())
            self.compile_expression()
            self.add(self.tokenizer.symbol())
        self.add(self.tokenizer.symbol())
        self.compile_expression()
        self.add(self.tokenizer.symbol())
        self.close_tag("letStatement")

    def compile_if(self) -> None:
        self.open_tag("ifStatement")
        self.compile_condition()
        self.tokenizer.advance()
        while self.tokenizer.keyword() == "else":
            self.add(self.tokenizer.keyword())
            self.add(self.tokenizer.symbol())
            self.compile_statements()
            self.add(self.tokenizer.symbol())
        self.close_tag("ifStatement")

    def compile_condition(self) -> None:
        self.add(self.tokenizer.keyword())
        self.add(self.tokenizer.symbol())
        self.compile_expression()
        self.add(self.tokenizer.symbol())
        self.add(self.tokenizer.symbol())
        self.compile_statements()
        self.add(self.tokenizer.symbol(), False)

    def compile_while(self) -> None:
        self.open_tag("whileStatement")
        self.compile_condition()
        self.close_tag("whileStatement")
        self.tokenizer.advance()

    def compile_do(self) -> None:
        self.open_tag("doStatement")
        self.add(self.tokenizer.keyword())
        self.add(self.tokenizer.identifier())
        while self.tokenizer.symbol() == ".":
            self.add(self.tokenizer.symbol())
            self.add(self.tokenizer.identifier())
        self.add(self.tokenizer.symbol())
        self.compile_list()
        self.add(self.tokenizer.symbol())
        self.add(self.tokenizer.symbol())
        self.close_tag("doStatement")

    def compile_return(self) -> None:
        self.open_tag("returnStatement")
        self.add(self.tokenizer.keyword())
        if (
            self.tokenizer.token_type() != Constants.SYMBOL
            or self.tokenizer.symbol() != ";"
        ):
            self.compile_expression()
        self.add(self.tokenizer.symbol(), False)
        self.close_tag("returnStatement")
        self.tokenizer.advance()

    def compile_expression(self) -> None:
        self.open_tag("expression")
        self.compile_term()
        while (
            self.tokenizer.token_type() == Constants.SYMBOL
            and self.tokenizer.symbol() in Constants.operations
        ):
            self.add(self.tokenizer.symbol())
            self.compile_term()
        self.close_tag("expression")

    def compile_term(self) -> None:
        self.open_tag("term")
        if self.tokenizer.token_type() == Constants.INT_CONST:
            self.add(str(self.tokenizer.int_val()))
        elif self.tokenizer.token_type() == Constants.STRING_CONST:
            self.add(self.tokenizer.string_val())
        elif self.tokenizer.token_type() == Constants.IDENTIFIER:
            self.compile_identifier()
        elif self.tokenizer.token_type() == Constants.KEYWORD:
            self.add(self.tokenizer.keyword())
        elif self.tokenizer.token_type() == Constants.SYMBOL:
            if self.tokenizer.symbol() == "(":
                self.add(self.tokenizer.symbol())
                self.compile_expression()
                self.add(self.tokenizer.symbol())
            elif self.tokenizer.symbol() == "~" or self.tokenizer.symbol() == "-":
                self.add(self.tokenizer.symbol())
                self.compile_term()
        self.close_tag("term")

    def compile_identifier(self) -> None:
        self.add(self.tokenizer.identifier())
        if self.tokenizer.symbol() == "[":
            self.add(self.tokenizer.symbol())
            self.compile_expression()
            self.add(self.tokenizer.symbol())
        elif self.tokenizer.symbol() == ".":
            self.add(self.tokenizer.symbol())
            self.add(self.tokenizer.identifier())
            self.add(self.tokenizer.symbol())
            self.compile_list()
            self.add(self.tokenizer.symbol())
        elif self.tokenizer.symbol() == "(":
            self.add(self.tokenizer.symbol())
            self.compile_list()
            self.add(self.tokenizer.symbol())

    def compile_list(self) -> None:
        self.open_tag("expressionList")
        while (
            self.tokenizer.token_type() != Constants.SYMBOL
            or self.tokenizer.symbol() != ")"
        ):
            self.compile_expression()
            if (
                self.tokenizer.token_type() == Constants.SYMBOL
                and self.tokenizer.symbol() == ","
            ):
                self.add(self.tokenizer.symbol())
        self.close_tag("expressionList")

    def add(self, token: str, flag: bool = True) -> None:
        element_type = self.tokenizer.token_type()
        escaped_content = (
            Constants.symbol_escape_mapping.get(token, token)
            if element_type == "symbol"
            else token
        )
        self.write_into_file(f"<{element_type}> {escaped_content} </{element_type}>")
        if flag:
            self.tokenizer.advance()
