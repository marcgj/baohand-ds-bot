package postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseController extends Thread{

    private static DatabaseController INSTANCE;
    private Connection conn;

    public Connection getConn() {
        return conn;
    }

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

    public static void main(String[] args) {
        var test = new DatabaseController();
        test.start();

    }
}
