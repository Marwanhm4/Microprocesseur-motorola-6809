# Résumé des Modes d'Adressage Implémentés

## 📊 Nombre de Modes d'Adressage : **4 modes principaux**

1. ✅ **Mode IMMÉDIAT** (#imm)
2. ✅ **Mode DIRECT PAGE** (<addr)  
3. ✅ **Mode INHÉRENT** (pas d'opérande)
4. ✅ **Mode RELATIF** (branches)

---

## 📋 Liste Détaillée par Mode

### 1. MODE IMMÉDIAT (#imm) - 11 instructions

**LOAD:**
- `0x86` - LDA #imm
- `0xC6` - LDB #imm
- `0x8E` - LDX #imm16
- `0xCE` - LDU #imm16
- `0x10BE` - LDY #imm16 (extended)

**ARITHMETIC:**
- `0x8B` - ADDA #imm
- `0xCB` - ADDB #imm
- `0x80` - SUBA #imm
- `0xC0` - SUBB #imm

**LOGICAL:**
- `0x84` - ANDA #imm
- `0xC4` - ANDB #imm
- `0x8A` - ORA #imm
- `0xCA` - ORB #imm
- `0x88` - EORA #imm
- `0xC8` - EORB #imm

**TOTAL: 11 instructions**

---

### 2. MODE DIRECT PAGE (<addr) - 4 instructions

**STORE:**
- `0x97` - STA <addr
- `0xD7` - STB <addr
- `0x9F` - STX <addr
- `0xDF` - STY <addr

**TOTAL: 4 instructions**

---

### 3. MODE INHÉRENT - 42 instructions

**INC/DEC (8-bit):**
- `0x4C` - INCA
- `0x5C` - INCB
- `0x4A` - DECA
- `0x5A` - DECB

**INC/DEC (16-bit):**
- `0x08` - INX
- `0x09` - DEX
- `0x103C` - INY (extended)
- `0x103D` - DEY (extended)

**LOGICAL:**
- `0x43` - COMA
- `0x53` - COMB
- `0x4F` - CLRA
- `0x5F` - CLRB

**NEGATE:**
- `0x40` - NEGA
- `0x50` - NEGB

**TEST:**
- `0x4D` - TSTA
- `0x5D` - TSTB

**ROTATE:**
- `0x49` - ROLA
- `0x59` - ROLB
- `0x46` - RORA
- `0x56` - RORB

**SHIFT:**
- `0x48` - ASLA/LSLA
- `0x58` - ASLB/LSLB
- `0x47` - ASRA
- `0x57` - ASRB
- `0x44` - LSRA
- `0x54` - LSRB

**STACK:**
- `0x36` - PSHA
- `0x37` - PSHB
- `0x32` - PULA
- `0x33` - PULB
- `0x34` - PSHS (avec post-byte)
- `0x35` - PULS (avec post-byte)

**ARITHMETIC:**
- `0x3D` - MUL
- `0x1D` - SEX
- `0x3A` - ABX

**CONTROL:**
- `0x12` - NOP
- `0x13` - SYNC
- `0x3F` - SWI
- `0x3B` - RTI
- `0x39` - RTS
- `0x3E` - RESET
- `0x3C` - CWAI (avec post-byte)

**TRANSFER:**
- `0x1F` - TFR (avec post-byte)
- `0x1E` - EXG (avec post-byte)

**CONDITION CODES:**
- `0x1A` - ORCC (avec immédiat)
- `0x1C` - ANDCC (avec immédiat)

**TOTAL: 42 instructions**

---

### 4. MODE RELATIF - 11 instructions

**BRANCHES:**
- `0x20` - BRA (branch always)
- `0x26` - BNE (branch if not equal)
- `0x27` - BEQ (branch if equal)
- `0x24` - BCC (branch if carry clear)
- `0x25` - BCS (branch if carry set)
- `0x2A` - BPL (branch if plus)
- `0x2B` - BMI (branch if minus)
- `0x2E` - BGT (branch if greater than)
- `0x2F` - BLE (branch if less or equal)
- `0x22` - BHI (branch if higher)
- `0x23` - BLS (branch if lower or same)

**TOTAL: 11 instructions**

---

## 📊 TOTAL GÉNÉRAL

**Total d'instructions implémentées : 68 instructions**

- Mode Immédiat : 11 instructions
- Mode Direct Page : 4 instructions
- Mode Inhérent : 42 instructions
- Mode Relatif : 11 instructions

---

## ❌ Modes d'Adressage NON Implémentés

1. **Mode INDEXÉ** - Le plus important à implémenter
2. **Mode ÉTENDU** - Adresse 16 bits complète
3. **Mode RELATIF LONG** - Branches avec offset 16 bits
4. **Mode INDIRECT** - Adressage indirect

