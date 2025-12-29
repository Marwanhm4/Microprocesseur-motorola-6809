package com.motorola6809.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.motorola6809.simulator.CPU;
import com.motorola6809.simulator.Memory;
import com.motorola6809.simulator.InstructionAssembler;

public class Simulateur6809 {
    static JMenu menu, submenu, menuSimulation, menuEdition, menuAffichage, menuOutils, menuFenetres;
    static JMenuBar mb;
    static JMenuItem i1, i2, i3, i11;

    static JTable ramTable;
    static JTable romTable;
    static JFrame mainFrame;

    static Memory memory;
    static CPU cpu;

    static JTextField pcField, uField, xField, yField;
    static JTextField aField, bField, dpField, spField;
    static JCheckBox[] flagCheckBoxes;
    static JTextArea instructionArea;
    static JTextArea consoleArea;
    static JTextArea historiqueArea;
    static JTextArea editeur;

    static Timer runTimer;
    static volatile boolean isRunning = false;

    public static void main(String[] args) {

        memory = new Memory();
        cpu = new CPU(memory);
        cpu.initialize();

        memory.setRAM(0x0000, 0x03FF);
        memory.setROM(0xFC00, 0xFFFD);
        memory.setRAM(0xFFFE, 0xFFFF);

        mainFrame = new JFrame("Simulateur 6809");
        mainFrame.setSize(1200, 800);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        createMenuBar(mainFrame);

        JPanel hautpanel = new JPanel(new BorderLayout());
        JPanel titre = new JPanel();
        titre.setBackground(Color.LIGHT_GRAY);
        titre.setPreferredSize(new Dimension(1200, 60));
        JLabel label = new JLabel("WELCOME MOTOROLA 6809");
        label.setFont(new Font("Verdana", Font.BOLD, 28));
        label.setForeground(Color.black);
        titre.add(label);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

<<<<<<< HEAD
        JButton reset = new JButton("RESET");
        JButton Enregistrer = new JButton("Enregistrer");
        JButton pasApas = new JButton("Pas à pas");
        JButton run = new JButton("Exécuter");
        JButton Pause = new JButton(" Pause ⏸");

        // Agrandir les boutons
        Dimension buttonSize = new Dimension(120, 40);
        reset.setPreferredSize(buttonSize);
        Enregistrer.setPreferredSize(buttonSize);
        pasApas.setPreferredSize(buttonSize);
        run.setPreferredSize(buttonSize);
        Pause.setPreferredSize(buttonSize);

        // Augmenter la taille de la police
        Font buttonFont = new Font("Arial", Font.BOLD, 12);
        reset.setFont(buttonFont);
        Enregistrer.setFont(buttonFont);
        pasApas.setFont(buttonFont);
        run.setFont(buttonFont);
        Pause.setFont(buttonFont);

        // Ajouter les icones aux boutons
        String basePath = System.getProperty("user.dir") + "/MOTOROLA_6809/images/";

        try {
            ImageIcon resetIcon = new ImageIcon(basePath + "reset.icon.jpg");
            Image rimg = resetIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            reset.setIcon(new ImageIcon(rimg));
            reset.setHorizontalTextPosition(SwingConstants.LEFT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            ImageIcon saveIcon = new ImageIcon(basePath + "save.icon.png");
            Image simg = saveIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            Enregistrer.setIcon(new ImageIcon(simg));
            Enregistrer.setHorizontalTextPosition(SwingConstants.LEFT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            ImageIcon pasIcon = new ImageIcon(basePath + "pas.icon.png");
            Image pimg = pasIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            pasApas.setIcon(new ImageIcon(pimg));
            pasApas.setHorizontalTextPosition(SwingConstants.LEFT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            ImageIcon runIcon = new ImageIcon(basePath + "run.icon.png");
            Image rrun = runIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            run.setIcon(new ImageIcon(rrun));
            run.setHorizontalTextPosition(SwingConstants.LEFT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

=======
        // Create buttons with icons
        JButton reset = createButtonWithIcon("RESET", "images/reset.icon.jpg", 16, 16);
        JButton Enregistrer = createButtonWithIcon("Enregistrer", "images/save.icon.png", 16, 16);
        JButton pasApas = createButtonWithIcon("Pas à pas", "images/pas.icon.png", 16, 16);
        JButton run = createButtonWithIcon("▶ Exécuter", "images/run.icon.png", 16, 16);
        JButton Pause = new JButton("⏸ Pause");

        // Set uniform size for all buttons
        Dimension buttonSize = new Dimension(130, 45);
        reset.setPreferredSize(buttonSize);
        Enregistrer.setPreferredSize(buttonSize);
        pasApas.setPreferredSize(buttonSize);
        run.setPreferredSize(buttonSize);
        Pause.setPreferredSize(buttonSize);

>>>>>>> 0000038d6c42683524e2f6bb090959cdcc9a6a5c
        reset.addActionListener(e -> resetCPU());
        Enregistrer.addActionListener(e -> saveProgram());
        pasApas.addActionListener(e -> stepCPU());
        run.addActionListener(e -> runCPU());
        Pause.addActionListener(e -> pauseCPU());

        toolBar.add(reset);
        toolBar.add(Enregistrer);
        toolBar.add(pasApas);
        toolBar.add(run);
        toolBar.add(Pause);

        hautpanel.add(titre, BorderLayout.NORTH);
        hautpanel.add(toolBar, BorderLayout.SOUTH);
        mainFrame.add(hautpanel, BorderLayout.NORTH);

        JPanel centrepanel = new JPanel(new GridLayout(1, 3, 10, 0));
        centrepanel.setBackground(Color.BLACK);

        JPanel gauchePanel = createRegistersPanel();

        JPanel editorSpace = createEditorPanel();

        JPanel droitePanel = new JPanel(new GridLayout(2, 1, 0, 10));
        droitePanel.setBackground(Color.BLACK);

        JPanel ramPanel = createMemoryTable("RAM", 0x0000, 0x03FF, true);
        JPanel romPanel = createMemoryTable("ROM", 0xFC00, 0xFFFD, false);

        droitePanel.add(ramPanel);
        droitePanel.add(romPanel);

        centrepanel.add(gauchePanel);
        centrepanel.add(editorSpace);
        centrepanel.add(droitePanel);

        mainFrame.add(centrepanel, BorderLayout.CENTER);

        JPanel basPanel = createConsolePanel();
        mainFrame.add(basPanel, BorderLayout.SOUTH);

        clearROM();

        memory.writeByte(0xFFFE, (byte) 0xFC);
        memory.writeByte(0xFFFF, (byte) 0x00);

        if (consoleArea != null) {
            consoleArea.setText("");
        }
        if (historiqueArea != null) {
            historiqueArea.setText("");
        }

        cpu.reset();

        if (cpu.getPC() != 0xFC00) {
            cpu.setPC(0xFC00);
        }

        updateAllViews();

        mainFrame.setVisible(true);

    }

    private static JPanel createRegistersPanel() {
        JPanel gauchePanel = new JPanel(new BorderLayout());
        gauchePanel.setBackground(Color.BLACK);
        gauchePanel.setPreferredSize(new Dimension(280, 600));

        JPanel registresPanel = new JPanel();
        registresPanel.setPreferredSize(new Dimension(280, 250));
        registresPanel.setBackground(Color.BLACK);
        registresPanel.setForeground(Color.WHITE);
        registresPanel.setLayout(new GridLayout(8, 2, 5, 5));
        registresPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "REGISTRES", 0, 0, new Font("Verdana", Font.BOLD, 12), Color.WHITE));

        pcField = createRegisterField("FC00");
        uField = createRegisterField("0000");
        xField = createRegisterField("0000");
        yField = createRegisterField("0000");
        aField = createRegisterField("00");
        bField = createRegisterField("00");
        dpField = createRegisterField("00");
        spField = createRegisterField("0000");

        addRegisterRow(registresPanel, "PC:", pcField);
        addRegisterRow(registresPanel, "U:", uField);
        addRegisterRow(registresPanel, "X:", xField);
        addRegisterRow(registresPanel, "Y:", yField);
        addRegisterRow(registresPanel, "A:", aField);
        addRegisterRow(registresPanel, "B:", bField);
        addRegisterRow(registresPanel, "DP:", dpField);
        addRegisterRow(registresPanel, "SP:", spField);

        JPanel flagsPanel = new JPanel();
        flagsPanel.setPreferredSize(new Dimension(280, 200));
        flagsPanel.setBackground(Color.BLACK);
<<<<<<< HEAD
        flagsPanel.setLayout(new GridLayout(2, 3, 15, 15));
=======
        flagsPanel.setLayout(new GridLayout(2, 3, 18, 18));
>>>>>>> 0000038d6c42683524e2f6bb090959cdcc9a6a5c
        flagsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),
                "FLAGS", 0, 0, new Font("Verdana", Font.BOLD, 16), Color.WHITE));

<<<<<<< HEAD
        String[] flags = { "P", "AC", "N", "Z", "V", "C" };
=======
        // Keep six visible flags. Map displayed labels to underlying CPU flags:
        // Display: AC (maps to H), P (maps to I), S (maps to N), Z, V, C
        String[] flags = { "AC", "P", "S", "Z", "V", "C" };
>>>>>>> 0000038d6c42683524e2f6bb090959cdcc9a6a5c
        flagCheckBoxes = new JCheckBox[flags.length];
        for (int i = 0; i < flags.length; i++) {
            flagCheckBoxes[i] = new JCheckBox(flags[i]);
            flagCheckBoxes[i].setBackground(Color.BLACK);
            flagCheckBoxes[i].setForeground(Color.WHITE);
            flagCheckBoxes[i].setFont(new Font("Arial", Font.BOLD, 18));
            flagCheckBoxes[i].setPreferredSize(new Dimension(90, 50));
            flagCheckBoxes[i].setFocusPainted(false);
            flagCheckBoxes[i].setHorizontalAlignment(SwingConstants.CENTER);
            flagsPanel.add(flagCheckBoxes[i]);
        }

        JPanel instructionPanel = new JPanel(new BorderLayout());
        instructionPanel.setPreferredSize(new Dimension(280, 80));
        instructionPanel.setBackground(Color.BLACK);
        instructionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "INSTRUCTION COURANTE", 0, 0, new Font("Verdana", Font.BOLD, 12), Color.WHITE));

        instructionArea = new JTextArea("LDA #$00");
        instructionArea.setBackground(Color.BLACK);
        instructionArea.setForeground(Color.ORANGE);
        instructionArea.setFont(new Font("Verdana", Font.BOLD, 20));
        instructionArea.setEditable(false);
        instructionPanel.add(instructionArea, BorderLayout.CENTER);

        gauchePanel.add(registresPanel, BorderLayout.NORTH);
        gauchePanel.add(flagsPanel, BorderLayout.CENTER);
        gauchePanel.add(instructionPanel, BorderLayout.SOUTH);

        return gauchePanel;
    }

    private static JTextField createRegisterField(String initialValue) {
        JTextField field = new JTextField(initialValue);
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setFont(new Font("Arial", Font.BOLD, 20));
        field.setPreferredSize(new Dimension(22, 22));
        field.setEditable(false);
        return field;
    }

    private static JTextField createRegisterLabelField(String labelText) {
        JTextField field = new JTextField(labelText);
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setFont(new Font("Arial", Font.BOLD, 20));
        field.setPreferredSize(new Dimension(22, 22));
        field.setEditable(false);
        field.setHorizontalAlignment(JTextField.CENTER);
        return field;
    }

    private static void addRegisterRow(JPanel panel, String labelText, JTextField field) {
        JTextField labelField = createRegisterLabelField(labelText);
        panel.add(labelField);
        panel.add(field);
    }

    private static JPanel createEditorPanel() {
        JPanel editorSpace = new JPanel(new BorderLayout());
        editorSpace.setBackground(Color.DARK_GRAY);
        editorSpace.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "ÉDITEUR", 0, 0, new Font("", Font.BOLD, 14), Color.WHITE));

        editeur = new JTextArea();
        editeur.setBackground(Color.BLACK);
        editeur.setForeground(Color.WHITE);
        // Police agrandie pour l'éditeur
        editeur.setFont(new Font("Monospaced", Font.PLAIN, 18));
        editeur.setEditable(true);
        editeur.setText("");
        editeur.setCaretColor(Color.WHITE);

        JScrollPane editorScroll = new JScrollPane(editeur);
        editorScroll.getViewport().setBackground(Color.BLACK);
        editorSpace.add(editorScroll, BorderLayout.CENTER);

        return editorSpace;
    }

    private static JPanel createConsolePanel() {
        JPanel basPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        basPanel.setPreferredSize(new Dimension(1200, 150));
        basPanel.setBackground(Color.BLACK);

        JPanel consolePanel = new JPanel(new BorderLayout());
        consolePanel.setBackground(Color.BLACK);
        consolePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "CONSOLE", 0, 0,
                new Font("Arial", Font.BOLD, 12), Color.WHITE));

        consoleArea = new JTextArea();
        consoleArea.setBackground(Color.BLACK);
        consoleArea.setForeground(Color.GREEN);
        consoleArea.setFont(new Font("Verdana", Font.PLAIN, 12));
        consoleArea.setEditable(false);
        JScrollPane consoleScroll = new JScrollPane(consoleArea);
        consolePanel.add(consoleScroll, BorderLayout.CENTER);

        JPanel historiquePanel = new JPanel(new BorderLayout());
        historiquePanel.setBackground(Color.BLACK);
        historiquePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "HISTORIQUE", 0, 0,
                new Font("Arial", Font.BOLD, 12), Color.WHITE));

        historiqueArea = new JTextArea();
        historiqueArea.setBackground(Color.BLACK);
        historiqueArea.setForeground(Color.GREEN);
        historiqueArea.setFont(new Font("Verdana", Font.PLAIN, 12));
        historiqueArea.setEditable(false);
        JScrollPane historiqueScroll = new JScrollPane(historiqueArea);
        historiquePanel.add(historiqueScroll, BorderLayout.CENTER);

        basPanel.add(consolePanel);
        basPanel.add(historiquePanel);

        return basPanel;
    }

    private static JPanel createMemoryTable(String title, int startAddr, int endAddr, boolean isRAM) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        navPanel.setBackground(Color.BLACK);

        JLabel goToLabel = new JLabel("Aller à:");
        goToLabel.setForeground(Color.WHITE);
        goToLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        navPanel.add(goToLabel);

        JTextField goAddrField = new JTextField(10);
        goAddrField.setBackground(Color.DARK_GRAY);
        goAddrField.setForeground(Color.WHITE);
        goAddrField.setToolTipText("Aller à l'adresse (hex), ex: 2000");
        goAddrField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        navPanel.add(goAddrField);

        JButton goButton = new JButton("→");
        goButton.setBackground(Color.DARK_GRAY);
        goButton.setForeground(Color.WHITE);
        goButton.setFont(new Font("Arial", Font.BOLD, 12));
        navPanel.add(goButton);

        topPanel.add(navPanel, BorderLayout.SOUTH);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columnNames = { "Adresse", "Valeur" };
        int rows = endAddr - startAddr + 1;
        Object[][] data = new Object[rows][2];

        for (int i = 0; i < rows; i++) {
            data[i][0] = String.format("%04X", startAddr + i);
            data[i][1] = "00";
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int col) {
                return col == 1 && isRAM;
            }
        };

        JTable table = new JTable(model);
        table.setBackground(Color.BLACK);
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.GRAY);
        table.setFont(new Font("Monospaced", Font.PLAIN, 18));
        table.getTableHeader().setBackground(Color.DARK_GRAY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 18));
        table.setRowHeight(28);
        table.setSelectionBackground(Color.DARK_GRAY);
        table.setSelectionForeground(Color.WHITE);

        model.addTableModelListener(e -> {
            if (e.getColumn() == 1) {
                int row = e.getFirstRow();
                String addrStr = (String) model.getValueAt(row, 0);
                String valueStr = (String) model.getValueAt(row, 1);
                updateMemoryFromTable(addrStr, valueStr, isRAM);
            }
        });

        if (isRAM) {
            ramTable = table;
        } else {
            romTable = table;
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.setBackground(Color.BLACK);
        panel.add(scrollPane, BorderLayout.CENTER);

        goButton.addActionListener(e -> gotoMemoryAddress(goAddrField.getText(), table));
        goAddrField.addActionListener(e -> gotoMemoryAddress(goAddrField.getText(), table));

        return panel;
    }

    private static void resetCPU() {
        pauseCPU();

        if (consoleArea != null) {
            consoleArea.setText("");
        }
        if (historiqueArea != null) {
            historiqueArea.setText("");
        }

        clearROM();

        memory.writeByte(0xFFFE, (byte) 0xFC);
        memory.writeByte(0xFFFF, (byte) 0x00);

        cpu.reset();

        if (cpu.getPC() != 0xFC00) {
            cpu.setPC(0xFC00);
        }

        cpu.setA(0);
        cpu.setB(0);
        cpu.setX(0);
        cpu.setY(0);
        cpu.setU(0);
        cpu.setS(0);
        cpu.setDP(0);

        updateAllViews();
    }

    private static void stepCPU() {
        if (isRunning) {
            pauseCPU();
        }
        try {
            int pcBefore = cpu.getPC();

            if (pcBefore < 0xFC00 || pcBefore > 0xFFFD) {
                logError("FIN DU PROGRAMME: PC=0x" + String.format("%04X", pcBefore)
                        + " est sorti de la ROM (0xFC00-0xFFFD)");
                log("Le programme est terminé.");
                return;
            }

            byte opcodeAtPC = memory.readByte(pcBefore);

            if (opcodeAtPC == 0x00) {
                log("FIN DU PROGRAMME: Instruction END détectée à l'adresse 0x" + String.format("%04X", pcBefore));
                log("Le programme est terminé.");
                return;
            }

            String instruction = disassembleInstruction(pcBefore);
            log(String.format("\n=== EXÉCUTION ==="));
            log(String.format("PC = 0x%04X (ROM)", pcBefore));
            log(String.format("Opcode = 0x%02X", opcodeAtPC & 0xFF));
            log(String.format("Instruction = %s", instruction));

            cpu.step();

            int pcAfter = cpu.getPC();

            if (pcAfter < 0xFC00 || pcAfter > 0xFFFD) {
                log("FIN DU PROGRAMME: PC=0x" + String.format("%04X", pcAfter)
                        + " est sorti de la ROM après exécution");
                log("Le programme est terminé.");
                updateAllViews();
                return;
            }

            updateAllViews();

            int pcIncrement = pcAfter - pcBefore;
            if (pcIncrement < 0) {
                pcIncrement += 0x10000;
            }

            log(String.format("Après exécution:"));
            log(String.format("  PC: 0x%04X → 0x%04X (+%d bytes)", pcBefore, pcAfter, pcIncrement));
            log(String.format("  Registres: A=0x%02X, B=0x%02X, X=0x%04X, Y=0x%04X, U=0x%04X, S=0x%04X",
                    cpu.getA(), cpu.getB(), cpu.getX(), cpu.getY(), cpu.getU(), cpu.getS()));
            log(String.format("  DP=0x%02X", cpu.getDP()));

            addHistory(String.format("EXÉCUTION: %s (PC 0x%04X → 0x%04X)", instruction, pcBefore, pcAfter));
        } catch (IllegalStateException e) {
            logError("Erreur d'exécution: " + e.getMessage());
            logError("PC actuel: 0x" + String.format("%04X", cpu.getPC()));
            logError("Vérifiez que le programme est bien chargé en ROM.");
        } catch (Exception e) {
            logError("Erreur d'exécution: " + e.getMessage());
            logError("PC actuel: 0x" + String.format("%04X", cpu.getPC()));
        }
    }

    private static void runCPU() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        log("Exécution du programme...");

        runTimer = new Timer(100, e -> {
            try {
                int pcBefore = cpu.getPC();

                if (pcBefore < 0xFC00 || pcBefore > 0xFFFD) {
                    log("FIN DU PROGRAMME: PC=0x" + String.format("%04X", pcBefore) + " est sorti de la ROM");
                    log("Le programme est terminé.");
                    pauseCPU();
                    return;
                }

                byte opcodeAtPC = memory.readByte(pcBefore);

                if (opcodeAtPC == 0x00) {
                    log("FIN DU PROGRAMME: Instruction END détectée à l'adresse 0x" + String.format("%04X", pcBefore));
                    log("Le programme est terminé.");
                    pauseCPU();
                    return;
                }

                cpu.step();

                int pcAfter = cpu.getPC();

                if (pcAfter < 0xFC00 || pcAfter > 0xFFFD) {
                    log("FIN DU PROGRAMME: PC=0x" + String.format("%04X", pcAfter) + " est sorti de la ROM");
                    log("Le programme est terminé.");
                    pauseCPU();
                    return;
                }

                updateAllViews();
                addHistory("STEP - PC: " + String.format("%04X", cpu.getPC()));
            } catch (IllegalStateException ex) {
                logError("Erreur d'exécution: " + ex.getMessage());
                log("Arrêt de l'exécution.");
                pauseCPU();
            } catch (Exception ex) {
                logError("Erreur d'exécution: " + ex.getMessage());
                log("Arrêt de l'exécution.");
                pauseCPU();
            }
        });
        runTimer.start();
    }

    private static void pauseCPU() {
        if (runTimer != null) {
            runTimer.stop();
        }
        isRunning = false;
        log("Pause");
    }

    private static void saveProgram() {
        try {

            String code = editeur.getText();
            if (code.trim().isEmpty()) {
                logError("L'éditeur est vide. Veuillez écrire un programme.");
                return;
            }

            consoleArea.setText("");

            log("=== CHARGEMENT DU PROGRAMME DEPUIS L'ÉDITEUR ===");
            log("Code source depuis l'éditeur:");
            String[] codeLines = code.split("\n");
            int lineNum = 1;
            for (String line : codeLines) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith(";")) {
                    log(String.format("  Ligne %d: %s", lineNum, line));
                }
                lineNum++;
            }

            // ÉTAPE 2: Assembler le code en opcodes
            log("\n=== ASSEMBLAGE EN OPCODES ===");
            String[] lines = code.split("\n");
            byte[] bytecode = InstructionAssembler.assemble(lines);

            if (bytecode.length == 0) {
                logError("Aucun code généré. Vérifiez votre syntaxe.");
                return;
            }

            log("Opcodes générés (" + bytecode.length + " bytes):");
            StringBuilder hexDump = new StringBuilder();
            for (int i = 0; i < bytecode.length; i++) {
                hexDump.append(String.format("%02X ", bytecode[i] & 0xFF));
                if ((i + 1) % 16 == 0) {
                    log("  " + hexDump.toString());
                    hexDump.setLength(0);
                }
            }
            if (hexDump.length() > 0) {
                log("  " + hexDump.toString());
            }

            log("\n=== VIDAGE DE LA ROM ===");
            clearROM();
            log("ROM vidée (0xFC00-0xFFFD)");

