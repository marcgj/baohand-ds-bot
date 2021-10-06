package command.commands.music;

import command.CommandContext;
import command.ICommand;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getEvent().getChannel();
        final Member self = ctx.getEvent().getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (selfVoiceState.inVoiceChannel()){
            System.out.println("caca");
            return;
        }

        final Member member = ctx.getEvent().getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()){
            ctx.getEvent().getChannel().sendMessage("Has de estar en una sala de veu primer").queue();
            return;
        }

        final AudioManager audioManager = ctx.getEvent().getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        audioManager.openAudioConnection(memberChannel);
        channel.sendMessage("Entrant a:" + memberChannel.getName()).queue();
    }

    @Override
    public String getName() {
        return "join";
    }
}
