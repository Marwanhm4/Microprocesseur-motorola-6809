# 🔧 CORRIGER LES ERREURS JAVAFX - GUIDE COMPLET

## 🎯 PROBLÈME
Les erreurs `The import javafx cannot be resolved` apparaissent dans l'IDE.

## ✅ SOLUTION ÉTAPE PAR ÉTAPE

### ÉTAPE 1 : Installer JavaFX SDK

**Télécharger :**
- Lien direct : https://download2.gluonhq.com/openjfx/21/openjfx-21_windows-x64_bin-sdk.zip
- Ou site : https://openjfx.io/ → Download → JavaFX 21 → Windows → SDK

**Installer :**
1. Extraire le ZIP téléchargé
2. Copier le contenu dans `C:\javafx-sdk-21`
3. Vérifier que `C:\javafx-sdk-21\lib\javafx.controls.jar` existe

### ÉTAPE 2 : Compiler le projet

**Commande PowerShell :**
```powershell
javac --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls -d build -sourcepath src src/com/motorola6809/simulator/*.java
```

**OU utiliser le script :**
```powershell
.\QUICK_FIX.bat
```

### ÉTAPE 3 : Corriger les erreurs dans l'IDE

#### Si vous utilisez VS Code :
Le fichier `.vscode/settings.json` a été créé automatiquement.
**Redémarrer VS Code** pour que les changements prennent effet.

#### Si vous utilisez Eclipse :
1. Clic droit projet → **Properties**
2. **Java Build Path** → **Libraries**
3. **Add External JARs...**
4. Sélectionner tous les JARs dans `C:\javafx-sdk-21\lib`
5. **Apply and Close**

#### Si vous utilisez IntelliJ :
1. **File** → **Project Structure** (Ctrl+Alt+Shift+S)
2. **Libraries** → **+** → **Java**
3. Sélectionner `C:\javafx-sdk-21\lib`
4. **Apply** → **OK**

### ÉTAPE 4 : Vérifier

**Vérifier l'installation :**
```powershell
Test-Path C:\javafx-sdk-21\lib\javafx.controls.jar
```

**Vérifier la compilation :**
```powershell
dir build\com\motorola6809\simulator\*.class
```

**Lancer l'application :**
```powershell
java --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls -cp build com.motorola6809.simulator.Moto6809App
```

---

## 🚨 SI LES ERREURS PERSISTENT

### Option 1 : Redémarrer l'IDE
Fermer et rouvrir complètement l'IDE (VS Code, Eclipse, IntelliJ).

### Option 2 : Nettoyer et recompiler
```powershell
# Supprimer les fichiers compilés
Remove-Item -Recurse -Force build
Remove-Item -Recurse -Force bin

# Recompiler
javac --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls -d build -sourcepath src src/com/motorola6809/simulator/*.java
```

### Option 3 : Vérifier le chemin JavaFX
Assurez-vous que le chemin est exactement : `C:\javafx-sdk-21\lib`

Si vous avez installé ailleurs, modifier :
- `.vscode/settings.json` (VS Code)
- `.classpath` (Eclipse)
- Project Structure (IntelliJ)

---

## 📝 COMMANDES RAPIDES

**Tout en une fois :**
```powershell
# 1. Vérifier JavaFX
Test-Path C:\javafx-sdk-21\lib\javafx.controls.jar

# 2. Compiler
javac --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls -d build -sourcepath src src/com/motorola6809/simulator/*.java

# 3. Lancer
java --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls -cp build com.motorola6809.simulator.Moto6809App
```

---

## ✅ RÉSULTAT ATTENDU

Après ces étapes :
- ✅ Les erreurs dans l'IDE disparaissent
- ✅ La compilation fonctionne
- ✅ L'application se lance correctement





