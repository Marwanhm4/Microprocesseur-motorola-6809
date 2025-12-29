package com.motorola6809.test;

import com.motorola6809.simulator.CPU;
import com.motorola6809.simulator.Memory;

public class FlagTester {
    private static void printFlags(CPU cpu) {
        System.out.printf("C=%b V=%b Z=%b S(N)=%b P(I)=%b AC(H)=%b\n",
                cpu.isFlagSet(CPU.Flag.C),
                cpu.isFlagSet(CPU.Flag.V),
                cpu.isFlagSet(CPU.Flag.Z),
                cpu.isFlagSet(CPU.Flag.N),
                cpu.isFlagSet(CPU.Flag.I),
                cpu.isFlagSet(CPU.Flag.H));
    }

    public static void main(String[] args) {
        Memory mem = new Memory();
        CPU cpu = new CPU(mem);
        cpu.initialize();

        System.out.println("=== Test Z/N via LDA #imm ===");
        // LDA #0 -> Z set
        mem.loadROM(0x1000, new byte[] {(byte)0x86, (byte)0x00});
        cpu.setPC(0x1000);
        cpu.step();
        printFlags(cpu);

        // LDA #0x80 -> N set
        mem.loadROM(0x1002, new byte[] {(byte)0x86, (byte)0x80});
        cpu.setPC(0x1002);
        cpu.step();
        printFlags(cpu);

        System.out.println("=== Test C via ADDA overflow and V via signed overflow ===");
        // ADDA immediate with carry: A=0xF0 + 0x20 => fullResult=0x110 -> C set
        cpu.setA(0xF0);
        mem.loadROM(0x2000, new byte[] {(byte)0x8B, (byte)0x20});
        cpu.setPC(0x2000);
        cpu.step();
        printFlags(cpu);

        // ADDA immediate with signed overflow: A=0x50 + 0x50 => result 0xA0 -> V set
        cpu.setA(0x50);
        mem.loadROM(0x2002, new byte[] {(byte)0x8B, (byte)0x50});
        cpu.setPC(0x2002);
        cpu.step();
        printFlags(cpu);

        System.out.println("=== Test SWI sets I and F ===");
        mem.loadROM(0x3000, new byte[] {(byte)0x3F});
        cpu.setPC(0x3000);
        cpu.step();
        printFlags(cpu);

        System.out.println("=== Note about AC(H) ===");
        System.out.println("In this implementation H (AC) is only updated via setCC/getCC (as part of CC ops).\n");
    }
}
