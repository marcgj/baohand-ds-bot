package command.commands;

import command.CommandContext;
import command.CommandManager;
import command.ICommand;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class HelpCommand implements ICommand
{
    @Override
    public void handle(CommandContext ctx) {

        final CommandManager manager = CommandManager.getInstance();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode(Dotenv.load().get("QUOTE_COLOR")));

        if(ctx.getArgs().length == 0){


            builder.setTitle( " **Les comandes disponibles son:**\n\n");
            for(ICommand cmd : manager.getCommands()){
                String line = String.format("`!%s`\n\n", cmd.getName() );
                builder.appendDescription(line);

            }
            builder.setFooter("Per mes informació escriu : !help <comanda>");

        }else{
            final String commandName = ctx.getArgs()[0];
            final ICommand cmd = manager.getCommand(commandName);
            builder.setTitle("Us de la comanda:");
            builder.setDescription(cmd.getHelp());
        }

        ctx.getEvent().getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return """
                `!help` per obtenir tots els comandos disponibles 
                `!help <comanda>` per obtenir mes informacio de la comanda
                """;
    }
}
