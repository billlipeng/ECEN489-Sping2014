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

    public ArrayList<DataPoint> readDBData(String tableName) {
        ArrayList<DataPoint> dataset = new ArrayList<DataPoint>();
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from " + tableName);
            while(rs.next())
            {
                dataset.add(new DataPoint(rs.getLong("time"), rs.getDouble("longitude"), rs.getDouble("latitude"), rs.getDouble("bearing"), rs.getDouble("speed")));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public void prepareDBData(String tableName) {
        String newTableName = "prepared_" + tableName;
        createNewTable(newTableName);

        ArrayList<DataPoint> originalData = readDBData(tableName);
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String sql = "";
            for (int i = 0; i < originalData.size(); i++) {
                sql += originalData.get(i).toSql(tableName);
            }
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeDBData(String tableName, ArrayList<DataPoint> analyzedData) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String deleteRowsSQL = "DELETE FROM " + tableName + ";";
            statement.executeUpdate(deleteRowsSQL);
            String sql = "";
            for (DataPoint dp : analyzedData) {
                sql += dp.toSql(tableName);
            }
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void createNewTable(String tableName) {
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);  // set timeout to 30 sec.
                String createTableSql = "CREATE TABLE " + tableName + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,time TEXT,longitude REAL,latitude REAL,bearing REAL,speed REAL,accelX INTEGER,accelY INTEGER,accelZ INTEGER,orientationA INTEGER,orientationP INTEGER,rientationR INTEGER,rotVecX INTEGER,rotVecY INTEGER,rotVecZ INTEGER,rotVecC INTEGER,linAccX INTEGER,linAccY INTEGER,linAccZ INTEGER,gravityX INTEGER,gravityY INTEGER,gravityZ INTEGER,gyroX INTEGER,gyroY INTEGER,gyroZ INTEGER);";
                statement.executeUpdate(createTableSql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
