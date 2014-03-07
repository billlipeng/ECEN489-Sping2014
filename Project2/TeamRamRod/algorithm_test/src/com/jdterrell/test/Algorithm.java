package com.jdterrell.test;

import java.lang.Math;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.FusiontablesScopes;

//import com.jdterrell.p2.AndroidPacket2;

/* this algorithm calculates the latitude and longitude of an unknown location using speed, bearing, and initial GPS
   location, resets initial GPS location every 10 iterations.
*/


// get data from fusion table
//need some type of loop structure to perform on the table starting from row 1 and resetting every 10 intervals

public class Algorithm {
	
	private static final String APPLICATION_NAME = "ECEN 489 Project Two";
    private static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.home"), ".store/fusiontables_sample");
    private static FileDataStoreFactory dataStoreFactory;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport httpTransport;
	
    @SuppressWarnings("unused")
    private static Fusiontables client;
    
    private static Credential authorize() throws Exception {

        Set<String> scopes = new HashSet<String>();
        scopes.add(FusiontablesScopes.FUSIONTABLES);
        scopes.add(FusiontablesScopes.FUSIONTABLES_READONLY);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, 
                JSON_FACTORY, 
                "391485295951.apps.googleusercontent.com",
                "478DjK9lvUho00AcpJrImzgq", 
                scopes).setDataStoreFactory(
                dataStoreFactory).build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }
    
    
	//static double t_sample = 10;	//10 seconds between every measurement point
	static double R = 6731000; //mean radius of Earth in meters
	static long time1;
	static long time2;
	static long delta_t;
	static double latitude1;
	static double longitude1;
	static double latitude2;
	static double longitude2;
	static double bearing;
	static double speed;
	static double distance;
	static double delta_lat;
	static double delta_lon;
	static String sql3;
	static String sql4;
	
	public static void main(String args[])	{
		Connection c = null;
		Statement stmt = null;
		try {

			//Fusion Table setup code
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            Credential credential = authorize();
            client = new Fusiontables.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
            
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:C:/projtwo.db");
		      c.setAutoCommit(false);
		      System.out.println("Opened database successfully");
		      
		      stmt = c.createStatement();
		      
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM ecen489_project2_data;" );
		      
		      while (rs.next())	{
		         
		    	 int id = rs.getInt(1);
		    	 
		         if (id > 4) {
		        	 break;
		         }
		         
		         //start over again from actual gps sample
//		         for(int i = 1; i<=10; i++) {
		        	 
		        	 //get the next 9 points
			         for(int j = 0; j<=4; j++) {
			         
				         if (id == j) {
					         System.out.println();
				        	 System.out.println( "Operating on Row: " + id );	
					         time1 = rs.getLong(2);
					         latitude1 = deg2rad(rs.getDouble(3));
					         longitude1 = deg2rad(rs.getDouble(4));		        	 
					         System.out.println( "Time = " + time1 );
					         System.out.println( "Latitude = " + rad2deg(latitude1) );
					         System.out.println( "Longitude = " + rad2deg(longitude1) );
				         }
				         
				         else if (id == (j+1)) {
				        	 
				        	 System.out.println( "Operating on Row: " + id );	
				        	 time2 = rs.getLong(2);
				        	 bearing = deg2rad(rs.getDouble(5));
				        	 speed = rs.getDouble(6);
							 
//				        	 delta_t = time2-time1;
				        	 delta_t = 10;
					         distance = speed/delta_t;
					         
					         latitude2 = rad2deg(Math.asin( Math.sin(latitude1)*Math.cos(distance/R) + 
							              Math.cos(latitude1)*Math.sin(distance/R)*Math.cos(bearing) ));				        	 
							 longitude2 = rad2deg(longitude1 + Math.atan2(Math.sin(bearing)*Math.sin(distance/R)*Math.cos(latitude1), 
					                     Math.cos(distance/R)-Math.sin(latitude1)*Math.sin(latitude2) ));	
							 
							 delta_lat = latitude2-rad2deg(latitude1);
							 delta_lon = longitude2-rad2deg(longitude1);
							 
					         System.out.println( "Bearing = " + rad2deg(bearing) );
					         System.out.println( "Speed = " + speed );		    
					         System.out.println( "Latitude2 = " + latitude2 );		
					         System.out.println( "Longitude2 = " + longitude2 );		
					         System.out.println("-------------------------");
					         System.out.println("(latitude1, longitude1) = (" + rad2deg(latitude1) + "," + rad2deg(longitude1) + ")");
					         System.out.println("(latitude2, longitude2) = (" + latitude2 + "," + longitude2 + ")");
					         System.out.println("Delta_Latitude = " + delta_lat);
					         System.out.println("Delta_Longitude = " + delta_lon);
					         System.out.println();
					        
					         //push calculation back to database
					         String sql1 = "UPDATE ecen489_project2_data set latitude = " + latitude2 + " where _id=" + (j+1) + ";'";
					         stmt.execute(sql1);
					         String sql2 = "UPDATE ecen489_project2_data set longitude = " + longitude2 + " where _id=" + (j+1) + ";'";
					         stmt.execute(sql2);
					         System.out.println("Row " + id + "updated!");					   
					         c.commit();
					         
					         sql3 = "INSERT INTO 1kMI1PaLFXtXuP06qk9SX7nARPeLehs1Jw5agd1M"+ "("
					            		+ "time, "
					            		+ "latitude,"
					            		+ "longitude,"
					            		+ "bearing,"
					            		+ "speed" + 
					            		") VALUES ("
					            		+ time1 + ", "
					            		+ latitude1 + ", "
					            		+ longitude1 + ", "
					            		+ bearing + ", "
					            		+ speed + ");";
					            
					         sql4 = "INSERT INTO 1kMI1PaLFXtXuP06qk9SX7nARPeLehs1Jw5agd1M"+ " ("
					            		+ "time, "
					            		+ "latitude,"
					            		+ "longitude,"
					            		+ "bearing,"
					            		+ "speed" + 
					            		") VALUES ("
					            		+ time2 + ", "
					            		+ latitude2 + ", "
					            		+ longitude2 + ", "
					            		+ bearing + ", "
					            		+ speed + ");";
					         
					            HttpContent content1 = ByteArrayContent.fromString(null, "sql=" + sql3);
					            HttpRequest httpRequest1 = client.getRequestFactory().buildPostRequest(new GenericUrl("https://www.googleapis.com/fusiontables/v1/query"), content1);
					            httpRequest1.execute();
					            
					            HttpContent content2 = ByteArrayContent.fromString(null, "sql=" + sql4);
					            HttpRequest httpRequest2 = client.getRequestFactory().buildPostRequest(new GenericUrl("https://www.googleapis.com/fusiontables/v1/query"), content2);
					            httpRequest2.execute();
				         }
			         }
//		         }
		         
		         System.out.println();

		      }
		      rs.close();
		      stmt.close();
		      c.close();
		    } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		    }
	}
//pick out values we want to work with

//perform the calculation
			
			//all degree values must be converted to radians for trigonometric operations
/*
			double lat1 = deg2rad(data.latitude);
			double lon1 = deg2rad(data.longitude);
			double bearing = deg2rad(data.bearing);	
			
			double speed = data.speed;
			double distance = speed/t_sample;
			
			double lat2 = Math.asin( Math.sin(lat1)*Math.cos(distance/R) + 
			              Math.cos(lat1)*Math.sin(distance/R)*Math.cos(bearing) );
			double lon2 = lon1 + Math.atan2(Math.sin(bearing)*Math.sin(distance/R)*Math.cos(lat1), 
			                     Math.cos(distance/R)-Math.sin(lat1)*Math.sin(lat2));
			
			lat2 = rad2deg(lat2);
			lon2 = rad2deg(lon2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
*/
	// function to convert decimal degrees to radians
		private static double deg2rad(double deg) {
			return (deg * Math.PI / 180.0);
		}

	// function to convert radians to decimal degrees
		private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
		}	
	
}



