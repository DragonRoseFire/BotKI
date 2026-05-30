package de.botki.database;

import de.botki.config.BotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {}

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void connect() {
        try {
            String url = BotConfig.getInstance().getDatabaseUrl();
            connection = DriverManager.getConnection(url);
            logger.info("✓ Connected to database: {}", url);
            initializeTables();
        } catch (SQLException e) {
            logger.error("Failed to connect to database", e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    private void initializeTables() {
        try (Statement stmt = connection.createStatement()) {
            // User data table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "user_id BIGINT PRIMARY KEY," +
                            "guild_id BIGINT," +
                            "experience BIGINT DEFAULT 0," +
                            "level INT DEFAULT 0," +
                            "warnings INT DEFAULT 0," +
                            "joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                            ")"
            );

            // Guild settings table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS guild_settings (" +
                            "guild_id BIGINT PRIMARY KEY," +
                            "prefix VARCHAR(5) DEFAULT '!'," +
                            "modlog_channel BIGINT," +
                            "welcome_channel BIGINT," +
                            "auto_role BIGINT," +
                            "level_up_messages BOOLEAN DEFAULT true" +
                            ")"
            );

            // Warnings table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS warnings (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "user_id BIGINT," +
                            "guild_id BIGINT," +
                            "reason VARCHAR(255)," +
                            "moderator_id BIGINT," +
                            "warned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                            ")"
            );

            // Mutes table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS mutes (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "user_id BIGINT," +
                            "guild_id BIGINT," +
                            "reason VARCHAR(255)," +
                            "muted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                            "duration INT" +
                            ")"
            );

            // Custom commands table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS custom_commands (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "guild_id BIGINT," +
                            "command_name VARCHAR(50) UNIQUE," +
                            "response TEXT," +
                            "created_by BIGINT," +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                            ")"
            );

            logger.info("✓ Database tables initialized");
        } catch (SQLException e) {
            logger.error("Failed to initialize database tables", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("✓ Database connection closed");
            }
        } catch (SQLException e) {
            logger.error("Failed to close database connection", e);
        }
    }

    public void addUser(long userId, long guildId) {
        String sql = "INSERT OR IGNORE INTO users (user_id, guild_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setLong(2, guildId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to add user to database", e);
        }
    }

    public void addExperience(long userId, long guildId, int xp) {
        String sql = "UPDATE users SET experience = experience + ? WHERE user_id = ? AND guild_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, xp);
            pstmt.setLong(2, userId);
            pstmt.setLong(3, guildId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to add experience", e);
        }
    }

    public int getLevel(long userId, long guildId) {
        String sql = "SELECT level FROM users WHERE user_id = ? AND guild_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setLong(2, guildId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("level");
            }
        } catch (SQLException e) {
            logger.error("Failed to get level", e);
        }
        return 0;
    }

    public long getExperience(long userId, long guildId) {
        String sql = "SELECT experience FROM users WHERE user_id = ? AND guild_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setLong(2, guildId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("experience");
            }
        } catch (SQLException e) {
            logger.error("Failed to get experience", e);
        }
        return 0;
    }
}