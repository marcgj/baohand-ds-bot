package command.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import command.CommandContext;
import command.ICommand;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class SkipCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getEvent().getChannel();
        final Member self = ctx.getEvent().getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        // Checks if the bot is in a voice channel
        if (!selfVoiceState.inVoiceChannel()) {
            ctx.getEvent().getChannel().sendMessage("El bot no esta a cap canal de veu").queue();
            return;
        }

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

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(channel.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        // Checks if there are songs in the queue
        if (audioPlayer.getPlayingTrack() == null) {
            ctx.getEvent().getChannel().sendMessage("No hi ha pista a la que saltar").queue();
            return;
        }

        musicManager.scheduler.nextTrack();
        ctx.getEvent().getChannel().sendMessageFormat("Saltant a la canço: `%s`", musicManager.audioPlayer.getPlayingTrack().getInfo().title).queue();
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getShort() {
        return "s";
    }

    @Override
    public String getHelp() {
        return """
                `!skip` salta a la seguent canço de la cua
                """;
    }
}
