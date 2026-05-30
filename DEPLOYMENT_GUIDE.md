# 🚀 BotKI Deployment Guide

## Overview
This guide covers all deployment options for running BotKI continuously in the cloud.

## Option 1: Railway (Recommended - Free Tier Available)

### Step 1: Create Railway Account
1. Go to https://railway.app
2. Sign up with GitHub
3. Create a new project

### Step 2: Connect Repository
1. Click "Create" → "Deploy from GitHub Repo"
2. Select your `BotKI` repository
3. Authorize Railway to access your GitHub

### Step 3: Configure Environment
1. In Railway dashboard, go to your project
2. Click "Variables"
3. Add the following variables:
   ```
   DISCORD_TOKEN=your_bot_token
   BOT_PREFIX=!
   DATABASE_URL=jdbc:sqlite:botki.db
   DEBUG_MODE=false
   OWNER_ID=your_discord_id
   ```
4. Click "Save"

### Step 4: Configure Build Settings
1. Go to "Settings" → "Build"
2. Build command: Leave default (Maven will be detected)
3. Start command: `java -jar target/BotKI.jar`

### Step 5: Deploy
1. The deployment starts automatically
2. Check logs for errors
3. Bot should come online in Discord!

## Option 2: Render (Generous Free Tier)

### Step 1: Create Render Account
1. Go to https://render.com
2. Sign up with GitHub

### Step 2: Create Service
1. Click "+ New"
2. Select "Web Service"
3. Connect your GitHub repository

### Step 3: Configure Service
```
Name: botki
Environment: Docker
Build Command: docker build -t botki .
Start Command: java -jar target/BotKI.jar
```

### Step 4: Add Environment Variables
Go to "Environment" and add:
```
DISCORD_TOKEN=your_token
BOT_PREFIX=!
DATABASE_URL=jdbc:sqlite:botki.db
DEBUG_MODE=false
OWNER_ID=your_id
```

### Step 5: Deploy
1. Click "Create Web Service"
2. Render will build and deploy automatically
3. Check logs to confirm bot is running

## Option 3: Heroku (Paid - $7/month minimum)

### Step 1: Create Heroku Account
1. Go to https://heroku.com
2. Create account
3. Install Heroku CLI: https://devcenter.heroku.com/articles/heroku-cli

### Step 2: Create Procfile
Create `Procfile` in project root:
```
worker: java -jar target/BotKI.jar
```

### Step 3: Deploy
```bash
# Install Heroku CLI
# Login
heroku login

# Create Heroku app
heroku create botki-yourname

# Add config variables
heroku config:set DISCORD_TOKEN=your_token
heroku config:set BOT_PREFIX=!
heroku config:set OWNER_ID=your_id

# Deploy
git push heroku main

# View logs
heroku logs --tail
```

## Option 4: Docker on VPS (Full Control)

### Step 1: Get a VPS
Recommended providers:
- DigitalOcean ($5/month)
- Linode ($5/month)
- AWS EC2 (free tier)
- Oracle Cloud (always free tier)

### Step 2: Install Docker
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install docker.io docker-compose -y
sudo usermod -aG docker $USER
```

### Step 3: Deploy Bot
```bash
# Clone repository
git clone https://github.com/DragonRoseFire/BotKI.git
cd BotKI

# Create .env file
cp .env.example .env
# Edit .env with your token
nano .env

# Start with docker-compose
sudo docker-compose up -d

# View logs
sudo docker-compose logs -f botki

# Stop bot
sudo docker-compose down
```

### Step 4: Set Auto-Restart
The docker-compose already has `restart: unless-stopped` configured.
But for extra safety, add to crontab:
```bash
crontab -e
# Add: 0 * * * * cd /path/to/BotKI && docker-compose up -d
```

## Option 5: Replit (Learning/Testing)

### Step 1: Fork on GitHub
Fork the repository to your GitHub account

### Step 2: Import to Replit
1. Go to https://replit.com
2. Click "Import from GitHub"
3. Paste your forked repository URL

### Step 3: Configure
1. Create `.env` file with your token
2. Set run command: `mvn clean compile exec:java -Dexec.mainClass="de.botki.BotKI"`

### Step 4: Keep Alive
To prevent Replit from sleeping:
1. Set up Replit monitoring with: https://uptimerobot.com
2. Point to your Replit URL
3. This will ping your bot every 5 minutes

## Monitoring & Logs

### Railway
- Logs automatically visible in dashboard
- Set up alerts in settings

### Render
- Logs in "Logs" tab
- Automatic email alerts for crashes

### Docker VPS
```bash
# View logs
docker-compose logs -f botki

# Follow specific number of lines
docker-compose logs -f --tail=50 botki
```

## Keep-Alive Strategies

### UptimeRobot (Free)
1. Go to https://uptimerobot.com
2. Create monitor for your bot URL
3. Set interval to 5 minutes
4. This prevents the service from sleeping

### Discord Webhook Monitor
Set up a task to ping your bot every 30 minutes:
```bash
# In crontab
*/30 * * * * curl -X GET https://your-bot-url/health
```

## Troubleshooting

### Bot Won't Start
1. Check `DISCORD_TOKEN` is correct
2. Verify bot has required intents enabled
3. Check logs for specific errors
4. Ensure Java 17+ is available

### Bot Goes Offline
1. Check for crashes in logs
2. Verify internet connectivity
3. Check Discord API status
4. Monitor service resource usage

### High CPU/Memory Usage
1. Reduce logging level (set DEBUG_MODE=false)
2. Optimize database queries
3. Monitor connected guilds

## Cost Comparison

| Service | Cost | Uptime | Build Time |
|---------|------|--------|------------|
| Railway | $5/month | 99.9% | ~2 min |
| Render | $7/month | 99.9% | ~3 min |
| Heroku | $7/month | 99.9% | ~3 min |
| DigitalOcean | $5/month | 99.99% | Manual |
| Replit | Free | 90% | ~2 min |

## Next Steps

1. Choose your preferred hosting
2. Follow the setup guide for your choice
3. Deploy the bot
4. Invite bot to your Discord server
5. Configure prefix and settings
6. Start using commands!

## Getting Your Discord Bot Token

1. Go to https://discord.com/developers/applications
2. Click "New Application"
3. Name it "BotKI"
4. Go to "Bot" section
5. Click "Add Bot"
6. Under TOKEN, click "Copy"
7. Paste into your `.env` file

## Inviting Bot to Server

1. In Developer Portal → OAuth2 → URL Generator
2. Select scopes: `bot`
3. Select permissions needed:
   - Send Messages
   - Embed Links
   - Manage Messages
   - Kick Members
   - Ban Members
   - Manage Roles (for muting)
4. Copy generated URL
5. Paste in browser and select server

---

**Your bot is now online 24/7! 🎉**