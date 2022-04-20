package otherFeatures.antispam;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.requests.Route;

import java.sql.Time;
import java.util.Map;

public class SpamThread extends Thread{
    private Map<User, Time> lastMessageMap;


    public void registerMessage(Message message){
        return;
    }

    @Override
    public void run() {

    }
}
