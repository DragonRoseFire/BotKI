package de.botki.listeners;

import de.botki.config.BotConfig;
import de.botki.database.DatabaseManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageListener extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Ignore bot messages and DMs
        if (event.getAuthor().isBot() || !event.isFromGuild()) {
            return;
        }

        String message = event.getMessage().getContentRaw();
        String prefix = BotConfig.getInstance().getPrefix();

        // Add XP to user
        DatabaseManager.getInstance().addUser(
                event.getAuthor().getIdLong(),
                event.getGuild().getIdLong()
        );
        DatabaseManager.getInstance().addExperience(
                event.getAuthor().getIdLong(),
                event.getGuild().getIdLong(),
                10 + (int) (Math.random() * 40)
        );

        // Check for command prefix
        if (!message.startsWith(prefix)) {
            return;
        }

        String[] args = message.substring(prefix.length()).split("\\s+");
        String command = args[0].toLowerCase();

        // Handle prefix commands
        switch (command) {
            case "ping" -> handlePing(event);
            case "help" -> handleHelp(event);
            case "profile" -> handleProfile(event, args);
            default -> {
                if (BotConfig.getInstance().isDebugMode()) {
                    logger.debug("Unknown command: {}", command);
                }
            }
        }
    }

    private void handlePing(MessageReceivedEvent event) {
        long ping = event.getJDA().getGatewayPing();
        event.getChannel().sendMessage(
                "🏓 Pong! Gateway ping: " + ping + "ms"
        ).queue();
    }

    private void handleHelp(MessageReceivedEvent event) {
        String prefix = BotConfig.getInstance().getPrefix();
        String helpMessage = "```" +
                "\n=== BotKI Help ===";
        helpMessage += "\n\n📋 Prefix Commands:";
        helpMessage += "\n" + prefix + "ping - Check bot latency";
        helpMessage += "\n" + prefix + "help - Show this message";
        helpMessage += "\n" + prefix + "profile [@user] - Show user profile";
        helpMessage += "\n" + prefix + "rank [@user] - Show user rank";
        helpMessage += "\n" + prefix + "leaderboard - Show server leaderboard";
        helpMessage += "\n\n🔧 Moderation Commands:";
        helpMessage += "\n" + prefix + "warn [@user] [reason] - Warn user";
        helpMessage += "\n" + prefix + "mute [@user] [duration] [reason] - Mute user";
        helpMessage += "\n" + prefix + "unmute [@user] - Unmute user";
        helpMessage += "\n" + prefix + "kick [@user] [reason] - Kick user";
        helpMessage += "\n" + prefix + "ban [@user] [reason] - Ban user";
        helpMessage += "\n" + prefix + "warnings [@user] - Show user warnings";
        helpMessage += "\n\n🎵 Music Commands:";
        helpMessage += "\n" + prefix + "play [song] - Play a song";
        helpMessage += "\n" + prefix + "stop - Stop music";
        helpMessage += "\n" + prefix + "pause - Pause music";
        helpMessage += "\n" + prefix + "resume - Resume music";
        helpMessage += "\n" + prefix + "queue - Show queue";
        helpMessage += "\n\n🎮 Fun Commands:";
        helpMessage += "\n" + prefix + "roll - Roll a dice";
        helpMessage += "\n" + prefix + "flip - Flip a coin";
        helpMessage += "\n" + prefix + "8ball [question] - 8ball game";
        helpMessage += "\n" + prefix + "say [message] - Make bot say something";
        helpMessage += "\n\n⚙️ Admin Commands:";
        helpMessage += "\n" + prefix + "setprefix [prefix] - Change prefix";
        helpMessage += "\n" + prefix + "announce [channel] [message] - Announce message";
        helpMessage += "\n```";

        event.getChannel().sendMessage(helpMessage).queue();
    }

    private void handleProfile(MessageReceivedEvent event, String[] args) {
        long userId = event.getAuthor().getIdLong();
        if (event.getMessage().getMentions().getUsers().size() > 0) {
            userId = event.getMessage().getMentions().getUsers().get(0).getIdLong();
        }

        DatabaseManager db = DatabaseManager.getInstance();
        long experience = db.getExperience(userId, event.getGuild().getIdLong());
        int level = db.getLevel(userId, event.getGuild().getIdLong());
        long nextLevelXp = (long) (level + 1) * 1000;

        String embed = String.format(
                "```%s\n" +
                "User Profile\n" +
                "XP: %d / %d\n" +
                "Level: %d\n" +
                "```",
                "═".repeat(30),
                experience % nextLevelXp,
                nextLevelXp,
                level
        );

        event.getChannel().sendMessage(embed).queue();
    }
}