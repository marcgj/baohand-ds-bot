import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import javax.security.auth.login.LoginException;

public class Bot {

    public static final Dotenv dotenv = Dotenv.load();

    private static final String TOKEN = dotenv.get("API_KEY");
    public static JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException {
        //Start the server
        jda = JDABuilder.createDefault(TOKEN,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_MESSAGES)
                .setActivity(Activity.playing(dotenv.get("STATUS"))).
                enableCache(CacheFlag.VOICE_STATE).
                build();

        //Add Event Listeners
        jda.addEventListener(new Listener());
        jda.awaitReady();
    }
}
