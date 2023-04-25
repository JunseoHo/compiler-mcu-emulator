package loader;

import cpu.CPU;
import memory.Memory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Loader {

    private CPU cpu;
    private Memory memory;

    public void associate(CPU cpu, Memory memory) {
        this.cpu = cpu;
        this.memory = memory;
    }

    public boolean load(String pathName) {
        List<String> instructions = new LinkedList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(pathName));
            while (scanner.hasNextLine()) instructions.add(scanner.nextLine());
            cpu.initialize(0, instructions.size());
            memory.loadProgram(0, instructions);
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Source not found : " + pathName);
            return false;
        }
        return true;
    }
}
