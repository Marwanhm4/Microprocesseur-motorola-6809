package com.motorola6809.simulator;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
public class InstructionAssembler {
    private static final Map<String, Integer> INSTRUCTION_MAP = new HashMap<>();
    static {
        INSTRUCTION_MAP.put("LDA#", 0x86);
        INSTRUCTION_MAP.put("LDB#", 0xC6);
        INSTRUCTION_MAP.put("LDX#", 0x8E);
        INSTRUCTION_MAP.put("LDU#", 0xCE);
        INSTRUCTION_MAP.put("LDS#", 0x10CE);
        INSTRUCTION_MAP.put("LDY#", 0x108E);
        INSTRUCTION_MAP.put("ADDA#", 0x8B);
        INSTRUCTION_MAP.put("ADDB#", 0xCB);
        INSTRUCTION_MAP.put("ADDX#", 0x8F);
        INSTRUCTION_MAP.put("SUBA#", 0x80);
        INSTRUCTION_MAP.put("SUBB#", 0xC0);
        INSTRUCTION_MAP.put("ANDA#", 0x84);
        INSTRUCTION_MAP.put("ANDB#", 0xC4);
        INSTRUCTION_MAP.put("ORA#", 0x8A);
        INSTRUCTION_MAP.put("ORB#", 0xCA);
        INSTRUCTION_MAP.put("EORA#", 0x88);
        INSTRUCTION_MAP.put("EORB#", 0xC8);
        INSTRUCTION_MAP.put("CMPA#", 0x81);
        INSTRUCTION_MAP.put("CMPB#", 0xC1);
        INSTRUCTION_MAP.put("CMPX#", 0x8C);
        INSTRUCTION_MAP.put("LDA<", 0x96);
        INSTRUCTION_MAP.put("LDB<", 0xD6);
        INSTRUCTION_MAP.put("LDX<", 0x9E);
        INSTRUCTION_MAP.put("LDY<", 0xDE);
        INSTRUCTION_MAP.put("LDU<", 0x9C);
        INSTRUCTION_MAP.put("LDS<", 0x9D);
        INSTRUCTION_MAP.put("STA<", 0x97);
        INSTRUCTION_MAP.put("STB<", 0xD7);
        INSTRUCTION_MAP.put("STX<", 0x9F);
        INSTRUCTION_MAP.put("STY<", 0xDF);
        INSTRUCTION_MAP.put("STU<", 0xDD);
        INSTRUCTION_MAP.put("ADDA<", 0x9B);
        INSTRUCTION_MAP.put("ADDB<", 0xDB);
        INSTRUCTION_MAP.put("SUBA<", 0x90);
        INSTRUCTION_MAP.put("SUBB<", 0xD0);
        INSTRUCTION_MAP.put("ANDA<", 0x94);
        INSTRUCTION_MAP.put("ANDB<", 0xD4);
        INSTRUCTION_MAP.put("ORA<", 0x9A);
        INSTRUCTION_MAP.put("ORB<", 0xDA);
        INSTRUCTION_MAP.put("EORA<", 0x98);
        INSTRUCTION_MAP.put("EORB<", 0xD8);
        INSTRUCTION_MAP.put("CMPA<", 0x91);
        INSTRUCTION_MAP.put("CMPB<", 0xD1);
        INSTRUCTION_MAP.put("LDA$", 0xB6);
        INSTRUCTION_MAP.put("LDB$", 0xF6);
        INSTRUCTION_MAP.put("LDX$", 0xBE);
        INSTRUCTION_MAP.put("LDY$", 0xFE);
        INSTRUCTION_MAP.put("STA$", 0xB7);
        INSTRUCTION_MAP.put("STB$", 0xF7);
        INSTRUCTION_MAP.put("STX$", 0xBF);
        INSTRUCTION_MAP.put("STY$", 0xFF);
        INSTRUCTION_MAP.put("ADDA$", 0xBB);
        INSTRUCTION_MAP.put("ADDB$", 0xFB);
        INSTRUCTION_MAP.put("SUBA$", 0xB0);
        INSTRUCTION_MAP.put("SUBB$", 0xF0);
        INSTRUCTION_MAP.put("ANDA$", 0xB4);
        INSTRUCTION_MAP.put("ANDB$", 0xF4);
        INSTRUCTION_MAP.put("ORA$", 0xBA);
        INSTRUCTION_MAP.put("ORB$", 0xFA);
        INSTRUCTION_MAP.put("EORA$", 0xB8);
        INSTRUCTION_MAP.put("EORB$", 0xF8);
        INSTRUCTION_MAP.put("CMPA$", 0xB1);
        INSTRUCTION_MAP.put("CMPB$", 0xF1);
        INSTRUCTION_MAP.put("JMP$", 0x7E);
        INSTRUCTION_MAP.put("JSR$", 0xBD);
        INSTRUCTION_MAP.put("INCA", 0x4C);
        INSTRUCTION_MAP.put("INCB", 0x5C);
        INSTRUCTION_MAP.put("DECA", 0x4A);
        INSTRUCTION_MAP.put("DECB", 0x5A);
        INSTRUCTION_MAP.put("INX", 0x08);
        INSTRUCTION_MAP.put("DEX", 0x09);
        INSTRUCTION_MAP.put("INY", 0x103C);
        INSTRUCTION_MAP.put("DEY", 0x103D);
        INSTRUCTION_MAP.put("COMA", 0x43);
        INSTRUCTION_MAP.put("COMB", 0x53);
        INSTRUCTION_MAP.put("CLRA", 0x4F);
        INSTRUCTION_MAP.put("CLRB", 0x5F);
        INSTRUCTION_MAP.put("NEGA", 0x40);
        INSTRUCTION_MAP.put("NEGB", 0x50);
        INSTRUCTION_MAP.put("TSTA", 0x4D);
        INSTRUCTION_MAP.put("TSTB", 0x5D);
        INSTRUCTION_MAP.put("ROLA", 0x49);
        INSTRUCTION_MAP.put("ROLB", 0x59);
        INSTRUCTION_MAP.put("RORA", 0x46);
        INSTRUCTION_MAP.put("RORB", 0x56);
        INSTRUCTION_MAP.put("ASLA", 0x48);
        INSTRUCTION_MAP.put("ASLB", 0x58);
        INSTRUCTION_MAP.put("ASRA", 0x47);
        INSTRUCTION_MAP.put("ASRB", 0x57);
        INSTRUCTION_MAP.put("LSRA", 0x44);
        INSTRUCTION_MAP.put("LSRB", 0x54);
        INSTRUCTION_MAP.put("MUL", 0x3D);
        INSTRUCTION_MAP.put("SEX", 0x1D);
        INSTRUCTION_MAP.put("ABX", 0x3A);
        INSTRUCTION_MAP.put("PSHA", 0x36);
        INSTRUCTION_MAP.put("PSHB", 0x37);
        INSTRUCTION_MAP.put("PULA", 0x32);
        INSTRUCTION_MAP.put("PULB", 0x33);
        INSTRUCTION_MAP.put("NOP", 0x12);
        INSTRUCTION_MAP.put("SYNC", 0x13);
        INSTRUCTION_MAP.put("SWI", 0x3F);
        INSTRUCTION_MAP.put("RTI", 0x3B);
        INSTRUCTION_MAP.put("RTS", 0x39);
        INSTRUCTION_MAP.put("RESET", 0x3E);
        INSTRUCTION_MAP.put("BRA", 0x20);
        INSTRUCTION_MAP.put("BRN", 0x21);
        INSTRUCTION_MAP.put("BHI", 0x22);
        INSTRUCTION_MAP.put("BLS", 0x23);
        INSTRUCTION_MAP.put("BCC", 0x24);
        INSTRUCTION_MAP.put("BCS", 0x25);
        INSTRUCTION_MAP.put("BNE", 0x26);
        INSTRUCTION_MAP.put("BEQ", 0x27);
        INSTRUCTION_MAP.put("BVC", 0x28);
        INSTRUCTION_MAP.put("BVS", 0x29);
        INSTRUCTION_MAP.put("BPL", 0x2A);
        INSTRUCTION_MAP.put("BMI", 0x2B);
        INSTRUCTION_MAP.put("BGE", 0x2C);
        INSTRUCTION_MAP.put("BLT", 0x2D);
        INSTRUCTION_MAP.put("BGT", 0x2E);
        INSTRUCTION_MAP.put("BLE", 0x2F);
        INSTRUCTION_MAP.put("BSR", 0x8D);
        INSTRUCTION_MAP.put("LBRA", 0x16);
        INSTRUCTION_MAP.put("LBSR", 0x17);
        INSTRUCTION_MAP.put("JMP", 0x7E);
        INSTRUCTION_MAP.put("JSR", 0xBD);
    }
    public static byte[] assemble(String[] lines) throws IllegalArgumentException {
        List<Byte> bytecode = new ArrayList<>();
        for (String line : lines) {
            line = line.trim().toUpperCase();
            if (line.isEmpty() || line.startsWith(";")) {
                continue;
            }
            if (line.contains(";")) {
                line = line.substring(0, line.indexOf(";")).trim();
            }
            if (line.equals("END")) {
                bytecode.add((byte)0x00);
                break;
            }
            AssemblyLine instruction = parseLine(line);
            if (instruction != null) {
                for (byte b : instruction.getBytes()) {
                    bytecode.add(b);
                }
            }
        }
        byte[] result = new byte[bytecode.size()];
        for (int i = 0; i < bytecode.size(); i++) {
            result[i] = bytecode.get(i);
        }
        return result;
    }
    private static AssemblyLine parseLine(String line) throws IllegalArgumentException {
        line = line.toUpperCase().trim();
        if (line.isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\s+", 2);
        String mnemonic = parts[0];
        String operand = parts.length > 1 ? parts[1] : "";
        if (operand.startsWith("#")) {
            String immediate = operand.substring(1).replace("$", "");
            Integer opcode = INSTRUCTION_MAP.get(mnemonic + "#");
            if (opcode != null) {
                return new AssemblyLine(opcode, immediate);
            }
        }
        if (operand.startsWith("<")) {
            String address = operand.substring(1).replace("$", "");
            Integer opcode = INSTRUCTION_MAP.get(mnemonic + "<");
            if (opcode != null) {
                return new AssemblyLine(opcode, address);
            }
        }
        if (operand.startsWith("$") && !operand.startsWith("#")) {
            String address = operand.substring(1).replace("$", "");
            Integer opcode = INSTRUCTION_MAP.get(mnemonic + "$");
            if (opcode != null) {
                return new AssemblyLine(opcode, address);
            }
        }
        Integer opcode = INSTRUCTION_MAP.get(mnemonic);
        if (opcode != null) {
            return new AssemblyLine(opcode, "");
        }
        if (mnemonic.startsWith("B") || mnemonic.startsWith("LB")) {
            Integer brOpcode = INSTRUCTION_MAP.get(mnemonic);
            if (brOpcode != null) {
                if (operand.equals("*")) {
                    if (mnemonic.startsWith("LB")) {
                        return new AssemblyLine(brOpcode, "FFFD");
                    } else {
                        return new AssemblyLine(brOpcode, "FE");
                    }
                } else if (!operand.isEmpty()) {
                    return new AssemblyLine(brOpcode, operand.replace("$", ""));
                } else {
                    return new AssemblyLine(brOpcode, "");
                }
            }
        }
        if (mnemonic.equals("JMP") || mnemonic.equals("JSR")) {
            Integer jmpOpcode = INSTRUCTION_MAP.get(mnemonic);
            if (jmpOpcode != null && !operand.isEmpty()) {
                String address = operand.replace("$", "");
                return new AssemblyLine(jmpOpcode, address);
            } else if (jmpOpcode != null) {
                throw new IllegalArgumentException(mnemonic + " nécessite une adresse");
            }
        }
        throw new IllegalArgumentException("Instruction inconnue: " + mnemonic + 
            ". Vérifiez la syntaxe et le mode d'adressage.");
    }
    private static class AssemblyLine {
        private byte[] bytes;
        AssemblyLine(int opcode, String operand) {
            List<Byte> list = new ArrayList<>();
            if (opcode > 0xFF) {
                list.add((byte) ((opcode >> 8) & 0xFF));
                list.add((byte) (opcode & 0xFF));
            } else {
                list.add((byte) (opcode & 0xFF));
            }
            if (!operand.isEmpty()) {
                operand = operand.replace("$", "").trim();
                if (operand.length() == 1) {
                    list.add((byte) Integer.parseInt(operand, 16));
                } else if (operand.length() == 2) {
                    list.add((byte) Integer.parseInt(operand, 16));
                } else if (operand.length() == 4) {
                    list.add((byte) Integer.parseInt(operand.substring(0, 2), 16));
                    list.add((byte) Integer.parseInt(operand.substring(2, 4), 16));
                } else {
                    int value = Integer.parseInt(operand);
                    if (value < 256) {
                        list.add((byte) value);
                    } else {
                        list.add((byte) ((value >> 8) & 0xFF));
                        list.add((byte) (value & 0xFF));
                    }
                }
            }
            bytes = new byte[list.size()];
            for (int i = 0; i < list.size(); i++) {
                bytes[i] = list.get(i);
            }
        }
        byte[] getBytes() {
            return bytes;
        }
    }
    public static String getInstructionList() {
        StringBuilder sb = new StringBuilder();
        sb.append("Instructions supportées:\n");
        sb.append("=== Mode Immédiat (#) ===\n");
        sb.append("LDA #$20     ; Charger A avec valeur immédiate\n");
        sb.append("LDB #$40     ; Charger B avec valeur immédiate\n");
        sb.append("ADDA #$10    ; Ajouter à A\n");
        sb.append("SUBA #$05    ; Soustraire de A\n");
        sb.append("\n=== Mode Direct (<) ===\n");
        sb.append("LDA <$10     ; Charger A depuis adresse $0010\n");
        sb.append("LDB <$20     ; Charger B depuis adresse $0020\n");
        sb.append("\n=== Sans Opérande ===\n");
        sb.append("INCA         ; Incrémenter A\n");
        sb.append("DECA         ; Décrémenter A\n");
        sb.append("CLRA         ; Effacer A\n");
        sb.append("NOP          ; Pas d'opération\n");
        sb.append("\n=== Branchements ===\n");
        sb.append("BRA *        ; Boucle infinie\n");
        sb.append("BRA $0100    ; Sauter à adresse\n");
        sb.append("BEQ $0100    ; Sauter si égal\n");
        sb.append("BNE $0100    ; Sauter si non égal\n");
        sb.append("\n=== Commentaires ===\n");
        sb.append("; Ceci est un commentaire\n");
        return sb.toString();
    }
}
