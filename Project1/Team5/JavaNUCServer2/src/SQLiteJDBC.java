


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.UUID;

import com.example.samcarey.AndroidPacket1;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.Fusiontables.Query.Sql;
import com.google.api.services.fusiontables.FusiontablesScopes;
import com.google.api.services.fusiontables.model.Column;
import com.google.api.services.fusiontables.model.Table;

public class SQLiteJDBC
{
	
	  /**
	   * Be sure to specify the name of your application. If the application name is {@code null} or
	   * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
	   */
	  private static final String APPLICATION_NAME = "Project 1/Sensor 5";
	
	  /** Directory to store user credentials. */
	  private static final java.io.File DATA_STORE_DIR =
	      new java.io.File(System.getProperty("user.home"), ".store/sensor_5");
	  
	  /**
	   * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
	   * globally shared instance across your application.
	   */
	  private static FileDataStoreFactory dataStoreFactory;
	
	  /** Global instance of the HTTP transport. */
	  private static HttpTransport httpTransport;
	
	  /** Global instance of the JSON factory. */
	  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	
	  private static Fusiontables fusiontables;
	  
	  static Connection DBconnection;
		@SuppressWarnings("unchecked")
		public static void main( String args[] )
		{
			int defaultPort = 5555;
			
	      try {
	    	  httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	    	  dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
					
			      // authorization
			      Credential credential = authorize();
			      
			   // set up global FusionTables instance
			      fusiontables = new Fusiontables.Builder(
			          httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
			      
			      ArrayList<AndroidPacket1> data;
			      
			    System.out.print("Enter Port (0 for default " + defaultPort + "): ");
			    Scanner sc = new Scanner(System.in);
			    int port = sc.nextInt();
			    if (port == 0) port = 5555;
			    sc.close();
			      
			      
			     while(true){
				System.out.println("Server initializing...");
				ServerSocket server = new ServerSocket(port, 1);
				System.out.println("Server waiting...");
				Socket connection = server.accept();
				System.out.println("Server connected!");
				ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
				System.out.println("Working?");
				data = (ArrayList<AndroidPacket1>) input.readObject();
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
			    storeDatabase(data);
			    DBconnection.close();
			    
			    // Fusion Tables
		        String tableId = createFusionTable();     //create a table
		        insertData(tableId, data);                //insert data into table
		      }
		      
		    }catch(Exception e){
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		    }
		}
	  
	  
	  //-------------------------------------------------------------------------------------
	  
	  /** Authorizes the installed application to access user's protected data. */
	  private static Credential authorize() throws Exception {
	    // load client secrets
	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
	        JSON_FACTORY, new InputStreamReader(
	        		SQLiteJDBC.class.getResourceAsStream("/client_secrets.json")));
	    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
	        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
	      System.out.println(
	          "Enter Client ID and Secret from https://code.google.com/apis/console/?api=fusiontables "
	          + "into fusiontables-cmdline-sample/src/main/resources/client_secrets.json");
	      System.exit(1);
	    }
	    // set up authorization code flow
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        httpTransport, JSON_FACTORY, clientSecrets,
	        Collections.singleton(FusiontablesScopes.FUSIONTABLES)).setDataStoreFactory(
	        dataStoreFactory).build();
	    // authorize
	    return null;//new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	  }
	
	  /** Create a table for the authenticated user. */
	  private static String createFusionTable() {

	    // Create a new table
	    Table table = new Table();
	    table.setName(UUID.randomUUID().toString());
	    table.setIsExportable(false);
	    table.setDescription("Sensor 5 Data");

	    // Set columns for new table
	    table.setColumns(Arrays.asList(
	        new Column().setName("date").setType("TEXT"),
	        new Column().setName("time").setType("TEXT"),
	        new Column().setName("client_id").setType("TEXT"),
	        new Column().setName("run_id").setType("TEXT"),
	        new Column().setName("latitude").setType("NUMBER"),
	        new Column().setName("longitude").setType("NUMBER"),
	        new Column().setName("bearing").setType("NUMBER"),
	        new Column().setName("speed").setType("NUMBER"),
	        new Column().setName("altitude").setType("NUMBER"),
	        new Column().setName("sensor_id").setType("TEXT"),
	        new Column().setName("sensor_type").setType("TEXT"),
	        new Column().setName("sensor_value").setType("NUMBER"),
	        new Column().setName("attribute").setType("TEXT")));
	   

	    // Adds a new column to the table.
	    Fusiontables.Table.Insert t;
		try {
			t = fusiontables.table().insert(table);
			Table r = t.execute();
		    return r.getTableId();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.print("Error creating Fusion Table");
			return null;
		}
	  }
	  
	  /** Inserts a row in the newly created table for the authenticated user. */
	  @SuppressWarnings("static-access")
	private static void insertData(String tableId, ArrayList<AndroidPacket1> data) {
		  try{
			  for (int i = 0 ; i < data.size() ; i++){
				  Sql sql;
				  sql = fusiontables.query().sql("INSERT INTO " + tableId
					+ " (" +
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
					"'" + data.get(i).attribute + "') ");
			
				  sql.execute();
			  }
			  System.out.println("Entry to Fusion Successful!");
		  } catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("Error: Entries to Fusion unsuccessful");
		  }
		  
	  }
	  
	@SuppressWarnings("static-access")
	public static void storeDatabase(ArrayList<AndroidPacket1> data){
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
