# 🤖 BotKI - Discord Bot

A comprehensive, fully-featured Discord Bot written in Java with support for moderation, music, leveling, fun commands, and more. Designed to be easily deployed and configured for public servers.

## ✨ Features

### 📊 Leveling System
- User experience and level tracking
- Leaderboards
- Level-up notifications
- Per-guild user statistics

### 🔧 Moderation Commands
- Warn users
- Mute/Unmute users
- Kick users
- Ban users
- Warning history
- Automated moderation logging

### 🎵 Music System (Lavaplayer)
- Play songs from various sources
- Queue management
- Pause/Resume/Stop
- Volume control
- Playlist support

### 🎮 Fun Commands
- Dice rolling
- Coin flip
- 8ball game
- Random jokes
- And more!

### ⚙️ Admin Features
- Custom prefix per guild
- Customizable welcome messages
- Auto-role assignment
- Guild-specific settings
- Custom command creation

### 💾 Database
- SQLite (default) or PostgreSQL
- User data persistence
- Guild settings storage
- Warning and mute history

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.9+
- Discord Bot Token

### Local Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/DragonRoseFire/BotKI.git
   cd BotKI
   ```

2. **Create `.env` file**
   ```bash
   cp .env.example .env
   ```

3. **Configure your token**
   Edit `.env` and add your Discord bot token:
   ```
   DISCORD_TOKEN=your_token_here
   OWNER_ID=your_discord_id
   ```

4. **Build the project**
   ```bash
   mvn clean package
   ```

5. **Run the bot**
   ```bash
   java -jar target/BotKI.jar
   ```

## 🐳 Docker Deployment

### Docker Setup

1. **Create `.env` file** with your credentials

2. **Build Docker image**
   ```bash
   docker build -t botki:latest .
   ```

3. **Run container**
   ```bash
   docker run -d \
     --name botki \
     --env-file .env \
     -v botki-data:/app/data \
     botki:latest
   ```

### Docker Compose (Recommended)

```bash
docker-compose up -d
```

## ☁️ Cloud Deployment

### Railway
1. Go to [railway.app](https://railway.app)
2. Create new project
3. Select "Deploy from GitHub"
4. Connect your repository
5. Add `DISCORD_TOKEN` to environment variables
6. Deploy!

### Render
1. Go to [render.com](https://render.com)
2. Create new "Web Service"
3. Connect GitHub repository
4. Set build command: `mvn clean package`
5. Set start command: `java -jar target/BotKI.jar`
6. Add `DISCORD_TOKEN` environment variable
7. Deploy!

### Heroku (with Procfile)
1. Create `Procfile` in root:
   ```
   worker: java -jar target/BotKI.jar
   ```
2. Push to Heroku:
   ```bash
   git push heroku main
   ```

## 📝 Configuration

Edit `.env` file to customize:

```env
# Bot token from Discord Developer Portal
DISCORD_TOKEN=your_token

# Command prefix (default: !)
BOT_PREFIX=!

# Database URL
DATABASE_URL=jdbc:sqlite:botki.db

# Your Discord ID (for owner commands)
OWNER_ID=123456789

# Debug mode
DEBUG_MODE=false
```

## 📖 Commands

### Prefix Commands (`!command`)
- `!ping` - Check bot latency
- `!help` - Show help message
- `!profile [@user]` - Show user profile
- `!rank [@user]` - Show user rank
- `!leaderboard` - Show server leaderboard

### Slash Commands (`/command`)
- `/ping` - Check bot latency
- `/help` - Show help
- `/profile [@user]` - Show profile
- `/rank [@user]` - Show rank
- `/leaderboard` - Show leaderboard
- `/warn <user> [reason]` - Warn user
- `/mute <user> [duration] [reason]` - Mute user
- `/unmute <user>` - Unmute user
- `/kick <user> [reason]` - Kick user
- `/ban <user> [reason]` - Ban user
- `/warnings [@user]` - Show warnings
- `/roll [sides]` - Roll dice
- `/flip` - Flip coin
- `/8ball <question>` - Ask 8ball
- `/say <message>` - Echo message
- `/botinfo` - Bot information

## 🛠️ Development

### Project Structure
```
BotKI/
├── src/main/java/de/botki/
│   ├── BotKI.java              # Main entry point
│   ├── config/                 # Configuration management
│   ├── database/               # Database operations
│   ├── listeners/              # Event listeners
│   └── music/                  # Music system
├── pom.xml                     # Maven configuration
├── Dockerfile                  # Docker build
├── docker-compose.yml          # Docker compose
└── README.md                   # This file
```

### Building from Source
```bash
mvn clean compile
mvn test
mvn package
```

## 📦 Dependencies
- **JDA 5.0.0** - Java Discord API
- **Lavaplayer 1.4.1** - Music playback
- **SQLite JDBC** - Database
- **SLF4J + Logback** - Logging
- **GSON** - JSON processing

## 🔐 Security
- Never commit your `.env` file
- Keep your bot token secret
- Use environment variables for all secrets
- Regularly update dependencies

## 📄 License
MIT License - See LICENSE file for details

## 🤝 Contributing
Feel free to submit issues and enhancement requests!

## 📧 Support
For issues and questions, please open a GitHub issue.

---

**Made with ❤️ by DragonRoseFire**