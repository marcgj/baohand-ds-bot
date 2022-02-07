package command.commands.music;

import Utils.EmbedTemplate;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand implements ICommand {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (selfVoiceState.inVoiceChannel()) {
            ctx.sendChannelMessage(EmbedTemplate.ErrorEmbed("Error al unirse:", "El bot ja esta en un canal de veu"));
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            ctx.sendChannelMessage(EmbedTemplate.ErrorEmbed("Error al unirse:", "Primer has d'estar en una sala de veu"));
            return;
        }

        final AudioManager audioManager = ctx.getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        audioManager.openAudioConnection(memberChannel);
        channel.sendMessage(EmbedTemplate.generalEmbed("", "Entrant a: `" + memberChannel.getName() + "`")).queue();
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getHelp() {
        return """
                `!join` per fer entrar al bot a la sala
                """;
    }
}
