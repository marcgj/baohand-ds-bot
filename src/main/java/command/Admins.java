package command;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import postgres.DatabaseController;

// TODO: Moureho a base de dades
public class Admins {
    public static ArrayList<String> adminIds = getAdminsDB();
    public static ArrayList<String> getAdminsDB(){
        var result = new ArrayList<String>();

        Statement statement;
        try {
            var conn = DatabaseController.getInstance().getConn();
            statement = conn.createStatement();
            var query = statement.executeQuery(String.format("select id from users where isadmin = true;"));

            while(query.next()){
                String id = query.getString("id");
                result.add(id);
            }

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
