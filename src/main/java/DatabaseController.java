import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

public class DatabaseController extends Thread{

    private static DatabaseController INSTANCE;
    private Connection conn;

    // This makes a singleton so only one database controller can exits
    // TODO: Making this a singleton might cause performance issues in the future so another approach should be found
    public static DatabaseController getInstance(){
        if (INSTANCE == null){
            INSTANCE = new DatabaseController();
        }
        return INSTANCE;
    }

    public DatabaseController() {
        try {
            String url = "jdbc:postgresql://localhost/test";

            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "1234");

            conn = DriverManager.getConnection(url, props);

            System.out.println("Database is connected!");

        } catch (SQLException e) {
            throw new Error("Error connecting the database: ", e);
        }
    }

    public int getUserLevel (User user){
        long userId = user.getIdLong();
        Statement statement;
        try {
            statement = conn.createStatement();
            var result = statement.executeQuery(String.format("select level from users where discordid = %d;", userId));
            result.next();
            return result.getInt("level");
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // If in the future we need the contents of a message we can use the id and jda to get it
    public boolean addNewMessage(Message msg){
        long userId = msg.getAuthor().getIdLong();
        long messageId = msg.getIdLong();

        try{
            var statement = conn.createStatement();
            statement.executeQuery(String.format("insert into messages (messageid, userid) VALUES (%d, %d);", messageId, userId));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    // Gets the number of messages a user has so we can see if he levels up
    public int getMessageCount(User user) {
        long userId = user.getIdLong();
        Statement statement;
        try {
            statement = conn.createStatement();
            var result = statement.executeQuery(String.format("select count(*) from messages inner join users u on u.discordid = messages.userid where userid = %d;", userId));
            result.next();
            return result.getInt("count");
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void levelUp(User user){
        long userId = user.getIdLong();

        try{
            var statement = conn.createStatement();
            statement.executeQuery(String.format("update users set level = level + 1 where discordid = %d;", userId));
        }catch (Exception e){
            System.out.printf("Error leveling up user: %s", user.getName());
        }
    }

    public void levelUp(User user, int level){
        long userId = user.getIdLong();

        try{
            var statement = conn.createStatement();
            statement.executeQuery(String.format("update users set level = %d where discordid = %d;", level, userId));
        }catch (Exception e){
            System.out.printf("Error leveling up user: %s", user.getName());
        }
    }


    private final Dotenv dotenv = Dotenv.load();

    public boolean nextLevel(User user){
        int level = getUserLevel(user);
        int messageCount = getMessageCount(user);

        int calculatedLevel = (int) (Integer.parseInt(dotenv.get("RATE")) * Math.sqrt(messageCount));

        return calculatedLevel > level;
    }

    public static void main(String[] args) {
        var test = new DatabaseController();
        test.start();

    }
}
