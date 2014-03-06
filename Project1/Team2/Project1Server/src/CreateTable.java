import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateTable
{
  public static void main( String args[] )
  {
    Connection c = null;
    Statement stmt = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:C:/projone.db");
      System.out.println("Opened database successfully");

      stmt = c.createStatement();
      String sql = "CREATE TABLE [ecen489_project1_data] ("
      		+ "[_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
      		+ "[date] TEXT, "
      		+ "[time] TEXT, "
      		+ "[client_id] TEXT, "
      		+ "[run_id] TEXT, "
      		+ "[latitude] REAL, "
      		+ "[longitude] REAL, "
      		+ "[bearing] REAL, "
      		+ "[speed] REAL, "
      		+ "[altitude] REAL, "
      		+ "[sensor_id] TEXT, "
      		+ "[sensor_type] TEXT, "
      		+ "[sensor_value] REAL, "
      		+ "[attribute] TEXT)"; 
      stmt.executeUpdate(sql);
      stmt.close();
      c.close();
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Table created successfully");
  }
}
