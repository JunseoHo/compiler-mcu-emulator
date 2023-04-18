package code_generator;

import lexer.Symbol;
import lexer.Token;
import parser.ParseNode;
import parser.Parser;
import parser.Statement;

import java.util.LinkedList;
import java.util.List;

public class Generator {

    private Parser parser;
    private List<String> instructions;
    private List<String> idTable;

    // addressing mode
    private final String IM = "00";
    private final String DI = "01";
    private final String IN = "11";

    // opcode
    private final String HLT = "0000";
    private final String PRT = "0001";
    private final String STO = "0010";
    private final String LDA = "0011";
    private final String ADD = "0100";
    private final String SUB = "0101";
    private final String MUL = "0110";
    private final String DIV = "0111";
    private final String JMP = "1000";
    private final String JPZ = "1001";
    private final String JPN = "1010";
    private final String JPP = "1011";
    private final String AND = "1100";
    private final String OR = "1101";
    private final String NOT = "1110";
    private final String XOR = "1111";

//    private final String IM = "IM   ";
//    private final String DI = "DI   ";
//    private final String IN = "IN   ";
//
//    private final String HLT = "HLT ";
//    private final String PRT = "PRT ";
//    private final String STO = "STO ";
//    private final String LDA = "LDA ";
//    private final String ADD = "ADD ";
//    private final String SUB = "SUB ";
//    private final String MUL = "MUL ";
//    private final String DIV = "DIV ";
//    private final String JMP = "JMP ";
//    private final String JPZ = "JPZ ";
//    private final String JPN = "JPN ";
//    private final String JPP = "JPP ";
//    private final String AND = "AND ";
//    private final String OR = "OR   ";
//    private final String NOT = "NOT ";
//    private final String XOR = "XOR ";

    public Generator() {
        instructions = new LinkedList<>();
        idTable = new LinkedList<>();
    }

    public void associate(Parser parser) {
        this.parser = parser;
    }

    public void generate() {
        Token root = parser.getParseTree();
        for (Token child : root.children)
            instructions.addAll(statement(child));
        instructions.add(IM + HLT + toBinary(0));
    }

    private List<String> statement(Token node) {
        if (node.statement == Statement.ASSIGNMENT) return assignment(node);
        else if (node.statement == Statement.SELECTION) return selection(node);
        else if (node.statement == Statement.ITERATION) return iteration(node);
        else if (node.statement == Statement.PRINT) return print(node);
        else if (node.symbol == Symbol.ENDWHILE || node.symbol == Symbol.ENDIF)
            return new LinkedList<>();
        else {
            System.out.println("Unknown Statement : " + node.statement);
            System.out.printf("%-20s%-7s\n", "Code Generation", "KO :(");
            System.exit(0);
        }
        return null;
    }

    private List<String> selection(Token node) {
        List<String> block = new LinkedList<>();
        int entryPoint = instructions.size();
        for (Token child : node.getChild(2).children)
            block.addAll(statement(child));
        int terminalPoint = entryPoint + block.size() + 4;
        Token leftOperand = node.getChild(1).getChild(0);
        Token operator = node.getChild(1).getChild(1);
        Token rightOperand = node.getChild(1).getChild(2);
        if (operator.value.equals("!=")) operator.value = JPZ;
        if (operator.value.equals(">")) operator.value = JPN;
        if (operator.value.equals("<")) operator.value = JPP;
        block.add(0, IM + operator.value + toBinary(terminalPoint));
        if (rightOperand.symbol == Symbol.IDENTIFIER)
            block.add(0, DI + SUB + toBinary(rightOperand));
        else block.add(0, IM + SUB + toBinary(rightOperand));
        if (leftOperand.symbol == Symbol.IDENTIFIER)
            block.add(0, DI + LDA + toBinary(leftOperand));
        else block.add(0, IM + LDA + toBinary(leftOperand));
        return block;
    }

    private List<String> iteration(Token node) {
        List<String> block = new LinkedList<>();
        int entryPoint = instructions.size();
        for (Token child : node.getChild(2).children)
            block.addAll(statement(child));
        int terminalPoint = entryPoint + block.size() + 4;
        block.add(IM + JMP + toBinary(entryPoint));
        Token leftOperand = node.getChild(1).getChild(0);
        Token operator = node.getChild(1).getChild(1);
        Token rightOperand = node.getChild(1).getChild(2);
        if (operator.value.equals("!=")) operator.value = JPZ;
        if (operator.value.equals(">")) operator.value = JPN;
        if (operator.value.equals("<")) operator.value = JPP;
        block.add(0, IM + operator.value + toBinary(terminalPoint));
        if (rightOperand.symbol == Symbol.IDENTIFIER)
            block.add(0, DI + SUB + toBinary(rightOperand));
        else block.add(0, IM + SUB + toBinary(rightOperand));
        if (leftOperand.symbol == Symbol.IDENTIFIER)
            block.add(0, DI + LDA + toBinary(leftOperand));
        else block.add(0, IM + LDA + toBinary(leftOperand));
        return block;
    }


    private List<String> expression(Token node) {
        List<String> block = new LinkedList<>();
        if (node.children.size() == 1) {
            if (node.getChild(0).symbol == Symbol.IDENTIFIER)
                block.add(DI + LDA + toBinary(node.getChild(0)));
            else block.add(IM + LDA + toBinary(node.getChild(0)));
            return block;
        }
        Token leftOperand = node.getChild(0);
        Token operator = node.getChild(1);
        Token rightOperand = node.getChild(2);
        if (leftOperand.symbol == Symbol.IDENTIFIER)
            block.add(DI + LDA + toBinary(leftOperand));
        else block.add(IM + LDA + toBinary(leftOperand));
        if (node.children.size() > 1) {
            if (operator.value.equals("+")) operator.value = ADD;
            if (operator.value.equals("-")) operator.value = SUB;
            if (operator.value.equals("*")) operator.value = MUL;
            if (operator.value.equals("/")) operator.value = DIV;
            if (rightOperand.symbol == Symbol.IDENTIFIER)
                block.add(DI + operator.value + toBinary(rightOperand));
            else block.add(IM + operator.value + toBinary(rightOperand));
        }
        return block;
    }

    private List<String> assignment(Token node) {
        List<String> block = new LinkedList<>(expression(node.getChild(2)));
        block.add(DI + STO + toBinary(node.getChild(0)));
        return block;
    }

    private List<String> print(Token node) {
        List<String> block = new LinkedList<>();
        if (node.getChild(1).symbol == Symbol.IDENTIFIER)
            block.add(DI + PRT + toBinary(node.getChild(1)));
        else block.add(IM + PRT + toBinary(node.getChild(1)));
        return block;
    }

    private String toBinary(Token token) {
        int value = 0;
        if (token.symbol == Symbol.IDENTIFIER) {
            int index = idTable.indexOf(token.value);
            if (index == -1) {
                idTable.add(token.value);
                value = idTable.size() - 1;
            } else value = index;
        } else value = Integer.parseInt(token.value);
        return toBinary(value);
    }

    private String toBinary(int value) {
        String operand = "";
        operand += Integer.toBinaryString(value);
        while (operand.length() < 26) operand = "0" + operand;
        return operand;
    }

    public void printInstructions() {
        for (String instruction : instructions) System.out.println(instruction);
    }
}
