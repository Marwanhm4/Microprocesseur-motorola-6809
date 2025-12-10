package com.motorola6809.simulator;

/**
 * Simple test class for CPU implementation
 */
public class CPUTest {
    
    private static Memory memory;
    private static CPU cpu;
    
    private static void setUp() {
        memory = new Memory();
        // Reset vector points to 0x0010
        memory.writeByte(0xFFFE, (byte) 0x00);
        memory.writeByte(0xFFFF, (byte) 0x10);
        cpu = new CPU(memory);
    }
    
    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(String.format("%s: expected 0x%02X, got 0x%02X", 
                message, expected, actual));
        }
    }
    
    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Motorola 6809 CPU Simulator - Test");
        System.out.println("==================================\n");
        
        int testsPassed = 0;
        int testsFailed = 0;
        
        try {
            // Test 1: LDA Immediate
            setUp();
            System.out.print("Test 1: LDA #imm ... ");
            memory.writeByte(0x0010, (byte) 0x86); // LDA #imm
            memory.writeByte(0x0011, (byte) 0x7F); // immediate value
            cpu.step();
            assertEquals(0x7F, cpu.getA(), "LDA immediate");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 2: LDB Immediate
            setUp();
            System.out.print("Test 2: LDB #imm ... ");
            memory.writeByte(0x0010, (byte) 0xC6); // LDB #imm
            memory.writeByte(0x0011, (byte) 0xAB); // immediate value
            cpu.step();
            assertEquals(0xAB, cpu.getB(), "LDB immediate");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 3: LDX Immediate
            setUp();
            System.out.print("Test 3: LDX #imm16 ... ");
            memory.writeByte(0x0010, (byte) 0x8E); // LDX #imm16
            memory.writeByte(0x0011, (byte) 0x12); // high byte
            memory.writeByte(0x0012, (byte) 0x34); // low byte
            cpu.step();
            assertEquals(0x1234, cpu.getX(), "LDX immediate");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 4: ADDA Immediate
            setUp();
            System.out.print("Test 4: ADDA #imm ... ");
            cpu.setA(0x20);
            memory.writeByte(0x0010, (byte) 0x8B); // ADDA #imm
            memory.writeByte(0x0011, (byte) 0x10); // immediate value
            cpu.step();
            assertEquals(0x30, cpu.getA(), "ADDA immediate");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 5: SUBA Immediate
            setUp();
            System.out.print("Test 5: SUBA #imm ... ");
            cpu.setA(0x30);
            memory.writeByte(0x0010, (byte) 0x80); // SUBA #imm
            memory.writeByte(0x0011, (byte) 0x10); // immediate value
            cpu.step();
            assertEquals(0x20, cpu.getA(), "SUBA immediate");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 6: INCA
            setUp();
            System.out.print("Test 6: INCA ... ");
            cpu.setA(0x05);
            memory.writeByte(0x0010, (byte) 0x4C); // INCA
            cpu.step();
            assertEquals(0x06, cpu.getA(), "INCA");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 7: DECA
            setUp();
            System.out.print("Test 7: DECA ... ");
            cpu.setA(0x05);
            memory.writeByte(0x0010, (byte) 0x4A); // DECA
            cpu.step();
            assertEquals(0x04, cpu.getA(), "DECA");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 8: CLRA
            setUp();
            System.out.print("Test 8: CLRA ... ");
            cpu.setA(0xFF);
            memory.writeByte(0x0010, (byte) 0x4F); // CLRA
            cpu.step();
            assertEquals(0x00, cpu.getA(), "CLRA");
            assertTrue(cpu.isFlagSet(CPU.Flag.Z), "CLRA sets Z flag");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 9: ANDA Immediate
            setUp();
            System.out.print("Test 9: ANDA #imm ... ");
            cpu.setA(0xFF);
            memory.writeByte(0x0010, (byte) 0x84); // ANDA #imm
            memory.writeByte(0x0011, (byte) 0xF0); // immediate value
            cpu.step();
            assertEquals(0xF0, cpu.getA(), "ANDA immediate");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 10: ORA Immediate
            setUp();
            System.out.print("Test 10: ORA #imm ... ");
            cpu.setA(0x0F);
            memory.writeByte(0x0010, (byte) 0x8A); // ORA #imm
            memory.writeByte(0x0011, (byte) 0xF0); // immediate value
            cpu.step();
            assertEquals(0xFF, cpu.getA(), "ORA immediate");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 11: COMA
            setUp();
            System.out.print("Test 11: COMA ... ");
            cpu.setA(0x55);
            memory.writeByte(0x0010, (byte) 0x43); // COMA
            cpu.step();
            assertEquals(0xAA, cpu.getA(), "COMA");
            assertTrue(cpu.isFlagSet(CPU.Flag.C), "COMA sets C flag");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 12: BRA
            setUp();
            System.out.print("Test 12: BRA ... ");
            memory.writeByte(0x0010, (byte) 0x20); // BRA
            memory.writeByte(0x0011, (byte) 0x10); // offset (+16)
            cpu.step();
            assertEquals(0x0022, cpu.getPC(), "BRA branch");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 13: MUL
            setUp();
            System.out.print("Test 13: MUL ... ");
            cpu.setA(0x08);
            cpu.setB(0x04);
            memory.writeByte(0x0010, (byte) 0x3D); // MUL
            cpu.step();
            assertEquals(0x00, cpu.getA(), "MUL high byte");
            assertEquals(0x20, cpu.getB(), "MUL result (8*4=32=0x20)");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 14: LDA Direct Page
            setUp();
            System.out.print("Test 14: LDA <addr (direct page) ... ");
            cpu.setDP(0x00); // Direct page = 0x00
            memory.writeByte(0x0050, (byte) 0x42);
            memory.writeByte(0x0010, (byte) 0x96); // LDA <addr
            memory.writeByte(0x0011, (byte) 0x50); // offset = 0x50
            cpu.step();
            assertEquals(0x42, cpu.getA(), "LDA direct page");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 15: LDA Extended
            setUp();
            System.out.print("Test 15: LDA $addr (extended) ... ");
            memory.writeByte(0x1234, (byte) 0xAB);
            memory.writeByte(0x0010, (byte) 0xB6); // LDA $addr
            memory.writeByte(0x0011, (byte) 0x12); // high byte
            memory.writeByte(0x0012, (byte) 0x34); // low byte
            cpu.step();
            assertEquals(0xAB, cpu.getA(), "LDA extended");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 16: LDA Indexed (sans offset)
            setUp();
            System.out.print("Test 16: LDA ,X (indexed) ... ");
            cpu.setX(0x1000);
            memory.writeByte(0x1000, (byte) 0x55);
            memory.writeByte(0x0010, (byte) 0xA6); // LDA indexed
            memory.writeByte(0x0011, (byte) 0x84); // Mode: X, pas d'offset (0x84 = 10000100)
            cpu.step();
            assertEquals(0x55, cpu.getA(), "LDA indexed");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 17: CMPA Immediate
            setUp();
            System.out.print("Test 17: CMPA #imm ... ");
            cpu.setA(0x50);
            memory.writeByte(0x0010, (byte) 0x81); // CMPA #imm
            memory.writeByte(0x0011, (byte) 0x30); // compare avec 0x30
            cpu.step();
            assertTrue(cpu.isFlagSet(CPU.Flag.N) == false, "CMPA: A > value, N should be clear");
            assertTrue(cpu.isFlagSet(CPU.Flag.Z) == false, "CMPA: A != value, Z should be clear");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 18: JSR Extended
            setUp();
            System.out.print("Test 18: JSR $addr ... ");
            cpu.setS(0x0100); // Initialize stack
            memory.writeByte(0x0010, (byte) 0xBD); // JSR $addr
            memory.writeByte(0x0011, (byte) 0x20); // high byte
            memory.writeByte(0x0012, (byte) 0x00); // low byte
            cpu.step();
            assertEquals(0x2000, cpu.getPC(), "JSR jump");
            assertEquals(0x00FE, cpu.getS(), "JSR stack decremented");
            // Vérifier que le PC de retour est sur la pile
            int returnAddr = memory.readWord(0x00FE);
            assertEquals(0x0013, returnAddr, "JSR return address on stack");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        try {
            // Test 19: RTS
            setUp();
            System.out.print("Test 19: RTS ... ");
            cpu.setS(0x00FE);
            memory.writeWord(0x00FE, 0x1234); // Return address on stack
            memory.writeByte(0x0010, (byte) 0x39); // RTS
            cpu.step();
            assertEquals(0x1234, cpu.getPC(), "RTS return");
            assertEquals(0x0100, cpu.getS(), "RTS stack incremented");
            System.out.println("✓ Passed");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Failed: " + e.getMessage());
            testsFailed++;
        }
        
        System.out.println("\n==================================");
        System.out.println("Tests passed: " + testsPassed);
        System.out.println("Tests failed: " + testsFailed);
        System.out.println("Total tests: " + (testsPassed + testsFailed));
    }
}
