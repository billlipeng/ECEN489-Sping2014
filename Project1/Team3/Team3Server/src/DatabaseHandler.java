import java.sql.*;
import org.sqlite.JDBC;

public class DatabaseHandler {
    private Connection connection = null;
    public DatabaseHandler() throws ClassNotFoundException, SQLException {
        // load the sqlite-JDBC driver using the current class loader
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:projone.db");
    }


}
