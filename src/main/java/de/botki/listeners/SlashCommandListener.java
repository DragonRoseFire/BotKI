package de.botki.listeners;

import de.botki.database.DatabaseManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.session.ReadyEvent;

public class SlashCommandListener extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SlashCommandListener.class);

    @Override
    public void onReady(ReadyEvent event) {
        // Register slash commands
        CommandListUpdateAction commands = event.getJDA().updateCommands();

        // Moderation commands
        commands.addCommands(
                Commands.slash("warn", "Warn a user")
                        .addOption(OptionType.USER, "user", "User to warn", true)
                        .addOption(OptionType.STRING, "reason", "Reason for warning", false),

                Commands.slash("mute", "Mute a user")
                        .addOption(OptionType.USER, "user", "User to mute", true)
                        .addOption(OptionType.INTEGER, "duration", "Duration in minutes", false)
                        .addOption(OptionType.STRING, "reason", "Reason for mute", false),

                Commands.slash("unmute", "Unmute a user")
                        .addOption(OptionType.USER, "user", "User to unmute", true),

                Commands.slash("kick", "Kick a user")
                        .addOption(OptionType.USER, "user", "User to kick", true)
                        .addOption(OptionType.STRING, "reason", "Reason for kick", false),

                Commands.slash("ban", "Ban a user")
                        .addOption(OptionType.USER, "user", "User to ban", true)
                        .addOption(OptionType.STRING, "reason", "Reason for ban", false),

                Commands.slash("warnings", "Check user warnings")
                        .addOption(OptionType.USER, "user", "User to check", false),

                // Profile commands
                Commands.slash("profile", "Show user profile")
                        .addOption(OptionType.USER, "user", "User profile to show", false),

                Commands.slash("rank", "Show user rank")
                        .addOption(OptionType.USER, "user", "User rank to show", false),

                Commands.slash("leaderboard", "Show server leaderboard"),

                // Fun commands
                Commands.slash("roll", "Roll a dice")
                        .addOption(OptionType.INTEGER, "sides", "Number of sides", false),

                Commands.slash("flip", "Flip a coin"),

                Commands.slash("8ball", "Ask the 8ball")
                        .addOption(OptionType.STRING, "question", "Your question", true),

                Commands.slash("say", "Make bot say something")
                        .addOption(OptionType.STRING, "message", "Message to say", true),

                // Info commands
                Commands.slash("ping", "Check bot ping"),
                Commands.slash("help", "Show help message"),
                Commands.slash("botinfo", "Show bot information")
        );

        commands.queue();
        logger.info("✓ Slash commands registered");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "ping" -> handlePing(event);
            case "help" -> handleHelp(event);
            case "profile" -> handleProfile(event);
            case "rank" -> handleRank(event);
            case "leaderboard" -> handleLeaderboard(event);
            case "roll" -> handleRoll(event);
            case "flip" -> handleFlip(event);
            case "8ball" -> handle8ball(event);
            case "say" -> handleSay(event);
            case "botinfo" -> handleBotInfo(event);
            case "warn" -> handleWarn(event);
            case "mute" -> handleMute(event);
            case "unmute" -> handleUnmute(event);
            case "kick" -> handleKick(event);
            case "ban" -> handleBan(event);
            case "warnings" -> handleWarnings(event);
            default -> event.reply("Unknown command!").setEphemeral(true).queue();
        }
    }

    private void handlePing(SlashCommandInteractionEvent event) {
        long ping = event.getJDA().getGatewayPing();
        event.reply("🏓 Pong! Gateway ping: " + ping + "ms").queue();
    }

    private void handleHelp(SlashCommandInteractionEvent event) {
        String help = "```" +
                "\n=== BotKI Help ===";
        help += "\n\n📋 Info Commands:";
        help += "\n/ping - Check bot latency";
        help += "\n/help - Show this message";
        help += "\n/botinfo - Show bot information";
        help += "\n\n👤 Profile Commands:";
        help += "\n/profile [@user] - Show user profile";
        help += "\n/rank [@user] - Show user rank";
        help += "\n/leaderboard - Show server leaderboard";
        help += "\n\n🔧 Moderation:";
        help += "\n/warn <user> [reason] - Warn user";
        help += "\n/mute <user> [duration] [reason] - Mute user";
        help += "\n/unmute <user> - Unmute user";
        help += "\n/kick <user> [reason] - Kick user";
        help += "\n/ban <user> [reason] - Ban user";
        help += "\n/warnings [@user] - Show warnings";
        help += "\n\n🎮 Fun Commands:";
        help += "\n/roll [sides] - Roll a dice";
        help += "\n/flip - Flip a coin";
        help += "\n/8ball <question> - Ask the 8ball";
        help += "\n/say <message> - Make bot say something";
        help += "\n```";

        event.reply(help).queue();
    }

    private void handleProfile(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        long userId = event.getUser().getIdLong();
        if (event.getOption("user") != null) {
            userId = event.getOption("user").getAsUser().getIdLong();
        }

        DatabaseManager db = DatabaseManager.getInstance();
        long experience = db.getExperience(userId, event.getGuild().getIdLong());
        int level = db.getLevel(userId, event.getGuild().getIdLong());
        long nextLevelXp = (long) (level + 1) * 1000;
        long currentXp = experience % nextLevelXp;

        String profile = String.format(
                "```\n" +
                "═══════════════════════════════\n" +
                "User Profile\n" +
                "═══════════════════════════════\n" +
                "XP: %d / %d\n" +
                "Level: %d\n" +
                "Progress: [" + "█".repeat((int) (currentXp / (nextLevelXp / 10))) + "░".repeat(10 - (int) (currentXp / (nextLevelXp / 10))) + "]\n" +
                "═══════════════════════════════\n" +
                "```",
                currentXp, nextLevelXp, level
        );

        event.getHook().sendMessage(profile).queue();
    }

    private void handleRank(SlashCommandInteractionEvent event) {
        event.reply("Rank command functionality to be implemented.").queue();
    }

    private void handleLeaderboard(SlashCommandInteractionEvent event) {
        event.reply("Leaderboard functionality to be implemented.").queue();
    }

    private void handleRoll(SlashCommandInteractionEvent event) {
        int sides = event.getOption("sides") != null ? (int) event.getOption("sides").getAsLong() : 6;
        int roll = (int) (Math.random() * sides) + 1;
        event.reply("🎲 You rolled a **" + roll + "** (1-" + sides + ")").queue();
    }

    private void handleFlip(SlashCommandInteractionEvent event) {
        boolean heads = Math.random() < 0.5;
        event.reply("🪙 You got **" + (heads ? "Heads" : "Tails") + "**").queue();
    }

    private void handle8ball(SlashCommandInteractionEvent event) {
        String[] responses = {
                "Yes", "No", "Maybe", "Ask again later", "Definitely",
                "Don't count on it", "Outlook good", "Very doubtful"
        };
        String answer = responses[(int) (Math.random() * responses.length)];
        event.reply("🔮 The 8ball says: **" + answer + "**").queue();
    }

    private void handleSay(SlashCommandInteractionEvent event) {
        String message = event.getOption("message").getAsString();
        event.reply(message).queue();
    }

    private void handleBotInfo(SlashCommandInteractionEvent event) {
        String info = "```" +
                "\n╔══════════════════════════════╗" +
                "\n║       BotKI Information       ║" +
                "\n╠══════════════════════════════╣" +
                "\n║ Version: 1.0.0               ║" +
                "\n║ Framework: JDA 5.0.0          ║" +
                "\n║ Language: Java 17             ║" +
                "\n║ Servers: " + String.format("%-19d", event.getJDA().getGuilds().size()) + "║" +
                "\n║ Users: " + String.format("%-20d", event.getJDA().getUsers().size()) + "║" +
                "\n╚══════════════════════════════╝" +
                "\n```";
        event.reply(info).queue();
    }

    private void handleWarn(SlashCommandInteractionEvent event) {
        event.reply("Warn command functionality to be implemented by admin.").setEphemeral(true).queue();
    }

    private void handleMute(SlashCommandInteractionEvent event) {
        event.reply("Mute command functionality to be implemented by admin.").setEphemeral(true).queue();
    }

    private void handleUnmute(SlashCommandInteractionEvent event) {
        event.reply("Unmute command functionality to be implemented by admin.").setEphemeral(true).queue();
    }

    private void handleKick(SlashCommandInteractionEvent event) {
        event.reply("Kick command functionality to be implemented by admin.").setEphemeral(true).queue();
    }

    private void handleBan(SlashCommandInteractionEvent event) {
        event.reply("Ban command functionality to be implemented by admin.").setEphemeral(true).queue();
    }

    private void handleWarnings(SlashCommandInteractionEvent event) {
        event.reply("Warnings command functionality to be implemented.").setEphemeral(true).queue();
    }
}