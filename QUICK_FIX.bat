@echo off
chcp 65001 >nul
echo ============================================
echo   CORRECTION RAPIDE JAVAFX
echo ============================================
echo.

REM Vérifier si JavaFX est installé
if exist "C:\javafx-sdk-21\lib\javafx.controls.jar" (
    echo ✅ JavaFX trouvé dans C:\javafx-sdk-21
    echo.
    echo Compilation...
    echo.
    
    REM Créer dossier build
    if not exist "build" mkdir build
    
    REM Compiler
    javac --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls -d build -sourcepath src src/com/motorola6809/simulator/*.java
    
    if %ERRORLEVEL% EQU 0 (
        echo.
        echo ============================================
        echo ✅ COMPILATION RÉUSSIE!
        echo ============================================
        echo.
        echo Pour lancer l'application:
        echo   java --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls -cp build com.motorola6809.simulator.Moto6809App
        echo.
        echo OU exécuter: run.bat
        echo.
    ) else (
        echo.
        echo ❌ Erreur de compilation
        echo.
    )
) else (
    echo ❌ JavaFX non trouvé dans C:\javafx-sdk-21
    echo.
    echo Solutions:
    echo   1. Exécuter: .\INSTALL_JAVAFX.ps1 (installation automatique)
    echo   2. Télécharger manuellement: https://openjfx.io/
    echo      Extraire dans C:\javafx-sdk-21
    echo      Puis relancer ce script
    echo   3. Installer Maven et utiliser: mvn clean compile
    echo.
    echo Voir COMMANDES_RAPIDES.md pour plus de détails
    echo.
)

pause





