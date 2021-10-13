package command.commands;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.Button;

public class HugCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        String reciver;

        User author = ctx.getAuthor();
        String[] args = ctx.getArgs();


        if (args.length != 0) {
            reciver = args[0];
        } else {
            reciver = "<@" + author.getId() + ">";
        }

        String message = "Un abruzo homosesual para: " + reciver + " :open_hands: ";
        Button boto = Button.link("https://www.youtube.com/watch?v=dQw4w9WgXcQ", "\uD83E\uDD75\uD83E\uDD75 NO CLICKIS " + author.getName() + ", ***** DE ZERO TWO \uD83E\uDD75\uD83E\uDD75");
        ctx.getChannel().sendMessage(message).setActionRow(boto).queue();
    }

    @Override
    public String getName() {
        return "hug";
    }

    @Override
    public String getHelp() {
        return """
                `!hug <gilipolles anonim>` per enviar un abraç a la teva waifu :open_hands:
                """;
    }
}
