package com.motorola6809.simulator;
import java.util.EnumSet;
public class CPU {
    private final Memory memory;
    private int regA;
    private int regB;
    private int regX;
    private int regY;
    private int regU;
    private int regS;
    private int regPC;
    private int regDP;
    private EnumSet<Flag> conditionCodes = EnumSet.noneOf(Flag.class);
    public CPU(Memory memory) {
        this.memory = memory;
    }
    
    public void initialize() {
        reset();
    }
    public int getRegA() {
        return regA;
    }
    public int getRegB() {
        return regB;
    }
    public int getRegPC() {
        return regPC;
    }
    public void reset() {
        regA = regB = 0;
        regX = regY = regU = regS = 0;
        regDP = 0;
        conditionCodes.clear();
        regPC = memory.readWord(0xFFFE);
    }
    public void step() {
        int opcode = fetchByte();
        switch (opcode) {
            case 0x86:
                opLDAimmediate();
                break;
            case 0xC6:
                opLDBimmediate();
                break;
            case 0x8E:
                opLDXimmediate();
                break;
            case 0xCE:
                opLDUimmediate();
                break;
            case 0x8B:
                opADDAimmediate();
                break;
            case 0xCB:
                opADDBimmediate();
                break;
            case 0x80:
                opSUBAimmediate();
                break;
            case 0xC0:
                opSUBBimmediate();
                break;
            case 0x84:
                opANDAimmediate();
                break;
            case 0xC4:
                opANDBimmediate();
                break;
            case 0x8A:
                opORAimmediate();
                break;
            case 0xCA:
                opORBimmediate();
                break;
            case 0x88:
                opEORAimmediate();
                break;
            case 0xC8:
                opEORBimmediate();
                break;
            case 0x96: opLDAdirect(); break;
            case 0xD6: opLDBdirect(); break;
            case 0x9E: opLDXdirect(); break;
            case 0xDE: opLDYdirect(); break;
            case 0x9C: opLDUdirect(); break;
            case 0x9D: opLDSdirect(); break;
            case 0x97: opSTAdirect(); break;
            case 0xD7: opSTBdirect(); break;
            case 0x9F: opSTXdirect(); break;
            case 0xDF: opSTYdirect(); break;
            case 0xDD: opSTUdirect(); break;
            case 0x9B: opADDAdirect(); break;
            case 0xDB: opADDBdirect(); break;
            case 0x90: opSUBAdirect(); break;
            case 0xD0: opSUBBDirect(); break;
            case 0x94: opANDAdirect(); break;
            case 0xD4: opANDBdirect(); break;
            case 0x9A: opORAdirect(); break;
            case 0xDA: opORBdirect(); break;
            case 0x98: opEORAdirect(); break;
            case 0xD8: opEORBdirect(); break;
            case 0xB6: opLDAextended(); break;
            case 0xF6: opLDBextended(); break;
            case 0xBE: opLDXextended(); break;
            case 0xFE: opLDYextended(); break;
            case 0xB7: opSTAextended(); break;
            case 0xF7: opSTBextended(); break;
            case 0xBF: opSTXextended(); break;
            case 0xFF: opSTYextended(); break;
            case 0xBB: opADDAextended(); break;
            case 0xFB: opADDBextended(); break;
            case 0xB0: opSUBAextended(); break;
            case 0xF0: opSUBBextended(); break;
            case 0xB4: opANDAextended(); break;
            case 0xF4: opANDBextended(); break;
            case 0xBA: opORAextended(); break;
            case 0xFA: opORBextended(); break;
            case 0xB8: opEORAextended(); break;
            case 0xF8: opEORBextended(); break;
            case 0xA6: opLDAindexed(); break;
            case 0xE6: opLDBindexed(); break;
            case 0xAE: opLDXindexed(); break;
            case 0xEE: opLDUindexed(); break;
            case 0xA7: opSTAindexed(); break;
            case 0xE7: opSTBindexed(); break;
            case 0xAF: opSTXindexed(); break;
            case 0xEF: opSTUindexed(); break;
            case 0xAB: opADDAindexed(); break;
            case 0xEB: opADDBindexed(); break;
            case 0xA0: opSUBAindexed(); break;
            case 0xE0: opSUBBindexed(); break;
            case 0xA4: opANDAindexed(); break;
            case 0xE4: opANDBindexed(); break;
            case 0xAA: opORAindexed(); break;
            case 0xEA: opORBindexed(); break;
            case 0xA8: opEORAindexed(); break;
            case 0xE8: opEORBindexed(); break;
            case 0x4C:
                opINCA();
                break;
            case 0x5C:
                opINCB();
                break;
            case 0x4A:
                opDECA();
                break;
            case 0x5A:
                opDECB();
                break;
            case 0x08:
                opINX();
                break;
            case 0x09:
                opDEX();
                break;
            case 0x43:
                opCOMA();
                break;
            case 0x53:
                opCOMB();
                break;
            case 0x4F:
                opCLRA();
                break;
            case 0x5F:
                opCLRB();
                break;
            case 0x40:
                opNEGA();
                break;
            case 0x50:
                opNEGB();
                break;
            case 0x4D:
                opTSTA();
                break;
            case 0x5D:
                opTSTB();
                break;
            case 0x49:
                opROLA();
                break;
            case 0x59:
                opROLB();
                break;
            case 0x46:
                opRORA();
                break;
            case 0x56:
                opRORB();
                break;
            case 0x48:
                opASLA();
                break;
            case 0x58:
                opASLB();
                break;
            case 0x47:
                opASRA();
                break;
            case 0x57:
                opASRB();
                break;
            case 0x44:
                opLSRA();
                break;
            case 0x54:
                opLSRB();
                break;
            case 0x3D:
                opMUL();
                break;
            case 0x1D:
                opSEX();
                break;
            case 0x3A:
                opABX();
                break;
            case 0x36:
                opPSHA();
                break;
            case 0x37:
                opPSHB();
                break;
            case 0x32:
                opPULA();
                break;
            case 0x33:
                opPULB();
                break;
            case 0x12:
                opNOP();
                break;
            case 0x13:
                opSYNC();
                break;
            case 0x3F:
                opSWI();
                break;
            case 0x3B:
                opRTI();
                break;
            case 0x39:
                opRTS();
                break;
            case 0x3E:
                opRESET();
                break;
            case 0x20:
                opBRA();
                break;
            case 0x26:
                opBNE();
                break;
            case 0x27:
                opBEQ();
                break;
            case 0x24:
                opBCC();
                break;
            case 0x25:
                opBCS();
                break;
            case 0x2A:
                opBPL();
                break;
            case 0x2B:
                opBMI();
                break;
            case 0x2E:
                opBGT();
                break;
            case 0x2F:
                opBLE();
                break;
            case 0x22:
                opBHI();
                break;
            case 0x23:
                opBLS();
                break;
            case 0x16: opLBRA(); break;
            case 0x17: opLBSR(); break;
            case 0x81: opCMPAimmediate(); break;
            case 0xC1: opCMPBimmediate(); break;
            case 0x8C: opCMPXimmediate(); break;
            case 0x91: opCMPAdirect(); break;
            case 0xD1: opCMPBdirect(); break;
            case 0xB1: opCMPAextended(); break;
            case 0xF1: opCMPBextended(); break;
            case 0xA1: opCMPAindexed(); break;
            case 0xE1: opCMPBindexed(); break;
            case 0x7E: opJMPextended(); break;
            case 0x6E: opJMPindexed(); break;
            case 0xBD: opJSRextended(); break;
            case 0xAD: opJSRindexed(); break;
            case 0x8D: opBSR(); break;
            case 0x1F:
                opTFR();
                break;
            case 0x1E:
                opEXG();
                break;
            case 0x34:
                opPSHS();
                break;
            case 0x35:
                opPULS();
                break;
            case 0x3C:
                opCWAI();
                break;
            case 0x1A:
                opORCC();
                break;
            case 0x1C:
                opANDCC();
                break;
            case 0x10:
                int postByte = fetchByte();
                switch (postByte) {
                    case 0xBE: opLDYimmediate(); break;
                    case 0xCE: opLDSimmediate(); break;
                    case 0xAF: opLDYindexed(); break;
                    case 0xBF: opSTYindexed(); break;
                    case 0x3C: opINY(); break;
                    case 0x3D: opDEY(); break;
                    case 0x21:
                        fetchWord();
                        break;
                    default:
                        throw new IllegalStateException(
                                String.format("Opcode étendu 0x10%02X non implémenté à PC=0x%04X", postByte,
                                        (regPC - 1) & 0xFFFF));
                }
                break;
            default:
                throw new IllegalStateException(
                        String.format("Opcode 0x%02X non implémenté à PC=0x%04X", opcode, (regPC - 1) & 0xFFFF));
        }
    }
    private int fetchByte() {
        int value = memory.readByte(regPC) & 0xFF;
        regPC = (regPC + 1) & 0xFFFF;
        return value;
    }
    private int fetchWord() {
        int hi = fetchByte();
        int lo = fetchByte();
        return (hi << 8) | lo;
    }
    private int fetchSignedByte() {
        int value = fetchByte();
        return (value & 0x80) != 0 ? value | 0xFFFFFF00 : value;
    }
    private int fetchSignedWord() {
        int value = fetchWord();
        return (value & 0x8000) != 0 ? value | 0xFFFF0000 : value;
    }
    private int calculateIndexedAddress(int postByte) {
        int register = (postByte >> 6) & 0x03;
        int mode = (postByte >> 3) & 0x07;
        boolean indirect = (postByte & 0x02) != 0;
        boolean autoModify = (postByte & 0x01) != 0;
        int baseAddress = 0;
        switch (register) {
            case 0: baseAddress = regX; break;
            case 1: baseAddress = regY; break;
            case 2: baseAddress = regU; break;
            case 3: baseAddress = regS; break;
        }
        int offset = 0;
        boolean updateRegister = false;
        int incrementAmount = 0;
        switch (mode) {
            case 0:
                offset = 0;
                break;
            case 1:
                int offset5 = fetchByte() & 0x1F;
                offset = (offset5 & 0x10) != 0 ? offset5 | 0xFFFFFFE0 : offset5;
                break;
            case 2:
                offset = fetchSignedByte();
                break;
            case 3:
                offset = fetchSignedWord();
                break;
            case 4:
                if (autoModify) {
                    incrementAmount = 1;
                    updateRegister = true;
                }
                offset = 0;
                break;
            case 5:
                if (autoModify) {
                    incrementAmount = 2;
                    updateRegister = true;
                }
                offset = 0;
                break;
            case 6:
                if (autoModify) {
                    incrementAmount = -1;
                    updateRegister = true;
                    baseAddress = (baseAddress + incrementAmount) & 0xFFFF;
                }
                offset = 0;
                break;
            case 7:
                if (autoModify) {
                    incrementAmount = -2;
                    updateRegister = true;
                    baseAddress = (baseAddress + incrementAmount) & 0xFFFF;
                }
                offset = 0;
                break;
        }
        int effectiveAddress = (baseAddress + offset) & 0xFFFF;
        if (updateRegister && incrementAmount > 0) {
            switch (register) {
                case 0: regX = (regX + incrementAmount) & 0xFFFF; break;
                case 1: regY = (regY + incrementAmount) & 0xFFFF; break;
                case 2: regU = (regU + incrementAmount) & 0xFFFF; break;
                case 3: regS = (regS + incrementAmount) & 0xFFFF; break;
            }
        }
        if (indirect) {
            effectiveAddress = memory.readWord(effectiveAddress);
        }
        return effectiveAddress;
    }
    private void opLDAimmediate() {
        regA = fetchByte();
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opLDBimmediate() {
        regB = fetchByte();
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opLDXimmediate() {
        regX = fetchWord();
        updateNZFlags16(regX);
        clearVFlag();
    }
    private void opLDYimmediate() {
        regY = fetchWord();
        updateNZFlags16(regY);
        clearVFlag();
    }
    private void opLDUimmediate() {
        regU = fetchWord();
        updateNZFlags16(regU);
        clearVFlag();
    }
    private void opLDSimmediate() {
        regS = fetchWord();
        updateNZFlags16(regS);
        clearVFlag();
    }
    private void opSTAdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        memory.writeByte(addr, (byte) regA);
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opSTBdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        memory.writeByte(addr, (byte) regB);
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opSTXdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        memory.writeWord(addr, regX);
        updateNZFlags16(regX);
        clearVFlag();
    }
    private void opSTYdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        memory.writeWord(addr, regY);
        updateNZFlags16(regY);
        clearVFlag();
    }
    private void opSTUdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        memory.writeWord(addr, regU);
        updateNZFlags16(regU);
        clearVFlag();
    }
    private void opLDAdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        regA = memory.readByte(addr) & 0xFF;
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opLDBdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        regB = memory.readByte(addr) & 0xFF;
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opLDXdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        regX = memory.readWord(addr);
        updateNZFlags16(regX);
        clearVFlag();
    }
    private void opLDYdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        regY = memory.readWord(addr);
        updateNZFlags16(regY);
        clearVFlag();
    }
    private void opLDUdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        regU = memory.readWord(addr);
        updateNZFlags16(regU);
        clearVFlag();
    }
    private void opLDSdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        regS = memory.readWord(addr);
        updateNZFlags16(regS);
        clearVFlag();
    }
    private void opADDAdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        int value = memory.readByte(addr) & 0xFF;
        int result = (regA & 0xFF) + value;
        regA = result & 0xFF;
        updateNZVCFlags(regA, (regA & 0xFF) - 128, value - 128, result);
    }
    private void opADDBdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        int value = memory.readByte(addr) & 0xFF;
        int result = (regB & 0xFF) + value;
        regB = result & 0xFF;
        updateNZVCFlags(regB, (regB & 0xFF) - 128, value - 128, result);
    }
    private void opSUBAdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        int value = memory.readByte(addr) & 0xFF;
        int result = (regA & 0xFF) - value;
        regA = result & 0xFF;
        updateNZVCFlags(regA, (regA & 0xFF) - 128, value - 128, result);
    }
    private void opSUBBDirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        int value = memory.readByte(addr) & 0xFF;
        int result = (regB & 0xFF) - value;
        regB = result & 0xFF;
        updateNZVCFlags(regB, (regB & 0xFF) - 128, value - 128, result);
    }
    private void opANDAdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        regA &= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opANDBdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        regB &= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opORAdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        regA |= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opORBdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        regB |= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opEORAdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        regA ^= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opEORBdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        regB ^= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opLDAextended() {
        int addr = fetchWord();
        regA = memory.readByte(addr) & 0xFF;
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opLDBextended() {
        int addr = fetchWord();
        regB = memory.readByte(addr) & 0xFF;
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opLDXextended() {
        int addr = fetchWord();
        regX = memory.readWord(addr);
        updateNZFlags16(regX);
        clearVFlag();
    }
    private void opLDYextended() {
        int addr = fetchWord();
        regY = memory.readWord(addr);
        updateNZFlags16(regY);
        clearVFlag();
    }
    private void opSTAextended() {
        int addr = fetchWord();
        memory.writeByte(addr, (byte) regA);
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opSTBextended() {
        int addr = fetchWord();
        memory.writeByte(addr, (byte) regB);
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opSTXextended() {
        int addr = fetchWord();
        memory.writeWord(addr, regX);
        updateNZFlags16(regX);
        clearVFlag();
    }
    private void opSTYextended() {
        int addr = fetchWord();
        memory.writeWord(addr, regY);
        updateNZFlags16(regY);
        clearVFlag();
    }
    private void opADDAextended() {
        int addr = fetchWord();
        int value = memory.readByte(addr) & 0xFF;
        int result = (regA & 0xFF) + value;
        regA = result & 0xFF;
        updateNZVCFlags(regA, (regA & 0xFF) - 128, value - 128, result);
    }
    private void opADDBextended() {
        int addr = fetchWord();
        int value = memory.readByte(addr) & 0xFF;
        int result = (regB & 0xFF) + value;
        regB = result & 0xFF;
        updateNZVCFlags(regB, (regB & 0xFF) - 128, value - 128, result);
    }
    private void opSUBAextended() {
        int addr = fetchWord();
        int value = memory.readByte(addr) & 0xFF;
        int result = (regA & 0xFF) - value;
        regA = result & 0xFF;
        updateNZVCFlags(regA, (regA & 0xFF) - 128, value - 128, result);
    }
    private void opSUBBextended() {
        int addr = fetchWord();
        int value = memory.readByte(addr) & 0xFF;
        int result = (regB & 0xFF) - value;
        regB = result & 0xFF;
        updateNZVCFlags(regB, (regB & 0xFF) - 128, value - 128, result);
    }
    private void opANDAextended() {
        int addr = fetchWord();
        regA &= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opANDBextended() {
        int addr = fetchWord();
        regB &= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opORAextended() {
        int addr = fetchWord();
        regA |= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opORBextended() {
        int addr = fetchWord();
        regB |= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opEORAextended() {
        int addr = fetchWord();
        regA ^= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opEORBextended() {
        int addr = fetchWord();
        regB ^= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opLDAindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        regA = memory.readByte(addr) & 0xFF;
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opLDBindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        regB = memory.readByte(addr) & 0xFF;
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opLDXindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        regX = memory.readWord(addr);
        updateNZFlags16(regX);
        clearVFlag();
    }
    private void opLDYindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        regY = memory.readWord(addr);
        updateNZFlags16(regY);
        clearVFlag();
    }
    private void opLDUindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        regU = memory.readWord(addr);
        updateNZFlags16(regU);
        clearVFlag();
    }
    private void opSTAindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        memory.writeByte(addr, (byte) regA);
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opSTBindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        memory.writeByte(addr, (byte) regB);
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opSTXindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        memory.writeWord(addr, regX);
        updateNZFlags16(regX);
        clearVFlag();
    }
    private void opSTYindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        memory.writeWord(addr, regY);
        updateNZFlags16(regY);
        clearVFlag();
    }
    private void opSTUindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        memory.writeWord(addr, regU);
        updateNZFlags16(regU);
        clearVFlag();
    }
    private void opADDAindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        int value = memory.readByte(addr) & 0xFF;
        int result = (regA & 0xFF) + value;
        regA = result & 0xFF;
        updateNZVCFlags(regA, (regA & 0xFF) - 128, value - 128, result);
    }
    private void opADDBindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        int value = memory.readByte(addr) & 0xFF;
        int result = (regB & 0xFF) + value;
        regB = result & 0xFF;
        updateNZVCFlags(regB, (regB & 0xFF) - 128, value - 128, result);
    }
    private void opSUBAindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        int value = memory.readByte(addr) & 0xFF;
        int result = (regA & 0xFF) - value;
        regA = result & 0xFF;
        updateNZVCFlags(regA, (regA & 0xFF) - 128, value - 128, result);
    }
    private void opSUBBindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        int value = memory.readByte(addr) & 0xFF;
        int result = (regB & 0xFF) - value;
        regB = result & 0xFF;
        updateNZVCFlags(regB, (regB & 0xFF) - 128, value - 128, result);
    }
    private void opANDAindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        regA &= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opANDBindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        regB &= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opORAindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        regA |= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opORBindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        regB |= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opEORAindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        regA ^= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opEORBindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        regB ^= (memory.readByte(addr) & 0xFF);
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opADDAimmediate() {
        int value = fetchByte();
        int result = (regA & 0xFF) + value;
        regA = result & 0xFF;
        updateNZVCFlags(regA, (regA & 0xFF) - 128, value - 128, result);
    }
    private void opADDBimmediate() {
        int value = fetchByte();
        int result = (regB & 0xFF) + value;
        regB = result & 0xFF;
        updateNZVCFlags(regB, (regB & 0xFF) - 128, value - 128, result);
    }
    private void opSUBAimmediate() {
        int value = fetchByte();
        int result = (regA & 0xFF) - value;
        regA = result & 0xFF;
        updateNZVCFlags(regA, (regA & 0xFF) - 128, value - 128, result);
    }
    private void opSUBBimmediate() {
        int value = fetchByte();
        int result = (regB & 0xFF) - value;
        regB = result & 0xFF;
        updateNZVCFlags(regB, (regB & 0xFF) - 128, value - 128, result);
    }
    private void opINCA() {
        regA = ((regA & 0xFF) + 1) & 0xFF;
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opINCB() {
        regB = ((regB & 0xFF) + 1) & 0xFF;
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opDECA() {
        regA = ((regA & 0xFF) - 1) & 0xFF;
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opDECB() {
        regB = ((regB & 0xFF) - 1) & 0xFF;
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opANDAimmediate() {
        regA &= fetchByte();
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opANDBimmediate() {
        regB &= fetchByte();
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opORAimmediate() {
        regA |= fetchByte();
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opORBimmediate() {
        regB |= fetchByte();
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opEORAimmediate() {
        regA ^= fetchByte();
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opEORBimmediate() {
        regB ^= fetchByte();
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opCOMA() {
        regA = (~regA) & 0xFF;
        updateNZFlags(regA);
        setCFlag();
        clearVFlag();
    }
    private void opCOMB() {
        regB = (~regB) & 0xFF;
        updateNZFlags(regB);
        setCFlag();
        clearVFlag();
    }
    private void opCLRA() {
        regA = 0;
        clearNZVCFlags();
        setZFlag();
    }
    private void opCLRB() {
        regB = 0;
        clearNZVCFlags();
        setZFlag();
    }
    private void opNEGA() {
        regA = (-regA) & 0xFF;
        updateNZVCFlags(regA, regA, 0, regA);
    }
    private void opNEGB() {
        regB = (-regB) & 0xFF;
        updateNZVCFlags(regB, regB, 0, regB);
    }
    private void opTSTA() {
        updateNZFlags(regA);
        clearVFlag();
        clearCFlag();
    }
    private void opTSTB() {
        updateNZFlags(regB);
        clearVFlag();
        clearCFlag();
    }
    private void opROLA() {
        int oldC = conditionCodes.contains(Flag.C) ? 1 : 0;
        int newC = (regA & 0x80) != 0 ? 1 : 0;
        regA = ((regA << 1) | oldC) & 0xFF;
        updateNZFlags(regA);
        if (newC != 0) {
            setCFlag();
        } else {
            clearCFlag();
        }
        clearVFlag();
    }
    private void opROLB() {
        int oldC = conditionCodes.contains(Flag.C) ? 1 : 0;
        int newC = (regB & 0x80) != 0 ? 1 : 0;
        regB = ((regB << 1) | oldC) & 0xFF;
        updateNZFlags(regB);
        if (newC != 0) {
            setCFlag();
        } else {
            clearCFlag();
        }
        clearVFlag();
    }
    private void opRORA() {
        int oldC = conditionCodes.contains(Flag.C) ? 0x80 : 0;
        int newC = (regA & 0x01) != 0 ? 1 : 0;
        regA = ((regA >> 1) | oldC) & 0xFF;
        updateNZFlags(regA);
        if (newC != 0) {
            setCFlag();
        } else {
            clearCFlag();
        }
        clearVFlag();
    }
    private void opRORB() {
        int oldC = conditionCodes.contains(Flag.C) ? 0x80 : 0;
        int newC = (regB & 0x01) != 0 ? 1 : 0;
        regB = ((regB >> 1) | oldC) & 0xFF;
        updateNZFlags(regB);
        if (newC != 0) {
            setCFlag();
        } else {
            clearCFlag();
        }
        clearVFlag();
    }
    private void opASLA() {
        int newC = (regA & 0x80) != 0 ? 1 : 0;
        regA = (regA << 1) & 0xFF;
        updateNZFlags(regA);
        if (newC != 0) {
            setCFlag();
        } else {
            clearCFlag();
        }
        boolean n = conditionCodes.contains(Flag.N);
        boolean c = conditionCodes.contains(Flag.C);
        if (n != c) {
            setVFlag();
        } else {
            clearVFlag();
        }
    }
    private void opASLB() {
        int newC = (regB & 0x80) != 0 ? 1 : 0;
        regB = (regB << 1) & 0xFF;
        updateNZFlags(regB);
        if (newC != 0) {
            setCFlag();
        } else {
            clearCFlag();
        }
        boolean n = conditionCodes.contains(Flag.N);
        boolean c = conditionCodes.contains(Flag.C);
        if (n != c) {
            setVFlag();
        } else {
            clearVFlag();
        }
    }
    private void opASRA() {
        int newC = (regA & 0x01) != 0 ? 1 : 0;
        int sign = regA & 0x80;
        regA = ((regA >> 1) | sign) & 0xFF;
        updateNZFlags(regA);
        if (newC != 0) {
            setCFlag();
        } else {
            clearCFlag();
        }
        clearVFlag();
    }
    private void opASRB() {
        int newC = (regB & 0x01) != 0 ? 1 : 0;
        int sign = regB & 0x80;
        regB = ((regB >> 1) | sign) & 0xFF;
        updateNZFlags(regB);
        if (newC != 0) {
            setCFlag();
        } else {
            clearCFlag();
        }
        clearVFlag();
    }
    private void opLSRA() {
        int newC = (regA & 0x01) != 0 ? 1 : 0;
        regA = (regA >> 1) & 0xFF;
        updateNZFlags(regA);
        if (newC != 0) {
            setCFlag();
        } else {
            clearCFlag();
        }
        clearVFlag();
    }
    private void opLSRB() {
        int newC = (regB & 0x01) != 0 ? 1 : 0;
        regB = (regB >> 1) & 0xFF;
        updateNZFlags(regB);
        if (newC != 0) {
            setCFlag();
        } else {
            clearCFlag();
        }
        clearVFlag();
    }
    private void opINX() {
        regX = (regX + 1) & 0xFFFF;
        updateNZFlags16(regX);
        clearVFlag();
    }
    private void opDEX() {
        regX = (regX - 1) & 0xFFFF;
        updateNZFlags16(regX);
        clearVFlag();
    }
    private void opINY() {
        regY = (regY + 1) & 0xFFFF;
        updateNZFlags16(regY);
        clearVFlag();
    }
    private void opDEY() {
        regY = (regY - 1) & 0xFFFF;
        updateNZFlags16(regY);
        clearVFlag();
    }
    private void opABX() {
        regX = (regX + (regB & 0xFF)) & 0xFFFF;
    }
    private void opNOP() {
    }
    private void opSYNC() {
    }
    private void opBRA() {
        int offset = fetchSignedByte();
        regPC = (regPC + offset) & 0xFFFF;
    }
    private void opBNE() {
        int offset = fetchSignedByte();
        if (!conditionCodes.contains(Flag.Z)) {
            regPC = (regPC + offset) & 0xFFFF;
        }
    }
    private void opBEQ() {
        int offset = fetchSignedByte();
        if (conditionCodes.contains(Flag.Z)) {
            regPC = (regPC + offset) & 0xFFFF;
        }
    }
    private void opBCC() {
        int offset = fetchSignedByte();
        if (!conditionCodes.contains(Flag.C)) {
            regPC = (regPC + offset) & 0xFFFF;
        }
    }
    private void opBCS() {
        int offset = fetchSignedByte();
        if (conditionCodes.contains(Flag.C)) {
            regPC = (regPC + offset) & 0xFFFF;
        }
    }
    private void opBPL() {
        int offset = fetchSignedByte();
        if (!conditionCodes.contains(Flag.N)) {
            regPC = (regPC + offset) & 0xFFFF;
        }
    }
    private void opBMI() {
        int offset = fetchSignedByte();
        if (conditionCodes.contains(Flag.N)) {
            regPC = (regPC + offset) & 0xFFFF;
        }
    }
    private void opBGT() {
        int offset = fetchSignedByte();
        boolean n = conditionCodes.contains(Flag.N);
        boolean v = conditionCodes.contains(Flag.V);
        boolean z = conditionCodes.contains(Flag.Z);
        if (!z && (n == v)) {
            regPC = (regPC + offset) & 0xFFFF;
        }
    }
    private void opBLE() {
        int offset = fetchSignedByte();
        boolean n = conditionCodes.contains(Flag.N);
        boolean v = conditionCodes.contains(Flag.V);
        boolean z = conditionCodes.contains(Flag.Z);
        if (z || (n != v)) {
            regPC = (regPC + offset) & 0xFFFF;
        }
    }
    private void opBHI() {
        int offset = fetchSignedByte();
        boolean c = conditionCodes.contains(Flag.C);
        boolean z = conditionCodes.contains(Flag.Z);
        if (!c && !z) {
            regPC = (regPC + offset) & 0xFFFF;
        }
    }
    private void opBLS() {
        int offset = fetchSignedByte();
        boolean c = conditionCodes.contains(Flag.C);
        boolean z = conditionCodes.contains(Flag.Z);
        if (c || z) {
            regPC = (regPC + offset) & 0xFFFF;
        }
    }
    private void opLBRA() {
        int offset = fetchSignedWord();
        regPC = (regPC + offset) & 0xFFFF;
    }
    private void opLBSR() {
        int offset = fetchSignedWord();
        pushWord(regPC);
        regPC = (regPC + offset) & 0xFFFF;
    }
    private void opBSR() {
        int offset = fetchSignedByte();
        pushWord(regPC);
        regPC = (regPC + offset) & 0xFFFF;
    }
    private void opCMPAimmediate() {
        int value = fetchByte();
        int result = (regA & 0xFF) - value;
        updateNZVCFlags(result & 0xFF, (regA & 0xFF) - 128, value - 128, result);
    }
    private void opCMPBimmediate() {
        int value = fetchByte();
        int result = (regB & 0xFF) - value;
        updateNZVCFlags(result & 0xFF, (regB & 0xFF) - 128, value - 128, result);
    }
    private void opCMPXimmediate() {
        int value = fetchWord();
        int result = regX - value;
        updateNZFlags16(result & 0xFFFF);
        if (result < 0) {
            setCFlag();
        } else {
            clearCFlag();
        }
        clearVFlag();
    }
    private void opCMPAdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        int value = memory.readByte(addr) & 0xFF;
        int result = (regA & 0xFF) - value;
        updateNZVCFlags(result & 0xFF, (regA & 0xFF) - 128, value - 128, result);
    }
    private void opCMPBdirect() {
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        int value = memory.readByte(addr) & 0xFF;
        int result = (regB & 0xFF) - value;
        updateNZVCFlags(result & 0xFF, (regB & 0xFF) - 128, value - 128, result);
    }
    private void opCMPAextended() {
        int addr = fetchWord();
        int value = memory.readByte(addr) & 0xFF;
        int result = (regA & 0xFF) - value;
        updateNZVCFlags(result & 0xFF, (regA & 0xFF) - 128, value - 128, result);
    }
    private void opCMPBextended() {
        int addr = fetchWord();
        int value = memory.readByte(addr) & 0xFF;
        int result = (regB & 0xFF) - value;
        updateNZVCFlags(result & 0xFF, (regB & 0xFF) - 128, value - 128, result);
    }
    private void opCMPAindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        int value = memory.readByte(addr) & 0xFF;
        int result = (regA & 0xFF) - value;
        updateNZVCFlags(result & 0xFF, (regA & 0xFF) - 128, value - 128, result);
    }
    private void opCMPBindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        int value = memory.readByte(addr) & 0xFF;
        int result = (regB & 0xFF) - value;
        updateNZVCFlags(result & 0xFF, (regB & 0xFF) - 128, value - 128, result);
    }
    private void opJMPextended() {
        int addr = fetchWord();
        regPC = addr;
    }
    private void opJMPindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        regPC = addr;
    }
    private void opJSRextended() {
        int addr = fetchWord();
        pushWord(regPC);
        regPC = addr;
    }
    private void opJSRindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        pushWord(regPC);
        regPC = addr;
    }
    private void opTFR() {
        int postByte = fetchByte();
        int srcReg = (postByte >> 4) & 0x0F;
        int dstReg = postByte & 0x0F;
        int value = getRegisterValue(srcReg);
        
        if (srcReg >= 8 && dstReg < 8) {
            value = value & 0xFF;
        } else if (srcReg < 8 && dstReg >= 8) {
            value = value & 0xFF;
        }
        
        setRegisterValue(dstReg, value);
        
        if (dstReg >= 8) {
            updateNZFlags(value & 0xFF);
        } else if (dstReg == 0) {
            updateNZFlags16(value & 0xFFFF);
        } else {
            updateNZFlags16(value & 0xFFFF);
        }
        clearVFlag();
    }
    private void opEXG() {
        int postByte = fetchByte();
        int reg1 = (postByte >> 4) & 0x0F;
        int reg2 = postByte & 0x0F;
        int temp = getRegisterValue(reg1);
        setRegisterValue(reg1, getRegisterValue(reg2));
        setRegisterValue(reg2, temp);
    }
    private void opPSHA() {
        regS = (regS - 1) & 0xFFFF;
        memory.writeByte(regS, (byte) regA);
    }
    private void opPSHB() {
        regS = (regS - 1) & 0xFFFF;
        memory.writeByte(regS, (byte) regB);
    }
    private void opPULA() {
        regA = memory.readByte(regS) & 0xFF;
        regS = (regS + 1) & 0xFFFF;
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opPULB() {
        regB = memory.readByte(regS) & 0xFF;
        regS = (regS + 1) & 0xFFFF;
        updateNZFlags(regB);
        clearVFlag();
    }
    private void opPSHS() {
        int postByte = fetchByte();
        if ((postByte & 0x80) != 0)
            pushWord(regPC);
        if ((postByte & 0x40) != 0)
            pushWord(regU);
        if ((postByte & 0x20) != 0)
            pushWord(regY);
        if ((postByte & 0x10) != 0)
            pushWord(regX);
        if ((postByte & 0x08) != 0)
            pushByte(regDP);
        if ((postByte & 0x04) != 0)
            pushByte(regB);
        if ((postByte & 0x02) != 0)
            pushByte(regA);
        if ((postByte & 0x01) != 0)
            pushByte(getCC());
    }
    private void opPULS() {
        int postByte = fetchByte();
        if ((postByte & 0x01) != 0)
            setCC(pullByte());
        if ((postByte & 0x02) != 0)
            regA = pullByte() & 0xFF;
        if ((postByte & 0x04) != 0)
            regB = pullByte() & 0xFF;
        if ((postByte & 0x08) != 0)
            regDP = pullByte() & 0xFF;
        if ((postByte & 0x10) != 0)
            regX = pullWord();
        if ((postByte & 0x20) != 0)
            regY = pullWord();
        if ((postByte & 0x40) != 0)
            regU = pullWord();
        if ((postByte & 0x80) != 0)
            regPC = pullWord();
    }
    private void opMUL() {
        int result = (regA & 0xFF) * (regB & 0xFF);
        regA = (result >> 8) & 0xFF;
        regB = result & 0xFF;
        updateNZFlags16(result & 0xFFFF);
        if ((regB & 0x80) != 0) {
            setCFlag();
        } else {
            clearCFlag();
        }
        clearVFlag();
    }
    private void opSEX() {
        regA = (regB & 0x80) != 0 ? 0xFF : 0x00;
        updateNZFlags(regA);
        clearVFlag();
    }
    private void opSWI() {
        pushWord(regPC);
        pushWord(regU);
        pushWord(regY);
        pushWord(regX);
        pushByte(regDP);
        pushByte(regB);
        pushByte(regA);
        pushByte(getCC());
        regPC = memory.readWord(0xFFFA);
        setIFlag();
        setFFlag();
    }
    private void opRTI() {
        setCC(pullByte());
        regA = pullByte() & 0xFF;
        regB = pullByte() & 0xFF;
        regDP = pullByte() & 0xFF;
        regX = pullWord();
        regY = pullWord();
        regU = pullWord();
        regPC = pullWord();
    }
    private void opRTS() {
        regPC = pullWord();
    }
    private void opRESET() {
    }
    private void opCWAI() {
        int mask = fetchByte();
        setCC(getCC() & mask);
        setIFlag();
        setFFlag();
    }
    private void opORCC() {
        int mask = fetchByte();
        setCC(getCC() | mask);
    }
    private void opANDCC() {
        int mask = fetchByte();
        setCC(getCC() & mask);
    }
    private void pushByte(int value) {
        regS = (regS - 1) & 0xFFFF;
        memory.writeByte(regS, (byte) (value & 0xFF));
    }
    private void pushWord(int value) {
        pushByte((value >> 8) & 0xFF);
        pushByte(value & 0xFF);
    }
    private int pullByte() {
        int value = memory.readByte(regS) & 0xFF;
        regS = (regS + 1) & 0xFFFF;
        return value;
    }
    private int pullWord() {
        int lo = pullByte();
        int hi = pullByte();
        return (hi << 8) | lo;
    }
    private int getRegisterValue(int reg) {
        switch (reg) {
            case 0:
                return regD() & 0xFFFF;
            case 1:
                return regX & 0xFFFF;
            case 2:
                return regY & 0xFFFF;
            case 3:
                return regU & 0xFFFF;
            case 4:
                return regS & 0xFFFF;
            case 5:
                return regPC & 0xFFFF;
            case 8:
                return regA & 0xFF;
            case 9:
                return regB & 0xFF;
            case 10:
                return getCC();
            case 11:
                return regDP & 0xFF;
            default:
                return 0;
        }
    }
    private void setRegisterValue(int reg, int value) {
        switch (reg) {
            case 0:
                setD(value);
                break;
            case 1:
                regX = value & 0xFFFF;
                break;
            case 2:
                regY = value & 0xFFFF;
                break;
            case 3:
                regU = value & 0xFFFF;
                break;
            case 4:
                regS = value & 0xFFFF;
                break;
            case 5:
                regPC = value & 0xFFFF;
                break;
            case 8:
                regA = value & 0xFF;
                break;
            case 9:
                regB = value & 0xFF;
                break;
            case 10:
                setCC(value & 0xFF);
                break;
            case 11:
                regDP = value & 0xFF;
                break;
        }
    }
    private int regD() {
        return ((regA & 0xFF) << 8) | (regB & 0xFF);
    }
    private void setD(int value) {
        regA = (value >> 8) & 0xFF;
        regB = value & 0xFF;
    }
    private int getCC() {
        int cc = 0;
        if (conditionCodes.contains(Flag.C))
            cc |= 0x01;
        if (conditionCodes.contains(Flag.V))
            cc |= 0x02;
        if (conditionCodes.contains(Flag.Z))
            cc |= 0x04;
        if (conditionCodes.contains(Flag.N))
            cc |= 0x08;
        if (conditionCodes.contains(Flag.I))
            cc |= 0x10;
        if (conditionCodes.contains(Flag.H))
            cc |= 0x20;
        if (conditionCodes.contains(Flag.F))
            cc |= 0x40;
        if (conditionCodes.contains(Flag.E))
            cc |= 0x80;
        return cc;
    }
    private void setCC(int cc) {
        conditionCodes.clear();
        if ((cc & 0x01) != 0)
            conditionCodes.add(Flag.C);
        if ((cc & 0x02) != 0)
            conditionCodes.add(Flag.V);
        if ((cc & 0x04) != 0)
            conditionCodes.add(Flag.Z);
        if ((cc & 0x08) != 0)
            conditionCodes.add(Flag.N);
        if ((cc & 0x10) != 0)
            conditionCodes.add(Flag.I);
        if ((cc & 0x20) != 0)
            conditionCodes.add(Flag.H);
        if ((cc & 0x40) != 0)
            conditionCodes.add(Flag.F);
        if ((cc & 0x80) != 0)
            conditionCodes.add(Flag.E);
    }
    private void updateNZFlags(int value) {
        value &= 0xFF;
        conditionCodes.remove(Flag.N);
        conditionCodes.remove(Flag.Z);
        if ((value & 0x80) != 0) {
            conditionCodes.add(Flag.N);
        }
        if (value == 0) {
            conditionCodes.add(Flag.Z);
        }
    }
    private void updateNZFlags16(int value) {
        value &= 0xFFFF;
        conditionCodes.remove(Flag.N);
        conditionCodes.remove(Flag.Z);
        if ((value & 0x8000) != 0) {
            conditionCodes.add(Flag.N);
        }
        if (value == 0) {
            conditionCodes.add(Flag.Z);
        }
    }
    private void updateNZVCFlags(int result, int a, int b, int fullResult) {
        result &= 0xFF;
        updateNZFlags(result);
        boolean overflow = ((a ^ b) & 0x80) == 0 && ((a ^ result) & 0x80) != 0;
        if (overflow) {
            setVFlag();
        } else {
            clearVFlag();
        }
        if (fullResult > 0xFF || fullResult < 0) {
            setCFlag();
        } else {
            clearCFlag();
        }
    }
    private void setCFlag() {
        conditionCodes.add(Flag.C);
    }
    private void clearCFlag() {
        conditionCodes.remove(Flag.C);
    }
    private void setVFlag() {
        conditionCodes.add(Flag.V);
    }
    private void clearVFlag() {
        conditionCodes.remove(Flag.V);
    }
    private void setZFlag() {
        conditionCodes.add(Flag.Z);
    }
    private void setIFlag() {
        conditionCodes.add(Flag.I);
    }
    private void setFFlag() {
        conditionCodes.add(Flag.F);
    }
    private void clearNZVCFlags() {
        conditionCodes.remove(Flag.N);
        conditionCodes.remove(Flag.Z);
        conditionCodes.remove(Flag.V);
        conditionCodes.remove(Flag.C);
    }
    public byte readByte(int address) {
        return memory.readByte(address);
    }
    public void writeByte(int address, byte value) {
        memory.writeByte(address, value);
    }
    public int getA() {
        return regA & 0xFF;
    }
    public int getB() {
        return regB & 0xFF;
    }
    public int getX() {
        return regX & 0xFFFF;
    }
    public int getY() {
        return regY & 0xFFFF;
    }
    public int getU() {
        return regU & 0xFFFF;
    }
    public int getS() {
        return regS & 0xFFFF;
    }
    public int getPC() {
        return regPC & 0xFFFF;
    }
    public int getDP() {
        return regDP & 0xFF;
    }
    public EnumSet<Flag> getConditionCodes() {
        return EnumSet.copyOf(conditionCodes);
    }
    public boolean isFlagSet(Flag flag) {
        return conditionCodes.contains(flag);
    }
    public void setPC(int address) {
        regPC = address & 0xFFFF;
    }
    public void setA(int value) {
        regA = value & 0xFF;
    }
    public void setB(int value) {
        regB = value & 0xFF;
    }
    public void setX(int value) {
        regX = value & 0xFFFF;
    }
    public void setY(int value) {
        regY = value & 0xFFFF;
    }
    public void setU(int value) {
        regU = value & 0xFFFF;
    }
    public void setS(int value) {
        regS = value & 0xFFFF;
    }
    public void setDP(int value) {
        regDP = value & 0xFF;
    }
    public enum Flag {
        C, V, Z, N, I, H, F, E
    }
}
