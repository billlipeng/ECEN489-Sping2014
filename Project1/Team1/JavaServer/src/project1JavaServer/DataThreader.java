package project1JavaServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.mhardiman.gps_retrieval.*;

public class DataThreader extends Thread{
	
	//Declarations
	protected Socket socket;
	dataRow row;
	
		public DataThreader(Socket client){
			this.socket = client;		
		}
		
		public void run(){
			
			ObjectInputStream receiver = null;			
			//Declaring data			
			try{
				receiver = new ObjectInputStream(socket.getInputStream());}
			catch(IOException e){
				System.err.println("Could not create I/O Streams..!");}
			
			//Connection connection = null;
			
			try{
				while(true){
				//listening for data object					
					ArrayList<dataRow> row = new ArrayList<dataRow>();						
					row = (ArrayList<dataRow>) receiver.readObject();
					//Testing SQL Connectivity:
					//dataRow r1 = new dataRow("sensor1team1", "team1", 0001, 30.1945, 90.342, "02/20/2014", "4:41:00PM", 202.11, "Light","Update");
					//r1.sensorID = "team1-sensor1";
					
					
					
					//Test Output
					System.out.println(row.get(0).sensorType);					
					System.out.println(row.get(0).latitude);
					System.out.println(row.get(1).longitude);
					System.out.println(row.get(2).longitude);
					
					//Stage 2 - Sending to SQL.
					System.out.println("Attempting connection to SQL server.");
					
					//SQL DATA ENTRY
					try { 
						Class.forName("org.sqlite.JDBC");
					  	Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/SQLite/DB1.db");

						connection.setAutoCommit(false);
						for(int i = 0; i< row.size(); i++){
							
						String insert = "INSERT INTO Data(sensorID, teamID, runID, latitude, longitude, date, time, sensorValue, sensorType, attribute) VALUES(?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement insertData = connection.prepareStatement(insert);
						insertData.setString(1, row.get(i).sensorID);
						insertData.setString(2, row.get(i).teamID);
						insertData.setInt(3, row.get(i).runID);
						insertData.setDouble(4, row.get(i).latitude);
						insertData.setDouble(5, row.get(i).longitude);
						insertData.setString(6, row.get(i).date);
						insertData.setString(7, row.get(i).time);
						insertData.setFloat(8, row.get(i).sensorValue);
						insertData.setString(9, row.get(i).sensorType);
						insertData.setString(10, row.get(i).attribute); 
						insertData.executeUpdate();
						connection.commit();}
						
						connection.setAutoCommit(true);
						try{							
							System.out.println("Socket terminated.");
							connection.close();				
						}					
						catch(SQLException e){
							System.out.println("SQL Exception failed trying to close!");
							System.err.println(e.getMessage());
						
						
					}
						
					}
					catch (SQLException e) {
						System.err.println(e.getMessage());
					}
					//DONE WITH DATA ENTRY TO SQL
					
					//System.exit(0);
				} //STILL LISTENING
				
					
			}
				catch(ClassNotFoundException e){
					System.err.println("Class not found...");
					}				
				catch(IOException e){					
					System.out.println("A client disconnected!");
					System.err.println(e);	}
				 
			try{
				socket.close();	}
			catch (IOException e){
				System.err.println(e);
				System.err.println("Couldn't close the socket!");}	
					
		} //END OF RUN
		
		
	
}


