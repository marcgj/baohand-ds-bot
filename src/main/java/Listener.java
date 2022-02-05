import command.CommandContext;
import command.CommandManager;
import command.commands.music.QueueCommand;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import otherFeatures.leveling.LevelingCore;
import postgres.DatabaseController;

public class Listener extends ListenerAdapter {
    private final CommandManager manager = CommandManager.getInstance();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        if (event.getAuthor().isBot() || event.isWebhookMessage()) return;

        if (event.getMessage().getContentRaw().startsWith(Bot.dotenv.get("PREFIX"))) {
            manager.handle(event);
        }

        LevelingCore.handle(event);
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {


        switch (event.getComponentId()) {
            case "forward" -> {
                CommandContext ctx = new CommandContext(event, new String[0]);
                QueueCommand cua = (QueueCommand) CommandManager.getInstance().getCommand("queue");
                cua.pag++;
                cua.handle(ctx, cua.pag);
            }
            case "backward" -> {
                CommandContext ctx = new CommandContext(event, new String[0]);
                QueueCommand cua = (QueueCommand) CommandManager.getInstance().getCommand("queue");
                cua.pag--;
                cua.handle(ctx, cua.pag);
            }
        }
    }

    // Auto disconnect if empty
    /*
    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        var self = event.getGuild().getSelfMember();
        var selfChannel = self.getVoiceState().getChannel();

        if (selfChannel == null) return;

        var channel = event.getChannelLeft();

        if (!selfChannel.equals(channel)) return;

        if (selfChannel.getMembers().size() > 1) return;

        event.getGuild().getAudioManager().closeAudioConnection();
    }
    */

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Bot ONLINE!");

        // Once the bot is started we connect to the database
        DatabaseController.getInstance();
    }
}
