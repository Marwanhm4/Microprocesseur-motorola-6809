# Configuration JavaFX pour MOTO6809

## Installation de JavaFX

### Option 1 : JavaFX SDK (Recommandé)

1. Télécharger JavaFX SDK depuis : https://openjfx.io/
2. Extraire dans un dossier (ex: `C:\javafx-sdk-21`)
3. Ajouter au classpath lors de la compilation et l'exécution

### Option 2 : Utiliser Maven (Plus simple)

Créer un fichier `pom.xml` à la racine du projet :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.motorola6809</groupId>
    <artifactId>simulator</artifactId>
    <version>1.0</version>
    
    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <javafx.version>21</javafx.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>${javafx.version}</version>
        </dependency>
    </dependencies>
</project>
```

## Compilation et Exécution

### Avec JavaFX SDK installé :

```bash
# Définir le chemin JavaFX (ajuster selon votre installation)
set PATH_TO_FX=C:\javafx-sdk-21\lib

# Compiler
javac --module-path %PATH_TO_FX% --add-modules javafx.controls -d . src/com/motorola6809/simulator/*.java

# Exécuter
java --module-path %PATH_TO_FX% --add-modules javafx.controls com.motorola6809.simulator.Moto6809App
```

### Avec Maven :

```bash
mvn clean compile
mvn javafx:run
```

## Structure de l'Interface

L'interface comprend :
- **Panneau Registres** : Affiche tous les registres du CPU (PC, S, U, A, B, X, Y, DP, CC)
- **Éditeur** : Zone de texte pour écrire du code assembleur
- **Vue Programme** : Affichage du code disassemblé avec highlight du PC
- **Vue RAM** : Affichage de la mémoire RAM
- **Vue ROM** : Affichage de la mémoire ROM
- **Barre d'outils** : Boutons Run, Step, Reset, Interrupts





