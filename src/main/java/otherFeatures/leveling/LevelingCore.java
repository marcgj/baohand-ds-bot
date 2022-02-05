package otherFeatures.leveling;

import Utils.EmbedTemplate;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.w3c.dom.events.Event;
import postgres.methods.LevelingController;

public class LevelingCore {
    public synchronized static void handle(GuildMessageReceivedEvent event){
        var message = event.getMessage();
        var user = event.getAuthor();

        if (LevelingController.addUser(user)) System.out.println("New user added: " + user.getName());

        if(!LevelingController.addNewMessage(message)) System.out.println("Error adding message");

        int level = LevelingController.getUserLevel(user);
        int messageCount = LevelingController.getMessageCount(user);
        int rank = LevelingController.getUserRank(user);

        if(LevelingController.nextLevel(user)){
           LevelingController.levelUp(user);
            event.getChannel().sendMessage(EmbedTemplate.levelUpEmbed(user, level, messageCount, rank)).queue();
        }
    }

}
