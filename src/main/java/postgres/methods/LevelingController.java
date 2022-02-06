package postgres.methods;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import postgres.DatabaseController;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class LevelingController {

    private static final Connection conn = DatabaseController.getInstance().getConn();


    public static boolean addUser (User user){
        long userId = user.getIdLong();
        String userName = user.getName();

        try{
            var statement = conn.createStatement();
            statement.executeUpdate(String.format("insert into users (id, name) VALUES (%d, '%s')", userId, userName));
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public static int getUserLevel (User user){
        long userId = user.getIdLong();
        Statement statement;
        try {
            statement = conn.createStatement();
            var result = statement.executeQuery(String.format("select level from users where id = %d;", userId));
            result.next();
            return result.getInt("level");
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // If in the future we need the contents of a message we can use the id and jda to get it
    public static boolean addNewMessage(Message msg){
        long userId = msg.getAuthor().getIdLong();
        long messageId = msg.getIdLong();

        try{
            var statement = conn.createStatement();
            statement.executeUpdate(String.format("insert into messages (id, userid) VALUES (%d, %d);", messageId, userId));
            statement.executeUpdate(String.format("update users set messageCount = 1 + users.messageCount where id = %d;", userId));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // Gets the number of messages a user has so we can see if he levels up
    public static int getMessageCount(User user) {
        long userId = user.getIdLong();
        Statement statement;
        try {
            statement = conn.createStatement();
            var result = statement.executeQuery(String.format("select messageCount from users where id = %d;", userId));
            result.next();
            return result.getInt("messagecount");
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void levelUp(User user){
        long userId = user.getIdLong();
        int userLevel = getUserLevel(user);

        try{
            var statement = conn.createStatement();
            statement.executeUpdate(String.format("update users set level = %d where id = %d;", userLevel + 1, userId));
        }catch (Exception e){
            System.out.printf("Error leveling up user: %s", user.getName());
        }
    }

    public static void setLevel(User user, int level){
        long userId = user.getIdLong();

        try{
            var statement = conn.createStatement();
            statement.executeUpdate(String.format("update users set level = %d where id = %d;", level, userId));
        }catch (Exception e){
            System.out.printf("Error leveling up user: %s", user.getName());
        }
    }


    private static final Dotenv dotenv = Dotenv.load();

    public static boolean nextLevel(User user){
        int level = getUserLevel(user);
        int messageCount = getMessageCount(user);

        int calculatedLevel = (int) (Float.parseFloat(dotenv.get("LEVELING_RATE")) * Math.sqrt(messageCount));

        return calculatedLevel > level;
    }

    public static int getUserRank(User user) {
        long userId = user.getIdLong();
        Statement statement;
        try {
            statement = conn.createStatement();
            var result = statement.executeQuery(String.format("select * " +
                    "from (select id, rank() " +
                    "over (order by messageCount desc) " +
                    "from users) as \"ranking\" where id = %d;", userId));
            result.next();
            return result.getInt("rank");
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static class Triplet{
        public int rank;
        public int messageCount;
        public String name;

        private Triplet(int rank, int messageCount, String name){
            this.rank = rank;
            this.messageCount = messageCount;
            this.name = name;
        }
    }

    public static LinkedList<Triplet> getRanking() {
        var list = new LinkedList<Triplet>();

        Statement statement;
        try {
            statement = conn.createStatement();
            var result = statement.executeQuery("select name, messageCount, rank() " +
                    "over (order by messageCount desc) from users");

            while(result.next()){
                var rank = result.getInt("rank");
                var messageCount = result.getInt("messagecount");
                var name = result.getString("name");

                System.out.println(name);
                var triplet = new Triplet(rank, messageCount, name);
                list.add(triplet);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
