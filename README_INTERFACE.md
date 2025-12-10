# 🎨 Interface Graphique MOTO6809 - Guide Complet

## ✅ Interface Créée

J'ai créé une interface graphique JavaFX moderne et créative pour le simulateur 6809 avec les composants suivants :

### 📦 Fichiers Créés

1. **Moto6809App.java** - Application principale JavaFX
2. **RegisterPanel.java** - Panneau d'affichage des registres
3. **MemoryView.java** - Vue mémoire (RAM/ROM)
4. **CodeEditor.java** - Éditeur de code assembleur
5. **ProgramView.java** - Vue programme disassemblé
6. **pom.xml** - Configuration Maven pour JavaFX
7. **run.bat** - Script de lancement Windows

## 🎨 Design Créatif

### Thème Sombre Moderne
- **Fond principal** : Bleu foncé (#1e1e2e)
- **Panneaux** : Bleu moyen (#252538)
- **Accents** :
  - Bleu : #60a5fa (registres, adresses)
  - Vert : #4ade80 (valeurs, succès)
  - Rouge : #f87171 (ROM, erreurs)
  - Jaune : #fbbf24 (ASCII, highlights)
  - Violet : #a78bfa (flags, accents)

### Layout Responsive
- **SplitPanes** pour redimensionner les panneaux
- **ScrollPanes** pour naviguer dans la mémoire
- **Mise en page flexible** qui s'adapte à la taille de la fenêtre

## 🚀 Installation et Lancement

### Option 1 : Avec Maven (Recommandé)

```bash
# Installer Maven si nécessaire
# Télécharger depuis : https://maven.apache.org/

# Compiler et lancer
mvn clean javafx:run
```

### Option 2 : Avec JavaFX SDK

1. Télécharger JavaFX SDK : https://openjfx.io/
2. Extraire dans `C:\javafx-sdk-21` (ou autre chemin)
3. Modifier `run.bat` avec le bon chemin
4. Exécuter `run.bat`

### Option 3 : Compilation Manuelle

```bash
# Définir le chemin JavaFX
set PATH_TO_FX=C:\javafx-sdk-21\lib

# Compiler
javac --module-path %PATH_TO_FX% --add-modules javafx.controls -d . src/com/motorola6809/simulator/*.java

# Lancer
java --module-path %PATH_TO_FX% --add-modules javafx.controls com.motorola6809.simulator.Moto6809App
```

## 📋 Fonctionnalités de l'Interface

### ✅ Implémenté

1. **Panneau Registres** :
   - Affichage de tous les registres (PC, S, U, X, Y, A, B, DP)
   - Condition Codes avec flags individuels
   - Mise à jour en temps réel

2. **Éditeur de Code** :
   - Zone de texte pour écrire du code assembleur
   - Police monospace
   - Thème sombre

3. **Vue Programme** :
   - Disassemblage automatique
   - Highlight du PC actuel en bleu
   - Affichage des adresses et instructions

4. **Vues Mémoire** :
   - RAM et ROM séparées
   - Affichage Adresse | Hex | ASCII
   - Scroll pour naviguer

5. **Contrôles** :
   - Boutons Run, Step, Reset
   - Boutons d'interruption (IRQ, FIRQ, NMI)
   - Contrôle de vitesse

6. **Menu** :
   - Fichier, Simulation, Outils, Aide

### 🔄 Mise à Jour Automatique

Toutes les vues se mettent à jour automatiquement après chaque instruction exécutée via :
- `updateAllViews()` appelée après `cpu.step()`

## 🎯 Prochaines Améliorations Possibles

1. **Assembleur intégré** : Convertir le code assembleur en opcodes
2. **Points d'arrêt** : Permettre de mettre des breakpoints
3. **Historique** : Afficher l'historique des instructions exécutées
4. **Graphiques** : Visualiser l'utilisation mémoire
5. **Thèmes** : Permettre de changer de thème (clair/sombre)
6. **Export/Import** : Sauvegarder/charger des programmes

## 📝 Notes Techniques

- **JavaFX 11+** requis (modulaire)
- **Java 11+** requis
- Les styles sont appliqués inline (pas de fichier CSS externe pour simplifier)
- L'interface est entièrement fonctionnelle et connectée au simulateur

## 🎉 Résultat

Vous avez maintenant une interface graphique complète et moderne pour votre simulateur 6809, similaire à celle de l'image mais avec un design créatif et personnalisé !





