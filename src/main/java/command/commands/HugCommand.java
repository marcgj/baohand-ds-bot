package command.commands;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class HugCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        String reciver;

        GuildMessageReceivedEvent e = ctx.getEvent();
        User author = e.getAuthor();
        String[] args = ctx.getArgs();


        if (args.length != 0){
            reciver = args[0];
        }else{
            reciver = "<@" + author.getId() + ">";
        }

        String message = "Un abruzo homosesual para: " + reciver;

        e.getChannel().sendMessage(message).queue();
    }

    @Override
    public String getName() {
        return "hug";
    }

}
