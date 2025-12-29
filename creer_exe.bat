@echo off
chcp 65001 >nul
echo ========================================
echo Creation de l'executable Simulateur6809.exe
echo ========================================
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-21.0.1
set JAVA_BIN=%JAVA_HOME%\bin

if not exist "%JAVA_BIN%\javac.exe" (
    echo ERREUR: javac.exe non trouve dans %JAVA_BIN%
    echo Veuillez verifier votre installation Java
    pause
    exit /b 1
)

echo [1/4] Compilation du projet...
"%JAVA_BIN%\javac.exe" -d build -sourcepath src src/com/motorola6809/simulator/*.java src/com/motorola6809/view/*.java
if errorlevel 1 (
    echo ERREUR: La compilation a echoue!
    pause
    exit /b 1
)
echo ✓ Compilation reussie!
echo.

echo [2/4] Creation du fichier JAR...
if exist Simulateur6809.jar del Simulateur6809.jar
"%JAVA_BIN%\jar.exe" cvfm Simulateur6809.jar MANIFEST.MF -C build . >nul 2>&1
if errorlevel 1 (
    echo ERREUR: La creation du JAR a echoue!
    pause
    exit /b 1
)
echo ✓ JAR cree avec succes: Simulateur6809.jar
echo.

echo [3/4] Verification de jpackage...
if exist "%JAVA_BIN%\jpackage.exe" (
    echo ✓ jpackage trouve!
    echo.
    echo [4/4] Creation de l'executable avec jpackage...
    if exist Simulateur6809.exe del Simulateur6809.exe
    if exist Simulateur6809 rmdir /s /q Simulateur6809 2>nul
    "%JAVA_BIN%\jpackage.exe" --input . --name "Simulateur6809" --main-jar Simulateur6809.jar --main-class com.motorola6809.view.Simulateur6809 --type app-image --dest . --win-console --app-version 1.0
    if errorlevel 1 (
        echo jpackage a echoue. Creation d'un lanceur batch a la place...
        goto :create_bat
    )
    
    if exist Simulateur6809\Simulateur6809.exe (
        copy Simulateur6809\Simulateur6809.exe . >nul 2>&1
        echo.
        echo ========================================
        echo ✓ SUCCES! Executable cree: Simulateur6809.exe
        echo Vous pouvez maintenant le copier sur votre bureau
        echo ========================================
        echo.
        echo Fichiers crees:
        echo   - Simulateur6809.exe (executable principal)
        echo   - Simulateur6809.jar (fichier JAR)
        echo.
        echo Note: Pour distribuer, copiez Simulateur6809.exe
        echo       ou le dossier Simulateur6809\ complet
        echo ========================================
    ) else (
        goto :create_bat
    )
) else (
    echo jpackage non disponible. Creation d'un lanceur batch...
    goto :create_bat
)
goto :end

:create_bat
echo Creation d'un lanceur batch executable...
(
echo @echo off
echo cd /d "%%~dp0"
echo start "" "%JAVA_BIN%\javaw.exe" -jar "%%~dp0Simulateur6809.jar"
) > Simulateur6809.bat
echo.
echo ========================================
echo Lanceur cree: Simulateur6809.bat
echo (Double-cliquez pour lancer le simulateur)
echo.
echo Note: Pour un vrai .exe, vous avez besoin de:
echo   1. JDK complet (avec jpackage)
echo   2. Ou utilisez Launch4j pour convertir le JAR en EXE
echo ========================================

:end
echo.
pause
