package command;

import command.commands.BanCommand;
import command.commands.HelpCommand;
import command.commands.HugCommand;
import command.commands.RankCommand;
import command.commands.ShutdownCommand;
import command.commands.UnBanCommand;
import command.commands.music.*;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {

    private static CommandManager INSTANCE;
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        commands.add(new HugCommand());
        commands.add(new HelpCommand());

        //Admin commands
        commands.add(new ShutdownCommand());
        commands.add(new BanCommand());
        commands.add(new UnBanCommand());


        //Music Commands:
        commands.add(new JoinCommand());
        commands.add(new PlayCommand());
        commands.add(new QueueCommand());
        commands.add(new SkipCommand());
        commands.add(new LeaveCommand());
        commands.add(new ClearQueueCommand());
        commands.add(new LoopCommand());

        // Leveling commands
        commands.add(new RankCommand());

    }

    public static CommandManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommandManager();
        }

        return INSTANCE;
    }

    public void handle(GuildMessageReceivedEvent e) {        
        String[] tokenized = e.getMessage().getContentRaw().split(" ");

        String commandName = tokenized[0].replace(Dotenv.load().get("PREFIX"), "");
        ICommand cmd = getCommand(commandName);

        if (cmd != null) {
            String[] args = Arrays.copyOfRange(tokenized, 1, tokenized.length);

            final String userId = e.getAuthor().getId();

            if (Admins.bannedIds.contains(userId)) return; //TODO enviar missatge humiliant

            if (cmd.adminCommand() && !Admins.adminIds.contains(userId)) {
                e.getChannel().sendMessage("Nomes els admins poden fer anar aquesta commanda").queue();
                return;
            }

            cmd.handle(new CommandContext(e, args));
        } else {
            e.getChannel().sendMessageFormat("La comanda **%s** no exesteix", commandName).queue();
        }
    }

    // Obtains the command whether is a short name or the long one is used
    public ICommand getCommand(String s) {
        String search = s.toLowerCase();

        if (s.length() == 1) {
            for (ICommand command : commands) {
                if (command.getShort() != null) {
                    if (command.getShort().equals(search)) return command;
                }
            }
        } else {
            for (ICommand command : commands) {
                if (command.getName().equals(search)) return command;
            }
        }
        return null;
    }

    public List<ICommand> getCommands() {
        return commands;
    }
}
