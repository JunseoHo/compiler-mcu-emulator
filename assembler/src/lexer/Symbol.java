package lexer;

public class Symbol {

    private String name;
    private String regex;

    public Symbol(String name, String regex) {
        this.name = name;
        this.regex = regex;
    }

    public String getName() {
        return name;
    }

    public String getRegex() {
        return regex;
    }

}
