package main;

import code_generator.Generator;
import lexer.Lexer;
import parser.Parser;
import preprocessor.Preprocessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String sourceCode;
        if (args.length != 0 || (sourceCode = read((new File("compiler/sample/sample01")))) == null) {
            System.out.print("Source file not found");
            return;
        }

        Preprocessor preprocessor = new Preprocessor();
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Generator generator = new Generator();

        parser.associate(lexer);
        generator.associate(parser);

        sourceCode = preprocessor.preprocess(sourceCode);

        lexer.analysis(sourceCode);
        if (Option.PRINT_OPTION) lexer.printSymbolTable();
        System.out.printf("%-20s%-7s\n", "Lexical Analysis", "OK :)");

        parser.parse();
        if (Option.PRINT_OPTION) parser.printParseTree();
        System.out.printf("%-20s%-7s\n", "Parsing", "OK :)");

        generator.generate();
        if (Option.PRINT_OPTION) generator.printInstructions();
        System.out.printf("%-20s%-7s\n", "Code Generation", "OK :)");

        System.out.println("The source code has been compiled successfully!");
    }

    public static String read(File file) {
        try {
            StringBuilder buffer = new StringBuilder();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine())
                buffer.append(scanner.nextLine()).append("\n");
            scanner.close();
            return buffer.toString();
        } catch (FileNotFoundException e) {
            return null;
        }
    }

}
