package command.commands;

import command.CommandContext;
import command.CommandManager;
import command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class HelpCommand implements ICommand
{
    @Override
    public void handle(CommandContext ctx) {

        final CommandManager manager = CommandManager.getInstance();

        if(ctx.getArgs().length == 0){

            StringBuilder builder = new StringBuilder();
            builder.append( ">>> Les comandes disponibles son:\n");
            for(ICommand cmd : manager.getCommands()){
                String line = String.format("`!%s`\n\n", cmd.getName() );
                builder.append(line);

            }
            ctx.getEvent().getChannel().sendMessage(builder.toString()).queue();
        }else{
            final String commandName = ctx.getArgs()[0];
            final ICommand cmd = manager.getCommand(commandName);
            ctx.getEvent().getChannel().sendMessage(cmd.getHelp()).queue();
        }
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return """
                **Us de la comanda:**
                `!help` per obtenir tots els comandos disponibles
                `!help <comanda>` per obtenir mes informacio de la comanda
                """;
    }
}
