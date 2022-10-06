package command.commands;

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
        Query query = new Query(conn, String.format("update users set banned = false where id = %s;", user.getId()));
        return query.update();
    }

    @Override
    public boolean adminCommand() {
        return true;
    }

    @Override
    public String getName() {
        return "unban";
    }

    @Override
    public void handle(CommandContext ctx) {
        var arg = ctx.getArgs()[0];
        var user = ctx.getGuild().getMemberByTag(arg).getUser();

        if (user == null) {
            // TODO missatge de error
            return;
        }

        long id = user.getIdLong();
        

        // TODO mirar si el usuari ja esta banejat
        if(ctx.getAuthor().isBot() || Admins.adminIds.contains(id)) return;

        

        if (unBanUser(user)){
            ctx.sendChannelMessage(EmbedTemplate.generalEmbed("Restringit acces a %s".formatted(user.getName()), "Per tonto ja no tindra acces a cap comanda del bot"));
        }
        
    }
}
