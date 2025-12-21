package com.motorola6809.simulator;
public class RAM {
    private static final int MEMORY_SIZE = 65536;
    private byte[] memory;
    public RAM() {
        memory = new byte[MEMORY_SIZE];
        for (int i = 0; i < MEMORY_SIZE; i++) {
            memory[i] = 0;
        }
    }
    public byte readByte(int address) {
        checkAddress(address);
        synchronized (this) {
            return memory[address];
        }
    }
    public void writeByte(int address, byte value) {
        checkAddress(address);
        synchronized (this) {
            memory[address] = value;
        }
    }
    public int readWord(int address) {
        checkAddress(address);
        checkAddress(address + 1);
        synchronized (this) {
            int highByte = memory[address] & 0xFF;
            int lowByte = memory[address + 1] & 0xFF;
            return (highByte << 8) | lowByte;
        }
    }
    public void writeWord(int address, int value) {
        checkAddress(address);
        checkAddress(address + 1);
        synchronized (this) {
            byte highByte = (byte) ((value >> 8) & 0xFF);
            byte lowByte = (byte) (value & 0xFF);
            memory[address] = highByte;
            memory[address + 1] = lowByte;
        }
    }
    public void load(int address, byte[] data) {
        checkAddress(address);
        if (address + data.length > MEMORY_SIZE) {
            throw new IllegalArgumentException(
                "Les données dépasseraient les limites de la mémoire");
        }
        synchronized (this) {
            for (int i = 0; i < data.length; i++) {
                memory[address + i] = data[i];
            }
        }
    }
    public void clear() {
        synchronized (this) {
            for (int i = 0; i < MEMORY_SIZE; i++) {
                memory[i] = 0;
            }
        }
    }
    public void clear(int startAddress, int endAddress) {
        checkAddress(startAddress);
        checkAddress(endAddress);
        if (startAddress > endAddress) {
            throw new IllegalArgumentException(
                "L'adresse de début doit être <= l'adresse de fin");
        }
        synchronized (this) {
            for (int i = startAddress; i <= endAddress; i++) {
                memory[i] = 0;
            }
        }
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
