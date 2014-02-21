package com.google.api.services.samples.fusiontables.cmdline;

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

import com.mhardiman.gps_retrieval.dataRow;

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
				ArrayList<dataRow> rowArray = null;
				while(rowArray == null){
				//listening for data object					
					rowArray = new ArrayList<dataRow>();						
					rowArray = (ArrayList<dataRow>) receiver.readObject();
					//Testing SQL Connectivity:
					//dataRow r1 = new dataRow("sensor1team1", "team1", 0001, 30.1945, 90.342, "02/20/2014", "4:41:00PM", 202.11, "Light","Update");
					//r1.sensorID = "team1-sensor1";


					new FusionTablesSample(rowArray).start();
					//Test Output
					System.out.println(rowArray.get(0).sensorType);					
					System.out.println(rowArray.get(0).latitude);
					System.out.println(rowArray.get(1).longitude);
					System.out.println(rowArray.get(2).longitude);

					//Stage 2 - Sending to SQL.
					System.out.println("Attempting connection to SQL server.");

					//SQL DATA ENTRY
					try { 
						Class.forName("org.sqlite.JDBC");
					  	Connection connection = DriverManager.getConnection("jdbc:sqlite: C:\\projone.db");

						
						dataRow row;
						Calendar cal = Calendar.getInstance();
				    	cal.getTime();
				      	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
				      	String runString = "team1_"+ sdf.format(cal.getTime());
						for(int i = 0; i< rowArray.size(); i++){
						connection.setAutoCommit(false);
						row = rowArray.get(i);
						String insert = "INSERT INTO ecen489_project1_data(date, time, client_id, run_id, latitude, longitude, bearing, speed, altitude, sensor_id, sensor_type, sensor_value, attribute) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement insertData = connection.prepareStatement(insert);
						insertData.setString(1, row.date);
						insertData.setString(2, row.time);
						insertData.setString(3, row.teamID);
						//format run_id: teamid_yyyymmdd_hhmmss
				    	insertData.setString(4,runString);
						insertData.setDouble(5, row.latitude);
						insertData.setDouble(6, row.longitude);
						insertData.setString(7, null);
						insertData.setFloat(8, 0.0f);//needs to be null
						insertData.setFloat(9, 0.0f);//needs to be null
						insertData.setString(10, row.sensorID);
						insertData.setString(11, row.sensorType);
						insertData.setDouble(12, row.sensorValue);
						insertData.setString(13, row.attribute); 
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

					//FUSION TABLE UPLOAD
					
						
				
				//DONE WITH FUSION TABLES
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
