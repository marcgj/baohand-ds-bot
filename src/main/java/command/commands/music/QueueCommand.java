package command.commands.music;

import command.CommandContext;
import command.ICommand;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;

public class QueueCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final PlayerManager player = PlayerManager.getInstance();
        final GuildMusicManager manager = player.getMusicManager(ctx.getEvent().getGuild());

        //TODO: Fer les comprobacions de que el bot te cua, esta en un canal de veu i el mateix que el ususari
        if (manager.scheduler.queued == 0) {
            ctx.getEvent().getChannel().sendMessage("La cua esta buida").queue();
        } else {
            final String twoOptions = manager.scheduler.queued > 1 ? "cançons" : "canço";

            ctx.getEvent().getChannel().sendMessage("Hi ha actualment **" + manager.scheduler.queued + "** " + twoOptions + " a la cua i son: \n" +
                    manager.scheduler).queue();
        }
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getShort() {
        return "q";
    }

    @Override
    public String getHelp() {
        return """
                `!queue` diu el nom de les cançons en cua amb l'ordre corresponent
                """;
    }
}
