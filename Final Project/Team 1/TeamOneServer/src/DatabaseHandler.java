import java.sql.*;
import java.util.ArrayList;

import com.zpartal.finalproject.datapackets.*;

/*
    The DatabaseHandler class is a thread that is created by passing in an array list of datapoints.
    The databasehandler thread will connect to the database and add every point from the dataset
 */

public class DatabaseHandler implements Runnable {
    private Config config = new Config();
    private Connection connection = null;
    private ArrayList<DataPoint> dataset = null;

    public DatabaseHandler(ArrayList<DataPoint> _dataset) throws ClassNotFoundException {
        // load the sqlite-JDBC driver using the current class loader
        Class.forName("org.sqlite.JDBC");
        this.dataset = _dataset;
    }

    @Override
    public void run() {
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:"+ config.DB_PATH);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            // Add all datapoints from the data set array
            for (DataPoint dp : dataset) {
                statement.executeUpdate(createInsertSQL(dp));
            }
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                System.err.println(e);
            }
        }
    }

    // Helper function to generate sql for adding a datapoint to the db
    public String createInsertSQL(DataPoint dp) {
        //  INSERT INTO Customers (CustomerName, ContactName, Address, City, PostalCode, Country)
        //  VALUES ('Cardinal','Tom B. Erichsen','Skagen 21','Stavanger','4006','Norway');
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(config.TABLE_NAME);
        sql.append(" (");
        String sep = "";
        for (String s : config.COLUMN_NAMES) {
            sql.append(sep);
            sql.append(s);
            sep = ",";
        }
        sql.append(") VALUES (");
        sql.append(dp.toString());
        sql.append(");");
        return sql.toString();
    }
}
