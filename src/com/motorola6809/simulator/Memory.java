package com.motorola6809.simulator;

/**
 * Memory class for Motorola 6809 Microprocessor Simulator
 * 
 * Manages RAM and ROM memory modules with address mapping.
 * Supports 64KB address space (0x0000-0xFFFF).
 */
public class Memory {
    // 64KB address space (65536 bytes)
    private static final int MEMORY_SIZE = 65536;
    
    // Memory modules
    private RAM ram;         // Random Access Memory (read/write)
    private ROM rom;         // Read-Only Memory (read only)
    private boolean[] romMap; // Tracks which addresses are ROM
    
    /**
     * Constructor - Initializes memory modules
     */
    public Memory() {
        ram = new RAM();
        rom = new ROM();
        romMap = new boolean[MEMORY_SIZE];
        
        // All addresses start as RAM
        for (int i = 0; i < MEMORY_SIZE; i++) {
            romMap[i] = false;
        }
    }
    
    /**
     * Reads a byte from memory at the specified address
     * 
     * @param address 16-bit address (0x0000-0xFFFF)
     * @return byte value at the address
     * @throws IllegalArgumentException if address is out of bounds
     */
    public byte readByte(int address) {
        checkAddress(address);
        
        if (romMap[address]) {
            return rom.readByte(address);
        } else {
            return ram.readByte(address);
        }
    }
    
    /**
     * Writes a byte to memory at the specified address
     * Only writes to RAM addresses; ROM writes are ignored
     * 
     * @param address 16-bit address (0x0000-0xFFFF)
     * @param value byte value to write
     * @throws IllegalArgumentException if address is out of bounds
     */
    public void writeByte(int address, byte value) {
        checkAddress(address);
        
        // Only write to RAM addresses
        if (!romMap[address]) {
            ram.writeByte(address, value);
        } else {
            // Silently ignore writes to ROM addresses (typical behavior)
            rom.writeByte(address, value);
        }
    }
    
    /**
     * Reads a 16-bit word (big-endian) from memory
     * 
     * @param address 16-bit address (0x0000-0xFFFF)
     * @return 16-bit value (0x0000-0xFFFF)
     * @throws IllegalArgumentException if address is out of bounds
     */
    public int readWord(int address) {
        checkAddress(address);
        checkAddress(address + 1); // Ensure we can read two bytes
        
        // Motorola 6809 uses big-endian byte order (high byte at lower address)
        int highByte = readByte(address) & 0xFF;
        int lowByte = readByte(address + 1) & 0xFF;
        
        return (highByte << 8) | lowByte;
    }
    
    /**
     * Writes a 16-bit word (big-endian) to memory
     * 
     * @param address 16-bit address (0x0000-0xFFFF)
     * @param value 16-bit value (0x0000-0xFFFF)
     * @throws IllegalArgumentException if address is out of bounds
     */
    public void writeWord(int address, int value) {
        checkAddress(address);
        checkAddress(address + 1); // Ensure we can write two bytes
        
        // Motorola 6809 uses big-endian byte order
        byte highByte = (byte) ((value >> 8) & 0xFF);
        byte lowByte = (byte) (value & 0xFF);
        
        writeByte(address, highByte);
        writeByte(address + 1, lowByte);
    }
    
    /**
     * Sets a memory region as ROM (Read-Only Memory)
     * 
     * @param startAddress starting address (inclusive)
     * @param endAddress ending address (inclusive)
     * @throws IllegalArgumentException if addresses are out of bounds or invalid range
     */
    public void setROM(int startAddress, int endAddress) {
        checkAddress(startAddress);
        checkAddress(endAddress);
        
        if (startAddress > endAddress) {
            throw new IllegalArgumentException(
                "Start address must be <= end address");
        }
        
        for (int i = startAddress; i <= endAddress; i++) {
            romMap[i] = true;
        }
    }
    
    /**
     * Sets a memory region as RAM (Random Access Memory)
     * 
     * @param startAddress starting address (inclusive)
     * @param endAddress ending address (inclusive)
     * @throws IllegalArgumentException if addresses are out of bounds or invalid range
     */
    public void setRAM(int startAddress, int endAddress) {
        checkAddress(startAddress);
        checkAddress(endAddress);
        
        if (startAddress > endAddress) {
            throw new IllegalArgumentException(
                "Start address must be <= end address");
        }
        
        for (int i = startAddress; i <= endAddress; i++) {
            romMap[i] = false;
        }
    }
    
    /**
     * Loads data into ROM at the specified address
     * 
     * @param address starting address
     * @param data byte array to load
     * @throws IllegalArgumentException if address is out of bounds or data would overflow
     */
    public void loadROM(int address, byte[] data) {
        checkAddress(address);
        
        if (address + data.length > MEMORY_SIZE) {
            throw new IllegalArgumentException(
                "Data would exceed memory bounds");
        }
        
        // Load data into ROM module and mark region as ROM
        rom.load(address, data);
        for (int i = 0; i < data.length; i++) {
            romMap[address + i] = true;
        }
    }
    
    /**
     * Loads data into RAM at the specified address
     * 
     * @param address starting address
     * @param data byte array to load
     * @throws IllegalArgumentException if address is out of bounds or data would overflow
     */
    public void loadRAM(int address, byte[] data) {
        checkAddress(address);
        
        if (address + data.length > MEMORY_SIZE) {
            throw new IllegalArgumentException(
                "Data would exceed memory bounds");
        }
        
        // Load data into RAM module and ensure region is marked as RAM
        ram.load(address, data);
        for (int i = 0; i < data.length; i++) {
            romMap[address + i] = false;
        }
    }
    
    /**
     * Checks if an address is within valid bounds
     * 
     * @param address address to check
     * @throws IllegalArgumentException if address is out of bounds
     */
    private void checkAddress(int address) {
        if (address < 0 || address >= MEMORY_SIZE) {
            throw new IllegalArgumentException(
                String.format("Address 0x%04X is out of bounds (0x0000-0xFFFF)", address));
        }
    }
    
    /**
     * Checks if an address is ROM
     * 
     * @param address address to check
     * @return true if address is ROM, false if RAM
     */
    public boolean isROM(int address) {
        checkAddress(address);
        return romMap[address];
    }
    
    /**
     * Clears all RAM (sets to zero)
     */
    public void clearRAM() {
        ram.clear();
    }
    
    /**
     * Gets the RAM module
     * 
     * @return RAM instance
     */
    public RAM getRAM() {
        return ram;
    }
    
    /**
     * Gets the ROM module
     * 
     * @return ROM instance
     */
    public ROM getROM() {
        return rom;
    }
    
    /**
     * Gets the total memory size
     * 
     * @return memory size in bytes (65536)
     */
    public int getMemorySize() {
        return MEMORY_SIZE;
    }
}

