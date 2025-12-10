# 📥 INSTALLATION MANUELLE DE JAVAFX

## ⚡ ÉTAPES RAPIDES

### 1. Télécharger JavaFX SDK 21

**Option A : Lien direct (Windows 64-bit)**
```
https://download2.gluonhq.com/openjfx/21/openjfx-21_windows-x64_bin-sdk.zip
```

**Option B : Site officiel**
1. Aller sur : https://openjfx.io/
2. Cliquer sur "Download"
3. Choisir "JavaFX 21" et "Windows" → "SDK"
4. Télécharger le fichier ZIP

### 2. Extraire JavaFX

1. Ouvrir le fichier ZIP téléchargé
2. Extraire le contenu dans `C:\javafx-sdk-21`
3. Structure attendue : `C:\javafx-sdk-21\lib\javafx.controls.jar`

### 3. Compiler le projet

**Dans PowerShell :**
```powershell
javac --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls -d build -sourcepath src src/com/motorola6809/simulator/*.java
```

**OU utiliser le script :**
```powershell
.\QUICK_FIX.bat
```

### 4. Lancer l'application

```powershell
java --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls -cp build com.motorola6809.simulator.Moto6809App
```

---

## 🔧 CORRIGER LES ERREURS DANS L'IDE

### Eclipse

1. **Clic droit sur le projet** → **Properties**
2. **Java Build Path** → **Libraries**
3. **Add External JARs...**
4. Naviguer vers `C:\javafx-sdk-21\lib`
5. Sélectionner **TOUS les fichiers .jar** :
   - `javafx.base.jar`
   - `javafx.controls.jar`
   - `javafx.fxml.jar`
   - `javafx.graphics.jar`
   - `javafx.media.jar`
   - `javafx.swing.jar`
   - `javafx.web.jar`
6. **Apply and Close**

### IntelliJ IDEA

1. **File** → **Project Structure** (Ctrl+Alt+Shift+S)
2. **Libraries** → **+** → **Java**
3. Naviguer vers `C:\javafx-sdk-21\lib`
4. Sélectionner le dossier `lib` → **OK**
5. **Apply** → **OK**

### VS Code

Créer le fichier `.vscode/settings.json` :
```json
{
    "java.project.referencedLibraries": [
        "C:/javafx-sdk-21/lib/**/*.jar"
    ]
}
```

---

## ✅ VÉRIFICATION

Après installation, vérifier :
```powershell
Test-Path C:\javafx-sdk-21\lib\javafx.controls.jar
```

Doit retourner : `True`

---

## 🎯 RÉSUMÉ

1. ✅ Télécharger JavaFX SDK 21
2. ✅ Extraire dans `C:\javafx-sdk-21`
3. ✅ Compiler avec la commande ci-dessus
4. ✅ Configurer l'IDE (optionnel mais recommandé)





