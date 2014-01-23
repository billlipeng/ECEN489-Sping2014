import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteJDBC {
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Class.forName("org.sqlite.JDBC");
		
		Connection connection = null;
		BufferedReader read = null;
		
		try {
			read = new BufferedReader(new InputStreamReader(System.in));
			connection = DriverManager.getConnection("jdbc:sqlite:test.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); 
			
			statement.executeUpdate("drop table if exists students");
			statement.executeUpdate("create table students (uin integer, fname string, lname string)");
			statement.executeUpdate("insert into students values(1, 'Zach', 'Partal')");
			statement.executeUpdate("insert into students values(2, 'Gob', 'Bluth')");
			
			// insert into students values(2, 'Tobias', 'Funke')
			System.out.print("SQL INS>>");
			String sql = read.readLine();
			statement.executeUpdate(sql);
			
			ResultSet rs = statement.executeQuery("select * from students");
			while(rs.next())
		      {
		        // read the result set
		        System.out.print("name: " + rs.getString("fname") + " " + rs.getString("lname") + "; ");
		        System.out.println("uin: " + rs.getInt("uin"));
		      }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
