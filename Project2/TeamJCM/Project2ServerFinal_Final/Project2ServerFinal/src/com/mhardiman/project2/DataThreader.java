package com.mhardiman.project2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mhardiman.project2app.imu;

public class DataThreader extends Thread{

	//Declarations
	protected Socket socket;
	
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
			try{
				ArrayList<imu> rowArray = null;
				while(rowArray == null){
				//listening for data object					
					rowArray = new ArrayList<imu>();		
					//receive object from app 
					rowArray = (ArrayList<imu>) receiver.readObject();					
					System.out.println("Attempting connection to SQL server.");
					//input data into the sql table
					try { 
						Class.forName("org.sqlite.JDBC");
					  	Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/Mark/Documents/Eclipse/Project2ServerFinal/projtwo.db");				
						imu row;						
						for(int i = 0; i< rowArray.size(); i++){
						connection.setAutoCommit(false);
						row = rowArray.get(i);
						System.out.println(rowArray.get(i).latitude + ", " + rowArray.get(i).longitude); 
						String insert = "INSERT INTO DataTable1(time, latitude, longitude, bearing, speed) VALUES(?,?,?,?,?)";
						PreparedStatement insertData = connection.prepareStatement(insert);
						insertData.setFloat(1, row.time);
						insertData.setFloat(2, (float) row.latitude);
						insertData.setFloat(3, (float) row.longitude);
						insertData.setFloat(4, (float) row.bearing);
						insertData.setFloat(5, (float) row.speed);						
						insertData.executeUpdate();
						connection.commit();
						connection.setAutoCommit(true);
						}
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
			}//data has been put into table.
				
				//now time to operate on it.					
				try{
				DBTest.main(null); //handing it off to DBTest
				//to test various algorithms, adjust DBTest
				}
				catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			} //STILL LISTENING


			
				catch(ClassNotFoundException e){
					System.err.println("Class not found...");
					System.err.println(e.getMessage());
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
