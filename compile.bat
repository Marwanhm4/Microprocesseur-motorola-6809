@echo off
REM Script de compilation avec JavaFX

REM Définir le chemin vers JavaFX SDK
REM Modifier ce chemin selon votre installation
REM Par défaut: C:\javafx-sdk-21\lib
set PATH_TO_FX=C:\javafx-sdk-21\lib

REM Vérifier si JavaFX est installé
if not exist "%PATH_TO_FX%\javafx.controls.jar" (
    echo.
    echo ============================================
    echo ERREUR: JavaFX SDK non trouvé!
    echo ============================================
    echo.
    echo Veuillez:
    echo 1. Télécharger JavaFX SDK depuis https://openjfx.io/
    echo 2. Extraire dans un dossier (ex: C:\javafx-sdk-21)
    echo 3. Modifier PATH_TO_FX dans ce script
    echo.
    echo OU utilisez Maven: mvn clean compile
    echo.
    pause
    exit /b 1
)

echo Compilation avec JavaFX...
echo.

REM Créer le dossier de sortie
if not exist "build" mkdir build

REM Compiler avec JavaFX dans le classpath
javac --module-path "%PATH_TO_FX%" --add-modules javafx.controls -d build -sourcepath src src/com/motorola6809/simulator/*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ============================================
    echo Compilation réussie!
    echo ============================================
    echo.
    echo Pour lancer l'application:
    echo   run.bat
    echo.
) else (
    echo.
    echo ============================================
    echo Erreur de compilation!
    echo ============================================
    echo.
)

pause

