package de.botki;

import de.botki.config.BotConfig;
import de.botki.database.DatabaseManager;
import de.botki.listeners.MessageListener;
import de.botki.listeners.SlashCommandListener;
import de.botki.music.MusicManager;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class BotKI {
    private static final Logger logger = LoggerFactory.getLogger(BotKI.class);

    public static void main(String[] args) {
        try {
            // Load configuration
            BotConfig config = BotConfig.getInstance();
            logger.info("Starting BotKI v1.0.0");
            logger.info("Prefix: {}", config.getPrefix());

            // Initialize database
            DatabaseManager.getInstance().connect();
            logger.info("Database initialized");

            // Build JDA bot
            JDABuilder builder = JDABuilder.createDefault(config.getToken());

            // Add intents
            builder.enableIntents(
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.DIRECT_MESSAGES,
                    GatewayIntent.MESSAGE_CONTENT,
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_VOICE_STATES,
                    GatewayIntent.GUILD_PRESENCES,
                    GatewayIntent.GUILD_MODERATION
            );

            // Set status
            builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
            builder.setActivity(Activity.watching("/help | BotKI v1.0.0"));

            // Add listeners
            builder.addEventListeners(
                    new MessageListener(),
                    new SlashCommandListener()
            );

            // Build and connect
            var jda = builder.build().awaitReady();
            logger.info("✓ BotKI is online! Logged in as: {}", jda.getSelfUser().getAsTag());
            logger.info("✓ Bot is in {} servers", jda.getGuilds().size());

        } catch (LoginException e) {
            logger.error("Invalid token! Check your .env file", e);
            System.exit(1);
        } catch (InterruptedException e) {
            logger.error("Bot startup interrupted", e);
            Thread.currentThread().interrupt();
            System.exit(1);
        } catch (Exception e) {
            logger.error("Fatal error during startup", e);
            System.exit(1);
        }
    }
}