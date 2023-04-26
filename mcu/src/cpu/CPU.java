package cpu;

import exception.AddressingModeException;
import main.Mnemonic;
import main.Option;
import memory.Memory;

import java.util.Scanner;

public class CPU {

    // associations
    private Memory memory;

    // registers
    private int PC;
    private int MBR;
    private int MAR;
    private int AC;

    // instruction registers
    private int IC;
    private int AMODE;
    private int OPCODE;
    private int OPERAND;

    // segment registers
    private int CS;
    private int DS;

    // status flags
    private boolean ZERO_FLAG;
    private boolean NEGATIVE_FLAG;

    // cpu status
    private boolean isRunning;

    public void associate(Memory memory) {
        this.memory = memory;
    }

    public void initialize(int codeEntryPoint, int dataEntryPoint) {
        this.PC = 0;
        this.MBR = 0;
        this.MAR = 0;
        this.AC = 0;
        this.IC = 0;
        this.AMODE = 0;
        this.OPCODE = 0;
        this.OPERAND = 0;
        this.CS = codeEntryPoint;
        this.DS = dataEntryPoint;
        this.ZERO_FLAG = false;
        this.NEGATIVE_FLAG = false;
        this.isRunning = true;
    }

    public int getMAR() {
        return MAR;
    }

    public int getMBR() {
        return MBR;
    }

    public void setMBR(int value) {
        this.MBR = value;
    }

    public void run() throws AddressingModeException {
        while (isRunning) {
            fetch();
            ++PC;
            decode();
            execute();
            if (Option.PRINT_OPTION) printInstruction();
            if (Option.DEBUGGING_OPTION) new Scanner(System.in).nextLine();
        }
    }

    private void fetch() {
        MAR = CS + PC;
        memory.load();
        IC = MBR;
    }

    private void decode() {
        AMODE = IC >> 30;
        OPCODE = (IC & 0x3C000000) >> 26;
        OPERAND = (IC & 0x3ffffff);
    }

    private void execute() throws AddressingModeException {
        switch (OPCODE) {
            case 0x00 -> hlt();
            case 0x01 -> prt();
            case 0x02 -> sto();
            case 0x03 -> lda();
            case 0x04 -> add();
            case 0x05 -> sub();
            case 0x06 -> mul();
            case 0x07 -> div();
            case 0x08 -> jmp();
            case 0x09 -> jpz();
            case 0x0A -> jpn();
            case 0x0B -> jpp();
            case 0x0C -> and();
            case 0x0D -> or();
            case 0x0E -> xor();
        }
    }

    private void hlt() {
        isRunning = false;
    }

    private void prt() throws AddressingModeException {
        if (AMODE == 0x00) System.out.printf("%#X\n", OPERAND);
        else if (AMODE == 0x01) {
            MAR = DS + OPERAND;
            memory.load();
            System.out.printf("%d\n", MBR);
        } else if (AMODE == 0x02) {
            MAR = DS + OPERAND;
            memory.load();
            MAR = DS + MBR;
            memory.load();
            System.out.printf("%d\n", MBR);
        } else
            throw new AddressingModeException("Addressing mode is invalid : " + AMODE);
    }

    private void sto() {
        MAR = DS + OPERAND;
        MBR = AC;
        memory.store();
    }

    private void lda() throws AddressingModeException {
        if (AMODE == 0x00) AC = OPERAND;
        else if (AMODE == 0x01) {
            MAR = DS + OPERAND;
            memory.load();
            AC = MBR;
        } else if (AMODE == 0x02) {
            MAR = DS + OPERAND;
            memory.load();
            MAR = DS + MBR;
            memory.load();
            AC = MBR;
        } else
            throw new AddressingModeException("Addressing mode is invalid : " + AMODE);
    }

    private void add() throws AddressingModeException {
        if (AMODE == 0x00) AC += OPERAND;
        else if (AMODE == 0x01) {
            MAR = DS + OPERAND;
            memory.load();
            AC += MBR;
        } else if (AMODE == 0x02) {
            MAR = DS + OPERAND;
            memory.load();
            MAR = DS + MBR;
            memory.load();
            AC += MBR;
        } else
            throw new AddressingModeException("Addressing mode is invalid : " + AMODE);
        ZERO_FLAG = AC == 0;
        NEGATIVE_FLAG = AC < 0;
    }

    private void sub() throws AddressingModeException {
        if (AMODE == 0x00) AC -= OPERAND;
        else if (AMODE == 0x01) {
            MAR = DS + OPERAND;
            memory.load();
            AC -= MBR;
        } else if (AMODE == 0x02) {
            MAR = DS + OPERAND;
            memory.load();
            MAR = DS + MBR;
            memory.load();
            AC -= MBR;
        } else
            throw new AddressingModeException("Addressing mode is invalid : " + AMODE);
        ZERO_FLAG = AC == 0;
        NEGATIVE_FLAG = AC < 0;
    }

