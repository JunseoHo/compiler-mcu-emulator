package parser;

import lexer.Token;

import java.util.LinkedList;
import java.util.List;

public class ParseNode {

    private Token token;
    private LinkedList<ParseNode> children;

    public ParseNode(String name) {
        this.token = new Token(name);
        children = new LinkedList<>();
    }

    public ParseNode(Token token) {
        this.token = token;
        children = new LinkedList<>();
    }

    public void addChild(ParseNode child) {
        children.add(child);
    }

    public ParseNode getChild(int index) {
        return children.get(index);
    }

    public String getName() {
        return token.name;
    }

    public Token getToken(){
        return token;
    }

    public void setName(String name) {
        this.token = new Token(name);
    }

    public LinkedList<ParseNode> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return token.value;
    }

}
