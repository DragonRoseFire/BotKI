package de.botki.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BotConfig {
    private static final Logger logger = LoggerFactory.getLogger(BotConfig.class);
    private static BotConfig instance;

    private final String token;
    private final String prefix;
    private final String databaseUrl;
    private final boolean debugMode;
    private final long ownerId;

    private BotConfig() {
        this.token = getEnv("DISCORD_TOKEN", null);
        this.prefix = getEnv("BOT_PREFIX", "!");
        this.databaseUrl = getEnv("DATABASE_URL", "jdbc:sqlite:botki.db");
        this.debugMode = Boolean.parseBoolean(getEnv("DEBUG_MODE", "false"));
        this.ownerId = Long.parseLong(getEnv("OWNER_ID", "0"));

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("DISCORD_TOKEN environment variable is not set!");
        }
    }

    public static BotConfig getInstance() {
        if (instance == null) {
            instance = new BotConfig();
        }
        return instance;
    }

    private String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        // Try loading from .env file
        value = loadFromEnvFile(key);
        return value != null ? value : defaultValue;
    }

    private String loadFromEnvFile(String key) {
        try {
            Path envPath = Paths.get(".env");
            if (Files.exists(envPath)) {
                for (String line : Files.readAllLines(envPath)) {
                    if (line.startsWith(key + "=")) {
                        return line.substring(key.length() + 1).trim();
                    }
                }
            }
        } catch (IOException e) {
            logger.warn("Could not read .env file", e);
        }
        return null;
    }

    public String getToken() { return token; }
    public String getPrefix() { return prefix; }
    public String getDatabaseUrl() { return databaseUrl; }
    public boolean isDebugMode() { return debugMode; }
    public long getOwnerId() { return ownerId; }
}