import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Server {

	static ServerSocket socket;
	final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS sensorData (time REAL, "
			+ "longitude REAL," 	+ "latitude REAL," 		+ "bearing REAL," 	+ "speed REAL,"
			+ "accelX REAL," 		+ "accelY REAL," 		+ "accelZ REAL," 	+ "orientationA REAL," 
			+ "orientationP REAL," 	+ "orientationR REAL," 	+ "rotVecX REAL," 	+ "rotVecY REAL,"
			+ "rotVecZ REAL," 		+ "rotVecC REAL," 		+ "linAccX REAL," 	+ "linAccY REAL,"
			+ "linAccZ REAL," 		+ "gravityX REAL," 		+ "gravityY REAL," 	+ "gravityZ REAL,"
			+ "gyroX REAL," 		+ "gyroY REAL," 		+ "gyroZ REAL);";
	final static String DROP_TABLE = "DROP TABLE IF EXISTS sensorData";

	public static void main(String args[]) throws Exception {
		Connection conn = null; // database connection
		Statement stmt = null; // used to execute SQL statements
		
		
		conn = DriverManager.getConnection("jdbc:sqlite:DataPoints.db"); // connect to database
		stmt = conn.createStatement();
		stmt.executeUpdate(DROP_TABLE); // drop existing table/data
		stmt.executeUpdate(CREATE_TABLE); // create table for sensor data
		stmt.close();
		conn.close();
		
		System.out.println("Waiting for new connections...");
		try {
			socket = new ServerSocket(5555);
			while (true) {
				ServerThread st= new ServerThread(socket.accept());
				Thread t = new Thread(st);
				t.start();
				System.out.println("A Client has Connected!");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}