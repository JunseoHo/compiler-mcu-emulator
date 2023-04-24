package lexer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Lexer {

    private final Queue<Token> symbolTable = new LinkedList<>();

    public boolean analysis(String sourceCode) {
        Scanner scanner = new Scanner(sourceCode);
        while (scanner.hasNext()) {
            String lexeme = scanner.next();
            Symbol symbol = findSymbol(lexeme);
            if (symbol == null) {
                System.out.println("Unknown Token : " + lexeme);
                System.out.printf("%-20s%-7s\n", "Lexical Analysis", "KO :(");
                return false;
            }
            symbolTable.add(new Token(symbol, lexeme));
        }
        scanner.close();
        return true;
    }

    private Symbol findSymbol(String value) {
        for (Symbol symbol : Symbol.values())
            if (Pattern.matches(symbol.getRegex(), value)) return (symbol);
        return null;
    }

    public Token getToken() {
        return symbolTable.isEmpty() ? null : symbolTable.poll();
    }

    public void printSymbolTable() {
        if (symbolTable.isEmpty()) {
            System.out.println("Symbol table is empty");
            return;
        }
        System.out.printf("%20s%20s\n", "Token Name", "Token Value");
        System.out.println("----------------------------------------");
        for (Token token : symbolTable)
            System.out.printf("%20s%20s\n", token.symbol, token.value);
    }

}