<<<<<<< HEAD
            log("\n=== TRAITEMENT DES DIRECTIVES DB ===");
            List<InstructionAssembler.DBInfo> dbList = InstructionAssembler.extractDBDirectives(bytecode);
            if (!dbList.isEmpty()) {
                log("Directives DB trouvées: " + dbList.size());
                for (InstructionAssembler.DBInfo db : dbList) {
                    memory.writeByte(db.address, db.value);
                    log(String.format("  DB: Adresse 0x%04X = 0x%02X", db.address, db.value & 0xFF));

                    byte verify = memory.readByte(db.address);
                    if (verify == db.value) {
                        log(String.format("  ✓ Vérifié: RAM/ROM[0x%04X] = 0x%02X", db.address, verify & 0xFF));
                    } else {
                        logError(String.format("  ✗ ERREUR: RAM/ROM[0x%04X] = 0x%02X (attendu: 0x%02X)",
                                db.address, verify & 0xFF, db.value & 0xFF));
                    }
                }
                updateMemoryDisplay();
            } else {
                log("Aucune directive DB trouvée dans le code.");
            }

            byte[] codeWithoutDB = new byte[bytecode.length];
            int writeIndex = 0;
            for (int i = 0; i < bytecode.length; i++) {
                if (i < bytecode.length - 4 &&
                        (bytecode[i] & 0xFF) == 0xFF &&
                        (bytecode[i + 1] & 0xFF) == 0xFF) {
                    i += 4;
                    continue;
                }
                codeWithoutDB[writeIndex++] = bytecode[i];
            }
            byte[] finalCode = new byte[writeIndex];
            System.arraycopy(codeWithoutDB, 0, finalCode, 0, writeIndex);
