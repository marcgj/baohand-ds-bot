package command.commands.music;

import command.CommandContext;
import command.ICommand;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URL;

public class PlayCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getEvent().getChannel();
        final Member self = ctx.getEvent().getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        // Checks if there are args
        if (ctx.getArgs().length == 0) {
            ctx.getEvent().getChannel().sendMessage("Potser que posis algo despres del play, no?").queue();
            return;
        }

        final Member member = ctx.getEvent().getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();


        // Checks if  the user is in a voice channel
        if (!memberVoiceState.inVoiceChannel()) {
            ctx.getEvent().getChannel().sendMessage("Entra en una sala de veu").queue();
            return;
        }

        // If the bot is not already in a voice channel it joins
        if (!selfVoiceState.inVoiceChannel()) {
            final AudioManager audioManager = ctx.getEvent().getGuild().getAudioManager();
            final VoiceChannel memberChannel = memberVoiceState.getChannel();

            audioManager.openAudioConnection(memberChannel);
        }
        // Checks if  the user is in the same voice channel as the bot
        else if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {

            ctx.getEvent().getChannel().sendMessage("Has de estar en el mateix canal de veu que el bot...").queue();
            return;
        }


        String link = String.join(" ", ctx.getArgs());


        // Chekcs if the arguments form a url
        if (!isUrl(link)) {
            link = "ytsearch:" + link;
        }

        PlayerManager.getInstance().loadAndPlay(channel, link);

    }

    private boolean isUrl(String link) {
        try {
            new URL(link).toURI();

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "play";
    }
}
