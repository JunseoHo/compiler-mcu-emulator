package parser;

import lexer.Lexer;
import lexer.Token;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Parser {

    private Lexer lexer;
    private Token token;
    private ParseNode root;
    private LinkedList<String> identifiers;

    public Parser() {
        root = new ParseNode(new Token("PROGRAM", "PROGRAM"));
        identifiers = new LinkedList<>();
    }

    public void associate(Lexer lexer) {
        this.lexer = lexer;
    }

    public ParseNode getParserTree() {
        return root;
    }

    public LinkedList<String> getIdentifiers(){
        return identifiers;
    }

    public void printParseTree() {
        System.out.println("[IDENTIFIER_TABLE]");
        System.out.printf("%20s%20s\n", "ID", "IDENTIFIER");
        System.out.println("----------------------------------------");
        for (int i = 0; i < identifiers.size(); i++)
            System.out.printf("%20d%20s\n", i, identifiers.get(i));
        System.out.println("[PARSE_TREE]");
        printParseTree(root, 0);
    }

    public void printParseTree(ParseNode node, int width) {
        for (int i = 0; i < width; i++)
            System.out.print(" ");
        if (width > 0)
            System.out.print("└ ");
        System.out.println(node);
        for (ParseNode child : node.getChildren())
            printParseTree(child, width + node.toString().length() - 1);
    }

    public void parse() {
        nextToken();
        program();
    }

    private void program() {
        while (token != null) {
            root.addChild(statement());
        }
    }

    private ParseNode expression() {
        ParseNode node = new ParseNode("EXPRESSION");
        if (!accept("IDENTIFIER", node)) expect("NUMBER", node);
        if (accept("ARITHMETIC_OPERATOR", node))
            if (!accept("IDENTIFIER", node)) expect("NUMBER", node);
        return node;
    }

    private ParseNode condition() {
        ParseNode node = new ParseNode("CONDITION");
        if (!accept("IDENTIFIER", node)) expect("NUMBER", node);
        expect("RELATIONAL_OPERATOR", node);
        if (!accept("IDENTIFIER", node)) expect("NUMBER", node);
        return node;
    }

    private ParseNode whileBlock() {
        ParseNode node = new ParseNode("WHILE_BLOCK");
        while (!accept("ENDWHILE", node)) node.addChild(statement());
        return node;
    }

    private ParseNode ifBlock() {
        ParseNode node = new ParseNode("IF_BLOCK");
        while (!accept("ENDIF", node)) node.addChild(statement());
        return node;
    }

    private ParseNode statement() {
        ParseNode node = new ParseNode("STATEMENT");
        if (accept("IDENTIFIER", node)) {
            node.setName("ASSIGNMENT");
            expect("EQUALS_SIGN", node);
            node.addChild(expression());
        } else if (accept("WHILE", node)) {
            node.setName("ITERATOR");
            node.addChild(condition());
            node.addChild(whileBlock());
        } else if (accept("IF", node)) {
            node.setName("SELECTION");
            node.addChild(condition());
            node.addChild(ifBlock());
        } else if (accept("PRINT_FUNC", node)) {
            node.setName("PRINT");
            if (!accept("IDENTIFIER", node)) expect("NUMBER", node);
        }
        return node;
    }

    private boolean accept(String name, ParseNode node) {
        if (token.name.equals(name)) {
            node.addChild(new ParseNode(token));
            if (name.equals("IDENTIFIER") && !identifiers.contains(token.value))
                identifiers.add(token.value);
            nextToken();
            return true;
        }
        return false;
    }

    private void expect(String name, ParseNode node) {
        if (!accept(name, node)) {
            System.out.println("Unexpected Symbol : " + token.value);
            System.exit(0);
        }
    }

    private void nextToken() {
        token = lexer.getToken();
    }
}
