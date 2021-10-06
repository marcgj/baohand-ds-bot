package command.commands.music;

import command.CommandContext;
import command.ICommand;

public class LeaveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        //TODO: Fer les comprovacions corresponents
        ctx.getEvent().getGuild().getAudioManager().closeAudioConnection();
        ctx.getEvent().getChannel().sendMessage("A la verga").queue();
    }

    @Override
    public String getName() {
        return "leave";
    }
}
