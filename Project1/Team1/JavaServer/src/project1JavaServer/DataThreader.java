package project1JavaServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import com.mhardian.gps_retrieval.dataRow;

public class DataThreader extends Thread{

	
	
		protected Socket socket;
		dataRow row;
		//declare secondary obj here for send if desired. 
		public DataThreader(Socket client){
			this.socket = client;
		}
		public void run(){
			
			//ObjectOutputStream sender = null; //Unneeded right now
			ObjectInputStream receiver = null;
			
			//Declaring data
			
			try{
				//sender = new ObjectOutputStream(socket.getOutputStream()); //Unneeded right now
				receiver = new ObjectInputStream(socket.getInputStream());					
			}
			catch(IOException e){
				System.err.println("Could not create I/O Streams..!");
			}
			Connection connection = null;
			
				try{
					while(true){
				//listening for packets
					//connection = DriverManager.getConnection("jdbc:sqlite:DataReadings.db");
					dataRow row;
					System.out.println("4");
					
					row = (dataRow) receiver.readObject();	
					//System.out.println(test);

					//dataRow[] dataCatch = (dataRow[]) row;
					
					System.out.println(row.sensorType);
					System.out.println(row.latitude);

					System.out.println(row.longitude);

					System.out.println("Attempting connection to SQL server.");
					
					/*try {
						connection.setAutoCommit(false);
						String insert = "INSERT INTO Data(sensorID, teamID, runID, latitude, longitude, date, time, sensorValue, sensorType, attribute) VALUES(?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement insertData = connection.prepareStatement(insert);
						insertData.setString(1, row.sensorID);
						insertData.setString(2, row.teamID);
						insertData.setInt(3, row.runID);
						insertData.setDouble(4, row.latitude);
						insertData.setDouble(5, row.longitude);
						insertData.setString(6, row.date);
						insertData.setString(7, row.time);
						insertData.setFloat(8, row.sensorValue);
						insertData.setString(9, row.sensorType);
						insertData.setString(10, row.attribute); 
						insertData.executeUpdate();
						connection.commit();
						connection.setAutoCommit(true);
					}
					catch (SQLException e) {
						System.err.println(e.getMessage());
					}
					
					//System.exit(0);
				*/}
					
					//Now has data read into row[].
				}
			/*	catch(ClassNotFoundException e){
					System.err.println("Class not found...");
				}*/
				catch(IOException e){
					
					System.out.println("A client disconnected!");
					System.err.println(e);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*catch(SQLException e){
					System.out.println("SQL Exception!");
					System.err.println(e.getMessage());
				 }*/
				try{
					socket.close();
					System.out.println("Socket terminated.");
					//connection.close();
					
				}
				catch(IOException e){
					System.err.println("Couldn't close the socket!");
				}
				/*catch(SQLException e){
					System.out.println("SQL Exception failed trying to close!");
					System.err.println(e.getMessage());
				}*/
				
			}
					
	}
