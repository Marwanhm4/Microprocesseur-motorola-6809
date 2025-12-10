package com.motorola6809.simulator;

import java.util.EnumSet;

/**
 * Motorola 6809 CPU implementation.
 * Supports a comprehensive set of 6809 instructions with immediate and direct
 * addressing modes.
 */
public class CPU {

    private final Memory memory;

    // 8-bit accumulators
    private int regA;
    private int regB;

    // 16-bit registers
    private int regX;
    private int regY;
    private int regU;
    private int regS;
    private int regPC;
    private int regDP;

    private EnumSet<Flag> conditionCodes = EnumSet.noneOf(Flag.class);

    public CPU(Memory memory) {
        this.memory = memory;
        reset();
    }

    /**
     * Reset sequence: clear registers and load PC from the reset vector.
     */
    public void reset() {
        regA = regB = 0;
        regX = regY = regU = regS = 0;
        regDP = 0;
        conditionCodes.clear();
        regPC = memory.readWord(0xFFFE);
    }

    /**
     * Execute one instruction at the current PC.
     */
    public void step() {
        int opcode = fetchByte();
        switch (opcode) {

            // ==================== MODE IMMÉDIAT (#imm) ====================
            // La valeur est directement dans l'instruction (ne touche PAS au registre DP)

            // LOAD - Immediate
            case 0x86:
                opLDAimmediate();
                break; // LDA #imm
            case 0xC6:
                opLDBimmediate();
                break; // LDB #imm
            case 0x8E:
                opLDXimmediate();
                break; // LDX #imm16
            case 0xCE:
                opLDUimmediate();
                break; // LDU #imm16

            // ARITHMETIC - Immediate
            case 0x8B:
                opADDAimmediate();
                break; // ADDA #imm
            case 0xCB:
                opADDBimmediate();
                break; // ADDB #imm
            case 0x80:
                opSUBAimmediate();
                break; // SUBA #imm
            case 0xC0:
                opSUBBimmediate();
                break; // SUBB #imm

            // LOGICAL - Immediate
            case 0x84:
                opANDAimmediate();
                break; // ANDA #imm
            case 0xC4:
                opANDBimmediate();
                break; // ANDB #imm
            case 0x8A:
                opORAimmediate();
                break; // ORA #imm
            case 0xCA:
                opORBimmediate();
                break; // ORB #imm
            case 0x88:
                opEORAimmediate();
                break; // EORA #imm
            case 0xC8:
                opEORBimmediate();
                break; // EORB #imm

            // ==================== MODE DIRECT PAGE (<addr) ====================
            // Utilise le registre DP pour former l'adresse: (DP << 8) | offset

            // LOAD - Direct Page
            case 0x96: opLDAdirect(); break;     // LDA <addr
            case 0xD6: opLDBdirect(); break;     // LDB <addr
            case 0x9E: opLDXdirect(); break;     // LDX <addr
            case 0xDE: opLDYdirect(); break;     // LDY <addr
            case 0x9C: opLDUdirect(); break;     // LDU <addr
            case 0x9D: opLDSdirect(); break;     // LDS <addr

            // STORE - Direct Page
            case 0x97: opSTAdirect(); break;     // STA <addr
            case 0xD7: opSTBdirect(); break;     // STB <addr
            case 0x9F: opSTXdirect(); break;     // STX <addr
            case 0xDF: opSTYdirect(); break;     // STY <addr
            case 0xDD: opSTUdirect(); break;     // STU <addr

            // ARITHMETIC - Direct Page
            case 0x9B: opADDAdirect(); break;    // ADDA <addr
            case 0xDB: opADDBdirect(); break;    // ADDB <addr
            case 0x90: opSUBAdirect(); break;    // SUBA <addr
            case 0xD0: opSUBBDirect(); break;    // SUBB <addr

            // LOGICAL - Direct Page
            case 0x94: opANDAdirect(); break;    // ANDA <addr
            case 0xD4: opANDBdirect(); break;    // ANDB <addr
            case 0x9A: opORAdirect(); break;     // ORA <addr
            case 0xDA: opORBdirect(); break;     // ORB <addr
            case 0x98: opEORAdirect(); break;    // EORA <addr
            case 0xD8: opEORBdirect(); break;    // EORB <addr

            // ==================== MODE ÉTENDU ($addr) ====================
            // Adresse 16 bits complète directement dans l'instruction

            // LOAD - Extended
            case 0xB6: opLDAextended(); break;   // LDA $addr
            case 0xF6: opLDBextended(); break;   // LDB $addr
            case 0xBE: opLDXextended(); break;   // LDX $addr
            case 0xFE: opLDYextended(); break;   // LDY $addr

            // STORE - Extended
            case 0xB7: opSTAextended(); break;   // STA $addr
            case 0xF7: opSTBextended(); break;   // STB $addr
            case 0xBF: opSTXextended(); break;   // STX $addr
            case 0xFF: opSTYextended(); break;   // STY $addr

            // ARITHMETIC - Extended
            case 0xBB: opADDAextended(); break;   // ADDA $addr
            case 0xFB: opADDBextended(); break;   // ADDB $addr
            case 0xB0: opSUBAextended(); break;   // SUBA $addr
            case 0xF0: opSUBBextended(); break;   // SUBB $addr

            // LOGICAL - Extended
            case 0xB4: opANDAextended(); break;   // ANDA $addr
            case 0xF4: opANDBextended(); break;   // ANDB $addr
            case 0xBA: opORAextended(); break;    // ORA $addr
            case 0xFA: opORBextended(); break;    // ORB $addr
            case 0xB8: opEORAextended(); break;   // EORA $addr
            case 0xF8: opEORBextended(); break;   // EORB $addr

            // ==================== MODE INDEXÉ (régistre + offset) ====================
            // Utilise un registre index (X, Y, U, S) avec offset optionnel

            // LOAD - Indexed
            case 0xA6: opLDAindexed(); break;    // LDA ,R (indexed)
            case 0xE6: opLDBindexed(); break;    // LDB ,R
            case 0xAE: opLDXindexed(); break;    // LDX ,R
            case 0xEE: opLDUindexed(); break;    // LDU ,R (indexed)

            // STORE - Indexed
            case 0xA7: opSTAindexed(); break;    // STA ,R
            case 0xE7: opSTBindexed(); break;    // STB ,R
            case 0xAF: opSTXindexed(); break;    // STX ,R
            case 0xEF: opSTUindexed(); break;    // STU ,R (indexed)

            // ARITHMETIC - Indexed
            case 0xAB: opADDAindexed(); break;    // ADDA ,R
            case 0xEB: opADDBindexed(); break;    // ADDB ,R
            case 0xA0: opSUBAindexed(); break;    // SUBA ,R
            case 0xE0: opSUBBindexed(); break;    // SUBB ,R

            // LOGICAL - Indexed
            case 0xA4: opANDAindexed(); break;    // ANDA ,R
            case 0xE4: opANDBindexed(); break;    // ANDB ,R
            case 0xAA: opORAindexed(); break;     // ORA ,R
            case 0xEA: opORBindexed(); break;     // ORB ,R
            case 0xA8: opEORAindexed(); break;    // EORA ,R
            case 0xE8: opEORBindexed(); break;    // EORB ,R

            // ==================== MODE INHÉRENT ====================
            // Pas d'opérande, opération directement sur les registres

            // INC/DEC - 8-bit registers
            case 0x4C:
                opINCA();
                break; // INCA
            case 0x5C:
                opINCB();
                break; // INCB
            case 0x4A:
                opDECA();
                break; // DECA
            case 0x5A:
                opDECB();
                break; // DECB

            // INC/DEC - 16-bit registers
            case 0x08:
                opINX();
                break; // INX (increment X)
            case 0x09:
                opDEX();
                break; // DEX (decrement X)

            // LOGICAL - Inherent
            case 0x43:
                opCOMA();
                break; // COMA
            case 0x53:
                opCOMB();
                break; // COMB
            case 0x4F:
                opCLRA();
                break; // CLRA
            case 0x5F:
                opCLRB();
                break; // CLRB

            // NEGATE - Inherent
            case 0x40:
                opNEGA();
                break; // NEGA
            case 0x50:
                opNEGB();
                break; // NEGB

            // TEST - Inherent
            case 0x4D:
                opTSTA();
                break; // TSTA
            case 0x5D:
                opTSTB();
                break; // TSTB

            // ROTATE - Inherent
            case 0x49:
                opROLA();
                break; // ROLA
            case 0x59:
                opROLB();
                break; // ROLB
            case 0x46:
                opRORA();
                break; // RORA
            case 0x56:
                opRORB();
                break; // RORB

            // SHIFT - Inherent
            case 0x48:
                opASLA();
                break; // ASLA/LSLA (shift left)
            case 0x58:
                opASLB();
                break; // ASLB/LSLB
            case 0x47:
                opASRA();
                break; // ASRA (arithmetic shift right)
            case 0x57:
                opASRB();
                break; // ASRB
            case 0x44:
                opLSRA();
                break; // LSRA (logical shift right)
            case 0x54:
                opLSRB();
                break; // LSRB

            // ARITHMETIC - Inherent
            case 0x3D:
                opMUL();
                break; // MUL (multiply A*B -> D)
            case 0x1D:
                opSEX();
                break; // SEX (sign extend B -> D)
            case 0x3A:
                opABX();
                break; // ABX (add B to X)

            // STACK - Inherent
            case 0x36:
                opPSHA();
                break; // PSHA (push A)
            case 0x37:
                opPSHB();
                break; // PSHB (push B)
            case 0x32:
                opPULA();
                break; // PULA (pull A)
            case 0x33:
                opPULB();
                break; // PULB (pull B)

            // CONTROL - Inherent
            case 0x12:
                opNOP();
                break; // NOP (no operation)
            case 0x13:
                opSYNC();
                break; // SYNC (synchronize)
            case 0x3F:
                opSWI();
                break; // SWI (software interrupt)
            case 0x3B:
                opRTI();
                break; // RTI (return from interrupt)
            case 0x39:
                opRTS();
                break; // RTS (return from subroutine)
            case 0x3E:
                opRESET();
                break; // RESET (reset external devices)

            // ==================== MODE RELATIF (branches) ====================
            // Offset signé 8 bits relatif au PC

            case 0x20:
                opBRA();
                break; // BRA (branch always)
            case 0x26:
                opBNE();
                break; // BNE (branch if not equal)
            case 0x27:
                opBEQ();
                break; // BEQ (branch if equal)
            case 0x24:
                opBCC();
                break; // BCC (branch if carry clear)
            case 0x25:
                opBCS();
                break; // BCS (branch if carry set)
            case 0x2A:
                opBPL();
                break; // BPL (branch if plus)
            case 0x2B:
                opBMI();
                break; // BMI (branch if minus)
            case 0x2E:
                opBGT();
                break; // BGT (branch if greater than)
            case 0x2F:
                opBLE();
                break; // BLE (branch if less or equal)
            case 0x22:
                opBHI();
                break; // BHI (branch if higher)
            case 0x23:
                opBLS();
                break; // BLS (branch if lower or same)

            // ==================== MODE RELATIF LONG (branches 16 bits) ====================
            // Offset signé 16 bits relatif au PC

            case 0x16: opLBRA(); break;          // LBRA (long branch always)
            case 0x17: opLBSR(); break;          // LBSR (long branch to subroutine)

            // ==================== INSTRUCTIONS DE COMPARAISON ====================

            // COMPARE - Immediate
            case 0x81: opCMPAimmediate(); break; // CMPA #imm
            case 0xC1: opCMPBimmediate(); break; // CMPB #imm
            case 0x8C: opCMPXimmediate(); break; // CMPX #imm16

            // COMPARE - Direct Page
            case 0x91: opCMPAdirect(); break;    // CMPA <addr
            case 0xD1: opCMPBdirect(); break;    // CMPB <addr

            // COMPARE - Extended
            case 0xB1: opCMPAextended(); break;  // CMPA $addr
            case 0xF1: opCMPBextended(); break;  // CMPB $addr

            // COMPARE - Indexed
            case 0xA1: opCMPAindexed(); break;   // CMPA ,R
            case 0xE1: opCMPBindexed(); break;   // CMPB ,R

            // ==================== INSTRUCTIONS DE SAUT ====================

            // JUMP - Extended
            case 0x7E: opJMPextended(); break;    // JMP $addr

            // JUMP - Indexed
            case 0x6E: opJMPindexed(); break;     // JMP ,R

            // JUMP TO SUBROUTINE - Extended
            case 0xBD: opJSRextended(); break;    // JSR $addr

            // JUMP TO SUBROUTINE - Indexed
            case 0xAD: opJSRindexed(); break;    // JSR ,R

            // BRANCH TO SUBROUTINE - Relative
            case 0x8D: opBSR(); break;           // BSR (branch to subroutine)

            // ==================== INSTRUCTIONS AVEC POST-BYTE ====================

            case 0x1F:
                opTFR();
                break; // TFR (transfer register - avec post-byte)
            case 0x1E:
                opEXG();
                break; // EXG (exchange registers - avec post-byte)
            case 0x34:
                opPSHS();
                break; // PSHS (push registers - avec post-byte)
            case 0x35:
                opPULS();
                break; // PULS (pull registers - avec post-byte)
            case 0x3C:
                opCWAI();
                break; // CWAI (clear and wait - avec post-byte)
            case 0x1A:
                opORCC();
                break; // ORCC (OR condition codes - avec immédiat)
            case 0x1C:
                opANDCC();
                break; // ANDCC (AND condition codes - avec immédiat)

            // ==================== OPCODES ÉTENDUS (préfixe 0x10) ====================

            case 0x10: // Extended opcode prefix
                int postByte = fetchByte();
                switch (postByte) {
                    case 0xBE: opLDYimmediate(); break; // LDY #imm16 (immédiat)
                    case 0xAF: opLDYindexed(); break;   // LDY ,R (indexed)
                    case 0xBF: opSTYindexed(); break;   // STY ,R (indexed)
                    case 0x3C: opINY(); break;          // INY (inhérent)
                    case 0x3D: opDEY(); break;          // DEY (inhérent)
                    case 0x21: // LBRN (long branch never)
                        fetchWord(); // Skip offset 16 bits
                        break;
                    default:
                        throw new IllegalStateException(
                                String.format("Extended opcode 0x10%02X not implemented at PC=0x%04X", postByte,
                                        (regPC - 1) & 0xFFFF));
                }
                break;

            default:
                throw new IllegalStateException(
                        String.format("Opcode 0x%02X not implemented at PC=0x%04X", opcode, (regPC - 1) & 0xFFFF));
        }
    }

