# 🔧 Configuration JavaFX

## Problème
L'erreur `The import javafx cannot be resolved` indique que JavaFX n'est pas dans le classpath.

## ✅ Solutions

### Option 1 : Maven (Recommandé - Plus Simple)

1. Installer Maven : https://maven.apache.org/download.cgi
2. Extraire et ajouter au PATH
3. Compiler et lancer :
```bash
mvn clean javafx:run
```

Maven téléchargera automatiquement JavaFX !

### Option 2 : JavaFX SDK (Manuel)

1. **Télécharger JavaFX SDK** :
   - Aller sur https://openjfx.io/
   - Télécharger JavaFX SDK 21 (ou version compatible)
   - Extraire dans `C:\javafx-sdk-21` (ou autre chemin)

2. **Compiler** :
```bash
compile.bat
```
   (Modifier `PATH_TO_FX` dans `compile.bat` si nécessaire)

3. **Lancer** :
```bash
run.bat
```

### Option 3 : Compilation Manuelle

```bash
# Définir le chemin JavaFX
set PATH_TO_FX=C:\javafx-sdk-21\lib

# Compiler
javac --module-path %PATH_TO_FX% --add-modules javafx.controls -d build -sourcepath src src/com/motorola6809/simulator/*.java

# Lancer
java --module-path %PATH_TO_FX% --add-modules javafx.controls -cp build com.motorola6809.simulator.Moto6809App
```

## 📝 Note

JavaFX n'est plus inclus dans Java depuis Java 11. Il faut l'ajouter manuellement au classpath ou utiliser Maven qui le gère automatiquement.





