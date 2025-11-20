package com.motorola6809.simulator;

/**
 * ROM (Read-Only Memory) class for Motorola 6809 Microprocessor Simulator
 * 
 * Implements read-only memory as a byte array with address bounds checking.
 * Supports 64KB address space (0x0000-0xFFFF).
 * Write operations are not allowed (will throw exception or be ignored).
 */
public class ROM {
    // 64KB address space (65536 bytes)
    private static final int MEMORY_SIZE = 65536;
    
    // Memory array
    private byte[] memory;
    private boolean[] initialized; // Tracks which addresses have been initialized
    
    /**
     * Constructor - Initializes ROM array
     */
    public ROM() {
        memory = new byte[MEMORY_SIZE];
        initialized = new boolean[MEMORY_SIZE];
        
        // Initialize all memory to zero
        for (int i = 0; i < MEMORY_SIZE; i++) {
            memory[i] = 0;
            initialized[i] = false;
        }
    }
    
    /**
     * Reads a byte from ROM at the specified address
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
     * Attempts to write a byte to ROM (not allowed - silently ignored)
     * 
     * @param address 16-bit address (0x0000-0xFFFF)
     * @param value byte value (ignored)
     * @throws IllegalArgumentException if address is out of bounds
     */
    public void writeByte(int address, byte value) {
        checkAddress(address);
        // Silently ignore writes to ROM (typical behavior)
    }
    
    /**
     * Reads a 16-bit word (big-endian) from ROM
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
     * Attempts to write a 16-bit word to ROM (not allowed - silently ignored)
     * 
     * @param address 16-bit address (0x0000-0xFFFF)
     * @param value 16-bit value (ignored)
     * @throws IllegalArgumentException if address is out of bounds
     */
    public void writeWord(int address, int value) {
        checkAddress(address);
        checkAddress(address + 1);
        // Silently ignore writes to ROM
    }
    
    /**
     * Loads data into ROM at the specified address
     * This is the only way to set ROM content (typically done during initialization)
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
        
        // Copy data into ROM and mark as initialized
        for (int i = 0; i < data.length; i++) {
            memory[address + i] = data[i];
            initialized[address + i] = true;
        }
    }
    
    /**
     * Checks if an address has been initialized with data
     * 
     * @param address address to check
     * @return true if address has been initialized, false otherwise
     */
    public boolean isInitialized(int address) {
        checkAddress(address);
        return initialized[address];
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

