package command.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import command.CommandContext;
import command.ICommand;
import io.github.cdimascio.dotenv.Dotenv;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.concurrent.BlockingQueue;

public class QueueCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        final PlayerManager player = PlayerManager.getInstance();
        final GuildMusicManager manager = player.getMusicManager(ctx.getEvent().getGuild());
        //TODO: Fer les comprobacions de que el bot te cua, esta en un canal de veu i el mateix que el ususari

        final BlockingQueue<AudioTrack> queue = manager.scheduler.getQueue();
        final int queueSize = queue.size();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode(Dotenv.load().get("QUOTE_COLOR"))); // Canvia el color de la barra lateral


        if (queueSize == 0) {
            builder.setTitle("La cua esta buida");
        } else {
            final String twoOptions = queueSize > 1 ? "cançons" : "canço";
            final StringBuilder stringBuilder = new StringBuilder();

            builder.setTitle(String.format("Hi ha **%s** %s a la cua:\n", queue.size() , twoOptions));

            //TODO: multiples pagines amb reactions
            int i = 1;
            for (AudioTrack track : queue){
                String fieldName = String.format("**%s.** [`%s`](%s)",i++, track.getInfo().title, track.getInfo().uri);
                String descField = String.format("[%s]", timeConverter((int) track.getDuration()/1000));

                stringBuilder.append(fieldName).append("\n").append(descField).append("\n\n");

                if (i > 10) {
                    stringBuilder.append("**...**");
                    break;
                };
            }
            builder.setDescription(stringBuilder.toString());

        }
        ctx.getEvent().getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getShort() {
        return "q";
    }

    // Font: https://www.codegrepper.com/code-examples/java/java+seconds+to+hours+minutes+seconds
    private String timeConverter(int s){
        int sec = s % 60;
        int min = (s/60)%60;
        int hours = (s/60)/60;

        String strSec=(sec<10)?"0"+Integer.toString(sec):Integer.toString(sec);
        String strmin=(min<10)?"0"+Integer.toString(min):Integer.toString(min);
        String strHours=(hours<10)?"0"+Integer.toString(hours):Integer.toString(hours);

        return (strHours + ":" + strmin + ":" + strSec);
    }
    
    @Override
    public String getHelp() {
        return """
                `!queue` diu el nom de les cançons en cua amb l'ordre corresponent
                """;
    }
}
