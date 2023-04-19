package lexer;

import parser.Statement;

import java.util.LinkedList;
import java.util.List;

public class Token {

    public Symbol symbol;
    public Statement statement;
    public String value;
    public List<Token> children;

    public Token() {
        this.symbol = null;
        this.value = null;
        this.statement = null;
        this.children = new LinkedList<>();
    }

    public Token(Statement statement) {
        this();
        this.statement = statement;
    }

    public Token(Symbol symbol, String value) {
        this();
        this.symbol = symbol;
        this.value = value;
    }

    public Token getChild(int index) {
        return children.get(index);
    }

    public void addChild(Token token) {
        children.add(token);
    }

    @Override
    public String toString() {
        return symbol != null ? value : statement.toString();
    }


}
