package main;

import generator.CodeGenerator;
import lexer.Lexer;
import parser.Parser;
import preprocessor.Preprocessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Preprocessor preprocessor = new Preprocessor();
        Lexer lexer = new Lexer(new File(Constant.SYMBOL_PATH_NAME));
        Parser parser = new Parser();
        CodeGenerator codeGenerator = new CodeGenerator();

        lexer.associate(preprocessor);
        parser.associate(lexer);
        codeGenerator.associate(parser);

        preprocessor.preprocess(readFile("sample_code/example01"));
        lexer.analysis();
        parser.parse();
        codeGenerator.generate();
    }

    private static String readFile(String pathName) throws FileNotFoundException {
        String file = "";
        Scanner scanner = new Scanner(new File(pathName));
        while (scanner.hasNextLine()) file += scanner.nextLine() + "\n";
        scanner.close();
        return file;
    }

}
