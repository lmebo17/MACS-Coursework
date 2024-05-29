from n2t.core.compiler.constants import Constants


class JackTokenizer:
    def has_more_tokens(self) -> bool:
        return self.current_position < len(self.tokens)

    def __init__(self, input_file: str) -> None:
        file = open(input_file, "r")
        self.input_lines = file.readlines()
        self.current_position = 0
        self.current_token = ""
        self.tokens: list[str] = []

        self._tokenize_input()

    def _tokenize_input(self) -> None:
        import re

        keyword_regex = (
            r"\b(class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|null"
            r"|this|let|do|if|else|while|return)\b"
        )
        symbol_regex = r"[{}()\[\].,;+\-*/&|<>=~]"
        int_const_regex = r"\d+"
        string_const_regex = r'"[^"\n]*"'
        identifier_regex = r"[a-zA-Z_]\w*"

        combined_regex = (
            f"({keyword_regex}|{symbol_regex}|{int_const_regex}|"
            f"{string_const_regex}|{identifier_regex})"
        )

        pattern = re.compile(combined_regex)

        in_block_comment = False
        for line in self.input_lines:
            line = line.strip()

            while "/*" in line:
                if "*/" in line:
                    line = re.sub(r"/\*.*?\*/", "", line)
                else:
                    in_block_comment = True
                    line = re.sub(r"/\*.*", "", line)
                    break

            if in_block_comment:
                if "*/" in line:
                    line = line.split("*/", 1)[1]
                    in_block_comment = False
                else:
                    continue

            line = re.sub(r"//.*", "", line)

            tokens = re.findall(pattern, line)
            for token in tokens:
                self.tokens.append(token[0])

        self.tokens = list(filter(lambda result: result.strip(), self.tokens))

    def advance(self) -> None:
        if self.has_more_tokens():
            self.current_token = self.tokens[self.current_position]
            self.current_position += 1
        else:
            self.current_token = ""

    def token_type(self) -> str:
        if self.current_token is None:
            return ""

        if self.current_token in Constants.keywords:
            return Constants.KEYWORD

        if self.current_token in Constants.symbols:
            return Constants.SYMBOL

        if self.current_token.isdigit():
            return Constants.INT_CONST

        if self.current_token.startswith('"') and self.current_token.endswith('"'):
            return Constants.STRING_CONST

        return Constants.IDENTIFIER

    def keyword(self) -> str:
        return self.current_token

    def symbol(self) -> str:
        return self.current_token

    def identifier(self) -> str:
        return self.current_token

    def int_val(self) -> str:
        return self.current_token

    def string_val(self) -> str:
        return self.current_token[1:-1]

    def token_value(self) -> str:
        return self.current_token
