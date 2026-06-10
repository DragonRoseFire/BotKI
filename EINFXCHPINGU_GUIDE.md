# 🚀 BotKI auf EinfxchPingu Hosting Deployen

Dieser Guide erklärt Schritt für Schritt, wie du deinen Discord Bot auf **EinfxchPingu** hostest.

## 📋 Voraussetzungen

- ✅ Discord Bot Token (neu generiert)
- ✅ EinfxchPingu Account (kostenlos)
- ✅ Deine Discord User ID

## 🔧 Schritt 1: Bot lokal bauen (Server.jar erstellen)

### Auf deinem Computer:

```bash
# Repository clonen
git clone https://github.com/DragonRoseFire/BotKI.git
cd BotKI

# JAR bauen
mvn clean package
```

**Ergebnis:** `target/Server.jar` (ca. 80-120 MB)

> ⚠️ Falls zu groß: Verkleinert automatisch durch das Build-System

## 🌐 Schritt 2: EinfxchPingu Account erstellen

1. Gehe zu https://dash.einfxchpingu.net
2. Registriere dich (kostenlos)
3. Bestätige deine E-Mail
4. Login

## 📂 Schritt 3: Neuen Server erstellen

1. Im Dashboard: **"+ Create Server"** oder **"New"**
2. **Server Name:** `BotKI`
3. **Language:** `Java`
4. **Speicher auswählen:** Mindestens 85 MB RAM (Standard)
5. Klick **"Create"**

## 📤 Schritt 4: Server.jar hochladen

1. Klick auf deinen neuen Server
2. Gehe zum **"File Manager"** oder **"Files"** Tab
3. Upload `Server.jar` von deinem Computer
   - Dateimanager öffnen
   - Server.jar rein-draggen ODER
   - "Upload" Button + Datei wählen

4. Auch hochladen:
   - `.env` Datei mit deinen Secrets

## ⚙️ Schritt 5: Konfigurieren

### Start Command einstellen:

1. Im Server Dashboard → **"Settings"** oder **"Config"**
2. **Start Command:**
   ```
   java -jar Server.jar
   ```

3. **Speichern/Save**

### Environment Variables:

Wenn dein Host ein `.env` Datei unterstützt, erstelle eine mit:

```env
DISCORD_TOKEN=dein_bot_token_hier
BOT_PREFIX=!
DATABASE_URL=jdbc:sqlite:botki.db
DEBUG_MODE=false
OWNER_ID=deine_discord_id
```

## ▶️ Schritt 6: Bot starten

1. Gehe zu **"Console"** Tab
2. Klick **"Start Server"** / **"Start"**
3. Warte 10-20 Sekunden
4. Logs sollten zeigen:

```
✓ BotKI is online! Logged in as: BotKI#1234
✓ Bot is in X servers
```

## ✅ Bot zum Discord Server hinzufügen

1. Gehe zu https://discord.com/developers/applications
2. Wähle deine BotKI Application
3. OAuth2 → URL Generator
4. Scopes: `bot`
5. Permissions:
   - Send Messages
   - Manage Messages
   - Embed Links
   - Kick Members
   - Ban Members
   - Manage Roles

6. URL kopieren und im Browser öffnen
7. Server wählen und "Authorize" klicken

## 🎮 Bot testen

Gehe zu deinem Discord Server und tippe:

```
/ping
```

Wenn der Bot antwortet → **Alles funktioniert!** 🎉

## 🔍 Troubleshooting

### Bot startet nicht / Error in Logs

**Problem:** "Invalid token"
```
→ Token regenerieren und neu uploaden
```

**Problem:** "OutOfMemoryError"
```
→ Bot braucht zu viel RAM
→ EinfxchPingu Support kontaktieren für mehr Speicher
```

**Problem:** "Cannot find database"
```
→ `.env` Datei im Root-Verzeichnis hochladen
→ oder DATABASE_URL in Console setzen
```

### Bot ist offline nach X Zeit

**Lösung:** Automatisches Restart einrichten
1. Im EinfxchPingu Dashboard
2. "Auto-restart" aktivieren
3. oder Cronjob für regelmäßige Restarts

## 📊 Resource Limits bei EinfxchPingu

| Ressource | Limit |
|-----------|-------|
| RAM | 85 MB (kostenlos) |
| Storage | 650 MB |
| Network | 300 Mbit/s |
| Uptime | 24/7 ✓ |

**Mehr brauchen?** Support Ticket im Discord öffnen!

## 🔐 Wichtige Sicherheit

⚠️ **NIEMALS deinen Token öffentlich posten!**

- `.env` Datei im `.gitignore`
- Token in EinfxchPingu Secrets/Console speichern
- Regelmäßig Token regenerieren

## 📱 Bot updaten

Wenn du den Bot änderst:

1. Lokal ändern und testen
2. Commit und Push zu GitHub
3. JAR neu bauen: `mvn clean package`
4. Neue `Server.jar` zu EinfxchPingu hochladen
5. Bot im Dashboard neu starten

## 🆘 Support

**Probleme bei EinfxchPingu?**
- https://docs.einfxchpingu.net
- Discord: https://discord.gg/einfxchpingu

**Probleme mit BotKI?**
- GitHub Issues: https://github.com/DragonRoseFire/BotKI/issues

---

**Viel Erfolg beim Deployen! 🚀**