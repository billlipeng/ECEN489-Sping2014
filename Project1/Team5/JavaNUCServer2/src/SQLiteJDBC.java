
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.example.samcarey.*;

public class SQLiteJDBC
{
	static Connection DBconnection;
	@SuppressWarnings("unchecked")
	public static void main( String args[] )
	{
		
		
		try{
			System.out.println("Server initializing...");
			ServerSocket server = new ServerSocket(5555, 1);
			System.out.println("Server waiting...");
			Socket connection = server.accept();
			System.out.println("Server connected!");
			ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
			System.out.println("Working?");
			ArrayList<AndroidPacket1> data = (ArrayList<AndroidPacket1>) input.readObject();
			System.out.println("Data Received");
			input.close();
			server.close();
			
			//Store in DataBase
		    DBconnection = null;
		    Class.forName("org.sqlite.JDBC");
		    String path = "/Users/samcarey/Desktop/Spring 2014/489/SQLite/test.db";
		    DBconnection = DriverManager.getConnection("jdbc:sqlite:" + path);
		    System.out.println("Opened database successfully");
		    createTable();		//No problem if table already exists
		    storeData(data);
		    DBconnection.close();
	    }catch(Exception e){
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	}
	
	@SuppressWarnings("static-access")
	public static void storeData(ArrayList<AndroidPacket1> data){
		try {
			Statement statement = DBconnection.createStatement();
			for (int i = 0 ; i < data.size() ; i++){
				String command = "INSERT INTO ecen489_project1_data(" +
			                 	 " date, " +
			                 	 " time, " + 
			                 	 " client_id, " + 
			                 	 " run_id, " + 
			                 	 " latitude, " + 
			                 	 " longitude, " + 
			                 	 " bearing, " + 
			                 	 " speed, " + 
			                 	 " altitude, " + 
			                 	 " sensor_id, " + 
			                 	 " sensor_type, " + 
			                 	 " sensor_value, " + 
			                 	 " attribute)" +
			                 	 
			                 	 " VALUES(" +
			                 	 	"'" + data.get(i).date + "', " +
			                 	 	"'" + data.get(i).timestamp + "', " +
			                 	 	"'" + data.get(i).client_id + "', " +
			                 	 	"'" + data.get(i).run_id + "', " +
			                 	 		  data.get(i).latitude + ", " +
			                 	 		  data.get(i).longitude + ", " +
			                 	 		  data.get(i).bearing + ", " +
			                 	 		  data.get(i).speed + ", " +
			                 	 		  data.get(i).altitude + ", " +
			                 	 	"'" + data.get(i).sensor_id + "', " +
			                 	 	"'" + data.get(i).sensor_type + "', " +
			                 	 		  data.get(i).sensor_value + ", " +
			                 	 	"'" + data.get(i).attribute + "') ";
				System.out.println(command);
			    statement.executeUpdate(command);
			}
		    statement.close();
		    System.out.println("Entries successful");
		} catch (SQLException e) {
			e.printStackTrace();
			//System.out.println("Table already exists");
		}
	}
	
	private static void createTable(){
		try {
			Statement statement = DBconnection.createStatement();
		    String command = "CREATE TABLE ecen489_project1_data( " +
		                 	 " date 		TEXT 	NOT NULL, " +
		                 	 " time 		TEXT    NOT NULL, " + 
		                 	 " client_id 	TEXT    NOT NULL, " + 
		                 	 " run_id 		TEXT    NOT NULL, " + 
		                 	 " latitude 	REAL    NOT NULL, " + 
		                 	 " longitude 	REAL    NOT NULL, " + 
		                 	 " bearing		REAL, " + 
		                 	 " speed		REAL, " + 
		                 	 " altitude		REAL, " + 
		                 	 " sensor_id	TEXT, " + 
		                 	 " sensor_type	TEXT, " + 
		                 	 " sensor_value	REAL, " + 
		                 	 " attribute	TEXT    NOT NULL)" ;
		    statement.executeUpdate(command);
		    statement.close();
		    System.out.println("Table created successfully");
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("Table already exists");
		}
	}
}