    private void mul() throws AddressingModeException {
        if (AMODE == 0x00) AC -= OPERAND;
        else if (AMODE == 0x01) {
            MAR = DS + OPERAND;
            memory.load();
            AC *= MBR;
        } else if (AMODE == 0x02) {
            MAR = DS + OPERAND;
            memory.load();
            MAR = DS + MBR;
            memory.load();
            AC *= MBR;
        } else
            throw new AddressingModeException("Addressing mode is invalid : " + AMODE);
        ZERO_FLAG = AC == 0;
        NEGATIVE_FLAG = AC < 0;
    }

    private void div() throws AddressingModeException {
        if (AMODE == 0x00) AC -= OPERAND;
        else if (AMODE == 0x01) {
            MAR = DS + OPERAND;
            memory.load();
            AC /= MBR;
        } else if (AMODE == 0x02) {
            MAR = DS + OPERAND;
            memory.load();
            MAR = DS + MBR;
            memory.load();
            AC /= MBR;
        } else
            throw new AddressingModeException("Addressing mode is invalid : " + AMODE);
        ZERO_FLAG = AC == 0;
        NEGATIVE_FLAG = AC < 0;
    }


    private void jmp() {
        PC = OPERAND;
    }

    private void jpz() {
        if (ZERO_FLAG) PC = OPERAND;
    }

    private void jpn() {
        if (NEGATIVE_FLAG) PC = OPERAND;
    }

    private void jpp() {
        if (!NEGATIVE_FLAG && !ZERO_FLAG) PC = OPERAND;
    }

    private void and() throws AddressingModeException {
        if (AMODE == 0x00) AC &= OPERAND;
        else if (AMODE == 0x01) {
            MAR = DS + OPERAND;
            memory.load();
            AC &= MBR;
        } else if (AMODE == 0x02) {
            MAR = DS + OPERAND;
            memory.load();
            MAR = DS + MBR;
            memory.load();
            AC &= MBR;
        } else
            throw new AddressingModeException("Addressing mode is invalid : " + AMODE);
        ZERO_FLAG = AC == 0;
    }

    private void or() throws AddressingModeException {
        if (AMODE == 0x00) AC |= OPERAND;
        else if (AMODE == 0x01) {
            MAR = DS + OPERAND;
            memory.load();
            AC |= MBR;
        } else if (AMODE == 0x02) {
            MAR = DS + OPERAND;
            memory.load();
            MAR = DS + MBR;
            memory.load();
            AC |= MBR;
        } else
            throw new AddressingModeException("Addressing mode is invalid : " + AMODE);
        ZERO_FLAG = AC == 0;
    }

    private void xor() throws AddressingModeException {
        if (AMODE == 0x00) AC ^= OPERAND;
        else if (AMODE == 0x01) {
            MAR = DS + OPERAND;
            memory.load();
            AC ^= MBR;
        } else if (AMODE == 0x02) {
            MAR = DS + OPERAND;
            memory.load();
            MAR = DS + MBR;
            memory.load();
            AC ^= MBR;
        } else
            throw new AddressingModeException("Addressing mode is invalid : " + AMODE);
        ZERO_FLAG = AC == 0;
    }

    private void printInstruction() {
        String addressingMode = null;
        switch (AMODE) {
            case 0x00 -> addressingMode = Mnemonic.IMMEDIATE;
            case 0x01 -> addressingMode = Mnemonic.DIRECT;
            case 0x02 -> addressingMode = Mnemonic.INDIRECT;
        }
        String mnemonic = null;
        switch (OPCODE) {
            case 0x00 -> mnemonic = Mnemonic.HLT;
            case 0x01 -> mnemonic = Mnemonic.PRT;
            case 0x02 -> mnemonic = Mnemonic.STO;
            case 0x03 -> mnemonic = Mnemonic.LDA;
            case 0x04 -> mnemonic = Mnemonic.ADD;
            case 0x05 -> mnemonic = Mnemonic.SUB;
            case 0x06 -> mnemonic = Mnemonic.MUL;
            case 0x07 -> mnemonic = Mnemonic.DIV;
            case 0x08 -> mnemonic = Mnemonic.JMP;
            case 0x09 -> mnemonic = Mnemonic.JPZ;
            case 0x0A -> mnemonic = Mnemonic.JPN;
            case 0x0B -> mnemonic = Mnemonic.JPP;
            case 0x0C -> mnemonic = Mnemonic.AND;
            case 0x0D -> mnemonic = Mnemonic.OR;
            case 0x0E -> mnemonic = Mnemonic.NOT;
            case 0x0F -> mnemonic = Mnemonic.XOR;
        }
        System.out.printf("%-4s%-5s%#X\n", addressingMode, mnemonic, OPERAND);
    }
}

