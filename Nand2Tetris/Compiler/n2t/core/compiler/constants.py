class Constants:
    KEYWORD = 'KEYWORD'
    SYMBOL = 'SYMBOL'
    IDENTIFIER = 'IDENTIFIER'
    INT_CONST = 'INT_CONST'
    STRING_CONST = 'STRING_CONST'

    CLASS = 'CLASS'
    METHOD = 'METHOD'
    FUNCTION = 'FUNCTION'
    CONSTRUCTOR = 'CONSTRUCTOR'
    INT = 'INT'
    BOOLEAN = 'BOOLEAN'
    CHAR = 'CHAR'
    VOID = 'VOID'
    VAR = 'VAR'
    STATIC = 'STATIC'
    FIELD = 'FIELD'
    LET = 'LET'
    DO = 'DO'
    IF = 'IF'
    ELSE = 'ELSE'
    WHILE = 'WHILE'
    RETURN = 'RETURN'
    TRUE = 'TRUE'
    FALSE = 'FALSE'
    NULL = 'NULL'
    THIS = 'THIS'

    keywords = {
        "class": CLASS,
        "constructor": CONSTRUCTOR,
        "function": FUNCTION,
        "method": METHOD,
        "field": FIELD,
        "static": STATIC,
        "var": VAR,
        "int": INT,
        "char": CHAR,
        "boolean": BOOLEAN,
        "void": VOID,
        "true": TRUE,
        "false": FALSE,
        "null": NULL,
        "this": THIS,
        "let": LET,
        "do": DO,
        "if": IF,
        "else": ELSE,
        "while": WHILE,
        "return": RETURN,
    }

    symbols = [
        "{", "}", "(", ")", "[", "]", ".", ",", ";", "+", "-", "*", "/", "&", "|", "<", ">", "=", "~"
    ]
