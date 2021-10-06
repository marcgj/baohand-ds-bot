package command.commands.music;

import command.CommandContext;
import command.ICommand;

public class LeaveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

    }

    @Override
    public String getName() {
        return "leave";
    }
}
