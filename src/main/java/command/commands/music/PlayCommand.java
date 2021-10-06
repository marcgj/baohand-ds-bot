package command.commands.music;

import command.CommandContext;
import command.ICommand;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class PlayCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getEvent().getChannel();
        final Member self = ctx.getEvent().getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()){
            //Todo: connectar el bot al canal actual, com si es crides el join
            return;
        }

        final Member member = ctx.getEvent().getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()){
            ctx.getEvent().getChannel().sendMessage("Entra en una sala de veu").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
            ctx.getEvent().getChannel().sendMessage("Has de estar en el mateix canal de veu que el bot...").queue();
            return;
        }

        PlayerManager.getInstance().loadAndPlay(channel, "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley");

    }

    @Override
    public String getName() {
        return "play";
    }
}
