package parser;

import lexer.Lexer;
import lexer.Symbol;
import lexer.Token;

public class Parser {

    private Lexer lexer;
    private Token token;
    private Token root;

    public void associate(Lexer lexer) {
        this.lexer = lexer;
    }

    public Token getParseTree() {
        return root;
    }

    public void printParseTree() {
        printParseTree(root, 0);
    }

    private void printParseTree(Token node, int width) {
        for (int i = 0; i < width; i++)
            System.out.print(" ");
        if (width > 0)
            System.out.print("L ");
        System.out.println(node);
        for (Token child : node.children)
            printParseTree(child, width + node.toString().length() - 1);
    }

    public boolean parse() {
        nextToken();
        return program();
    }

    private boolean program() {
        root = new Token(Statement.PROGRAM);
        while (token != null) {
            Token token = statement();
            if (token == null) {
                System.out.println("Unexpected Symbol : " + token.value);
                System.out.printf("%-20s%-7s\n", "Parsing", "KO :(");
                return false;
            }
            root.addChild(token);
        }
        return true;
    }

    private Token statement() {
        Token node = new Token();
        if (accept(Symbol.DATA_TYPE, node)) {
            node.statement = Statement.DECLARATION;
            expect(Symbol.IDENTIFIER, node);
            expect(Symbol.EQUALS_SIGN, node);
            node.addChild(expression());
        } else if (accept(Symbol.IDENTIFIER, node)) {
            node.statement = Statement.ASSIGNMENT;
            expect(Symbol.EQUALS_SIGN, node);
            node.addChild(expression());
        } else if (accept(Symbol.WHILE, node)) {
            node.statement = Statement.ITERATION;
            node.addChild(condition());
            node.addChild(whileBlock());
        } else if (accept(Symbol.IF, node)) {
            node.statement = Statement.SELECTION;
            node.addChild(condition());
            node.addChild(ifBlock());
        } else if (accept(Symbol.PRINT_FUNC, node)) {
            node.statement = Statement.PRINT;
            if (!accept(Symbol.IDENTIFIER, node)) expect(Symbol.NUMBER, node);
        } else return null;
        return node;
    }

    private Token expression() {
        Token node = new Token(Statement.EXPRESSION);
        if (!accept(Symbol.IDENTIFIER, node)) expect(Symbol.NUMBER, node);
        if (accept(Symbol.ARITHMETIC_OPERATOR, node))
            if (!accept(Symbol.IDENTIFIER, node)) expect(Symbol.NUMBER, node);
        return node;
    }

    private Token condition() {
        Token node = new Token(Statement.CONDITION);
        if (!accept(Symbol.IDENTIFIER, node)) expect(Symbol.NUMBER, node);
        expect(Symbol.RELATIONAL_OPERATOR, node);
        if (!accept(Symbol.IDENTIFIER, node)) expect(Symbol.NUMBER, node);
        return node;
    }

    private Token whileBlock() {
        Token node = new Token(Statement.BRANCH);
        while (!accept(Symbol.ENDWHILE, node)) node.addChild(statement());
        return node;
    }

    private Token ifBlock() {
        Token node = new Token(Statement.BRANCH);
        while (!accept(Symbol.ENDIF, node)) node.addChild(statement());
        return node;
    }

    private boolean accept(Symbol symbol, Token node) {
        if (token.symbol == symbol) {
            node.addChild(token);
            nextToken();
            return true;
        }
        return false;
    }

    private void expect(Symbol symbol, Token node) {
        if (!accept(symbol, node)) {
            System.out.println("Unexpected Symbol : " + token.value);
            System.out.printf("%-20s%-7s\n", "Parsing", "KO :(");
            System.exit(0);
        }
    }

    private void nextToken() {
        token = lexer.getToken();
    }
}
