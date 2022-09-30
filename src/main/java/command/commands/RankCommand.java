package command.commands;

import Utils.EmbedTemplate;
import command.CommandContext;
import command.ICommand;
import postgres.methods.LevelingController;

public class RankCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        var builder = new StringBuilder();
        var ranking = LevelingController.getRanking();

        assert ranking != null;
        for (var lst : ranking) {
            builder.append(
                String.format("**%s.** %s amb **%s** missatges\n", 
                lst[0], lst[1], lst[2]));
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
