# Guide de l'Interface Graphique MOTO6809

## 🎨 Design Créatif

L'interface utilise un thème sombre moderne avec :
- **Couleurs principales** :
  - Fond : `#1e1e2e` (bleu foncé)
  - Panneaux : `#252538` (bleu moyen)
  - Accents : `#60a5fa` (bleu), `#4ade80` (vert), `#f87171` (rouge)
  - Texte : `#e2e8f0` (blanc cassé)

## 📋 Composants de l'Interface

### 1. Barre de Menu
- **Fichier** : Nouveau, Ouvrir, Enregistrer, Quitter
- **Simulation** : Exécuter, Pas à pas, Réinitialiser
- **Outils** : Assembler, Désassembler
- **Aide** : À propos

### 2. Barre d'Outils
- **▶ Exécuter** : Exécute le programme
- **⏭ Pas à pas** : Exécute une instruction
- **↻ Réinitialiser** : Reset le CPU
- **IRQ/FIRQ/NMI** : Boutons d'interruption
- **Vitesse** : Contrôle la vitesse d'exécution

### 3. Panneau Registres (Gauche)
Affiche tous les registres du CPU :
- **PC** : Program Counter (bleu clair, mis en évidence)
- **S, U** : Stack Pointers
- **X, Y** : Index Registers
- **A, B** : Accumulators
- **DP** : Direct Page Register
- **CC** : Condition Codes avec flags individuels (E, F, H, I, N, Z, V, C)

### 4. Éditeur (Centre-Haut)
Zone de texte pour écrire du code assembleur :
- Police monospace (Courier New)
- Fond sombre
- Boutons "Mise à jour" et "Édition"

### 5. Vue Programme (Centre-Bas)
Affichage du code disassemblé :
- Adresses en hexadécimal
- Instructions décodées
- **Highlight du PC actuel** en bleu

### 6. Vues Mémoire (Droite)
- **RAM** : Mémoire lecture/écriture (bleu)
- **ROM** : Mémoire lecture seule (rouge)
- Affichage : Adresse | Hex | ASCII
- Scroll pour naviguer

## 🚀 Lancement

### Avec Maven (Recommandé) :
```bash
mvn clean javafx:run
```

### Avec JavaFX SDK :
```bash
# Windows
run.bat

# Linux/Mac
chmod +x run.sh
./run.sh
```

## 🎯 Fonctionnalités

### Exécution
- **Pas à pas** : Exécute une instruction et met à jour toutes les vues
- **Réinitialisation** : Reset tous les registres et remet PC au vecteur reset

### Mise à Jour Automatique
Toutes les vues se mettent à jour automatiquement après chaque instruction :
- Registres mis à jour en temps réel
- Mémoire affichée avec valeurs actuelles
- Programme highlight le PC actuel

## 🔧 Personnalisation

Les couleurs et styles peuvent être modifiés dans :
- `Moto6809App.java` : Styles principaux
- `RegisterPanel.java` : Styles des registres
- `MemoryView.java` : Styles de la mémoire
- `CodeEditor.java` : Styles de l'éditeur

## 📝 Notes

- L'interface est responsive et s'adapte à la taille de la fenêtre
- Les SplitPanes permettent de redimensionner les panneaux
- Le design est moderne et professionnel avec un thème sombre





