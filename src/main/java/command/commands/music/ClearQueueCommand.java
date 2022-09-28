package command.commands.music;

import Utils.EmbedTemplate;
import command.CommandContext;
import command.ICommand;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;

public class ClearQueueCommand implements ICommand {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final GuildMusicManager manager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();


        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        // Checks if the member is in a voice channel
        if (!memberVoiceState.inVoiceChannel()) {
            ctx.sendChannelMessage(EmbedTemplate.ErrorEmbed("No es pot netejar la cua", "Entra primer a un canal de veu"));
            return;
        }
        // Checks if the member is in the same voice channel
        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            ctx.sendChannelMessage(EmbedTemplate.ErrorEmbed("No es pot netejar la cua", "Has d'estar al mateix canal que el bot"));
            return;
        }

        final int queueSize = manager.scheduler.getQueue().size();

        // Checks if there are songs in the queue
        if (queueSize <= 0) {
            ctx.sendChannelMessage(EmbedTemplate.ErrorEmbed("No es pot buidar la cua", "No es pot borrar la cua si no hi ha cap canço"));
        } else {
            manager.scheduler.getQueue().clear();

            final String twoOptions = queueSize > 1 ? "cançons" : "canço";
            ctx.sendChannelMessage(EmbedTemplate.generalEmbed("Netejant la cua:",
                    String.format("Borrant **%s** %s de la cua", queueSize, twoOptions)));
        }

    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getHelp() {
        return """
                `!clear` per buidar la cua
                """;
    }
}
