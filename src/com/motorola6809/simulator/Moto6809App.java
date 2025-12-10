package com.motorola6809.simulator;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Application JavaFX principale pour le simulateur Motorola 6809
 * Interface graphique moderne avec vue des registres, mémoire, éditeur et contrôles
 */
public class Moto6809App extends Application {
    
    private Memory memory;
    private CPU cpu;
    private RegisterPanel registerPanel;
    private MemoryView ramView;
    private MemoryView romView;
    private CodeEditor codeEditor;
    private ProgramView programView;
    
    @Override
    public void start(Stage primaryStage) {
        // Initialiser le simulateur
        memory = new Memory();
        cpu = new CPU(memory);
        
        // Charger un programme de test en mémoire
        initializeTestProgram();
        
        // Créer l'interface principale
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ffffff;");
        
        // Barre de menu
        MenuBar menuBar = createMenuBar();
        
        // Barre d'outils
        ToolBar toolBar = createToolBar();
        
        // Zone principale avec split panes
        SplitPane mainPane = new SplitPane();
        mainPane.setOrientation(Orientation.HORIZONTAL);
        
        // Panneau gauche : Registres CPU
        registerPanel = new RegisterPanel(cpu);
        
        // Panneau central : Éditeur et Programme
        SplitPane centerPane = new SplitPane();
        centerPane.setOrientation(Orientation.VERTICAL);
        codeEditor = new CodeEditor();
        programView = new ProgramView(cpu, memory);
        centerPane.getItems().addAll(codeEditor.getNode(), programView.getNode());
        centerPane.setDividerPositions(0.5);
        
        // Panneau droit : Mémoire (RAM/ROM)
        SplitPane memoryPane = new SplitPane();
        memoryPane.setOrientation(Orientation.VERTICAL);
        ramView = new MemoryView(memory, false); // RAM
        romView = new MemoryView(memory, true);  // ROM
        memoryPane.getItems().addAll(ramView.getNode(), romView.getNode());
        memoryPane.setDividerPositions(0.5);
        
        // Assembler les panneaux
        mainPane.getItems().addAll(registerPanel.getNode(), centerPane, memoryPane);
        mainPane.setDividerPositions(0.25, 0.65);
        
        // Assembler la scène
        VBox topBox = new VBox(menuBar, toolBar);
        root.setTop(topBox);
        root.setCenter(mainPane);
        
        // Créer la scène avec taille adaptée à l'écran
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        double width = Math.min(bounds.getWidth() * 0.95, 1600);
        double height = Math.min(bounds.getHeight() * 0.95, 1000);
        
        Scene scene = new Scene(root, width, height);
        
        primaryStage.setTitle("MOTO6809 - Motorola 6809 Simulator");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(600);
        // S'assurer que les boutons Windows sont visibles (pas de StageStyle.UNDECORATED)
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        primaryStage.show();
        
        // Mettre à jour les vues
        updateAllViews();
    }
    
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");
        
        Menu fileMenu = new Menu("Fichier");
        fileMenu.setStyle("-fx-text-fill: #333333;");
        MenuItem newItem = new MenuItem("Nouveau");
        MenuItem openItem = new MenuItem("Ouvrir");
        MenuItem saveItem = new MenuItem("Enregistrer");
        MenuItem exitItem = new MenuItem("Quitter");
        fileMenu.getItems().addAll(newItem, openItem, saveItem, new SeparatorMenuItem(), exitItem);
        
        Menu simMenu = new Menu("Simulation");
        simMenu.setStyle("-fx-text-fill: #333333;");
        MenuItem runItem = new MenuItem("Exécuter");
        MenuItem stepItem = new MenuItem("Pas à pas");
        MenuItem resetItem = new MenuItem("Réinitialiser");
        simMenu.getItems().addAll(runItem, stepItem, resetItem);
        
        Menu toolsMenu = new Menu("Outils");
        toolsMenu.setStyle("-fx-text-fill: #333333;");
        MenuItem assembleItem = new MenuItem("Assembler");
        MenuItem disassembleItem = new MenuItem("Désassembler");
        toolsMenu.getItems().addAll(assembleItem, disassembleItem);
        
        Menu helpMenu = new Menu("Aide");
        helpMenu.setStyle("-fx-text-fill: #333333;");
        MenuItem aboutItem = new MenuItem("À propos");
        helpMenu.getItems().add(aboutItem);
        
        menuBar.getMenus().addAll(fileMenu, simMenu, toolsMenu, helpMenu);
        
