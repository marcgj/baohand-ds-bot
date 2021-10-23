package command.commands.music;

import Utils.EmbedTemplate;
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        // Checks if there are args
        if (ctx.getArgs().length == 0) {
            ctx.sendChannelMessage(EmbedTemplate.ErrorEmbed("Error", "Has de posar algo mes despres del play...", "`/help play` per a mes ajuda"));
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();


        // Checks if  the user is in a voice channel
        if (!memberVoiceState.inVoiceChannel()) {
            ctx.sendChannelMessage(EmbedTemplate.ErrorEmbed("Error", "Entra en una sala de veu"));
            return;
        }

        // If the bot is not already in a voice channel it joins
        if (!selfVoiceState.inVoiceChannel()) {
            final AudioManager audioManager = ctx.getAudioManager();
            final VoiceChannel memberChannel = memberVoiceState.getChannel();

            audioManager.openAudioConnection(memberChannel);
        }
        // Checks if  the user is in the same voice channel as the bot
        else if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {

            ctx.sendChannelMessage(EmbedTemplate.ErrorEmbed("Error", "Has d'estar en el mateix canal de veu que el bot"));
            return;
        }

        String link = String.join(" ", ctx.getArgs());

        // Checks if the arguments form a url
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

    @Override
    public String getShort() {
        return "p";
    }

    @Override
    public String getHelp() {
        return """
                `!play <nom_canço o url>` per reproduir una canço
                """;
    }
}
