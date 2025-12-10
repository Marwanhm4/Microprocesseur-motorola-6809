@echo off
chcp 65001 >nul
echo ============================================
echo   LANCEMENT DE L'APPLICATION MOTO6809
echo ============================================
echo.

REM Vérifier JavaFX
if not exist "C:\javafx-sdk-21\lib\javafx.controls.jar" (
    echo ❌ ERREUR: JavaFX non trouvé!
    echo.
    echo Veuillez installer JavaFX SDK 21 dans C:\javafx-sdk-21
    echo Voir INSTALLATION_MANUELLE.md pour les instructions
    echo.
    pause
    exit /b 1
)

REM Vérifier si les classes sont compilées
if not exist "build\com\motorola6809\simulator\Moto6809App.class" (
    echo ⚠️  Compilation nécessaire...
    echo.
    call compile.bat
    if %ERRORLEVEL% NEQ 0 (
        echo.
        echo ❌ Erreur de compilation
        pause
        exit /b 1
    )
    echo.
)

echo ✅ Lancement de l'application...
echo.

REM Lancer l'application
java --module-path C:\javafx-sdk-21\lib --add-modules javafx.controls -cp build com.motorola6809.simulator.Moto6809App

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Erreur lors du lancement
    echo.
    pause
)