        // Actions
        stepItem.setOnAction(e -> {
            cpu.step();
            updateAllViews();
        });
        
        resetItem.setOnAction(e -> {
            cpu.reset();
            updateAllViews();
        });
        
        exitItem.setOnAction(e -> System.exit(0));
        
        return menuBar;
    }
    
    private ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();
        toolBar.setStyle("-fx-background-color: #f8f8f8; -fx-padding: 8; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");
        
        Button runBtn = new Button("▶ Exécuter");
        runBtn.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 6 12;");
        runBtn.setOnAction(e -> {
            // TODO: Implémenter exécution continue 
            cpu.step();
            updateAllViews();
        });
        
        Button stepBtn = new Button("⏭ Pas à pas");
        stepBtn.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 6 12;");
        stepBtn.setOnAction(e -> {
            cpu.step();
            updateAllViews();
        });
        
        Button resetBtn = new Button("↻ Réinitialiser");
        resetBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 6 12;");
        resetBtn.setOnAction(e -> {
            cpu.reset();
            updateAllViews();
        });
        
        Separator sep1 = new Separator();
        sep1.setOrientation(Orientation.VERTICAL);
        
        Button irqBtn = new Button("IRQ");
        irqBtn.setStyle("-fx-background-color: #9c27b0; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 6 12;");
        
        Button firqBtn = new Button("FIRQ");
        firqBtn.setStyle("-fx-background-color: #9c27b0; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 6 12;");
        
        Button nmiBtn = new Button("NMI");
        nmiBtn.setStyle("-fx-background-color: #9c27b0; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 6 12;");
        
        Separator sep2 = new Separator();
        sep2.setOrientation(Orientation.VERTICAL);
        
        Label speedLabel = new Label("Vitesse:");
        speedLabel.setStyle("-fx-text-fill: #333333;");
        Spinner<Integer> speedSpinner = new Spinner<>(1, 1000, 10);
        speedSpinner.setEditable(true);
        
        toolBar.getItems().addAll(runBtn, stepBtn, resetBtn, sep1, irqBtn, firqBtn, nmiBtn, sep2, speedLabel, speedSpinner);
        
        return toolBar;
    }
    
    private void initializeTestProgram() {
        // Adresse de départ du programme (placé en ROM pour affichage dans la vue ROM)
        int startAddr = 0xFC00;
        
        // Programme de test simple :
        // LDA #$20    (0x86 0x20) - Charge 0x20 dans le registre A
        // LDB #$64    (0xC6 0x64) - Charge 0x64 dans le registre B
        // ADDA #$10   (0x8B 0x10) - Ajoute 0x10 à A
        // INCA        (0x4C)      - Incrémente A
        // BRA *       (0x20 0xFE) - Boucle infinie (branchement relatif -2)
        
        byte[] program = {
            (byte)0x86, (byte)0x20,  // LDA #$20
            (byte)0xC6, (byte)0x64,  // LDB #$64
            (byte)0x8B, (byte)0x10,  // ADDA #$10
            (byte)0x4C,              // INCA
            (byte)0x20, (byte)0xFE    // BRA * (boucle infinie)
        };
        
        // Charger le programme en ROM (visible dans la vue ROM)
        memory.loadROM(startAddr, program);
        
        // Initialiser les vecteurs d'interruption en ROM (0xFFF0-0xFFFF)
        // Zone ROM pour les vecteurs d'interruption du 6809
        byte[] interruptVectors = new byte[16];
        
        // Vecteur de Reset (0xFFFE-0xFFFF) : pointe vers le programme
        interruptVectors[14] = (byte)((startAddr >> 8) & 0xFF);  // 0xFFFE
        interruptVectors[15] = (byte)(startAddr & 0xFF);            // 0xFFFF
        
        // Autres vecteurs d'interruption (initialisés à 0x0000 par défaut)
        // 0xFFF0: Reserved
        // 0xFFF2: SWI3
        // 0xFFF4: SWI2
        // 0xFFF6: FIRQ
        // 0xFFF8: IRQ
        // 0xFFFA: SWI
        // 0xFFFC: NMI
        // 0xFFFE: Reset (défini ci-dessus)
        
        // Charger les vecteurs en ROM
        memory.loadROM(0xFFF0, interruptVectors);
        
        // Réinitialiser le CPU pour charger le PC depuis le vecteur de reset
        cpu.reset();
    }
    
    private void updateAllViews() {
        registerPanel.update();
        ramView.update();
        romView.update();
        programView.update();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

