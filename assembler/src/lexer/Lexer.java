package lexer;

import exception.LexerException;
import main.Constant;
import preprocessor.Preprocessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

public class Lexer {

    private Preprocessor preprocessor;
    private List<Symbol> symbols;
    private Queue<Token> symbolTable;

    public Lexer(File symbolFile) throws FileNotFoundException {
        this.symbolTable = new LinkedList<>();
        this.symbols = createSymbolList(symbolFile);
    }

    public void associate(Preprocessor preprocessor) {
        this.preprocessor = preprocessor;
    }

    public void analysis() throws LexerException {
        Scanner scanner = new Scanner(preprocessor.getCode());
        if (!scanner.nextLine().equals(".header")) throw new LexerException("Header is not found");
        String lexeme;
        while (scanner.hasNext() && !(lexeme = scanner.next()).equals(".code")) addToken(lexeme);
        while (scanner.hasNext() && !(lexeme = scanner.next()).equals(".end")) addToken(lexeme);
        printSymbolQueue();
    }

    public Token getToken() {
        return symbolTable.isEmpty() ? null : symbolTable.poll();
    }


    private List<Symbol> createSymbolList(File symbolFile) throws FileNotFoundException {
        List<Symbol> symbolList = new LinkedList<>();
        Scanner scanner = new Scanner(symbolFile);
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("=");
            symbolList.add(new Symbol(line[0], line[1]));
        }
        scanner.close();
        return symbolList;
    }

    private void addToken(String lexeme) throws LexerException {
        for (Symbol symbol : symbols) {
            if (Pattern.matches(symbol.getRegex(), lexeme)) {
                symbolTable.add(new Token(symbol.getName(), lexeme));
                return;
            }
        }
        throw new LexerException("Unknown Token : " + lexeme);
    }

    private void printSymbolQueue() {
        System.out.println(Constant.LEXER_PRINT_HEADER);
        System.out.printf("%20s%20s\n", Constant.TOKEN_NAME, Constant.TOKEN_VALUE);
        System.out.println(Constant.HORIZONTAL_LINE);
        for (Token token : symbolTable) System.out.printf("%20s%20s\n", token.getName(), token.getValue());
    }
}
