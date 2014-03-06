package com.mhardiman.project2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

			//Connection connection = null;

			try{
				ArrayList<imu> rowArray = null;
				while(rowArray == null){
				//listening for data object					
					rowArray = new ArrayList<imu>();						
					rowArray = (ArrayList<imu>) receiver.readObject();
					//Testing SQL Connectivity:
					//dataRow r1 = new dataRow("sensor1team1", "team1", 0001, 30.1945, 90.342, "02/20/2014", "4:41:00PM", 202.11, "Light","Update");
					//r1.sensorID = "team1-sensor1";
				//	for(int i = 0; i< rowArray.size(); i++)
					//{
						//System.out.println(rowArray.get(i).latitude + ", " + rowArray.get(i).longitude); //+ "; " + rowArray.get(i).time);
					//}

					//Stage 2 - Sending to SQL.
					System.out.println("Attempting connection to SQL server.");

					//SQL DATA ENTRY
					try { 
						Class.forName("org.sqlite.JDBC");
					  	Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/Mark/Documents/Eclipse/Project2ServerTemp/proj2.db");

						
						imu row;
						//Calendar cal = Calendar.getInstance();
				    	//cal.getTime();
				      	//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
				      	//String runString = "team1_"+ sdf.format(cal.getTime());
						for(int i = 0; i< rowArray.size(); i++){
						connection.setAutoCommit(false);
						row = rowArray.get(i);
						System.out.println(rowArray.get(i).latitude + ", " + rowArray.get(i).longitude); //+ "; " + rowArray.get(i).time);
						String insert = "INSERT INTO table1(time, latitude, longitude, bearing, speed) VALUES(?,?,?,?,?)";
						PreparedStatement insertData = connection.prepareStatement(insert);
						insertData.setFloat(1, row.time);
						insertData.setFloat(2, (float) row.latitude);
						insertData.setFloat(3, (float) row.longitude);
						insertData.setFloat(4, (float) row.bearing);
						insertData.setFloat(5, (float) row.speed);
						//format run_id: teamid_yyyymmdd_hhmmss
				    	/*insertData.setString(4,runString);
						insertData.setDouble(5, row.latitude);
						insertData.setDouble(6, row.longitude);
						insertData.setString(7, null);
						insertData.setFloat(8, 0.0f);//needs to be null
						insertData.setFloat(9, 0.0f);//needs to be null
						insertData.setString(10, row.sensorID);
						insertData.setString(11, row.sensorType);
						insertData.setDouble(12, row.sensorValue);
						insertData.setString(13, row.attribute);*/ 
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
					//DONE WITH DATA ENTRY TO SQL
				}
				//aogirthm time
				//try{
					double coordinates[][] = new double[2][rowArray.size()];//lat,long
					for(int i = 0; i < rowArray.size()-10; i+=10){
						imu row;
						imu row2;
						row = rowArray.get(i);
						row2 = rowArray.get(i+10);
						double a = row.latitude;
						double b = row.longitude;
						double a1 = row2.latitude;
						double b1 = row2.longitude;
						double deltx = b - b1;
						double delty = a - a1;
						for(int j = 0; j<10;j++){
							coordinates[0][i+j] = b + deltx*j/10;
							coordinates[1][i+j] = a + delty*j/10;
							System.out.println(coordinates[1][i+j] + ", " + coordinates[0][i+j]);
						}
						
					}
				//}
			/*catch{
					
				}*/
					
				
			
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
