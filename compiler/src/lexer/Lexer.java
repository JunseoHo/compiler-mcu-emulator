package lexer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Lexer {

    private final Queue<Token> symbolTable = new LinkedList<>();

    public void analysis(String sourceCode) {
        Scanner scanner = new Scanner(sourceCode);
        while (scanner.hasNext()) {
            String token = scanner.next();
            Symbol symbol = findSymbol(token);
            if (symbol == null) {
                System.out.println("Unknown Token : " + token);
                System.out.printf("%-20s%-7s\n", "Lexical Analysis", "KO :(");
                System.exit(0);
            }
            symbolTable.add(new Token(symbol, token));
        }
        scanner.close();
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
