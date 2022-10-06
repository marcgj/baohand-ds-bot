package command.commands.music;

import Utils.EmbedTemplate;
import command.Admins;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.User;
import postgres.DatabaseController;
import postgres.methods.Query;

public class BanCommand implements ICommand {

    private boolean banUser(User user){
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

        if (banUser(user)){
            ctx.sendChannelMessage(EmbedTemplate.generalEmbed("Restringit acces a %s".formatted(user.getName()), "Per tonto ja no tindra acces a cap comanda del bot"));
        }
        
    }
    
}
