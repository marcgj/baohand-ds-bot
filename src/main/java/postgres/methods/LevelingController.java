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

    static class Query {
        final Connection conn;
        final String query;

        public Query(Connection conn, String query) {
            this.conn = conn;
            this.query = query;
        }

        public boolean update() {
            try {
                var statement = conn.createStatement();
                statement.executeUpdate(query);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public LinkedList<String[]> getColumn(String column) {
            return getColumns(new String[]{column});
        }

        public LinkedList<String[]> getColumns(String[] columns) {
            try {
                var statement = conn.createStatement();
                var result = statement.executeQuery(query);

                final LinkedList list = new LinkedList<String[]>();
                
                while(result.next()){
                    String[] tempArr = new String[columns.length];
                    
                    int i = 0;
                    for(var col : columns){
                        tempArr[i++] = result.getString(col);
                    }
                    list.add(tempArr);
                }

                return list;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private static final Dotenv dotenv = Dotenv.load();
    private static final Connection conn = DatabaseController.getInstance().getConn();

    public static boolean addUser(User user) {
        long userId = user.getIdLong();
        String userName = user.getName();
        Query query = new Query(conn,
                String.format("insert into users (id, name) VALUES (%d, '%s')", userId, userName));
        return query.update();
    }

    public static int getUserLevel(User user) {
        final long userId = user.getIdLong();
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

    // If in the future we need the contents of a message we can use the id and jda
    // to get it
    public static boolean addNewMessage(Message msg) {
        final long userId = msg.getAuthor().getIdLong();
        Query query = new Query(conn,
                String.format("update users set messageCount = 1 + users.messageCount where id = %d;", userId));
        return query.update();
    }

    // Gets the number of messages a user has so we can see if he levels up
    public static int getMessageCount(User user) {
        final long userId = user.getIdLong();
        Query query = new Query(conn, String.format("select messageCount from users where id = %d;", userId));
        return Integer.parseInt(query.getColumn("messagecount").get(0)[0]);
    }

    public static void levelUp(User user) {
        final long userId = user.getIdLong();
        Query query = new Query(conn, String.format("update users set level = level + 1 where id = %d;", userId));
        query.update();
    }

    public static void setLevel(User user, int level) {
        final long userId = user.getIdLong();
        Query query = new Query(conn, String.format("update users set level = %d where id = %d;", level, userId));
        query.update();
    }

    public static boolean nextLevel(User user) {
        final int level = getUserLevel(user);
        final int messageCount = getMessageCount(user);

        final int calculatedLevel = (int) (Float.parseFloat(dotenv.get("LEVELING_RATE")) * Math.sqrt(messageCount));

        return calculatedLevel > level;
    }

    public static int getUserRank(User user) {
        final long userId = user.getIdLong();
        Query query = new Query(conn, String.format("select * " +
        "from (select id, rank() " +
        "over (order by messageCount desc) " +
        "from users) as \"ranking\" where id = %d;", userId));

        return Integer.parseInt(query.getColumn("rank").get(0)[0]);
    }

    public static LinkedList<String[]> getRanking() {
        Query query = new Query(conn, "select name, messageCount, rank() " +
        "over (order by messageCount desc) from users");

        return query.getColumns(new String[]{"rank", "name", "messagecount"});
    }

}
