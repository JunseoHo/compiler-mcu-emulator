package lexer;

public enum Symbol {

    IF("^if$"),

    ENDIF("^endif$"),

    WHILE("^while$"),

    ENDWHILE("^endwhile$"),

    DATA_TYPE("^short|int|long$"),

    RELATIONAL_OPERATOR("^!=|<=|>=$"),

    ARITHMETIC_OPERATOR("^\\+|-|/|\\*|&|\\||\\^$"),

    EQUALS_SIGN("^=$"),

    PRINT_FUNC("^print$"),

    IDENTIFIER("^[^0-9][a-zA-Z0-9]*$"),

    NUMBER("^[0-9]*$");

    private final String regex;

    Symbol(String regex) {
        this.regex = regex;
    }

    String getRegex() {
        return regex;
    }
}
