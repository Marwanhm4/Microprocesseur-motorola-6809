@echo off
chcp 65001 >nul
echo ============================================
echo   CORRECTION DES ERREURS JAVAFX
echo ============================================
echo.

REM Vérifier Java
echo [1/4] Vérification de Java...
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Java non trouvé! Installez Java 11+ d'abord.
    pause
    exit /b 1
)
echo ✅ Java trouvé
echo.

REM Vérifier Maven
echo [2/4] Vérification de Maven...
mvn -version >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo ✅ Maven trouvé - Utilisation de Maven (Solution recommandée)
    echo.
    echo [3/4] Téléchargement et compilation avec Maven...
    echo.
    mvn clean compile
    if %ERRORLEVEL% EQU 0 (
        echo.
        echo ============================================
        echo ✅ COMPILATION RÉUSSIE!
        echo ============================================
        echo.
        echo Pour lancer l'application:
        echo   mvn javafx:run
        echo.
        echo OU
        echo   java --module-path target/classes --add-modules javafx.controls -cp "target/classes;%USERPROFILE%\.m2\repository\org\openjfx\javafx-controls\21\javafx-controls-21.jar" com.motorola6809.simulator.Moto6809App
        echo.
        pause
        exit /b 0
    ) else (
        echo.
        echo ❌ Erreur de compilation avec Maven
        echo.
        goto :manual_install
    )
) else (
    echo ⚠️  Maven non trouvé
    echo.
    goto :manual_install
)

:manual_install
echo [3/4] Installation manuelle de JavaFX...
echo.
echo Option A: Installer Maven (Recommandé)
echo   Télécharger: https://maven.apache.org/download.cgi
echo   Extraire et ajouter au PATH
echo   Puis relancer ce script
echo.
echo Option B: Installer JavaFX SDK manuellement
echo   1. Télécharger JavaFX SDK 21: https://openjfx.io/
echo   2. Extraire dans C:\javafx-sdk-21
echo   3. Modifier PATH_TO_FX dans compile.bat
echo   4. Exécuter: compile.bat
echo.
echo Option C: Télécharger JavaFX maintenant
echo   Voulez-vous ouvrir la page de téléchargement? (O/N)
set /p choice="> "
if /i "%choice%"=="O" (
    start https://openjfx.io/
)
echo.
echo ============================================
echo   SOLUTIONS DISPONIBLES
echo ============================================
echo.
echo SOLUTION 1 (Recommandée): Installer Maven
echo   1. Télécharger: https://maven.apache.org/download.cgi
echo   2. Extraire dans C:\Program Files\Apache\maven
echo   3. Ajouter au PATH: C:\Program Files\Apache\maven\bin
echo   4. Relancer: FIX_JAVAFX.bat
echo.
echo SOLUTION 2: Installer JavaFX SDK
echo   1. Télécharger: https://openjfx.io/
echo   2. Extraire dans C:\javafx-sdk-21
echo   3. Exécuter: compile.bat
echo.
pause





