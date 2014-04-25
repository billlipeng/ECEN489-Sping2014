import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler {
    private Connection connection = null;
    private String filePath = null;

    public DatabaseHandler(String _filePath) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        this.filePath = _filePath;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + filePath);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Made DB Connection...");
    }

    public ArrayList<Coordinates> readDBData(String tableName) {
        ArrayList<Coordinates> dataset = new ArrayList<Coordinates>();
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from " + tableName);
            while(rs.next())
            {
                dataset.add(new Coordinates(rs.getDouble("latitude"), rs.getDouble("longitude")));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public ArrayList<String> getTableList() {
        ArrayList<String> tables = new ArrayList<String>();
        if (connection != null) {
            try {
                DatabaseMetaData meta = connection.getMetaData();
                ResultSet rs = meta.getTables(null, null, "%", null);
                while (rs.next()) {
                    String tablename = rs.getString(3);
                    if (!tablename.equals("sqlite_sequence")) {
                        if (!tablename.equals("master")) {
                            tables.add(rs.getString(3));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tables;
    }

}
