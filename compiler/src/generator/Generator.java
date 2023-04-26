package generator;

import lexer.Symbol;
import lexer.Token;
import parser.Parser;
import parser.Statement;

import java.util.LinkedList;
import java.util.List;

public class Generator {

    private Parser parser;
    private List<String> instructions;
    private List<Identifier> idTable;

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
    private final String XOR = "1110";

    public Generator() {
        instructions = new LinkedList<>();
        idTable = new LinkedList<>();
    }

    public void associate(Parser parser) {
        this.parser = parser;
    }

    public boolean generate() {
        Token root = parser.getParseTree();
        for (Token child : root.children) {
            List<String> block = statement(child, instructions.size());
            if (block == null || checkNullContained(block))
                return false;
            instructions.addAll(block);
        }
        instructions.add(IM + HLT + toBinary(0));
        return true;
    }

    private List<String> statement(Token node, int entryPoint) {
        if (node.statement == Statement.DECLARATION) return declaration(node);
        else if (node.statement == Statement.ASSIGNMENT) return assignment(node);
        else if (node.statement == Statement.SELECTION) return selection(node, entryPoint);
        else if (node.statement == Statement.ITERATION) return iteration(node, entryPoint);
        else if (node.statement == Statement.PRINT) return print(node);
        else if (node.symbol == Symbol.ENDWHILE || node.symbol == Symbol.ENDIF) return new LinkedList<>();
        else {
            System.out.println("Unknown Statement : " + node.statement);
            System.out.printf("%-20s%-7s\n", "Code Generation", "KO :(");
            return null;
        }
    }

    private List<String> declaration(Token node) {
        List<String> block = new LinkedList<>(expression(node.getChild(3)));
        addIdentifier(node.getChild(0), node.getChild(1));
        block.add(DI + STO + toBinary(node.getChild(1)));
        return block;
    }

    private List<String> selection(Token node, int entryPoint) {
        List<String> block = new LinkedList<>();
        for (Token child : node.getChild(2).children)
            block.addAll(statement(child, entryPoint + block.size() + 3));
        int terminalPoint = entryPoint + block.size() + 4;
        block.addAll(0, condition(node, terminalPoint));
        return block;
    }

    private List<String> iteration(Token node, int entryPoint) {
        List<String> block = new LinkedList<>();
        for (Token child : node.getChild(2).children)
            block.addAll(statement(child, entryPoint + block.size() + 3));
        int terminalPoint = entryPoint + block.size() + 4;
        block.add(IM + JMP + toBinary(entryPoint));
        block.addAll(0, condition(node, terminalPoint));
        return block;
    }

    private List<String> condition(Token node, int terminalPoint) {
        List<String> block = new LinkedList<>();
        Token leftOperand = node.getChild(1).getChild(0);
        Token operator = node.getChild(1).getChild(1);
        Token rightOperand = node.getChild(1).getChild(2);
        if (leftOperand.symbol == Symbol.IDENTIFIER)
            block.add(DI + LDA + toBinary(leftOperand));
        else block.add(IM + LDA + toBinary(leftOperand));
        if (rightOperand.symbol == Symbol.IDENTIFIER)
            block.add(DI + SUB + toBinary(rightOperand));
        else block.add(IM + SUB + toBinary(rightOperand));
        if (operator.value.equals("!=")) operator.value = JPZ;
        if (operator.value.equals(">=")) operator.value = JPN;
        if (operator.value.equals("<=")) operator.value = JPP;
        block.add(IM + operator.value + toBinary(terminalPoint));
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
            if (operator.value.equals("&")) operator.value = AND;
            if (operator.value.equals("|")) operator.value = OR;
            if (operator.value.equals("^")) operator.value = XOR;
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

    private boolean checkNullContained(List<String> block) {
        for (String line : block) {
            if (line.contains("null"))
                return true;
        }
        return false;
    }

    private void addIdentifier(Token type, Token id) {
        Identifier identifier = new Identifier();
        identifier.name = id.value;
        identifier.type = type.value;
        for (DataType dataType : DataType.values()) {
            if (dataType.getName().equals(identifier.type)) {
                identifier.size = dataType.getSize();
                break;
            }
        }
        if (idTable.isEmpty()) identifier.address = 0;
        else {
            Identifier lastId = idTable.get(idTable.size() - 1);
            identifier.address = lastId.address + lastId.size;
        }
        idTable.add(identifier);
    }

    private String toBinary(Token token) {
        Integer value = null;
        if (token.symbol == Symbol.IDENTIFIER) {
            for (Identifier id : idTable) {
                if (id.name.equals(token.value)) {
                    value = id.address;
                    break;
                }
            }
        } else value = Integer.parseInt(token.value);
        if (value == null) {
            System.out.println("Unknown Identifier : " + token.value);
            System.out.printf("%-20s%-7s\n", "Code Generation", "KO :(");
            return null;
        }
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

    public String getInstructions() {
        String codes = "";
        for (String line : instructions)
            codes += line + "\n";
        return codes;
    }
}
