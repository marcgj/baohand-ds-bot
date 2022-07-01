package command.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import command.CommandContext;
import command.ICommand;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.commons.io.monitor.FileAlterationListener;

public class LoopCommand implements ICommand {

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

        final String[] args = ctx.getArgs();
        boolean loop = false;
        if (args.length != 1){
            loop = musicManager.scheduler.toggleLoop();
        }else {
            if (args[0].equals("on"))
                loop = true;
            else if (args[0].equals("off"))
                loop = false;
            else
                return;
        }

        musicManager.scheduler.setLoop(loop);

        if (loop) {
            var track = audioPlayer.getPlayingTrack();
            if(track != null){
                musicManager.scheduler.setActualTrack(track);
                ctx.sendChannelMessage(String.format("Posant en bucle la canço: `%s`", track.getInfo().title));
            }
        } else {
            ctx.sendChannelMessage(String.format("A pendre per cul el bucle"));
        }
    }

    @Override
    public String getName() {
        return "loop";
    }

    @Override
    public String getHelp() {
        return """
                `!loop <on|off>` per a posar en bucle la canço actual
                """;
    }
}
