class Constants:
    KEYWORD = "keyword"
    SYMBOL = "symbol"
    IDENTIFIER = "identifier"
    INT_CONST = "integerConstant"
    STRING_CONST = "stringConstant"

    CLASS = "CLASS"
    METHOD = "METHOD"
    FUNCTION = "FUNCTION"
    CONSTRUCTOR = "CONSTRUCTOR"
    INT = "INT"
    BOOLEAN = "BOOLEAN"
    CHAR = "CHAR"
    VOID = "VOID"
    VAR = "VAR"
    STATIC = "STATIC"
    FIELD = "FIELD"
    LET = "LET"
    DO = "DO"
    IF = "IF"
    ELSE = "ELSE"
    WHILE = "WHILE"
    RETURN = "RETURN"
    TRUE = "TRUE"
    FALSE = "FALSE"
    NULL = "NULL"
    THIS = "THIS"

    keywords = {
        "class": "class",
        "constructor": "constructor",
        "function": "function",
        "method": "method",
        "field": "field",
        "static": "static",
        "var": "var",
        "int": "int",
        "char": "char",
        "boolean": "boolean",
        "void": "void",
        "true": "true",
        "false": "false",
        "null": "null",
        "this": "this",
        "let": "let",
        "do": "do",
        "if": "if",
        "else": "else",
        "while": "while",
        "return": "return",
    }

    symbols = [
        "{",
        "}",
        "(",
        ")",
        "[",
        "]",
        ".",
        ",",
        ";",
        "+",
        "-",
        "*",
        "/",
        "&",
        "|",
        "<",
        ">",
        "=",
        "~",
    ]

    operations = ["+", "-", "/", "*", ">", "<", "=", "&", "|"]

    symbol_escape_mapping = {"<": "&lt;", ">": "&gt;", "&": "&amp;"}
