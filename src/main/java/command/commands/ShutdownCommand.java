package command.commands;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.User;

public class ShutdownCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        ctx.getEvent().getChannel().sendMessage("Apagant el bot...").queue();
        ctx.getEvent().getJDA().shutdown();
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
