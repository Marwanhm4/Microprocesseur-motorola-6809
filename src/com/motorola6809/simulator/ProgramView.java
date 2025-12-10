package com.motorola6809.simulator;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vue du programme disassemblé
 */
public class ProgramView {
    private CPU cpu;
    private Memory memory;
    private VBox root;
    private VBox contentBox;
    private ScrollPane scrollPane;
    
    public ProgramView(CPU cpu, Memory memory) {
        this.cpu = cpu;
        this.memory = memory;
        createView();
    }
    
    private void createView() {
        root = new VBox(5);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #f8f8f8; -fx-background-radius: 8; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 8;");
        
        // Titre
        Label title = new Label("Programme");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        title.setTextFill(Color.web("#f57c00"));
        
        // En-tête
        HBox header = new HBox(10);
        Label addrHeader = new Label("Adresse");
        addrHeader.setFont(Font.font("Courier New", FontWeight.BOLD, 11));
        addrHeader.setTextFill(Color.web("#424242"));
        addrHeader.setMinWidth(80);
        
        Label codeHeader = new Label("Code");
        codeHeader.setFont(Font.font("Courier New", FontWeight.BOLD, 11));
        codeHeader.setTextFill(Color.web("#424242"));
        
        header.getChildren().addAll(addrHeader, codeHeader);
        header.setPadding(new Insets(5));
        header.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 4;");
        
        // Zone de contenu
        contentBox = new VBox(2);
        contentBox.setPadding(new Insets(5));
        
        scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e0e0e0;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        root.getChildren().addAll(title, header, scrollPane);
        
        update();
    }
    
    public void update() {
        contentBox.getChildren().clear();
        
        // Disassembler à partir de l'adresse de départ
        int startAddr = 0xFC00;
        int currentAddr = startAddr;
        int maxLines = 50;
        
        while (currentAddr < 0xFFFF && maxLines-- > 0) {
            HBox row = new HBox(15);
            row.setPadding(new Insets(3));
            
            // Mettre en évidence le PC actuel
            boolean isCurrentPC = (currentAddr == cpu.getPC());
            if (isCurrentPC) {
                row.setStyle("-fx-background-color: #2196f3; -fx-background-radius: 4;");
            } else {
                row.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 2; -fx-border-color: #f0f0f0; -fx-border-width: 0 0 1 0;");
            }
            
            // Adresse
            Label addrLabel = new Label(String.format("%04X", currentAddr));
            addrLabel.setFont(Font.font("Courier New", 11));
            addrLabel.setTextFill(isCurrentPC ? Color.WHITE : Color.web("#1976d2"));
            addrLabel.setMinWidth(80);
            
            // Code disassemblé (simplifié)
            String instruction = disassemble(currentAddr);
            Label codeLabel = new Label(instruction);
            codeLabel.setFont(Font.font("Courier New", 11));
            codeLabel.setTextFill(isCurrentPC ? Color.WHITE : Color.web("#2e7d32"));
            
            row.getChildren().addAll(addrLabel, codeLabel);
            contentBox.getChildren().add(row);
            
            // Avancer selon la taille de l'instruction
            currentAddr += getInstructionSize(currentAddr);
        }
    }
    
    private String disassemble(int addr) {
        int opcode = memory.readByte(addr) & 0xFF;
        
        // Table de disassemblage simplifiée
        switch (opcode) {
            case 0x86: return String.format("LDA #$%02X", memory.readByte(addr + 1) & 0xFF);
            case 0xC6: return String.format("LDB #$%02X", memory.readByte(addr + 1) & 0xFF);
            case 0x8E: return String.format("LDX #$%04X", memory.readWord(addr + 1));
            case 0x4C: return "INCA";
            case 0x5C: return "INCB";
            case 0x4A: return "DECA";
            case 0x5A: return "DECB";
            case 0x20: return String.format("BRA $%02X", memory.readByte(addr + 1) & 0xFF);
            case 0x39: return "RTS";
            case 0x3F: return "SWI";
            default: return String.format("DB $%02X", opcode);
        }
    }
    
    private int getInstructionSize(int addr) {
        int opcode = memory.readByte(addr) & 0xFF;
        
        // Tailles d'instructions simplifiées
        switch (opcode) {
            case 0x86: case 0xC6: return 2; // LDA/LDB #imm
            case 0x8E: case 0xCE: return 3; // LDX/LDU #imm16
            case 0x20: return 2; // BRA
            default: return 1; // Instructions 1 byte
        }
    }
    
    public VBox getNode() {
        return root;
    }
}

