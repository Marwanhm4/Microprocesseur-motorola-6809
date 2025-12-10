# 🔧 Configuration IDE pour JavaFX

## Problème
L'erreur `The import javafx cannot be resolved` dans l'IDE signifie que JavaFX n'est pas configuré dans le classpath de l'IDE.

## ✅ Solutions par IDE

### Eclipse

1. **Clic droit sur le projet** → Properties
2. **Java Build Path** → Libraries
3. **Add External JARs...**
4. Naviguer vers `C:\javafx-sdk-21\lib`
5. Sélectionner **tous les JARs** :
   - `javafx.base.jar`
   - `javafx.controls.jar`
   - `javafx.fxml.jar`
   - `javafx.graphics.jar`
   - `javafx.media.jar`
   - `javafx.swing.jar`
   - `javafx.web.jar`
6. **Apply and Close**

**OU utiliser Maven** :
- Clic droit sur projet → Configure → Convert to Maven Project
- Maven gérera automatiquement JavaFX

### IntelliJ IDEA

1. **File** → Project Structure (Ctrl+Alt+Shift+S)
2. **Libraries** → **+** → Java
3. Naviguer vers `C:\javafx-sdk-21\lib`
4. Sélectionner le dossier `lib` → OK
5. **Apply** → **OK**

**OU utiliser Maven** :
- IntelliJ détecte automatiquement `pom.xml`
- Maven gérera JavaFX

### VS Code

1. Installer l'extension **Extension Pack for Java**
2. Créer `.vscode/settings.json` :
```json
{
    "java.project.referencedLibraries": [
        "C:/javafx-sdk-21/lib/**/*.jar"
    ]
}
```

**OU utiliser Maven** :
- VS Code détecte automatiquement `pom.xml`

## 🎯 Solution Recommandée : Maven

Maven gère automatiquement JavaFX via `pom.xml` :

```bash
mvn clean compile
```

L'IDE détectera automatiquement les dépendances Maven.

## 📝 Note

L'erreur dans l'IDE n'empêche pas la compilation avec les scripts `compile.bat` ou `run.bat` qui incluent JavaFX manuellement. C'est juste une question de configuration de l'IDE.





