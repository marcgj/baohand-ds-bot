package command;

import command.commands.HugCommand;
import command.commands.music.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {

    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        commands.add(new HugCommand());


        //Music Commands:
        commands.add(new JoinCommand());
        commands.add(new PlayCommand());
        commands.add(new QueueCommand());
        commands.add(new SkipCommand());
        commands.add(new LeaveCommand());
    }

    public void handle(GuildMessageReceivedEvent e){
        String[] tokenized = e.getMessage().getContentRaw().split(" ");

        String commandName = tokenized[0].replace("!", "");
        ICommand cmd = getCommand(commandName);

        if (cmd != null) {
            String[] args = Arrays.copyOfRange(tokenized, 1, tokenized.length);

            cmd.handle(new CommandContext(e, args));
        }

    }

    private ICommand getCommand(String s) {
        String search = s.toLowerCase();

        for (ICommand command : commands) {
            if (command.getName().equals(search)) return command;
        }

        return null;

    }


}
