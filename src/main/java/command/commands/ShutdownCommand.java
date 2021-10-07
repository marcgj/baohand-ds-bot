package command.commands;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ShutdownCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final User user = ctx.getEvent().getAuthor();

        //Checks if the user is Takotero
        if(!user.getId().equals("230029852831383553") ){
            ctx.getEvent().getChannel().sendMessage("Nomes el <@230029852831383553> pot fer anar aquesta commanda").queue();
            return;
        }

        ctx.getEvent().getChannel().sendMessage("Apagant el bot...").queue();
        ctx.getEvent().getJDA().shutdown();
    }

    @Override
    public String getName() {
        return "shutdown";
    }
}
