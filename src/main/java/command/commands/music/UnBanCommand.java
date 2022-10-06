package command.commands.music;

import javax.swing.Icon;

import Utils.EmbedTemplate;
import command.Admins;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.User;
import postgres.DatabaseController;
import postgres.methods.Query;

public class UnBanCommand implements ICommand {
    private boolean unBanUser(User user){
        var conn = DatabaseController.getInstance().getConn();
        Query query = new Query(conn, "update users set banned = true where id = %d;");
        return query.update();
    }

    @Override
    public boolean adminCommand() {
        return true;
    }

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public void handle(CommandContext ctx) {
        var user = ctx.getAuthor();
        
        // TODO mirar si el usuari ja esta banejat
        if(user.isBot() || Admins.adminIds.contains(user.getId())) return;

        if (unBanUser(user)){
            ctx.sendChannelMessage(EmbedTemplate.generalEmbed("%s pot tornar a fer anar el bot".formatted(user.getName()), "Pero que vigili i no toqui els ous"));
        }
        
    }
}
