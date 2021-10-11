import command.CommandContext;
import command.CommandManager;
import command.ICommand;
import command.commands.music.QueueCommand;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Listener extends ListenerAdapter {
    private final CommandManager manager = CommandManager.getInstance();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        if (event.getAuthor().isBot() || event.isWebhookMessage()) return;

        if (event.getMessage().getContentRaw().startsWith(Bot.dotenv.get("PREFIX"))) {
            manager.handle(event);
        }
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

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Bot ONLINE!");
    }
}
