package command;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import postgres.DatabaseController;
import postgres.methods.Query;

public class Admins {
    public static LinkedList<String> adminIds = getAdminsDB();
    public static LinkedList<String> getAdminsDB(){
        var conn = DatabaseController.getInstance().getConn();
        Query query = new Query(conn, "select id from users where isadmin = true;");
        return query.getColumn("id");
    }
}
