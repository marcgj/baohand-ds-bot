package command.commands.music;

import command.CommandContext;
import command.ICommand;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

public class PlayCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getEvent().getChannel();
        final Member self = ctx.getEvent().getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (ctx.getArgs().length == 0){
            ctx.getEvent().getChannel().sendMessage("Potser que posis algo despres del play, no?").queue();
            return;
        }


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

        String link = String.join(" ", ctx.getArgs());

        if (!isUrl(link)) {
            link = "ytsearch:" + link;
        }

        PlayerManager.getInstance().loadAndPlay(channel, link);

    }

    private boolean isUrl(String link) {
        try{
            new URL(link).toURI();

        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "play";
    }
}
