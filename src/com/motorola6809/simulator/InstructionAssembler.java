package com.motorola6809.simulator;

<<<<<<< HEAD
import java.util.HashMap;
import java.util.Map;
=======
>>>>>>> 0000038d6c42683524e2f6bb090959cdcc9a6a5c
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
<<<<<<< HEAD
=======
import java.util.Map;
>>>>>>> 0000038d6c42683524e2f6bb090959cdcc9a6a5c

public class InstructionAssembler {
    private static final Map<String, Integer> INSTRUCTION_MAP = new HashMap<>();

    static {
        // Representative opcode map (extend as needed)
        INSTRUCTION_MAP.put("LDA#", 0x86);
        INSTRUCTION_MAP.put("LDB#", 0xC6);
        INSTRUCTION_MAP.put("LDX#", 0x8E);
        INSTRUCTION_MAP.put("LDD$", 0xFC);
        INSTRUCTION_MAP.put("JMP$", 0x7E);
        INSTRUCTION_MAP.put("JSR$", 0xBD);
        INSTRUCTION_MAP.put("INCA", 0x4C);
        INSTRUCTION_MAP.put("INCB", 0x5C);
        INSTRUCTION_MAP.put("DECA", 0x4A);
        INSTRUCTION_MAP.put("DECB", 0x5A);
        INSTRUCTION_MAP.put("INX", 0x08);
        INSTRUCTION_MAP.put("DEX", 0x09);
<<<<<<< HEAD
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
        INSTRUCTION_MAP.put("PSHS", 0x34);
        INSTRUCTION_MAP.put("PULS", 0x35);
=======
>>>>>>> 0000038d6c42683524e2f6bb090959cdcc9a6a5c
        INSTRUCTION_MAP.put("NOP", 0x12);
        INSTRUCTION_MAP.put("RTS", 0x39);
        INSTRUCTION_MAP.put("RESET", 0x3E);
        INSTRUCTION_MAP.put("BRA", 0x20);
        INSTRUCTION_MAP.put("BSR", 0x8D);
        INSTRUCTION_MAP.put("LBRA", 0x16);
        INSTRUCTION_MAP.put("LBSR", 0x17);
<<<<<<< HEAD
        INSTRUCTION_MAP.put("JMP", 0x7E);
        INSTRUCTION_MAP.put("JSR", 0xBD);
        INSTRUCTION_MAP.put("TFR", 0x1F);
        INSTRUCTION_MAP.put("EXG", 0x1E);
        INSTRUCTION_MAP.put("DB", -1);
=======
        INSTRUCTION_MAP.put("TFR", 0x1F);
        INSTRUCTION_MAP.put("EXG", 0x1E);
>>>>>>> 0000038d6c42683524e2f6bb090959cdcc9a6a5c
    }

