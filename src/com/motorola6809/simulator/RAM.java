package com.motorola6809.simulator;

/**
 * RAM (Random Access Memory) class for Motorola 6809 Microprocessor Simulator
 * 
 * Implements read/write memory as a byte array with address bounds checking.
 * Supports 64KB address space (0x0000-0xFFFF).
 */
public class RAM {
    // 64KB address space (65536 bytes)
    private static final int MEMORY_SIZE = 65536;
    
    // Memory array
    private byte[] memory;
    
    /**
     * Constructor - Initializes RAM array
     */
    public RAM() {
        memory = new byte[MEMORY_SIZE];
        
        // Initialize all memory to zero
        for (int i = 0; i < MEMORY_SIZE; i++) {
            memory[i] = 0;
        }
    }
    
    /**
     * Reads a byte from RAM at the specified address
     * 
     * @param address 16-bit address (0x0000-0xFFFF)
     * @return byte value at the address
     * @throws IllegalArgumentException if address is out of bounds
     */
    public byte readByte(int address) {
        checkAddress(address);
        return memory[address];
    }
    
    /**
     * Writes a byte to RAM at the specified address
     * 
     * @param address 16-bit address (0x0000-0xFFFF)
     * @param value byte value to write
     * @throws IllegalArgumentException if address is out of bounds
     */
    public void writeByte(int address, byte value) {
        checkAddress(address);
        memory[address] = value;
    }
    
    /**
     * Reads a 16-bit word (big-endian) from RAM
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
     * Writes a 16-bit word (big-endian) to RAM
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
     * Loads data into RAM at the specified address
     * 
     * @param address starting address
     * @param data byte array to load
     * @throws IllegalArgumentException if address is out of bounds or data would overflow
     */
    public void load(int address, byte[] data) {
        checkAddress(address);
        
        if (address + data.length > MEMORY_SIZE) {
            throw new IllegalArgumentException(
                "Data would exceed memory bounds");
        }
        
        // Copy data into RAM
        for (int i = 0; i < data.length; i++) {
            memory[address + i] = data[i];
        }
    }
    
    /**
     * Clears all RAM (sets to zero)
     */
    public void clear() {
        for (int i = 0; i < MEMORY_SIZE; i++) {
            memory[i] = 0;
        }
    }
    
    /**
     * Clears a specific region of RAM (sets to zero)
     * 
     * @param startAddress starting address (inclusive)
     * @param endAddress ending address (inclusive)
     * @throws IllegalArgumentException if addresses are out of bounds or invalid range
     */
    public void clear(int startAddress, int endAddress) {
        checkAddress(startAddress);
        checkAddress(endAddress);
        
        if (startAddress > endAddress) {
            throw new IllegalArgumentException(
                "Start address must be <= end address");
        }
        
        for (int i = startAddress; i <= endAddress; i++) {
            memory[i] = 0;
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
     * Gets the total memory size
     * 
     * @return memory size in bytes (65536)
     */
    public int getMemorySize() {
        return MEMORY_SIZE;
    }
}

