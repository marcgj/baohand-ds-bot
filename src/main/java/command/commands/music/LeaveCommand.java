package command.commands.music;

import Utils.EmbedTemplate;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;

public class LeaveCommand implements ICommand {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        // Checks if the bot is in a voice channel
        if (!selfVoiceState.inVoiceChannel()) {
            ctx.sendChannelMessage(EmbedTemplate.ErrorEmbed("Error al sortir de la sala", "El bot no esta a cap canal de veu"));
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        // Checks if the member is in a voice channel
        if (!memberVoiceState.inVoiceChannel()) {
            ctx.sendChannelMessage(EmbedTemplate.ErrorEmbed("Error al sortir de la sala", "Has d'estar primer en una sala de veu"));
            return;
        }
        // Checks if the member is in the same voice channel
        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            ctx.sendChannelMessage(EmbedTemplate.ErrorEmbed("Error al sortir de la sala", "Has d'estar en la mateixa sala de veu que el bot"));
            return;
        }

        ctx.getAudioManager().closeAudioConnection();
        ctx.sendChannelMessage("A la verga");
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return """
                `!leave` per treure el bot del chat de veu
                """;
    }
}
