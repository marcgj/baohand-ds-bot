package command.commands;

import command.CommandContext;
import command.ICommand;

public class ShutdownCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        ctx.getChannel().sendMessage("Apagant el bot...").queue();
        ctx.getJDA().shutdown();
    }

    @Override
    public String getName() {
        return "shutdown";
    }

    @Override
    public boolean adminCommand() {
        return true;
    }
}
