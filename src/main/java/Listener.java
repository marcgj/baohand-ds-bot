import command.CommandContext;
import command.CommandManager;
import command.commands.music.QueueCommand;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import postgres.LevelingCore;
import postgres.DatabaseController;

public class Listener extends ListenerAdapter {
    private final CommandManager manager = CommandManager.getInstance();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        if (event.getAuthor().isBot() || event.isWebhookMessage()) return;

        if (event.getMessage().getContentRaw().startsWith(Bot.dotenv.get("PREFIX"))) {
            manager.handle(event);
        }

        //LevelingCore.handle(event);
    }

    // Used for queue command where it allows the user to switch between pages
    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        switch (event.getComponentId()) {
            case "forward" -> {
                CommandContext ctx = new CommandContext(event, new String[0]);
                QueueCommand queue = (QueueCommand) CommandManager.getInstance().getCommand("queue");
                queue.pag++;
                queue.handle(ctx, queue.pag);
            }
            case "backward" -> {
                CommandContext ctx = new CommandContext(event, new String[0]);
                QueueCommand queue = (QueueCommand) CommandManager.getInstance().getCommand("queue");
                queue.pag--;
                queue.handle(ctx, queue.pag);
            }
        }
    }


    // If the bot is left alone it leaves
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        final var bot = event.getGuild().getSelfMember();
        final var botChannel = bot.getVoiceState().getChannel();

        final var channel = event.getChannelLeft();

        if(!channel.equals(botChannel)) return;

        if(channel.getMembers().size() > 1) return;

        bot.getGuild().getAudioManager().closeAudioConnection();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Bot ONLINE!");
        // Once the bot is started we connect to the database
        //DatabaseController.getInstance();
    }
}
