package parser;

import exception.ParseException;
import lexer.Lexer;
import lexer.Token;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

public class Parser {

    private Lexer lexer;
    private Map<String, String> identifiers;
    private ParseNode head;

    public Parser() throws FileNotFoundException {
        identifiers = new HashMap<>();
        head = new ParseNode(null);
    }

    public void associate(Lexer lexer) {
        this.lexer = lexer;
    }

    public void parse() throws ParseException {
        Token token = null;
        while ((token = lexer.getToken()) != null) {
            switch (token.getName()) {
                case "IDENTIFIER", "LABEL" -> productDeclareStatement(token);
                case "TRANSFER_OPERATOR" -> productTransferStatement(token);
                case "ARITHMETIC_OPERATOR", "CONTROLFLOW_OPERATOR", "MEMORY_OPERATOR" ->
                        productArithmeticStatement(token);
                case "TERMINATE_OPERATOR" -> head.addChild(new ParseNode(token));
                default -> throw new ParseException("Unknown Token : " + token.getValue());
            }
        }
        changeIdentifiers(head);
        printParseTree();
    }

    private void changeIdentifiers(ParseNode node) {
        if (node.token != null && node.token.getName().equals("IDENTIFIER")) {
            if (Pattern.matches("^[A-Z]*$", node.token.getValue())) {
                node.token = new Token("NUMBER", "[" + identifiers.get(node.token.getValue()) + "]");
            } else
                node.token = new Token("MEMORY_ADDRESS", identifiers.get(node.token.getValue()));
        }
        for (ParseNode child : node.children) {
            changeIdentifiers(child);
        }
    }

    private void productArithmeticStatement(Token token) throws ParseException {
        head.addChild(new ParseNode(token));
        Token operand = lexer.getToken();
        if (!operand.getName().equals("MEMORY_ADDRESS") && !operand.getName().equals("NUMBER") && !operand.getName().equals("IDENTIFIER"))
            throw new ParseException("Unknown Token : " + token.getValue());
        head.getLastChild().addChild(new ParseNode(operand));
    }

    private void productTransferStatement(Token token) throws ParseException {
        head.addChild(new ParseNode(token));
        Token operand = lexer.getToken();
        if (!operand.getName().equals("REGISTER"))
            throw new ParseException("Unknown Token : " + token.getValue());
        head.getLastChild().addChild(new ParseNode(operand));
        operand = lexer.getToken();
        if (!operand.getName().equals("MEMORY_ADDRESS") && !operand.getName().equals("NUMBER") && !operand.getName().equals("IDENTIFIER"))
            throw new ParseException("Unknown Token : " + token.getValue());
        head.getLastChild().addChild(new ParseNode(operand));
    }

    private void productDeclareStatement(Token token) throws ParseException {
        String identifierName = token.getValue();
        if (token.getName().equals("IDENTIFIER")) {
            token = lexer.getToken();
            if (!token.getName().equals("MEMORY_ADDRESS"))
                throw new ParseException("Unknown Token : " + token.getValue());
            identifiers.put(identifierName, token.getValue());
        } else if (token.getName().equals("LABEL")) {
            identifiers.put(identifierName.substring(0, identifierName.length() - 1), String.valueOf(head.children.size()));
        }
    }

    public ParseNode getParseTree() {
        return head;
    }

    public void printParseTree() {
        System.out.println("========================================");
        System.out.println("============== Parse Tree ==============");
        System.out.println("========================================");
        printParseNode(head, 0);
    }

    public void printParseNode(ParseNode node, int depth) {
        if (node.token != null) {
            for (int i = 0; i < depth; i++)
                System.out.print("\t");
            System.out.println(node.token.getValue());
        } else
            System.out.println("PROGRAM");

        for (ParseNode child : node.children) {
            printParseNode(child, depth + 1);
        }
    }

}
