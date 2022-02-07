package command.commands;

import Utils.EmbedTemplate;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import postgres.methods.LevelingController;

public class RankCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        var builder = new StringBuilder();
        var ranking = LevelingController.getRanking();

        assert ranking != null;
        for (LevelingController.Triplet triplet : ranking) {
            builder.append(String.format("**%d.** %s amb **%d** missatges\n", triplet.rank, triplet.name, triplet.messageCount));
        }

        ctx.sendChannelMessage(EmbedTemplate.generalEmbed("Ranking del servidor:", builder.toString()));
    }

    @Override
    public String getName() {
        return "rank";
    }

    @Override
    public String getHelp() {
        return "WIP";
    }
}
