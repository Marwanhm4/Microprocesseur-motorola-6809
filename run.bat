@echo off
REM Script pour lancer l'application JavaFX
REM Ajuster le chemin selon votre installation JavaFX

set PATH_TO_FX=C:\javafx-sdk-21\lib

if not exist "%PATH_TO_FX%" (
    echo Erreur: JavaFX SDK non trouve a %PATH_TO_FX%
    echo Veuillez installer JavaFX SDK ou modifier le chemin dans run.bat
    pause
    exit /b 1
)

echo Compilation...
javac --module-path %PATH_TO_FX% --add-modules javafx.controls -d . src/com/motorola6809/simulator/*.java

if %ERRORLEVEL% NEQ 0 (
    echo Erreur de compilation
    pause
    exit /b 1
)

echo Lancement de l'application...
java --module-path %PATH_TO_FX% --add-modules javafx.controls com.motorola6809.simulator.Moto6809App

pause

