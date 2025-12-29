package com.motorola6809.simulator;
public class ROM {
    private static final int MEMORY_SIZE = 65536;
    private byte[] memory;
    private boolean[] initialized;
    public ROM() {
        memory = new byte[MEMORY_SIZE];
        initialized = new boolean[MEMORY_SIZE];
        for (int i = 0; i < MEMORY_SIZE; i++) {
            memory[i] = 0;
            initialized[i] = false;
        }
    }
    public byte readByte(int address) {
        checkAddress(address);
        return memory[address];
    }
    public void writeByte(int address, byte value) {
        checkAddress(address);
        memory[address] = value;
        initialized[address] = true;
    }
    public int readWord(int address) {
        checkAddress(address);
        checkAddress(address + 1);
        int highByte = readByte(address) & 0xFF;
        int lowByte = readByte(address + 1) & 0xFF;
        return (highByte << 8) | lowByte;
    }
    public void writeWord(int address, int value) {
        checkAddress(address);
        checkAddress(address + 1);
    }
    public void load(int address, byte[] data) {
        checkAddress(address);
        if (address + data.length > MEMORY_SIZE) {
            throw new IllegalArgumentException(
                    "Les données dépasseraient les limites de la mémoire");
        }
        for (int i = 0; i < data.length; i++) {
            memory[address + i] = data[i];
            initialized[address + i] = true;
        }
    }
    public void clear() {
        for (int i = 0; i < MEMORY_SIZE; i++) {
            memory[i] = 0;
            initialized[i] = false;
        }
    }
    public boolean isInitialized(int address) {
        checkAddress(address);
        return initialized[address];
    }
    private void checkAddress(int address) {
        if (address < 0 || address >= MEMORY_SIZE) {
            throw new IllegalArgumentException(
                    String.format("Adresse 0x%04X hors limites (0x0000-0xFFFF)", address));
        }
    }
    public int getMemorySize() {
        return MEMORY_SIZE;
    }
}
