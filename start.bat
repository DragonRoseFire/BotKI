@echo off
REM BotKI Starter Script for Windows
REM Dieses Script startet den Bot automatisch

echo ======================================
echo 🤖 BotKI Discord Bot Starter
echo ======================================
echo.

REM Check if .env exists
if not exist .env (
    echo ❌ FEHLER: .env Datei nicht gefunden!
    echo 📝 Bitte erstelle eine .env Datei mit:
    echo    DISCORD_TOKEN=dein_token
    echo    BOT_PREFIX=!
    echo    OWNER_ID=deine_id
    pause
    exit /b 1
)

REM Check if Server.jar exists
if not exist Server.jar (
    echo ❌ FEHLER: Server.jar nicht gefunden!
    echo 📥 Bitte stelle sicher, dass Server.jar im gleichen Ordner liegt
    pause
    exit /b 1
)

echo ✅ Konfiguration geladen
echo.
echo 🚀 Bot wird gestartet...
echo.

REM Start bot with memory limits
java -Xmx85m -jar Server.jar

pause