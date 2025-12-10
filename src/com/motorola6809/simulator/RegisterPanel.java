package com.motorola6809.simulator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.EnumSet;

/**
 * Panneau d'affichage des registres du CPU 6809
 */
public class RegisterPanel {
    private CPU cpu;
    private VBox root;
    private Label pcLabel, sLabel, uLabel, aLabel, bLabel, xLabel, yLabel, dpLabel;
    private Label ccLabel;
    private Label[] flagLabels;
    
    public RegisterPanel(CPU cpu) {
        this.cpu = cpu;
        createPanel();
    }
    
    private void createPanel() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f8f8f8; -fx-background-radius: 8; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 8;");
        
        // Titre
        Label title = new Label("Architecture 6809");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#1976d2"));
        title.setAlignment(Pos.CENTER);
        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);
        
        // Registres 16 bits
        VBox reg16Box = new VBox(10);
        reg16Box.setPadding(new Insets(10));
        reg16Box.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 6; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 6;");
        
        pcLabel = createRegisterLabel("PC", "FC04");
        sLabel = createRegisterLabel("S", "0000");
        uLabel = createRegisterLabel("U", "0000");
        xLabel = createRegisterLabel("X", "0000");
        yLabel = createRegisterLabel("Y", "0000");
        
        reg16Box.getChildren().addAll(pcLabel, sLabel, uLabel, xLabel, yLabel);
        
        // Registres 8 bits
        VBox reg8Box = new VBox(10);
        reg8Box.setPadding(new Insets(10));
        reg8Box.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 6; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 6;");
        
        aLabel = createRegisterLabel("A", "00");
        bLabel = createRegisterLabel("B", "00");
        dpLabel = createRegisterLabel("DP", "00");
        
        reg8Box.getChildren().addAll(aLabel, bLabel, dpLabel);
        
        // Condition Codes
        VBox ccBox = new VBox(10);
        ccBox.setPadding(new Insets(10));
        ccBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 6; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 6;");
        
        Label ccTitle = new Label("Condition Codes");
        ccTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        ccTitle.setTextFill(Color.web("#7b1fa2"));
        
        ccLabel = new Label("EFHINZVC");
        ccLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 14));
        ccLabel.setTextFill(Color.web("#f57c00"));
        
        // Flags individuels
        HBox flagsBox = new HBox(5);
        flagsBox.setAlignment(Pos.CENTER);
        flagLabels = new Label[8];
        String[] flagNames = {"E", "F", "H", "I", "N", "Z", "V", "C"};
        for (int i = 0; i < 8; i++) {
            flagLabels[i] = new Label(flagNames[i]);
            flagLabels[i].setFont(Font.font("Courier New", 12));
            flagLabels[i].setTextFill(Color.web("#757575"));
            flagsBox.getChildren().add(flagLabels[i]);
        }
        
        ccBox.getChildren().addAll(ccTitle, ccLabel, flagsBox);
        
        // Assembler
        root.getChildren().addAll(titleBox, reg16Box, reg8Box, ccBox);
    }
    
    private Label createRegisterLabel(String name, String value) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        
        Label nameLabel = new Label(name + ":");
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        nameLabel.setTextFill(Color.web("#424242"));
        nameLabel.setMinWidth(30);
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 14));
        valueLabel.setTextFill(Color.web("#2e7d32"));
        valueLabel.setMinWidth(60);
        valueLabel.setAlignment(Pos.CENTER_RIGHT);
        
        box.getChildren().addAll(nameLabel, valueLabel);
        
        // Créer un label conteneur
        Label container = new Label();
        container.setGraphic(box);
        return container;
    }
    
    public void update() {
        // Mettre à jour les registres
        updateRegisterLabel(pcLabel, "PC", String.format("%04X", cpu.getPC()));
        updateRegisterLabel(sLabel, "S", String.format("%04X", cpu.getS()));
        updateRegisterLabel(uLabel, "U", String.format("%04X", cpu.getU()));
        updateRegisterLabel(xLabel, "X", String.format("%04X", cpu.getX()));
        updateRegisterLabel(yLabel, "Y", String.format("%04X", cpu.getY()));
        updateRegisterLabel(aLabel, "A", String.format("%02X", cpu.getA()));
        updateRegisterLabel(bLabel, "B", String.format("%02X", cpu.getB()));
        updateRegisterLabel(dpLabel, "DP", String.format("%02X", cpu.getDP()));
        
        // Mettre à jour les flags
        EnumSet<CPU.Flag> flags = cpu.getConditionCodes();
        String[] flagNames = {"E", "F", "H", "I", "N", "Z", "V", "C"};
        CPU.Flag[] flagEnums = {CPU.Flag.E, CPU.Flag.F, CPU.Flag.H, CPU.Flag.I, 
                                CPU.Flag.N, CPU.Flag.Z, CPU.Flag.V, CPU.Flag.C};
        
        StringBuilder ccString = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            boolean set = flags.contains(flagEnums[i]);
            ccString.append(set ? flagNames[i] : "·");
            flagLabels[i].setTextFill(set ? Color.web("#2e7d32") : Color.web("#bdbdbd"));
        }
        ccLabel.setText(ccString.toString());
    }
    
    private void updateRegisterLabel(Label label, String name, String value) {
        if (label.getGraphic() != null && label.getGraphic() instanceof HBox) {
            HBox box = (HBox) label.getGraphic();
            if (box.getChildren().size() > 1 && box.getChildren().get(1) instanceof Label) {
                Label valueLabel = (Label) box.getChildren().get(1);
                valueLabel.setText(value);
            }
        }
    }
    
    public VBox getNode() {
        return root;
    }
}