=======
            byte[] finalCode = bytecode;
>>>>>>> 0000038d6c42683524e2f6bb090959cdcc9a6a5c

            log("\n=== CHARGEMENT DES OPCODES EN ROM ===");
            log("Chargement de " + finalCode.length + " bytes à l'adresse 0xFC00...");
            memory.loadROM(0xFC00, finalCode);

            log("Vérification du chargement:");
            boolean allOk = true;
            for (int i = 0; i < finalCode.length; i++) {
                int address = 0xFC00 + i;
                byte loaded = memory.readByte(address);
                byte expected = finalCode[i];
                if (loaded != expected) {
                    logError(String.format("  ✗ ROM[0x%04X] = 0x%02X (attendu: 0x%02X)",
                            address, loaded & 0xFF, expected & 0xFF));
                    allOk = false;
                } else {
                    log(String.format("  ✓ ROM[0x%04X] = 0x%02X", address, loaded & 0xFF));
                }
            }

            if (!allOk) {
                logError("ERREUR: Certains opcodes n'ont pas été correctement chargés en ROM!");
                return;
            }

            log("✓ Tous les opcodes sont correctement chargés en ROM");

            log("\n=== CONFIGURATION DU VECTEUR DE RESET ===");
            memory.writeByte(0xFFFE, (byte) 0xFC);
            memory.writeByte(0xFFFF, (byte) 0x00);
            log("Vecteur de reset configuré: 0xFFFE-0xFFFF = 0xFC00");

            log("\n=== INITIALISATION DU CPU ===");
            cpu.reset();
            cpu.setPC(0xFC00);
            log("PC initialisé à 0xFC00 (première instruction en ROM)");

            cpu.setA(0);
            cpu.setB(0);
            cpu.setX(0);
            cpu.setY(0);
            cpu.setU(0);
            cpu.setS(0);
            cpu.setDP(0);
            log("Tous les registres réinitialisés à zéro");

            byte firstOpcode = memory.readByte(0xFC00);
            int currentPC = cpu.getPC();

            log("\n=== RÉSUMÉ ===");
            log("✓ Programme assemblé: " + finalCode.length + " bytes");
