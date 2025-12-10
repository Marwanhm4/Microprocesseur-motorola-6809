package com.motorola6809.simulator;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Éditeur de code assembleur
 */
public class CodeEditor {
    private VBox root;
    private TextArea textArea;
    
    public CodeEditor() {
        createEditor();
    }
    
    private void createEditor() {
        root = new VBox(5);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #f8f8f8; -fx-background-radius: 8; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 8;");
        
        // Titre et toolbar
        HBox header = new HBox(10);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        Label title = new Label("Éditeur");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        title.setTextFill(Color.web("#1976d2"));
        
        Button updateBtn = new Button("Mise à jour");
        updateBtn.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 4 12;");
        
        Button editBtn = new Button("Édition");
        editBtn.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 4 12;");
        
        header.getChildren().addAll(title, updateBtn, editBtn);
        
        // Zone de texte
        textArea = new TextArea();
        textArea.setFont(Font.font("Courier New", 12));
        textArea.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #212121; -fx-control-inner-background: #ffffff; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");
        textArea.setPrefRowCount(15);
        textArea.setText("LDA #$20\nLDB #$64\nEND");
        
        root.getChildren().addAll(header, textArea);
    }
    
    public String getCode() {
        return textArea.getText();
    }
    
    public void setCode(String code) {
        textArea.setText(code);
    }
    
    public VBox getNode() {
        return root;
    }
}

