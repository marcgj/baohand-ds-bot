package command.commands.music;

import command.CommandContext;
import command.ICommand;
import io.github.cdimascio.dotenv.Dotenv;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;

public class ClearQueueCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final GuildMusicManager manager = PlayerManager.getInstance().getMusicManager(ctx.getEvent().getGuild());

        final TextChannel channel = ctx.getEvent().getChannel();
        final Member self = ctx.getEvent().getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();


        final Member member = ctx.getEvent().getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        // Checks if the member is in a voice channel
        if (!memberVoiceState.inVoiceChannel()) {
            ctx.getEvent().getChannel().sendMessage("Entra en una sala de veu").queue();
            return;
        }
        // Checks if the member is in the same voice channel
        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            ctx.getEvent().getChannel().sendMessage("Has de estar en el mateix canal de veu que el bot...").queue();
            return;
        }


        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode(Dotenv.load().get("QUOTE_COLOR")));
        final int queueSize = manager.scheduler.getQueue().size();

        // Checks if there are songs in the queue
        if (queueSize <= 0) {
            builder.setTitle("La cua esta buida");
        }else {

            manager.scheduler.getQueue().clear();

            final String twoOptions = queueSize > 1 ? "cançons" : "canço";
            builder.setTitle(String.format("Borrant **%s** %s de la cua", queueSize, twoOptions));
        }

        ctx.getEvent().getChannel().sendMessage(builder.build()).queue();


    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getHelp() {
        return """
                `!clear` per a buidar la cua
                """;
    }
}
