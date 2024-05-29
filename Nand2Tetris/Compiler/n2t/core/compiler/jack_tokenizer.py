from n2t.core.compiler.constants import *
class JackTokenizer:
    def __init__(self, input_file):
        file = open(input_file, 'r')
        self.input_lines = file.readlines()
        self.current_line = 0
        self.current_position = 0
        self.current_token = None
        self.tokens = []

        self._tokenize_input()
        print(self.tokens)

    def _tokenize_input(self):
        import re

        keyword_regex = (r"\b(class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|null"
                         r"|this|let|do|if|else|while|return)\b")
        symbol_regex = r"[{}()\[\].,;+\-*/&|<>=~]"
        int_const_regex = r"\d+"
        string_const_regex = r'"[^"\n]*"'
        identifier_regex = r"[a-zA-Z_]\w*"

        combined_regex = f"({keyword_regex}|{symbol_regex}|{int_const_regex}|{string_const_regex}|{identifier_regex})"

        pattern = re.compile(combined_regex)

        for line in self.input_lines:
            line = line.strip()
            if not line:
                continue

            line = re.sub(r"//.*", "", line)

            if '/*' in line:
                line = re.split(r'/\*', line, 1)[0]

            tokens = re.findall(pattern, line)
            for token in tokens:
                if token[0].strip():
                    self.tokens.append(token[0])

        self.tokens = list(filter(lambda result: result.strip(), self.tokens))
        print(len(self.tokens))

    def has_more_tokens(self):
        return self.current_position < len(self.tokens)

    def advance(self):
        if self.has_more_tokens():
            self.current_token = self.tokens[self.current_position]
            self.current_position += 1
        else:
            self.current_token = None

    def token_type(self):
        if self.current_token is None:
            return None

        if self.current_token in Constants.keywords:
            return Constants.KEYWORD

        if self.current_token in Constants.symbols:
            return Constants.SYMBOL

        if self.current_token.isdigit():
            return Constants.INT_CONST

        if self.current_token.startswith('"') and self.current_token.endswith('"'):
            return Constants.STRING_CONST

        return Constants.IDENTIFIER

    def keyword(self):
        return Constants.keywords[self.current_token]

    def symbol(self):
        return self.current_token

    def identifier(self):
        return self.current_token

    def int_val(self):
        return int(self.current_token)

    def string_val(self):
        return self.current_token[1:-1]
