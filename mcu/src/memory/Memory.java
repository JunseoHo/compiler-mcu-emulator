package memory;

import cpu.CPU;

import java.util.List;

public class Memory {

    private CPU cpu;
    private int[] memory;

    public Memory() {
        this.memory = new int[1024];
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

    public void loadProgram(int entryPoint, List<String> instructions) {
        for (String instruction : instructions)
            memory[entryPoint++] = Integer.parseInt(instruction, 2);
    }

}
