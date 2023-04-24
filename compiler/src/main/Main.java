package main;

import generator.Generator;
import lexer.Lexer;
import parser.Parser;
import preprocessor.Preprocessor;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) printFile("usage");
        else {
            String sourceFile = null;
            for (String arg : args) {
                switch (arg) {
                    case "-help" -> Option.HELP_OPTION = true;
                    case "-p" -> Option.PRINT_OPTION = true;
                    default -> sourceFile = Constant.PATH_NAME + arg;
                }
            }
            String sourceCode = read(sourceFile);
            if (Option.HELP_OPTION) printFile("help");
            else if (sourceCode != null) {
                Preprocessor preprocessor = new Preprocessor();
                Lexer lexer = new Lexer();
                Parser parser = new Parser();
                Generator generator = new Generator();

                parser.associate(lexer);
                generator.associate(parser);

                sourceCode = preprocessor.preprocess(sourceCode);

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
                write(sourceFile + Constant.EXTENSION_NAME, generator.getInstructions());
            } else printFile("usage");
        }
    }

    public static String read(String filePath) {
        try {
            StringBuilder buffer = new StringBuilder();
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine())
                buffer.append(scanner.nextLine()).append("\n");
            scanner.close();
            return buffer.toString();
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static void write(String filePath, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(content);
        writer.close();
    }

    public static void printFile(String pathName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(pathName));
        while (scanner.hasNextLine()) System.out.println(scanner.nextLine());
        scanner.close();
    }

}
