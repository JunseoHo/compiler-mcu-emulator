package generator;

import exception.CodeGenerateException;
import lexer.Token;
import main.Constant;
import parser.ParseNode;
import parser.Parser;

public class CodeGenerator {

    private Parser parser;
    private String machineCode;

    public void associate(Parser parser) {
        this.parser = parser;
        machineCode = "";
    }

    public void generate() throws CodeGenerateException {
        ParseNode head = parser.getParseTree();
        for (ParseNode node : head.children)
            machineCode = machineCode + createCommand(node) + "\n";
        System.out.println(Constant.CODE_GENERATOR_PRINT_HEADER);
        System.out.println(machineCode);
    }

    private String createCommand(ParseNode node) throws CodeGenerateException {
        String command = "";
        switch (node.token.getValue()) {
            case "MOV":
                if (node.children.get(0).token.getValue().equals("AX"))
                    command += "0011";
                if (node.children.get(1).token.getName().equals("NUMBER"))
                    command = "00" + command;
                else if (node.children.get(1).token.getName().equals("MEMORY_ADDRESS"))
                    command = "01" + command;
                command += extractValue(node.children.get(1).token);
                return command;
            case "STO":
                command = "010010";
                command += extractValue(node.children.get(0).token);
                return command;
            case "LDA":
                command = "010011";
                command += extractValue(node.children.get(0).token);
                return command;
            case "ADD":
                command = "0100";
                if (node.children.get(0).token.getName().equals("NUMBER"))
                    command = "00" + command;
                else if (node.children.get(0).token.getName().equals("MEMORY_ADDRESS"))
                    command = "01" + command;
                command += extractValue(node.children.get(0).token);
                return command;
            case "SUB":
                command = "0101";
                if (node.children.get(0).token.getName().equals("NUMBER"))
                    command = "00" + command;
                else if (node.children.get(0).token.getName().equals("MEMORY_ADDRESS"))
                    command = "01" + command;
                command += extractValue(node.children.get(0).token);
                return command;
            case "JPZ":
                command = "001001";
                command += extractValue(node.children.get(0).token);
                return command;
            case "JMP":
                command = "001000";
                command += extractValue(node.children.get(0).token);
                return command;
            case "HLT":
                return "00000000000000000000000000000000";
            default:
                throw new CodeGenerateException("Unknown Operator : " + node.token.getValue());
        }
    }

    private String extractValue(Token token) {
        String value;
        String bin = "";
        if (token.getName().equals("NUMBER"))
            value = token.getValue().substring(1, token.getValue().length() - 1);
        else
            value = token.getValue();
        bin = Integer.toBinaryString(Integer.parseInt(value));
        while (bin.length() < 26)
            bin = "0" + bin;
        return bin;
    }

}
