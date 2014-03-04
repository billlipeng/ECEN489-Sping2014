import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseHandler {
    private Connection connection = null;
    private String filePath = null;

    public DatabaseHandler(String _filePath) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        this.filePath = _filePath;
    }

    public ArrayList<DataPoint> readDBData() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + filePath);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<DataPoint>();
    }

    public void writeDBData(ArrayList<DataPoint> analyzedData) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + filePath);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            // For some loop {
            // statement.executeUpdate(SOME SQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
