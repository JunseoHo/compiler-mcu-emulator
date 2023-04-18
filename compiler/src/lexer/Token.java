package lexer;

import parser.Statement;

import java.util.LinkedList;
import java.util.List;

public class Token {

    public Statement statement;
    public Symbol symbol;
    public String value;
    public List<Token> children;

    public Token() {
        this.symbol = null;
        this.value = null;
        this.statement = null;
        this.children = new LinkedList<>();
    }

    public Token(Statement statement) {
        this.symbol = null;
        this.value = null;
        this.statement = statement;
        this.children = new LinkedList<>();
    }

    public Token(Symbol symbol, String value) {
        this.symbol = symbol;
        this.value = value;
        this.statement = null;
        this.children = new LinkedList<>();
    }

    public Token getChild(int index) {
        return children.get(index);
    }

    public void addChild(Token token) {
        children.add(token);
    }

    @Override
    public String toString() {
        if (symbol != null)
            return value;
        return statement.toString();
    }


}
