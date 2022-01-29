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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();


        // Checks if the bot is in a voice channel
        if (!selfVoiceState.inVoiceChannel()) {
            ctx.sendChannelMessage("El bot no esta a cap canal de veu");
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        // Checks if the member is in a voice channel
        if (!memberVoiceState.inVoiceChannel()) {
            ctx.sendChannelMessage("Entra en una sala de veu");
            return;
        }
        // Checks if the member is in the same voice channel
        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            ctx.sendChannelMessage("Has de estar en el mateix canal de veu que el bot...");
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(channel.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        // Checks if there are songs in the queue
        if (musicManager.scheduler.getQueue().size() == 0) {
            ctx.sendChannelMessage("No hi ha pista a la que saltar");
            return;
        }

        final String[] args = ctx.getArgs();

        if (args.length == 0) {
            musicManager.scheduler.nextTrack();
        } else if (args.length > 1) {
            ctx.sendChannelMessage("Posa nomes un argument, per a mes ajuda `!help skip`");
            return;
        } else {
            final int pos = Integer.parseInt(args[0]);

            if (pos <= musicManager.scheduler.getQueue().size() && pos > 0) {
                musicManager.scheduler.nextTrack(pos);
            } else {
                ctx.sendChannelMessage(String.format("No hi ha cap canço a la posicio **%s** de la cua", pos));
                return;
            }
        }

        ctx.sendChannelMessage(String.format("Saltant a la canço: `%s`", audioPlayer.getPlayingTrack().getInfo().title));
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
                `!skip <pos>` salta a una canço especifica de la cua
                """;
    }
}
