package com.motorola6809.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        INSTRUCTION_MAP.put("NOP", 0x12);
        INSTRUCTION_MAP.put("RTS", 0x39);
        INSTRUCTION_MAP.put("RESET", 0x3E);
        INSTRUCTION_MAP.put("BRA", 0x20);
        INSTRUCTION_MAP.put("BSR", 0x8D);
        INSTRUCTION_MAP.put("LBRA", 0x16);
        INSTRUCTION_MAP.put("LBSR", 0x17);
        INSTRUCTION_MAP.put("TFR", 0x1F);
        INSTRUCTION_MAP.put("EXG", 0x1E);
    }

    public static byte[] assemble(String[] lines) throws IllegalArgumentException {
        List<Byte> bytecode = new ArrayList<>();

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
                bytecode.add((byte) 0x00);
                break;
            }

            AssemblyLine asm = parseLine(upInst);
            if (asm != null) {
                for (byte b : asm.getBytes())
                    bytecode.add(b);
            }
        }

        byte[] result = new byte[bytecode.size()];
        for (int i = 0; i < bytecode.size(); i++)
            result[i] = bytecode.get(i);
        return result;
    }

    private static AssemblyLine parseLine(String line) {
        if (line == null || line.isEmpty())
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

        Integer opcode = INSTRUCTION_MAP.get(mnemonic);
        if (opcode != null)
            return new AssemblyLine(opcode, operand.replace("$", "").trim());

        return null;
    }

    private static class AssemblyLine {
        private final byte[] bytes;

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
}
