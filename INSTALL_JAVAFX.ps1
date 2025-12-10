# Script PowerShell pour installer et configurer JavaFX automatiquement

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  INSTALLATION AUTOMATIQUE JAVAFX" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Vérifier Java
Write-Host "[1/4] Vérification de Java..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-Object -First 1
    Write-Host "✅ Java trouvé: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Java non trouvé! Installez Java 11+ d'abord." -ForegroundColor Red
    exit 1
}

# Chemin d'installation
$javafxPath = "C:\javafx-sdk-21"
$javafxLib = "$javafxPath\lib"

# Vérifier si JavaFX est déjà installé
if (Test-Path $javafxLib) {
    Write-Host ""
    Write-Host "✅ JavaFX déjà installé dans $javafxPath" -ForegroundColor Green
    Write-Host ""
    Write-Host "Compilation..." -ForegroundColor Yellow
    & javac --module-path $javafxLib --add-modules javafx.controls -d build -sourcepath src src/com/motorola6809/simulator/*.java
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "✅ COMPILATION RÉUSSIE!" -ForegroundColor Green
        Write-Host ""
        Write-Host "Pour lancer l'application:" -ForegroundColor Cyan
        Write-Host "  java --module-path $javafxLib --add-modules javafx.controls -cp build com.motorola6809.simulator.Moto6809App" -ForegroundColor White
    }
    exit 0
}

Write-Host ""
Write-Host "[2/4] JavaFX non trouvé. Installation nécessaire..." -ForegroundColor Yellow
Write-Host ""
Write-Host "Option 1: Téléchargement automatique (recommandé)" -ForegroundColor Cyan
Write-Host "Option 2: Installation manuelle" -ForegroundColor Cyan
Write-Host ""
$choice = Read-Host "Votre choix (1 ou 2)"

if ($choice -eq "1") {
    Write-Host ""
    Write-Host "[3/4] Téléchargement de JavaFX SDK 21..." -ForegroundColor Yellow
    Write-Host "URL: https://download2.gluonhq.com/openjfx/21/openjfx-21_windows-x64_bin-sdk.zip" -ForegroundColor Gray
    
    $zipPath = "$env:TEMP\javafx-sdk-21.zip"
    $extractPath = "C:\"
    
    try {
        # Télécharger JavaFX
        Write-Host "Téléchargement en cours..." -ForegroundColor Yellow
        Invoke-WebRequest -Uri "https://download2.gluonhq.com/openjfx/21/openjfx-21_windows-x64_bin-sdk.zip" -OutFile $zipPath
        
        Write-Host "[4/4] Extraction..." -ForegroundColor Yellow
        Expand-Archive -Path $zipPath -DestinationPath $extractPath -Force
        
        # Renommer le dossier si nécessaire
        if (Test-Path "C:\javafx-sdk-21.0.2") {
            Rename-Item -Path "C:\javafx-sdk-21.0.2" -NewName "javafx-sdk-21" -Force
        }
        
        Write-Host "✅ JavaFX installé dans $javafxPath" -ForegroundColor Green
        
        # Compiler
        Write-Host ""
        Write-Host "Compilation..." -ForegroundColor Yellow
        & javac --module-path $javafxLib --add-modules javafx.controls -d build -sourcepath src src/com/motorola6809/simulator/*.java
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host ""
            Write-Host "============================================" -ForegroundColor Green
            Write-Host "✅ INSTALLATION ET COMPILATION RÉUSSIES!" -ForegroundColor Green
            Write-Host "============================================" -ForegroundColor Green
            Write-Host ""
            Write-Host "Pour lancer l'application:" -ForegroundColor Cyan
            Write-Host "  java --module-path $javafxLib --add-modules javafx.controls -cp build com.motorola6809.simulator.Moto6809App" -ForegroundColor White
            Write-Host ""
            Write-Host "OU exécuter: run.bat" -ForegroundColor Cyan
        } else {
            Write-Host "❌ Erreur de compilation" -ForegroundColor Red
        }
        
    } catch {
        Write-Host ""
        Write-Host "❌ Erreur lors du téléchargement: $_" -ForegroundColor Red
        Write-Host ""
        Write-Host "Installation manuelle requise:" -ForegroundColor Yellow
        Write-Host "1. Télécharger: https://openjfx.io/" -ForegroundColor White
        Write-Host "2. Extraire dans C:\javafx-sdk-21" -ForegroundColor White
        Write-Host "3. Exécuter: compile.bat" -ForegroundColor White
    }
} else {
    Write-Host ""
    Write-Host "Installation manuelle:" -ForegroundColor Yellow
    Write-Host "1. Télécharger JavaFX SDK 21: https://openjfx.io/" -ForegroundColor White
    Write-Host "2. Extraire dans C:\javafx-sdk-21" -ForegroundColor White
    Write-Host "3. Exécuter: compile.bat" -ForegroundColor White
    Write-Host ""
    Write-Host "Ou installer Maven et utiliser: mvn clean compile" -ForegroundColor Cyan
}





