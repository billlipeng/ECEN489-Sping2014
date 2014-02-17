import java.sql.*;
import java.util.ArrayList;

import com.zpartal.project1.datapackets.*;

public class DatabaseHandler implements Runnable {
    private Properties properties = new Properties();
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
            connection = DriverManager.getConnection("jdbc:sqlite:"+properties.DB_PATH);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.


            statement.executeUpdate("insert into person values(2, 'yui')");
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

    public String createInsertSQL(DataPoint dp) {
        //  INSERT INTO Customers (CustomerName, ContactName, Address, City, PostalCode, Country)
        //  VALUES ('Cardinal','Tom B. Erichsen','Skagen 21','Stavanger','4006','Norway');
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(properties.TABLE_NAME);
        sql.append(" (");
        String sep = "";
        for (String s : properties.COLUMN_NAMES) {
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
