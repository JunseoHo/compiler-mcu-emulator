package memory;

import cpu.CPU;

import java.util.LinkedList;
import java.util.List;

public class Memory {

    private CPU cpu;
    private int[] memory;
    private List<String> outputBuffer;

    public Memory() {
        this.memory = new int[1024];
        this.outputBuffer = new LinkedList<>();
    }

    public void associate(CPU cpu) {
        this.cpu = cpu;
    }

    public void load() {
        cpu.setMBR(memory[cpu.getMAR()]);
    }

    public void store() {
        memory[cpu.getMAR()] = cpu.getMBR();
    }

    public void writeOutput(String s) {
        outputBuffer.add(s);
    }

    public String getOutputBuffer() {
        if (outputBuffer.isEmpty()) return null;
        String data = "";
        for (String str : outputBuffer)
            data += str + "\n";
        outputBuffer.clear();
        return data;
    }

    public void loadProgram(int entryPoint, List<String> instructions) {
        for (String instruction : instructions)
            memory[entryPoint++] = Integer.parseInt(instruction, 2);
    }

}
