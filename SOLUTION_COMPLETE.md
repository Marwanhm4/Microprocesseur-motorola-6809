# 🔧 SOLUTION COMPLÈTE - Correction des Erreurs JavaFX

## 🎯 Problème
```
The import javafx cannot be resolved
```

## ✅ SOLUTION 1 : Maven (RECOMMANDÉ - Plus Simple)

### Étape 1 : Vérifier si Maven est installé
```powershell
mvn -version
```

### Étape 2A : Si Maven est installé
```powershell
# Compiler avec Maven (télécharge JavaFX automatiquement)
mvn clean compile

# Lancer l'application
mvn javafx:run
```

### Étape 2B : Si Maven n'est PAS installé

**Installer Maven :**
1. Télécharger : https://maven.apache.org/download.cgi
2. Extraire dans `C:\Program Files\Apache\maven`
3. Ajouter au PATH :
   - Ouvrir "Variables d'environnement"
   - Ajouter `C:\Program Files\Apache\maven\bin` au PATH
4. Redémarrer le terminal
5. Vérifier : `mvn -version`
6. Compiler : `mvn clean compile`

---

## ✅ SOLUTION 2 : JavaFX SDK Manuel

### Étape 1 : Télécharger JavaFX SDK
1. Aller sur : https://openjfx.io/
2. Télécharger **JavaFX SDK 21** (Windows)
3. Extraire dans `C:\javafx-sdk-21`

### Étape 2 : Compiler avec JavaFX
```powershell
# Option A : Utiliser compile.bat (modifier PATH_TO_FX si nécessaire)
compile.bat

# Option B : Commande manuelle
$env:PATH_TO_FX = "C:\javafx-sdk-21\lib"
javac --module-path $env:PATH_TO_FX --add-modules javafx.controls -d build -sourcepath src src/com/motorola6809/simulator/*.java
```

### Étape 3 : Lancer l'application
```powershell
java --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls -cp build com.motorola6809.simulator.Moto6809App
```

---

## ✅ SOLUTION 3 : Script Automatique

Exécuter le script qui détecte et corrige automatiquement :
```powershell
.\FIX_JAVAFX.bat
```

---

## 🎯 SOLUTION RAPIDE (Copier-Coller)

### Si vous avez Maven :
```powershell
mvn clean compile
mvn javafx:run
```

### Si vous n'avez pas Maven :
```powershell
# 1. Télécharger JavaFX SDK depuis https://openjfx.io/
# 2. Extraire dans C:\javafx-sdk-21
# 3. Exécuter :
$env:PATH_TO_FX = "C:\javafx-sdk-21\lib"
javac --module-path $env:PATH_TO_FX --add-modules javafx.controls -d build -sourcepath src src/com/motorola6809/simulator/*.java
java --module-path $env:PATH_TO_FX --add-modules javafx.controls -cp build com.motorola6809.simulator.Moto6809App
```

---

## 📝 Configuration IDE (Pour éliminer les erreurs dans l'éditeur)

### Eclipse
1. Clic droit projet → **Properties**
2. **Java Build Path** → **Libraries**
3. **Add External JARs...**
4. Sélectionner tous les JARs dans `C:\javafx-sdk-21\lib`

### IntelliJ IDEA
1. **File** → **Project Structure** (Ctrl+Alt+Shift+S)
2. **Libraries** → **+** → **Java**
3. Sélectionner le dossier `C:\javafx-sdk-21\lib`

### VS Code
Créer `.vscode/settings.json` :
```json
{
    "java.project.referencedLibraries": [
        "C:/javafx-sdk-21/lib/**/*.jar"
    ]
}
```

---

## ✅ Vérification

Après compilation, vérifier :
```powershell
# Vérifier que les classes sont compilées
dir build\com\motorola6809\simulator\*.class

# Ou avec Maven
dir target\classes\com\motorola6809\simulator\*.class
```

---

## 🎉 Résultat Attendu

- ✅ Compilation sans erreurs
- ✅ Application JavaFX qui se lance
- ✅ Interface graphique visible