    public static byte[] assemble(String[] lines) throws IllegalArgumentException {
        List<Byte> bytecode = new ArrayList<>();
<<<<<<< HEAD
        for (String originalLine : lines) {
            String line = originalLine.trim();
            if (line.isEmpty()) {
                continue;
            }

            String instructionPart = "";
            String commentPart = "";

            if (line.contains(";")) {
                int semicolonIndex = line.indexOf(";");
                commentPart = line.substring(semicolonIndex + 1).trim();
                instructionPart = line.substring(0, semicolonIndex).trim().toUpperCase();
            } else {
                instructionPart = line.toUpperCase();
            }

            if (instructionPart.isEmpty() && !commentPart.isEmpty()) {
                continue;
            }

            if (instructionPart.isEmpty()) {
                continue;
            }

            if (instructionPart.equals("END")) {
=======

        for (String originalLine : lines) {
            String line = originalLine == null ? "" : originalLine.trim();
            if (line.isEmpty())
                continue;

            String instructionPart = line;
            int sem = line.indexOf(';');
            if (sem >= 0) {
                instructionPart = line.substring(0, sem).trim();
            }

            if (instructionPart.isEmpty()) {
                continue;
            }

            String upInst = instructionPart.toUpperCase();
            if (upInst.equals("END")) {
>>>>>>> 0000038d6c42683524e2f6bb090959cdcc9a6a5c
                bytecode.add((byte) 0x00);
                break;
            }

<<<<<<< HEAD
            if (instructionPart.equals("DB")) {
                int address = -1;
                int value = 0;

                if (commentPart.matches("^[0-9A-F]{4}.*")) {
                    String addrStr = commentPart.substring(0, 4);
                    String valuePart = commentPart.substring(4).trim();
                    try {
                        address = Integer.parseInt(addrStr, 16);
                        if (valuePart.startsWith("#$")) {
                            value = Integer.parseInt(valuePart.substring(2).trim(), 16);
                        } else if (valuePart.startsWith("#")) {
                            value = Integer.parseInt(valuePart.substring(1).trim(), 16);
                        } else if (valuePart.startsWith("$")) {
                            value = Integer.parseInt(valuePart.substring(1).trim(), 16);
                        } else {
                            throw new IllegalArgumentException("DB nécessite une valeur (ex: ;0002 DB #$10)");
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(
                                "Format d'adresse ou valeur invalide pour DB: " + originalLine);
                    }
                } else {
                    String[] parts = instructionPart.split("\\s+");
                    if (parts.length >= 2) {
                        try {
                            String addrStr = parts[1].replace("$", "").trim();
                            address = Integer.parseInt(addrStr, 16);
                            if (parts.length >= 3) {
                                String valueStr = parts[2].replace("#$", "").replace("#", "").replace("$", "").trim();
                                value = Integer.parseInt(valueStr, 16);
                            } else if (!commentPart.isEmpty()) {
                                String valuePart = commentPart.trim();
                                if (valuePart.startsWith("#$")) {
                                    value = Integer.parseInt(valuePart.substring(2).trim(), 16);
                                } else if (valuePart.startsWith("#")) {
                                    value = Integer.parseInt(valuePart.substring(1).trim(), 16);
                                } else if (valuePart.startsWith("$")) {
                                    value = Integer.parseInt(valuePart.substring(1).trim(), 16);
                                } else {
                                    value = Integer.parseInt(valuePart, 16);
                                }
                            } else {
                                throw new IllegalArgumentException("DB nécessite une valeur (ex: DB $0002 #$10)");
                            }
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Format invalide pour DB: " + originalLine);
                        }
                    } else {
                        throw new IllegalArgumentException(
                                "DB nécessite une adresse et une valeur (ex: ;0002 DB #$10)");
                    }
                }

                if (address == -1) {
                    throw new IllegalArgumentException("DB nécessite une adresse (ex: ;0002 DB #$10)");
                }

                bytecode.add((byte) 0xFF);
                bytecode.add((byte) 0xFF);
                bytecode.add((byte) ((address >> 8) & 0xFF));
                bytecode.add((byte) (address & 0xFF));
                bytecode.add((byte) (value & 0xFF));
                continue;
            }

            AssemblyLine instruction = parseLine(instructionPart);
            if (instruction != null) {
                for (byte b : instruction.getBytes()) {
=======
            AssemblyLine asm = parseLine(upInst);
            if (asm != null) {
                for (byte b : asm.getBytes())
>>>>>>> 0000038d6c42683524e2f6bb090959cdcc9a6a5c
                    bytecode.add(b);
            }
        }

        byte[] result = new byte[bytecode.size()];
        for (int i = 0; i < bytecode.size(); i++)
            result[i] = bytecode.get(i);
        return result;
    }

<<<<<<< HEAD
    public static class DBInfo {
        public int address;
        public byte value;

        public DBInfo(int address, byte value) {
            this.address = address;
            this.value = value;
        }
    }

    public static List<DBInfo> extractDBDirectives(byte[] bytecode) {
        List<DBInfo> dbList = new ArrayList<>();
        for (int i = 0; i < bytecode.length - 4; i++) {
            if ((bytecode[i] & 0xFF) == 0xFF && (bytecode[i + 1] & 0xFF) == 0xFF) {
                if (i + 4 < bytecode.length) {
                    int address = ((bytecode[i + 2] & 0xFF) << 8) | (bytecode[i + 3] & 0xFF);
                    byte value = bytecode[i + 4];
                    dbList.add(new DBInfo(address, value));
                    i += 4;
                }
            }
        }
        return dbList;
    }

    private static AssemblyLine parseLine(String line) throws IllegalArgumentException {
        line = line.toUpperCase().trim();
        if (line.isEmpty()) {
=======
    private static AssemblyLine parseLine(String line) {
        if (line == null || line.isEmpty())
>>>>>>> 0000038d6c42683524e2f6bb090959cdcc9a6a5c
            return null;
        String[] parts = line.split("\\s+", 2);
        String mnemonic = parts[0];
        String operand = parts.length > 1 ? parts[1] : "";

        if (operand.startsWith("#")) {
            String imm = operand.substring(1).replace("$", "").trim();
            Integer opcode = INSTRUCTION_MAP.get(mnemonic + "#");
            if (opcode != null)
                return new AssemblyLine(opcode, imm);
        }

        if (operand.startsWith("<")) {
            String addr = operand.substring(1).replace("$", "").trim();
            Integer opcode = INSTRUCTION_MAP.get(mnemonic + "<");
            if (opcode != null)
                return new AssemblyLine(opcode, addr);
        }

        if (operand.startsWith("$")) {
            String addr = operand.substring(1).replace("$", "").trim();
            Integer opcode = INSTRUCTION_MAP.get(mnemonic + "$");
            if (opcode != null)
                return new AssemblyLine(opcode, addr);
        }
<<<<<<< HEAD
        if (mnemonic.equals("TFR") || mnemonic.equals("EXG")) {
            Integer trOpcode = INSTRUCTION_MAP.get(mnemonic);
            if (trOpcode != null) {
                if (operand.isEmpty()) {
                    throw new IllegalArgumentException(mnemonic + " nécessite deux registres (ex: TFR A,B)");
                }
                String[] regs = operand.split(",");
                if (regs.length != 2) {
                    throw new IllegalArgumentException(
                            mnemonic + " nécessite deux registres séparés par une virgule (ex: TFR A,B)");
                }
                int srcReg = getRegisterCode(regs[0].trim().toUpperCase());
                int dstReg = getRegisterCode(regs[1].trim().toUpperCase());
                int postByte = (srcReg << 4) | dstReg;
                return new AssemblyLine(trOpcode, String.format("%02X", postByte & 0xFF));
            }
        }
        Integer opcode = INSTRUCTION_MAP.get(mnemonic);
        if (opcode != null) {
            if (mnemonic.equals("PSHS") || mnemonic.equals("PULS")) {
                if (operand.isEmpty()) {
                    throw new IllegalArgumentException(mnemonic + " nécessite un opérande (masque de registres)");
                }
                String mask = operand.replace("$", "").trim();
                int maskValue;
                if (mask.length() == 1) {
                    maskValue = Integer.parseInt(mask, 16);
                } else if (mask.length() == 2) {
                    maskValue = Integer.parseInt(mask, 16);
                } else {
                    maskValue = Integer.parseInt(mask, 16);
                }
                return new AssemblyLine(opcode, String.format("%02X", maskValue & 0xFF));
            }
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
=======

        Integer opcode = INSTRUCTION_MAP.get(mnemonic);
        if (opcode != null)
            return new AssemblyLine(opcode, operand.replace("$", "").trim());

        return null;
    }

    private static class AssemblyLine {
        private final byte[] bytes;
>>>>>>> 0000038d6c42683524e2f6bb090959cdcc9a6a5c

        AssemblyLine(int opcode, String operand) {
            List<Byte> list = new ArrayList<>();
            if (opcode > 0xFF) {
                list.add((byte) ((opcode >> 8) & 0xFF));
                list.add((byte) (opcode & 0xFF));
            } else {
                list.add((byte) (opcode & 0xFF));
            }
            if (operand != null && !operand.isEmpty()) {
                String op = operand.replace("$", "").trim();
                try {
                    if (op.length() == 1 || op.length() == 2) {
                        list.add((byte) Integer.parseInt(op, 16));
                    } else if (op.length() == 4) {
                        list.add((byte) Integer.parseInt(op.substring(0, 2), 16));
                        list.add((byte) Integer.parseInt(op.substring(2, 4), 16));
                    } else {
                        int v = Integer.parseInt(op);
                        if (v < 256)
                            list.add((byte) v);
                        else {
                            list.add((byte) ((v >> 8) & 0xFF));
                            list.add((byte) (v & 0xFF));
                        }
                    }
                } catch (NumberFormatException ex) {
                    // ignore malformed operand here
                }
            }
            bytes = new byte[list.size()];
            for (int i = 0; i < list.size(); i++)
                bytes[i] = list.get(i);
        }

        byte[] getBytes() {
            return bytes;
        }
    }
<<<<<<< HEAD

    public static String getInstructionList() {
        StringBuilder sb = new StringBuilder();
        sb.append("Instructions supportées:\n");
        sb.append(" Mode Immédiat (#)  \n");
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

    private static int getRegisterCode(String reg) {
        switch (reg) {
            case "D":
                return 0;
            case "X":
                return 1;
            case "Y":
                return 2;
            case "U":
                return 3;
            case "S":
                return 4;
            case "PC":
                return 5;
            case "A":
                return 8;
            case "B":
                return 9;
            case "CC":
                return 10;
            case "DP":
                return 11;
            default:
                throw new IllegalArgumentException(
                        "Registre inconnu: " + reg + ". Registres valides: D, X, Y, U, S, PC, A, B, CC, DP");
        }
    }
=======
>>>>>>> 0000038d6c42683524e2f6bb090959cdcc9a6a5c
}
