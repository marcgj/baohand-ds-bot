package postgres;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseController extends Thread{

    private static DatabaseController INSTANCE;
    private Connection conn;

    private final Dotenv dotenv = Dotenv.load();
    public Connection getConn() {
        return conn;
    }

    // This makes a singleton so only one database controller can exits
    // TODO: Making this a singleton might cause performance issues in the future so another approach should be found
    public static DatabaseController getInstance(){
        if (INSTANCE == null){
            INSTANCE = new DatabaseController();
            INSTANCE.start();
        }
        return INSTANCE;
    }

    public DatabaseController() {
        try {
            String url = dotenv.get("JDBC_STRING");

            Properties props = new Properties();
            props.setProperty("user", dotenv.get("JDBC_USER"));
            props.setProperty("password", dotenv.get("JDBC_PASSWORD"));

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