    // ==================== FETCH METHODS ====================

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

    // ==================== INDEXED ADDRESSING MODE HELPER ====================
    /**
     * Calcule l'adresse effective pour le mode d'adressage indexé.
     * Le post-byte encode le registre index, le mode d'adressage, et les options.
     * 
     * Format du post-byte: [RRMMM0AI]
     *   RR (bits 6-7): Registre index (00=X, 01=Y, 10=U, 11=S)
     *   MMM (bits 3-5): Mode d'adressage
     *   0: réservé
     *   A (bit 1): Auto-modification (incrément/décrément)
     *   I (bit 0): Indirect
     * 
     * @param postByte Le byte post-instruction qui encode le mode indexé
     * @return L'adresse effective calculée
     */
    private int calculateIndexedAddress(int postByte) {
        int register = (postByte >> 6) & 0x03; // Bits 6-7: registre (X=00, Y=01, U=10, S=11)
        int mode = (postByte >> 3) & 0x07;      // Bits 3-5: mode d'adressage
        boolean indirect = (postByte & 0x02) != 0;  // Bit 1: indirect
        boolean autoModify = (postByte & 0x01) != 0; // Bit 0: auto-modification
        
        // Obtenir le registre de base
        int baseAddress = 0;
        switch (register) {
            case 0: baseAddress = regX; break;  // X
            case 1: baseAddress = regY; break;  // Y
            case 2: baseAddress = regU; break;  // U
            case 3: baseAddress = regS; break;   // S
        }
        
        int offset = 0;
        boolean updateRegister = false;
        int incrementAmount = 0;
        
        switch (mode) {
            case 0: // Pas d'offset (0,R)
                offset = 0;
                break;
            case 1: // Offset 5 bits signé (-16 à +15)
                int offset5 = fetchByte() & 0x1F;
                offset = (offset5 & 0x10) != 0 ? offset5 | 0xFFFFFFE0 : offset5;
                break;
            case 2: // Offset 8 bits signé (-128 à +127)
                offset = fetchSignedByte();
                break;
            case 3: // Offset 16 bits signé
                offset = fetchSignedWord();
                break;
            case 4: // Auto-incrément (R+)
                if (autoModify) {
                    incrementAmount = 1;
                    updateRegister = true;
                }
                offset = 0;
                break;
            case 5: // Auto-incrément de 2 (R++)
                if (autoModify) {
                    incrementAmount = 2;
                    updateRegister = true;
                }
                offset = 0;
                break;
            case 6: // Auto-décrément (-R)
                if (autoModify) {
                    incrementAmount = -1;
                    updateRegister = true;
                    baseAddress = (baseAddress + incrementAmount) & 0xFFFF;
                }
                offset = 0;
                break;
            case 7: // Auto-décrément de 2 (--R)
                if (autoModify) {
                    incrementAmount = -2;
                    updateRegister = true;
                    baseAddress = (baseAddress + incrementAmount) & 0xFFFF;
                }
                offset = 0;
                break;
        }
        
        // Calculer l'adresse effective
        int effectiveAddress = (baseAddress + offset) & 0xFFFF;
        
        // Mettre à jour le registre si nécessaire (après calcul de l'adresse)
        if (updateRegister && incrementAmount > 0) {
            // Auto-incrément: mettre à jour après utilisation
            switch (register) {
                case 0: regX = (regX + incrementAmount) & 0xFFFF; break;
                case 1: regY = (regY + incrementAmount) & 0xFFFF; break;
                case 2: regU = (regU + incrementAmount) & 0xFFFF; break;
                case 3: regS = (regS + incrementAmount) & 0xFFFF; break;
            }
        }
        
        // Adressage indirect: lire l'adresse depuis la mémoire
        if (indirect) {
            effectiveAddress = memory.readWord(effectiveAddress);
        }
        
        return effectiveAddress;
    }

