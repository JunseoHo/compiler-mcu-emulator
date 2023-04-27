package main;

import cpu.CPU;
import exception.AddressingModeException;
import loader.Loader;
import memory.Memory;
import output.Monitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, AddressingModeException {
        if (args.length == 0) printFile("usage");
        else {
            String sourceFile = null;
            for (String arg : args) {
                switch (arg) {
                    case "-help" -> Option.HELP_OPTION = true;
                    case "-p" -> Option.PRINT_OPTION = true;
                    case "-d" -> Option.DEBUGGING_OPTION = true;
                    default -> sourceFile = "../../" + arg;
                }
            }
            if (Option.HELP_OPTION) printFile("help");
            else if (sourceFile != null) {
                CPU cpu = new CPU();
                Memory memory = new Memory();
                Loader loader = new Loader();
                Monitor monitor = new Monitor();

                loader.associate(cpu, memory);
                cpu.associate(memory);
                memory.associate(cpu);
                monitor.associate(memory);
                if (loader.load(sourceFile)) {
                    cpu.run();
                    monitor.run();
                }
            } else printFile("usage");
        }
    }

    public static void printFile(String pathName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(pathName));
        while (scanner.hasNextLine()) System.out.println(scanner.nextLine());
        scanner.close();
    }

}
