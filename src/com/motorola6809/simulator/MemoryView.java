package com.motorola6809.simulator;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vue pour afficher la mémoire (RAM ou ROM)
 */
public class MemoryView {
    private Memory memory;
    private boolean isROM;
    private VBox root;
    private VBox contentBox;
    private ScrollPane scrollPane;
    
    public MemoryView(Memory memory, boolean isROM) {
        this.memory = memory;
        this.isROM = isROM;
        createView();
    }
    
    private void createView() {
        root = new VBox(5);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #f8f8f8; -fx-background-radius: 8; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 8;");
        
        // Titre
        Label title = new Label(isROM ? "ROM" : "RAM");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        title.setTextFill(isROM ? Color.web("#d32f2f") : Color.web("#1976d2"));
        
        // En-tête
        HBox header = new HBox(10);
        Label addrHeader = new Label("Adresse");
        addrHeader.setFont(Font.font("Courier New", FontWeight.BOLD, 11));
        addrHeader.setTextFill(Color.web("#424242"));
        addrHeader.setMinWidth(60);
        
        Label hexHeader = new Label("Hex");
        hexHeader.setFont(Font.font("Courier New", FontWeight.BOLD, 11));
        hexHeader.setTextFill(Color.web("#424242"));
        hexHeader.setMinWidth(40);
        
        Label asciiHeader = new Label("ASCII");
        asciiHeader.setFont(Font.font("Courier New", FontWeight.BOLD, 11));
        asciiHeader.setTextFill(Color.web("#424242"));
        asciiHeader.setMinWidth(50);
        
        header.getChildren().addAll(addrHeader, hexHeader, asciiHeader);
        header.setPadding(new Insets(5));
        header.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 4;");
        
        // Zone de contenu avec scroll
        contentBox = new VBox(2);
        contentBox.setPadding(new Insets(5));
        
        scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e0e0e0;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        root.getChildren().addAll(title, header, scrollPane);
        
        update();
    }
    
    public void update() {
        contentBox.getChildren().clear();
        
        if (isROM) {
            // Pour la ROM : afficher les vecteurs d'interruption (0xFFF0-0xFFFF)
            // et autres zones ROM importantes
            int startAddr = 0xFFF0;
            int endAddr = 0xFFFF;
            
            // En-tête pour les vecteurs
            HBox headerRow = new HBox(10);
            headerRow.setPadding(new Insets(5));
            headerRow.setStyle("-fx-background-color: #fff3e0; -fx-background-radius: 4;");
            
            Label vectorLabel = new Label("Vecteurs d'interruption 6809");
            vectorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
            vectorLabel.setTextFill(Color.web("#e65100"));
            headerRow.getChildren().add(vectorLabel);
            contentBox.getChildren().add(headerRow);
            
            // Noms des vecteurs
            String[] vectorNames = {
                "0xFFF0: Reserved",
                "0xFFF2: SWI3",
                "0xFFF4: SWI2",
                "0xFFF6: FIRQ",
                "0xFFF8: IRQ",
                "0xFFFA: SWI",
                "0xFFFC: NMI",
                "0xFFFE: Reset"
            };
            
            for (int addr = startAddr; addr <= endAddr; addr += 2) {
                if (!memory.isROM(addr) && !memory.isROM(addr + 1)) continue;
                
                HBox row = new HBox(10);
                row.setPadding(new Insets(3));
                row.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 2; -fx-border-color: #f0f0f0; -fx-border-width: 0 0 1 0;");
                
                // Nom du vecteur
                int vectorIndex = (addr - startAddr) / 2;
                Label nameLabel = new Label(vectorIndex < vectorNames.length ? vectorNames[vectorIndex] : String.format("%04X:", addr));
                nameLabel.setFont(Font.font("Courier New", 10));
                nameLabel.setTextFill(Color.web("#757575"));
                nameLabel.setMinWidth(120);
                
                // Adresse
                Label addrLabel = new Label(String.format("%04X", addr));
                addrLabel.setFont(Font.font("Courier New", 11));
                addrLabel.setTextFill(Color.web("#1976d2"));
                addrLabel.setMinWidth(60);
                
                // Valeur 16-bit (vecteur)
                int vectorValue = memory.readWord(addr);
                Label hexLabel = new Label(String.format("%04X", vectorValue));
                hexLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 11));
                hexLabel.setTextFill(Color.web("#d32f2f"));
                hexLabel.setMinWidth(50);
                
                row.getChildren().addAll(nameLabel, addrLabel, hexLabel);
                contentBox.getChildren().add(row);
            }
        } else {
            // Pour la RAM : afficher les 256 premières adresses (0x0000-0x00FF)
            for (int addr = 0; addr < 256; addr++) {
                if (memory.isROM(addr)) continue;
                
                HBox row = new HBox(10);
                row.setPadding(new Insets(2));
                row.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 2; -fx-border-color: #f0f0f0; -fx-border-width: 0 0 1 0;");
                
                // Adresse
                Label addrLabel = new Label(String.format("%04X", addr));
                addrLabel.setFont(Font.font("Courier New", 11));
                addrLabel.setTextFill(Color.web("#1976d2"));
                addrLabel.setMinWidth(60);
                
                // Valeur hex
                byte value = memory.readByte(addr);
                Label hexLabel = new Label(String.format("%02X", value & 0xFF));
                hexLabel.setFont(Font.font("Courier New", 11));
                hexLabel.setTextFill(Color.web("#2e7d32"));
                hexLabel.setMinWidth(40);
                
                // ASCII
                char ascii = (value >= 32 && value < 127) ? (char) value : '·';
                Label asciiLabel = new Label(String.valueOf(ascii));
                asciiLabel.setFont(Font.font("Courier New", 11));
                asciiLabel.setTextFill(Color.web("#f57c00"));
                asciiLabel.setMinWidth(50);
                
                row.getChildren().addAll(addrLabel, hexLabel, asciiLabel);
                contentBox.getChildren().add(row);
            }
        }
    }
    
    public VBox getNode() {
        return root;
    }
}

