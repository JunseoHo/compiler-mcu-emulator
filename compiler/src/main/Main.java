package main;

import generator.Generator;
import lexer.Lexer;
import parser.Parser;
import preprocessor.Preprocessor;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            print(Constant.USAGE_FILE_PATH);
            return;
        }
        String filePath = null;
        for (String arg : args) {
            switch (arg) {
                case Option.HELP_FLAG -> Option.HELP_OPTION = true;
                case Option.PRINT_FLAG -> Option.PRINT_OPTION = true;
                default -> filePath = Constant.PATH_NAME + arg;
            }
        }
        if (Option.HELP_OPTION) print(Constant.HELP_FILE_PATH);
        else compile(filePath);
    }

    private static void compile(String filePath) throws IOException {
        String sourceCode;
        if ((sourceCode = read(filePath)) != null) {
            Preprocessor preprocessor = new Preprocessor();
            Lexer lexer = new Lexer();
            Parser parser = new Parser();
            Generator generator = new Generator();

            parser.associate(lexer);
            generator.associate(parser);

            sourceCode = preprocessor.preprocess(sourceCode);
            System.out.printf("%-20s%-7s\n", "Preprocess", "OK :)");

            if (!lexer.analysis(sourceCode)) return;
            if (Option.PRINT_OPTION) lexer.printSymbolTable();
            System.out.printf("%-20s%-7s\n", "Lexical Analysis", "OK :)");

            if (!parser.parse()) return;
            if (Option.PRINT_OPTION) parser.printParseTree();
            System.out.printf("%-20s%-7s\n", "Parsing", "OK :)");

            if (!generator.generate()) return;
            if (Option.PRINT_OPTION) generator.printInstructions();
            System.out.printf("%-20s%-7s\n", "Code Generation", "OK :)");

            System.out.println("The source code has been compiled successfully!");
            write(filePath + Constant.EXTENSION_NAME, generator.getInstructions());
        } else print(Constant.USAGE_FILE_PATH);
    }

    private static String read(String filePath) {
        if (filePath == null) return null;
        try {
            StringBuilder buffer = new StringBuilder();
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) buffer.append(scanner.nextLine()).append("\n");
            scanner.close();
            return buffer.toString();
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private static void write(String filePath, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(content);
        writer.close();
    }

    private static void print(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        while (scanner.hasNextLine()) System.out.println(scanner.nextLine());
        scanner.close();
    }

}
