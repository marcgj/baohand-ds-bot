package command.commands;

import Utils.EmbedTemplate;
import command.Admins;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.User;
import postgres.DatabaseController;
import postgres.methods.Query;

public class BanCommand implements ICommand {
    private boolean banUser(String id){
        var conn = DatabaseController.getInstance().getConn();
        Query query = new Query(conn, String.format("update users set banned = true where id = %s;", id));
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
        var arg = ctx.getArgs()[0];

        var member = ctx.getGuild().getMemberById(arg);
        //
        if (member == null) {
            // TODO missatge de error
            System.out.println(arg);
            return;
        }
        
        var id = member.getId();
        
        
        // TODO mirar si el usuari ja esta banejat
        if(ctx.getAuthor().isBot() || Admins.adminIds.contains(id)) {
            System.out.println("Trying to ban admin");
            return;
        }
        

        if (banUser(id)){
            ctx.sendChannelMessage(EmbedTemplate.generalEmbed("Restringit acces a %s".formatted(member.getNickname()), "Per tonto ja no tindra acces a cap comanda del bot"));
            Admins.bannedIds.add(id);
        }
    }
    
}
