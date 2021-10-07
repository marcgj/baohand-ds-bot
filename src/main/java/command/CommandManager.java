package command;

import command.commands.HugCommand;
import command.commands.ShutdownCommand;
import command.commands.music.*;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.ObjectInputFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {

    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        commands.add(new HugCommand());


        //Admin commands
        commands.add(new ShutdownCommand());


        //Music Commands:
        commands.add(new JoinCommand());
        commands.add(new PlayCommand());
        commands.add(new QueueCommand());
        commands.add(new SkipCommand());
        commands.add(new LeaveCommand());
    }

    public void handle(GuildMessageReceivedEvent e) {
        String[] tokenized = e.getMessage().getContentRaw().split(" ");

        String commandName = tokenized[0].replace("!", "");
        ICommand cmd = getCommand(commandName);

        if (cmd != null) {
            String[] args = Arrays.copyOfRange(tokenized, 1, tokenized.length);

            final String userId = e.getAuthor().getId();


            if (cmd.adminCommand() && !Admins.adminIds.contains(userId)) {
                e.getChannel().sendMessage("Nomes els admins poden fer anar aquesta commanda").queue();
                return;
            }

            cmd.handle(new CommandContext(e, args));
        }else{
            e.getChannel().sendMessageFormat("La comanda **%s** no exesteix", commandName).queue();
        }

    }

    // Obtains the command whether is a short name or the long one is used
    private ICommand getCommand(String s) {
        String search = s.toLowerCase();

        if (s.length() == 1){
            for (ICommand command : commands) {
                if (command.getShort() != null){
                    if (command.getShort().equals(search)) return command;
                }
            }
        }else{
            for (ICommand command : commands) {
                if (command.getName().equals(search)) return command;
            }
        }

        return null;
    }


}
