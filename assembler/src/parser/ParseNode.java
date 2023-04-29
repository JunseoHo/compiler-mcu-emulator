package parser;

import lexer.Token;

import java.util.LinkedList;
import java.util.List;

public class ParseNode {

    public Token token;
    public List<ParseNode> children;

    public ParseNode(Token token) {
        this.token = token;
        this.children = new LinkedList<>();
    }

    public void addChild(ParseNode node) {
        children.add(node);
    }

    public ParseNode getLastChild() {
        return children.get(children.size() - 1);
    }

}
