# 🔧 CORRIGER LES ERREURS DANS L'IDE

## ✅ JavaFX est installé dans `C:\javafx-sdk-21`

## 🔄 Actions à faire dans VS Code :

### 1. Redémarrer VS Code
- Fermer complètement VS Code
- Rouvrir VS Code
- Ouvrir le projet

### 2. Recharger la fenêtre Java
- Appuyer sur `Ctrl+Shift+P`
- Taper : `Java: Clean Java Language Server Workspace`
- Sélectionner et confirmer
- Redémarrer VS Code

### 3. Vérifier la configuration
Le fichier `.vscode/settings.json` est déjà configuré avec :
```json
{
    "java.project.referencedLibraries": [
        "C:/javafx-sdk-21/lib/**/*.jar"
    ]
}
```

### 4. Si les erreurs persistent
- Ouvrir la palette de commandes (`Ctrl+Shift+P`)
- Taper : `Java: Rebuild Projects`
- Attendre la fin de la reconstruction

## ✅ Vérification

Après ces étapes, les erreurs `The import javafx cannot be resolved` devraient disparaître.

## 🚀 Compilation

Pour compiler manuellement :
```powershell
javac --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls -d build -sourcepath src src/com/motorola6809/simulator/*.java
```





