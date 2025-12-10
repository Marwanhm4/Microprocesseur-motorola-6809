# 🚀 COMMANDES RAPIDES - Correction JavaFX

## ⚡ SOLUTION LA PLUS RAPIDE

### Option A : Script Automatique (Recommandé)
```powershell
# Exécuter le script d'installation automatique
.\INSTALL_JAVAFX.ps1
```

### Option B : Installation Manuelle JavaFX

**1. Télécharger JavaFX SDK 21 :**
- URL : https://openjfx.io/
- Ou direct : https://download2.gluonhq.com/openjfx/21/openjfx-21_windows-x64_bin-sdk.zip
- Extraire dans `C:\javafx-sdk-21`

**2. Compiler :**
```powershell
javac --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls -d build -sourcepath src src/com/motorola6809/simulator/*.java
```

**3. Lancer :**
```powershell
java --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls -cp build com.motorola6809.simulator.Moto6809App
```

---

## 🔧 SOLUTION AVEC MAVEN (Alternative)

**1. Installer Maven :**
- Télécharger : https://maven.apache.org/download.cgi
- Extraire dans `C:\Program Files\Apache\maven`
- Ajouter au PATH : `C:\Program Files\Apache\maven\bin`

**2. Compiler et lancer :**
```powershell
mvn clean compile
mvn javafx:run
```

---

## 📋 TOUTES LES COMMANDES EN UNE FOIS

### Si JavaFX est dans C:\javafx-sdk-21 :
```powershell
# Compiler
javac --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls -d build -sourcepath src src/com/motorola6809/simulator/*.java

# Lancer
java --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls -cp build com.motorola6809.simulator.Moto6809App
```

### Avec variable d'environnement :
```powershell
$env:PATH_TO_FX = "C:\javafx-sdk-21\lib"
javac --module-path $env:PATH_TO_FX --add-modules javafx.controls -d build -sourcepath src src/com/motorola6809/simulator/*.java
java --module-path $env:PATH_TO_FX --add-modules javafx.controls -cp build com.motorola6809.simulator.Moto6809App
```

---

## ✅ VÉRIFICATION

```powershell
# Vérifier que les classes sont compilées
dir build\com\motorola6809\simulator\*.class

# Vérifier JavaFX
Test-Path C:\javafx-sdk-21\lib\javafx.controls.jar
```

---

## 🎯 RÉSUMÉ

**Pour corriger les erreurs JavaFX, choisissez UNE de ces options :**

1. **Script automatique** : `.\INSTALL_JAVAFX.ps1`
2. **Installation manuelle** : Télécharger JavaFX SDK → Extraire → Compiler avec les commandes ci-dessus
3. **Maven** : Installer Maven → `mvn clean compile`





