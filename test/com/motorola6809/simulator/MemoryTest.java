package com.motorola6809.simulator;

/**
 * Simple test class for Memory implementation
 */
public class MemoryTest {
    public static void main(String[] args) {
        System.out.println("Motorola 6809 Memory Simulator - Test");
        System.out.println("=====================================\n");
        
        Memory memory = new Memory();
        
        // Test 1: Basic byte read/write
        System.out.println("Test 1: Basic byte read/write");
        memory.writeByte(0x1000, (byte) 0x42);
        byte value = memory.readByte(0x1000);
        System.out.println("Wrote 0x42 to address 0x1000, read: 0x" + 
                          String.format("%02X", value & 0xFF));
        assert value == 0x42 : "Byte read/write failed";
        System.out.println("✓ Passed\n");
        
        // Test 2: Word read/write (big-endian)
        System.out.println("Test 2: Word read/write (big-endian)");
        memory.writeWord(0x2000, 0x1234);
        int word = memory.readWord(0x2000);
        System.out.println("Wrote 0x1234 to address 0x2000, read: 0x" + 
                          String.format("%04X", word));
        assert word == 0x1234 : "Word read/write failed";
        System.out.println("✓ Passed\n");
        
        // Test 3: ROM protection
        System.out.println("Test 3: ROM protection");
        memory.setROM(0x8000, 0x8FFF);
        memory.writeByte(0x8000, (byte) 0xFF);
        byte romValue = memory.readByte(0x8000);
        System.out.println("Set 0x8000 as ROM, tried to write 0xFF, read: 0x" + 
                          String.format("%02X", romValue & 0xFF));
        assert romValue == 0 : "ROM write protection failed";
        System.out.println("✓ Passed\n");
        
        // Test 4: Load data into ROM
        System.out.println("Test 4: Load data into ROM");
        byte[] romData = {(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78};
        memory.loadROM(0x8000, romData);
        byte romByte = memory.readByte(0x8001);
        System.out.println("Loaded ROM data, read from 0x8001: 0x" + 
                          String.format("%02X", romByte & 0xFF));
        assert romByte == 0x34 : "ROM load failed";
        System.out.println("✓ Passed\n");
        
        // Test 5: Address bounds checking
        System.out.println("Test 5: Address bounds checking");
        try {
            memory.readByte(0x10000); // Out of bounds
            System.out.println("✗ Failed: Should have thrown exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Passed: Correctly caught out-of-bounds address");
        }
        System.out.println();
        
        // Test 6: Verify big-endian byte order
        System.out.println("Test 6: Verify big-endian byte order");
        memory.writeWord(0x3000, 0xABCD);
        byte high = memory.readByte(0x3000);
        byte low = memory.readByte(0x3001);
        System.out.println("Wrote 0xABCD, high byte (0x3000): 0x" + 
                          String.format("%02X", high & 0xFF) + 
                          ", low byte (0x3001): 0x" + 
                          String.format("%02X", low & 0xFF));
        assert (high & 0xFF) == 0xAB && (low & 0xFF) == 0xCD : "Big-endian order incorrect";
        System.out.println("✓ Passed\n");
        
        System.out.println("All tests completed!");
    }
}

