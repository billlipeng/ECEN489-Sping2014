package PhaseOne;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;






import java.util.Scanner;

import com.FinalPhaseOnePacket.*;

public class PhaseOneServer {

	public void start(){
		int port = 5555;
		ServerSocket serversocket = null;
		System.out.println("Waiting for New Connection..........");
		try{
			serversocket = new ServerSocket(port);
		}catch (IOException e){
			e.printStackTrace();
		}
		
		while(true){
			try {
				Socket socket = serversocket.accept();
				Task task = new Task(socket);
				Thread thread = new Thread(task);
				thread.start();
				System.out.println("New Connection");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	public static void main(String[] args) {
		new PhaseOneServer().start();
	}

	
	private class Task implements Runnable{
		private String sql = null;
		private Connection con = null;
		private Statement stmt = null;
		private String TableName = "RSSI_Location";
		private FinalProjectPhaseOnePacket1 packets;
		private Socket socket;
		private ObjectInputStream is;
		private ObjectOutputStream os;
		
		Double[] latitude;
		Double[] longitude;
		Double[] RSSI;
		
		public Task(Socket socket){
			super();
			this.socket = socket;
		}
		
		
		@Override
		public void run() {
			try {
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection("jdbc:sqlite://E:/FinalProjectPhaseOne.db");
				
				con.setAutoCommit(false);
				uploadfusiontable uf = new uploadfusiontable();
				System.out.println("Connection is establised!");
				is = new ObjectInputStream(socket.getInputStream());
				packets = (FinalProjectPhaseOnePacket1) is.readObject();
				latitude = packets.getLatitude();
				longitude = packets.getLongitude();
				RSSI =packets.getRSSI();
				for (int i = 0;i<RSSI.length;i++){
					stmt = con.createStatement();
					int curr_id = 0;
					ResultSet rs = stmt.executeQuery("SELECT * FROM "+TableName
							+";");
//					while(rs.next()){
//						curr_id++;
//						
//					}
					sql = "INSERT INTO "+ TableName +"("
//							+ "_id"
							+ "RSSI"
							+ ", latitude"
							+ ", longitude"
							+ ") " +
	    	 				"VALUES ("
//	    	 						+  (curr_id+1)	
									+ RSSI[i] 
	 								+ ", " + latitude[i] 		
									+ ", " + longitude[i]				
									+ ");";
					stmt.executeUpdate(sql);
			        stmt.close();
			       // System.out.println("i= "+i);
				}
				
				System.out.println("committing data....");
				con.commit();
				con.close();
				Scanner scanner = new Scanner(System.in);
				System.out.println("1.Create and insert into a new Fusion Table: press 1");
				System.out.println("2.Insert into an existing Fusion Table: press 2");
				System.out.println("Exit press 3");
				String Choice = scanner.next();
				boolean check = true;
				while (check){
				if (Choice.equals("1")){
					System.out.println("Please type the Table Name you want to create for Fusion Table");
					String tableName = scanner.next();
					String tableID = uf.createTable(tableName);
					System.out.println("Fusion Table "+ tableName+" created successfully!");
					uf.update(tableID,latitude, longitude, RSSI);
					System.out.println("upload data successfully!");
					check = false;
				}
				else if (Choice.equals("2")){
					System.out.println("Please type the TableID for the fusion table:");
					String tableID = scanner.next();
//					String tableID = uf.createTable(tableName);
//					System.out.println("Fusion Table "+ tableName+" created successfully!");
					uf.update(tableID,latitude, longitude, RSSI);
					System.out.println("upload data successfully!");
					check = false;
				}
				else if(Choice.equals("3")){
					check = false;
				} 
				else{
					System.out.println("invalid input, please input number from 1 to 3:");
					check = true;
				}
				
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try{
				is.close();
				System.out.println("Connection Closed");
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
	}
	
	
	
}
