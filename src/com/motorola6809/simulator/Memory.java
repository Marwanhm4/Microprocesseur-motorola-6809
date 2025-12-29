package com.motorola6809.simulator;
public class Memory {
    private static final int MEMORY_SIZE = 65536;
    private RAM ram;
    private ROM rom;
    private boolean[] romMap;
    public Memory() {
        ram = new RAM();
        rom = new ROM();
        romMap = new boolean[MEMORY_SIZE];
        for (int i = 0; i < MEMORY_SIZE; i++) {
            romMap[i] = false;
        }
    }
    private void checkAddress(int address) {
        if (address < 0 || address >= MEMORY_SIZE) {
            throw new IllegalArgumentException(
                String.format("Adresse 0x%04X hors limites (0x0000-0xFFFF)", address));
        }
    }
    
    public byte readByte(int address) {
        checkAddress(address);
        if (romMap[address]) {
            return rom.readByte(address);
        } else {
            return ram.readByte(address);
        }
    }
    public void writeByte(int address, byte value) {
        checkAddress(address);
        if (!romMap[address]) {
            ram.writeByte(address, value);
        } else {
            rom.writeByte(address, value);
        }
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
        byte highByte = (byte) ((value >> 8) & 0xFF);
        byte lowByte = (byte) (value & 0xFF);
        writeByte(address, highByte);
        writeByte(address + 1, lowByte);
    }
    public void setROM(int startAddress, int endAddress) {
        checkAddress(startAddress);
        checkAddress(endAddress);
        if (startAddress > endAddress) {
            throw new IllegalArgumentException(
                "L'adresse de début doit être <= l'adresse de fin");
        }
        for (int i = startAddress; i <= endAddress; i++) {
            romMap[i] = true;
        }
    }
    public void setRAM(int startAddress, int endAddress) {
        checkAddress(startAddress);
        checkAddress(endAddress);
        if (startAddress > endAddress) {
            throw new IllegalArgumentException(
                "L'adresse de début doit être <= l'adresse de fin");
        }
        for (int i = startAddress; i <= endAddress; i++) {
            romMap[i] = false;
        }
    }
    public void loadROM(int address, byte[] data) {
        checkAddress(address);
        if (address + data.length > MEMORY_SIZE) {
            throw new IllegalArgumentException(
                "Les données dépasseraient les limites de la mémoire");
        }
        rom.load(address, data);
        for (int i = 0; i < data.length; i++) {
            romMap[address + i] = true;
        }
    }
    public void loadRAM(int address, byte[] data) {
        checkAddress(address);
        if (address + data.length > MEMORY_SIZE) {
            throw new IllegalArgumentException( "Data would exceed memory bounds");
        }
        ram.load(address, data);
        for (int i = 0; i < data.length; i++) {
            romMap[address + i] = false;
        }
    }
    public boolean isROM(int address) {
        checkAddress(address);
        return romMap[address];
    }
    public void clearRAM() {
        ram.clear();
    }
    public RAM getRAM() {
        return ram;
    }
    public ROM getROM() {
        return rom;
    }
    public int getMemorySize() {
        return MEMORY_SIZE;
    }
}
