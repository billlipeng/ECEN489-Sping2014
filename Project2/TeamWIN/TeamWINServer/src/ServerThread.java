import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.example.gpsimureader.DataPoint;


public class ServerThread implements Runnable{

	Socket socket = null;
	ObjectInputStream in;
	ObjectOutputStream out;
	DataPoint dp = null;
	
	Connection conn = null; // database connection
	Statement stmt = null; // SQL statements
	ArrayList<DataPoint> listOfData = new ArrayList<DataPoint>(); // Array List of Sensor Data Objects
	
	ServerThread(Socket clientSocket) {
		super();
		this.socket = clientSocket;
	}

	public void run() {
		try {
			Class.forName("org.sqlite.JDBC");
			in = new ObjectInputStream(socket.getInputStream());
			while (true) {
				conn = DriverManager.getConnection("jdbc:sqlite:DataPoints.db"); // connect to database
				dp = (DataPoint) in.readObject(); // read in DataPoint object
				listOfData.add(dp);
				System.out.println("\nReceived new DataPoint Object!\n");
				System.out.println("\nAttempting to insert object into SQLite DB\n");
				
				conn.setAutoCommit(false);
				String insert = "INSERT INTO sensorData(time, latitude, longitude, bearing, "
						+ "speed, accelX, accelY, accelZ, orientationA, orientationP, "
						+ "orientationR, rotVecX, rotVecY, rotVecZ, rotVecC, linAccX, linAccY, "
						+ "linAccZ, gravityX, gravityY, gravityZ, gyroX, gyroY, gyroZ) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement insertData = conn.prepareStatement(insert);
				insertData.setDouble(1, dp.getTime());
				insertData.setDouble(2, dp.getLongitude());
				insertData.setDouble(3, dp.getLatitude());
				insertData.setDouble(4, dp.getBearing());
				insertData.setDouble(5, dp.getSpeed());
				insertData.setDouble(6, dp.getAccelX());
				insertData.setDouble(7, dp.getAccelY());
				insertData.setDouble(8, dp.getAccelZ());
				insertData.setDouble(9, dp.getOrientationA());
				insertData.setDouble(10, dp.getOrientationP());
				insertData.setDouble(11, dp.getOrientationR());
				insertData.setDouble(12, dp.getRotVecX());
				insertData.setDouble(13, dp.getRotVecY());
				insertData.setDouble(14, dp.getRotVecZ());
				insertData.setDouble(15, dp.getRotVecC());
				insertData.setDouble(16, dp.getLinAccX());
				insertData.setDouble(17, dp.getLinAccY());
				insertData.setDouble(18, dp.getLinAccZ());
				insertData.setDouble(19, dp.getGravityX());
				insertData.setDouble(20, dp.getGravityY());
				insertData.setDouble(21, dp.getGravityZ());
				insertData.setDouble(22, dp.getGyroX());
				insertData.setDouble(23, dp.getGyroY());
				insertData.setDouble(24, dp.getGyroZ());
				insertData.executeUpdate();
				conn.commit();
				conn.setAutoCommit(true);
				System.out.println("\nData has been inserted into DB!\n");
				
			}
		} catch (IOException | ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		}

		finally {
			try {
				in.close();
				socket.close();
				conn.close();
			} catch (IOException | SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}