<<<<<<< HEAD
            if (!dbList.isEmpty()) {
                log("✓ " + dbList.size() + " directive(s) DB traitées");
            }
=======
>>>>>>> 0000038d6c42683524e2f6bb090959cdcc9a6a5c
            log("✓ Opcodes chargés en ROM à partir de 0xFC00");
            log("✓ PC = 0x" + String.format("%04X", currentPC) + " (pointe vers la ROM)");
            log("✓ Premier opcode à 0xFC00: 0x" + String.format("%02X", firstOpcode & 0xFF));
            log("✓ CPU prêt pour l'exécution");
            log("\n→ Cliquez sur 'Pas à pas' pour exécuter instruction par instruction");
            log("→ Cliquez sur 'Exécuter' pour exécution continue");
            log("=== FIN CHARGEMENT ===");

            updateAllViews();

<<<<<<< HEAD
            if (!dbList.isEmpty()) {
                log("\n=== MISE À JOUR DE L'AFFICHAGE RAM ===");
                log("Les valeurs DB ont été écrites et l'affichage RAM a été mis à jour.");
            }

=======
>>>>>>> 0000038d6c42683524e2f6bb090959cdcc9a6a5c
            addHistory("PROGRAMME CHARGÉ - " + bytecode.length + " bytes à 0xFC00");
        } catch (IllegalArgumentException e) {
            logError("ERREUR D'ASSEMBLAGE: " + e.getMessage());
            logError("Vérifiez la syntaxe de vos instructions.");
            logError("Instructions supportées: LDA #$XX, ADDA #$XX, STA <$XX, BRA *, etc.");
        } catch (Exception e) {
            logError("ERREUR LORS DU CHARGEMENT: " + e.getMessage());
        }
    }

    private static void clearROM() {
        try {

            int romSize = 0xFFFD - 0xFC00 + 1;
            byte[] zeros = new byte[romSize];

            memory.loadROM(0xFC00, zeros);
        } catch (Exception e) {
            logError("Erreur lors du vidage de la ROM: " + e.getMessage());
        }
    }

    private static void updateAllViews() {
        updateRegistersDisplay();
        updateMemoryDisplay();
        updateInstructionDisplay();
    }

    private static void updateRegistersDisplay() {
        if (pcField == null)
            return;

        pcField.setText(String.format("%04X", cpu.getPC()));
        uField.setText(String.format("%04X", cpu.getU()));
        xField.setText(String.format("%04X", cpu.getX()));
        yField.setText(String.format("%04X", cpu.getY()));
        aField.setText(String.format("%02X", cpu.getA()));
        bField.setText(String.format("%02X", cpu.getB()));
        dpField.setText(String.format("%02X", cpu.getDP()));
        spField.setText(String.format("%04X", cpu.getS()));

        // Mise à jour des flags (6 visibles): mapping affiché -> CPU.Flag
        // indices: 0=AC(H), 1=P(I), 2=S(N), 3=Z, 4=V, 5=C
        if (flagCheckBoxes != null) {
            flagCheckBoxes[5].setSelected(cpu.isFlagSet(CPU.Flag.C)); // C
            flagCheckBoxes[4].setSelected(cpu.isFlagSet(CPU.Flag.V)); // V
            flagCheckBoxes[3].setSelected(cpu.isFlagSet(CPU.Flag.Z)); // Z
<<<<<<< HEAD
            flagCheckBoxes[2].setSelected(cpu.isFlagSet(CPU.Flag.N)); // N
            flagCheckBoxes[1].setSelected(cpu.isFlagSet(CPU.Flag.I)); // AC (affichage pour I)
            flagCheckBoxes[0].setSelected(cpu.isFlagSet(CPU.Flag.H)); // S (affichage pour H)
=======
            flagCheckBoxes[2].setSelected(cpu.isFlagSet(CPU.Flag.N)); // N -> S
            flagCheckBoxes[1].setSelected(cpu.isFlagSet(CPU.Flag.I)); // I -> P
            flagCheckBoxes[0].setSelected(cpu.isFlagSet(CPU.Flag.H)); // H -> AC
>>>>>>> 0000038d6c42683524e2f6bb090959cdcc9a6a5c
        }
    }

    private static void updateInstructionDisplay() {
        if (instructionArea == null)
            return;
        String instruction = disassembleInstruction(cpu.getPC());
        instructionArea.setText(instruction);
    }

    private static void updateMemoryDisplay() {

        if (ramTable != null) {
            DefaultTableModel model = (DefaultTableModel) ramTable.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                String addr = (String) model.getValueAt(i, 0);
                int address = Integer.parseInt(addr, 16);
                byte value = memory.readByte(address);
                model.setValueAt(String.format("%02X", value & 0xFF), i, 1);
            }
        }

        if (romTable != null) {
            DefaultTableModel model = (DefaultTableModel) romTable.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                String addr = (String) model.getValueAt(i, 0);
                int address = Integer.parseInt(addr, 16);
                byte value = memory.readByte(address);
                model.setValueAt(String.format("%02X", value & 0xFF), i, 1);
            }
        }
    }

    private static void updateMemoryFromTable(String addrStr, String valueStr, boolean isRAM) {
        try {
            int addr = Integer.parseInt(addrStr, 16);
            int value = Integer.parseInt(valueStr, 16);
            if (isRAM) {
                memory.writeByte(addr, (byte) value);
                log("RAM[" + addrStr + "] = " + valueStr);
            }
        } catch (Exception e) {
            logError("Erreur de mise à jour mémoire: " + e.getMessage());
        }
    }

    private static String disassembleInstruction(int pc) {
        try {
            int opcode = memory.readByte(pc) & 0xFF;

            if (opcode == 0x00) {
                return "END";
            }

            String mnemonic = getMnemonic(opcode);

            if (opcode == 0x86 || opcode == 0xC6 || opcode == 0x8B || opcode == 0xCB ||
                    opcode == 0x80 || opcode == 0xC0 || opcode == 0x84 || opcode == 0xC4 ||
                    opcode == 0x8A || opcode == 0xCA || opcode == 0x88 || opcode == 0xC8) {
                int imm = memory.readByte(pc + 1) & 0xFF;
                return String.format("%s #$%02X", mnemonic, imm);
            }

            if (opcode == 0x8E || opcode == 0xCE || opcode == 0x8F || opcode == 0xCF) {
                int imm = (memory.readByte(pc + 1) & 0xFF) << 8 | (memory.readByte(pc + 2) & 0xFF);
                return String.format("%s #$%04X", mnemonic, imm);
            }

            if (opcode == 0x96 || opcode == 0xD6 || opcode == 0x9E || opcode == 0xDE ||
                    opcode == 0x97 || opcode == 0xD7 || opcode == 0x9F || opcode == 0xDF) {
                int addr = memory.readByte(pc + 1) & 0xFF;
                return String.format("%s <$%02X", mnemonic, addr);
            }

            if (opcode == 0xB6 || opcode == 0xF6 || opcode == 0xBE || opcode == 0xFE ||
                    opcode == 0xB7 || opcode == 0xF7 || opcode == 0xBF || opcode == 0xFF) {
                int addr = (memory.readByte(pc + 1) & 0xFF) << 8 | (memory.readByte(pc + 2) & 0xFF);
                return String.format("%s $%04X", mnemonic, addr);
            }

            if (opcode == 0x20 || opcode == 0x26 || opcode == 0x27 || opcode == 0x21 ||
                    opcode == 0x22 || opcode == 0x23 || opcode == 0x24 || opcode == 0x25) {
                byte offsetByte = memory.readByte(pc + 1);
                int offset = offsetByte;
                int target = (pc + 2 + offset) & 0xFFFF;
                return String.format("%s $%04X", mnemonic, target);
            }

            return mnemonic;
        } catch (Exception e) {
            return "???";
        }
    }

    private static String getMnemonic(int opcode) {
        switch (opcode) {
            case 0x86:
                return "LDA";
            case 0xC6:
                return "LDB";
            case 0x8E:
                return "LDX";
            case 0xCE:
                return "LDU";
            case 0x8B:
                return "ADDA";
            case 0xCB:
                return "ADDB";
            case 0x80:
                return "SUBA";
            case 0xC0:
                return "SUBB";
            case 0x84:
                return "ANDA";
            case 0xC4:
                return "ANDB";
            case 0x8A:
                return "ORA";
            case 0xCA:
                return "ORB";
            case 0x88:
                return "EORA";
            case 0xC8:
                return "EORB";
            case 0x96:
                return "LDA";
            case 0xD6:
                return "LDB";
            case 0x97:
                return "STA";
            case 0xD7:
                return "STB";
            case 0xB6:
                return "LDA";
            case 0xF6:
                return "LDB";
            case 0xB7:
                return "STA";
            case 0xF7:
                return "STB";
            case 0x20:
                return "BRA";
            case 0x26:
                return "BNE";
            case 0x27:
                return "BEQ";
            case 0x4C:
                return "INCA";
            case 0x5C:
                return "INCB";
            case 0x4A:
                return "DECA";
            case 0x5A:
                return "DECB";
            case 0x3D:
                return "MUL";
            case 0x1A:
                return "ORCC";
            case 0x1C:
                return "ANDCC";
            case 0x3B:
                return "RTI";
            case 0x39:
                return "RTS";
            case 0x3F:
                return "SWI";
            case 0x10:
                return "NOP";
            default:
                return String.format("OP%02X", opcode);
        }
    }

    private static void gotoMemoryAddress(String address, JTable table) {
        try {
            address = address.trim().replace("0x", "").replace("0X", "");
            int addr = Integer.parseInt(address, 16);

            for (int row = 0; row < table.getRowCount(); row++) {
                String tableAddr = (String) table.getValueAt(row, 0);
                int tableAddrInt = Integer.parseInt(tableAddr, 16);

                if (tableAddrInt == addr) {
                    table.scrollRectToVisible(table.getCellRect(row, 0, true));
                    table.setRowSelectionInterval(row, row);
                    table.setSelectionBackground(Color.CYAN);
                    return;
                }
            }

            JOptionPane.showMessageDialog(mainFrame,
                    "Adresse " + address + " non trouvable dans cette zone mémoire",
                    "Adresse introuvable", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Adresse invalide. Utilisez le format hexadécimal (ex: 1000, FC00)",
                    "Erreur de format", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void log(String message) {
        if (consoleArea != null) {
            consoleArea.append(message + "\n");
            consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
        }
    }

    private static void logError(String message) {
        if (consoleArea != null) {
            consoleArea.append("[ERREUR] " + message + "\n");
            consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
        }
    }

    private static void addHistory(String entry) {
        if (historiqueArea != null) {
            historiqueArea.append(entry + "\n");
            historiqueArea.setCaretPosition(historiqueArea.getDocument().getLength());
        }
    }

    private static void createMenuBar(JFrame frame) {
        mb = new JMenuBar();

        menu = new JMenu("Fichier");
        submenu = new JMenu("Nouveau");
        i1 = new JMenuItem("Éditeur");
        i11 = new JMenuItem("Quitter");

        i1.addActionListener(e -> {

            JFrame editorFrame = new JFrame("Éditeur");
            editorFrame.setSize(600, 400);
            editorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            editorFrame.setLayout(new BorderLayout());

            JTextArea textArea = new JTextArea();
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            JScrollPane scrollPane = new JScrollPane(textArea);
            editorFrame.add(scrollPane, BorderLayout.CENTER);

            editorFrame.setVisible(true);
        });

        i11.addActionListener(e -> System.exit(0));

        submenu.add(i1);
        menu.add(submenu);
        menu.add(i11);

        menuSimulation = new JMenu("Simulation");
        i2 = new JMenuItem("Pas à pas");
        i3 = new JMenuItem("Exécuter");
        i2.addActionListener(e -> stepCPU());
        i3.addActionListener(e -> runCPU());
        menuSimulation.add(i2);
        menuSimulation.add(i3);

        menuEdition = new JMenu("Édition");
        JMenuItem i4 = new JMenuItem("Couper");
        JMenuItem i5 = new JMenuItem("Copier");
        menuEdition.add(i4);
        menuEdition.add(i5);

        menuAffichage = new JMenu("Affichage");
        JMenuItem i6 = new JMenuItem("Zoom");
        menuAffichage.add(i6);

        menuOutils = new JMenu("Outils");
        JMenuItem i7 = new JMenuItem("Calculatrice");
        menuOutils.add(i7);

        menuFenetres = new JMenu("Fenêtres");
        JMenuItem i8 = new JMenuItem("Programme");
        JMenuItem i9 = new JMenuItem("RAM");
        JMenuItem i10 = new JMenuItem("ROM");
        menuFenetres.add(i8);
        menuFenetres.add(i9);
        menuFenetres.add(i10);

        mb.add(menu);
        mb.add(menuSimulation);
        mb.add(menuEdition);
        mb.add(menuAffichage);
        mb.add(menuOutils);
        mb.add(menuFenetres);

        frame.setJMenuBar(mb);
    }

    private static JButton createButtonWithIcon(String text, String iconPath, int width, int height) {
        JButton button = new JButton(text);
        try {
            String basePath = System.getProperty("user.dir");
            String fullPath = basePath + java.io.File.separator + iconPath;
            ImageIcon icon = new ImageIcon(fullPath);
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
            button.setHorizontalTextPosition(SwingConstants.LEFT);
            button.setVerticalTextPosition(SwingConstants.CENTER);
            button.setIconTextGap(8);
        } catch (Exception e) {
            System.err.println("Could not load icon: " + iconPath);
        }
        return button;
    }
}