    // ==================== LOAD INSTRUCTIONS ====================
    // Note: Mode IMMEDIAT ne touche PAS au registre DP
    // La valeur est directement dans l'instruction (après l'opcode)

    private void opLDAimmediate() {
        // Mode IMMEDIAT: valeur directement dans l'instruction (#imm)
        // Ne nécessite PAS le registre DP
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

    // ==================== STORE INSTRUCTIONS ====================
    // Note: Mode DIRECT PAGE utilise le registre DP pour former l'adresse
    // Adresse effective = (DP << 8) | offset_8bits
    // Cela permet d'adresser 256 bytes dans la "direct page" (page mémoire pointée
    // par DP)

    private void opSTAdirect() {
        // Mode DIRECT PAGE: adresse = (DP << 8) | offset
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        memory.writeByte(addr, (byte) regA);
        updateNZFlags(regA);
        clearVFlag();
    }

    private void opSTBdirect() {
        // Mode DIRECT PAGE: adresse = (DP << 8) | offset
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        memory.writeByte(addr, (byte) regB);
        updateNZFlags(regB);
        clearVFlag();
    }

    private void opSTXdirect() {
        // Mode DIRECT PAGE: adresse = (DP << 8) | offset
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        memory.writeWord(addr, regX);
        updateNZFlags16(regX);
        clearVFlag();
    }

    private void opSTYdirect() {
        // Mode DIRECT PAGE: adresse = (DP << 8) | offset
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        memory.writeWord(addr, regY);
        updateNZFlags16(regY);
        clearVFlag();
    }

    private void opSTUdirect() {
        // Mode DIRECT PAGE: adresse = (DP << 8) | offset
        int offset = fetchByte() & 0xFF;
        int addr = (regDP << 8) | offset;
        memory.writeWord(addr, regU);
        updateNZFlags16(regU);
        clearVFlag();
    }

    // ==================== LOAD DIRECT PAGE ====================

    private void opLDAdirect() {
        // Mode DIRECT PAGE: adresse = (DP << 8) | offset
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

    // ==================== ARITHMETIC DIRECT PAGE ====================

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

    // ==================== LOGICAL DIRECT PAGE ====================

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

    // ==================== LOAD EXTENDED ====================

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


    // ==================== STORE EXTENDED ====================

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


    // ==================== ARITHMETIC EXTENDED ====================

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

    // ==================== LOGICAL EXTENDED ====================

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

    // ==================== LOAD INDEXED ====================

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

    // ==================== STORE INDEXED ====================

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

    // ==================== ARITHMETIC INDEXED ====================

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

    // ==================== LOGICAL INDEXED ====================

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

    // ==================== ARITHMETIC INSTRUCTIONS ====================

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

    // ==================== LOGICAL INSTRUCTIONS ====================

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

    // ==================== NEGATE INSTRUCTIONS (INHERENT) ====================

    private void opNEGA() {
        regA = (-regA) & 0xFF;
        updateNZVCFlags(regA, regA, 0, regA);
    }

    private void opNEGB() {
        regB = (-regB) & 0xFF;
        updateNZVCFlags(regB, regB, 0, regB);
    }

    // ==================== TEST INSTRUCTIONS (INHERENT) ====================

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

    // ==================== ROTATE INSTRUCTIONS (INHERENT) ====================

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

    // ==================== SHIFT INSTRUCTIONS (INHERENT) ====================

    private void opASLA() {
        // ASL/LSL: shift left, C = bit 7, V = N XOR C
        int newC = (regA & 0x80) != 0 ? 1 : 0;
        regA = (regA << 1) & 0xFF;
        updateNZFlags(regA);
        if (newC != 0) {
            setCFlag();
        } else {
            clearCFlag();
        }
        // V = N XOR C
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
        // ASR: arithmetic shift right, preserve sign bit
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
        // LSR: logical shift right, shift in 0
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

    // ==================== 16-BIT REGISTER INSTRUCTIONS (INHERENT)
    // ====================

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
        // ABX: Add B to X (unsigned, no flags affected)
        regX = (regX + (regB & 0xFF)) & 0xFFFF;
    }

    // ==================== CONTROL INSTRUCTIONS (INHERENT) ====================

    private void opNOP() {
        // No operation - do nothing
    }

    private void opSYNC() {
        // Synchronize - wait for interrupt
        // In simulator, this is a no-op
    }

    // ==================== BRANCH INSTRUCTIONS ====================

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
        if (!z && (n == v)) { // Z=0 and N=V
            regPC = (regPC + offset) & 0xFFFF;
        }
    }

    private void opBLE() {
        int offset = fetchSignedByte();
        boolean n = conditionCodes.contains(Flag.N);
        boolean v = conditionCodes.contains(Flag.V);
        boolean z = conditionCodes.contains(Flag.Z);
        if (z || (n != v)) { // Z=1 or N!=V
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

    // ==================== LONG BRANCH INSTRUCTIONS ====================

    private void opLBRA() {
        int offset = fetchSignedWord();
        regPC = (regPC + offset) & 0xFFFF;
    }

    private void opLBSR() {
        int offset = fetchSignedWord();
        // Push PC (adresse de retour) sur la pile
        pushWord(regPC);
        regPC = (regPC + offset) & 0xFFFF;
    }

    private void opBSR() {
        int offset = fetchSignedByte();
        // Push PC (adresse de retour) sur la pile
        pushWord(regPC);
        regPC = (regPC + offset) & 0xFFFF;
    }

    // ==================== COMPARE INSTRUCTIONS ====================

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
        // Pour CMPX, C flag = borrow (si result < 0)
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

    // ==================== JUMP INSTRUCTIONS ====================

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
        // Push PC (adresse de retour) sur la pile
        pushWord(regPC);
        regPC = addr;
    }

    private void opJSRindexed() {
        int postByte = fetchByte();
        int addr = calculateIndexedAddress(postByte);
        // Push PC (adresse de retour) sur la pile
        pushWord(regPC);
        regPC = addr;
    }

    // ==================== TRANSFER AND EXCHANGE ====================

    private void opTFR() {
        int postByte = fetchByte();
        int srcReg = (postByte >> 4) & 0x0F;
        int dstReg = postByte & 0x0F;

        int value = getRegisterValue(srcReg);
        setRegisterValue(dstReg, value);

        if (dstReg <= 7) { // 8-bit register
            updateNZFlags(value & 0xFF);
        } else { // 16-bit register
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

    // ==================== STACK INSTRUCTIONS ====================

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
        // Push registers in order: PC, U, Y, X, DP, B, A, CC
        if ((postByte & 0x80) != 0)
            pushWord(regPC); // PC
        if ((postByte & 0x40) != 0)
            pushWord(regU); // U
        if ((postByte & 0x20) != 0)
            pushWord(regY); // Y
        if ((postByte & 0x10) != 0)
            pushWord(regX); // X
        if ((postByte & 0x08) != 0)
            pushByte(regDP); // DP
        if ((postByte & 0x04) != 0)
            pushByte(regB); // B
        if ((postByte & 0x02) != 0)
            pushByte(regA); // A
        if ((postByte & 0x01) != 0)
            pushByte(getCC()); // CC
    }

    private void opPULS() {
        int postByte = fetchByte();
        // Pull registers in reverse order: CC, A, B, DP, X, Y, U, PC
        if ((postByte & 0x01) != 0)
            setCC(pullByte()); // CC
        if ((postByte & 0x02) != 0)
            regA = pullByte() & 0xFF; // A
        if ((postByte & 0x04) != 0)
            regB = pullByte() & 0xFF; // B
        if ((postByte & 0x08) != 0)
            regDP = pullByte() & 0xFF; // DP
        if ((postByte & 0x10) != 0)
            regX = pullWord(); // X
        if ((postByte & 0x20) != 0)
            regY = pullWord(); // Y
        if ((postByte & 0x40) != 0)
            regU = pullWord(); // U
        if ((postByte & 0x80) != 0)
            regPC = pullWord(); // PC
    }

    // ==================== OTHER INSTRUCTIONS ====================

    private void opMUL() {
        // Multiply A * B, result in D (A:B)
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
        // Sign extend B to D (A:B)
        regA = (regB & 0x80) != 0 ? 0xFF : 0x00;
        updateNZFlags(regA);
        clearVFlag();
    }

    private void opSWI() {
        // Software interrupt - push registers and jump to interrupt vector
        pushWord(regPC);
        pushWord(regU);
        pushWord(regY);
        pushWord(regX);
        pushByte(regDP);
        pushByte(regB);
        pushByte(regA);
        pushByte(getCC());
        regPC = memory.readWord(0xFFFA); // SWI vector
        setIFlag();
        setFFlag();
    }

    private void opRTI() {
        // Return from interrupt - restore registers
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
        // Return from subroutine
        regPC = pullWord();
    }

    private void opRESET() {
        // Reset external devices - in simulator, do nothing
        // In real hardware, this would reset external devices
    }

    private void opCWAI() {
        // Clear and wait for interrupt
        int mask = fetchByte();
        setCC(getCC() & mask);
        // Wait for interrupt - in simulator, just set flags
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

    // ==================== HELPER METHODS ====================

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
                return regD() & 0xFFFF; // D (A:B)
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
                break; // D (A:B)
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

    // ==================== FLAG UPDATE METHODS ====================

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

        // V flag: signed overflow
        boolean overflow = ((a ^ b) & 0x80) == 0 && ((a ^ result) & 0x80) != 0;
        if (overflow) {
            setVFlag();
        } else {
            clearVFlag();
        }

        // C flag: carry
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

    // ==================== PUBLIC ACCESSORS ====================

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
