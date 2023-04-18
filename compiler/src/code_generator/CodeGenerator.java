package code_generator;

import lexer.Token;
import parser.ParseNode;
import parser.Parser;

import java.util.LinkedList;
import java.util.List;

public class CodeGenerator {

    private Parser parser;
    private LinkedList<String> instructions;
    private LinkedList<String> identifiers;

    private final String IM = "00";
    private final String DI = "01";
    private final String IN = "11";

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

    public CodeGenerator() {
        this.instructions = new LinkedList<>();
        this.identifiers = new LinkedList<>();
    }

    public void associate(Parser parser) {
        this.parser = parser;
    }

    public void generate() {
        ParseNode root = parser.getParserTree();
        for (ParseNode child : root.getChildren()) {
            instructions.addAll(generate(child));
        }
        instructions.add(IM + HLT + convertOperand(0));
        for (String i : instructions)
            System.out.println(i);
    }

    public LinkedList<String> generate(ParseNode node) {
        LinkedList<String> procedure = new LinkedList<>();
        if (node.getName().equals("ASSIGNMENT")) {
            procedure.addAll(expression(node.getChild(2)));
            procedure.add(DI + STO + convertOperand(node.getChild(0).getToken()));
        } else if (node.getName().equals("ITERATOR")) {
            int entryPoint = instructions.size();
            for (ParseNode child : node.getChild(2).getChildren())
                procedure.addAll(generate(child));
            int terminalPoint = entryPoint + 3 + procedure.size() + 1;
            procedure.add(IM + JMP + convertOperand(entryPoint));
            String relationalOperator = node.getChild(1).getChild(1).getToken().value;
            if (relationalOperator.equals("!="))
                procedure.addFirst(IM + JPZ + convertOperand(terminalPoint));
            if (relationalOperator.equals(">"))
                procedure.addFirst(IM + JPN + convertOperand(terminalPoint));
            if (relationalOperator.equals("<"))
                procedure.addFirst(IM + JPP + convertOperand(terminalPoint));
            if (node.getChild(1).getChild(2).getToken().name.equals("IDENTIFIER")) {
                procedure.addFirst(DI + SUB + convertOperand(node.getChild(1).getChild(2).getToken()));
            } else {
                procedure.addFirst(IM + SUB + convertOperand(node.getChild(1).getChild(2).getToken()));
            }
            if (node.getChild(1).getChild(0).getToken().name.equals("IDENTIFIER")) {
                procedure.addFirst(DI + LDA + convertOperand(node.getChild(1).getChild(0).getToken()));
            } else {
                procedure.addFirst(IM + LDA + convertOperand(node.getChild(1).getChild(0).getToken()));
            }
        } else if (node.getName().equals("PRINT")) {
            if (node.getChild(1).getName().equals("IDENTIFIER"))
                procedure.add(DI + PRT + convertOperand(node.getChild(1).getToken()));
            else
                procedure.add(IM + PRT + convertOperand(node.getChild(1).getToken()));
        }
        return procedure;
    }

    private List<String> expression(ParseNode child) {
        String instruction = "";
        List<String> procedure = new LinkedList<>();
        if (child.getChild(0).getName().equals("IDENTIFIER")) {
            instruction += DI + LDA + convertOperand(child.getChild(0).getToken());
        } else
            instruction += IM + LDA + convertOperand(child.getChild(0).getToken());
        procedure.add(instruction);
        if (child.getChildren().size() > 1) {
            instruction = "";
            String operator = child.getChild(1).getToken().value;
            if (operator.equals("+")) instruction += ADD;
            if (operator.equals("-")) instruction += SUB;
            if (operator.equals("*")) instruction += MUL;
            if (operator.equals("/")) instruction += DIV;
            if (child.getChild(2).getName().equals("IDENTIFIER")) {
                instruction = DI + instruction + convertOperand(child.getChild(2).getToken());
            } else
                instruction = IM + instruction + convertOperand(child.getChild(2).getToken());
            procedure.add(instruction);
        }
        return procedure;
    }

    private void assignment() {

    }

    private String convertOperand(int i) {
        int value = i;
        String operand = "";
        operand += Integer.toBinaryString(value);
        while (operand.length() < 26) operand = "0" + operand;
        return operand;
    }

    private String convertOperand(Token token) {
        int value = 0;
        String operand = "";
        if (token.name.equals("IDENTIFIER")) {
            int index = identifiers.indexOf(token.value);
            if (index == -1) {
                identifiers.add(token.value);
                value = identifiers.size() - 1;
            } else value = index;
        } else value = Integer.parseInt(token.value);
        operand += Integer.toBinaryString(value);
        while (operand.length() < 26) operand = "0" + operand;
        return operand;
    }

}
