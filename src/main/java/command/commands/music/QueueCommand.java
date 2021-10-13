package command.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import command.CommandContext;
import command.ICommand;
import io.github.cdimascio.dotenv.Dotenv;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.util.concurrent.BlockingQueue;

public class QueueCommand implements ICommand {
    public int pag = 0;

    @Override
    public void handle(CommandContext ctx) {
        pag = 0;
        handle(ctx, pag);
    }

    public void handle(CommandContext ctx, int pag) {
        final PlayerManager player = PlayerManager.getInstance();
        final GuildMusicManager manager = player.getMusicManager(ctx.getGuild());

        final BlockingQueue<AudioTrack> queue = manager.scheduler.getQueue();
        final int queueSize = queue.size();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode(Dotenv.load().get("QUOTE_COLOR"))); // Canvia el color de la barra lateral


        if (queueSize == 0) {
            builder.setTitle("La cua esta buida");
        } else {
            final String twoOptions = queueSize > 1 ? "cançons" : "canço";
            final StringBuilder stringBuilder = new StringBuilder();

            builder.setTitle(String.format("Hi ha **%s** %s a la cua:\n", queue.size(), twoOptions));

            int paginaInici = pag * 10;
            int paginaFinal = 10 + pag * 10;
            int i = 1;
            //FER RANG 0 PAG
            for (AudioTrack track : queue) {

                if (i > paginaInici && i <= paginaFinal) {
                    System.out.println(pag);
                    String fieldName = String.format("**%s.** [`%s`](%s)", i, track.getInfo().title, track.getInfo().uri);
                    String descField = String.format("[%s]", timeConverter((int) track.getDuration() / 1000));

                    stringBuilder.append(fieldName).append("\n").append(descField).append("\n\n");
                }

                if (i >= paginaFinal) {
                    stringBuilder.append(" **Pagina** ").append(pag + 1).append(" de ").append(queueSize / 10 + 1);
                    break;
                }
                i++;
            }
            builder.setDescription(stringBuilder.toString());
        }

        if (queueSize < 10) {
            ctx.getChannel().sendMessage(builder.build()).queue();
            return;
        }

        Button backward = Button.primary("backward", "<<");
        Button forward = Button.secondary("forward", ">>");

        if (pag <= 0) {
            ctx.getChannel().sendMessage(builder.build()).setActionRow(forward).queue();
        } else if (pag == queueSize / 10) {
            ctx.getChannel().sendMessage(builder.build()).setActionRow(backward).queue();
        } else {
            ctx.getButtonEvent().editMessageEmbeds(builder.build()).setActionRow(backward, forward).queue();
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

    // Font: https://www.codegrepper.com/code-examples/java/java+seconds+to+hours+minutes+seconds
    private String timeConverter(int s) {
        int sec = s % 60;
        int min = (s / 60) % 60;
        int hours = (s / 60) / 60;

        String strSec = (sec < 10) ? "0" + sec : Integer.toString(sec);
        String strMin = (min < 10) ? "0" + min : Integer.toString(min);
        String strHours = (hours < 10) ? "0" + hours : Integer.toString(hours);

        return (strHours + ":" + strMin + ":" + strSec);
    }

    @Override
    public String getHelp() {
        return """
                `!queue` llista les cançons en cua
                """;
    }
}
