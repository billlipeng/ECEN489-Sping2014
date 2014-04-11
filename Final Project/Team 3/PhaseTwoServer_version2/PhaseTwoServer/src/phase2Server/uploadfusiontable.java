package phase2Server;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
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
import com.google.api.services.fusiontables.Fusiontables.Query.Sql;
import com.google.api.services.fusiontables.model.Column;
import com.google.api.services.fusiontables.model.Table;
import com.google.api.services.fusiontables.FusiontablesScopes;
//import com.google.api.services.samples.fusiontables.cmdline.View;



import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;


public class uploadfusiontable {

	
	public static final String APPLICATION_NAME = "FusionTableFinalProject";
	public static final String SERVICE_URL = "https://www.google.com/fusiontables/api/query";
	public static FileDataStoreFactory dataStoreFactory;
	public static HttpTransport httpTransport;
	public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	public static Fusiontables fusiontables;
	public static final java.io.File DATA_STORE_DIR =
		      new java.io.File(System.getProperty("user.home"),".credentials/fusiontable");
	
	private static Credential authorize() throws Exception {
	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
	        JSON_FACTORY, new InputStreamReader(
	            uploadfusiontable.class.getResourceAsStream("/client_secrets.json")));
	  	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        httpTransport, JSON_FACTORY, clientSecrets,
	        Collections.singleton(FusiontablesScopes.FUSIONTABLES)).setDataStoreFactory(
	        dataStoreFactory).build();
	    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	  }
	
	public uploadfusiontable() {  
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
		      Credential credential = authorize();
		      fusiontables = new Fusiontables.Builder(
		          httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();	
			
		} catch (GeneralSecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	}

	
	String createTable(String TableName) throws IOException {
		String TableID;
	    // Create a new table
	    Table table = new Table();
	    table.setName(UUID.randomUUID().toString());
	    table.setIsExportable(false);
	    table.setDescription(TableName);
	    table.setName(TableName);
	    // Set columns for new table
	    table.setColumns(Arrays.asList(new Column().setName("Latitude").setType("LOCATION"),
	        new Column().setName("Longitude").setType("LOCATION"),
	        new Column().setName("RSSI").setType("NUMBER")));

	    // Adds a new column to the table.
	    Fusiontables.Table.Insert t = fusiontables.table().insert(table);
	    Table r = t.execute();

	    TableID = r.getTableId();
	    return TableID;
	  }
	
	
	public void update(String tableID,double[] Latitude,double[] Longitude, double[] RSSI){

//		String tableId= "1tRyntPKpPfNfRFZTo34UkBGDondBNzh5u0rq5-fD";
		String tableId = tableID;
		
		try {
		      
		      
		      String Sqlstring = "";
		      for(int iter=0;iter < Latitude.length ; iter++)
		      {
		    	  Sqlstring =Sqlstring+" INSERT INTO " + tableId + " (Latitude,Longitude,RSSI) "
					        + "VALUES (" + Latitude[iter] + ", " +Longitude[iter] + ", " + RSSI[iter]  + ");";
		    				
		    	  //Sql sql = fusiontables.query().sql(Sqlstring);
			      
				   
		      }
		      
		      try {
			    	HttpContent content = ByteArrayContent.fromString(null, "sql=" + Sqlstring);
			    	HttpRequest httpRequest = fusiontables.getRequestFactory().buildPostRequest(new GenericUrl("https://www.googleapis.com/fusiontables/v1/query"), content);
			    	System.out.println("Writing to fusion table............");
			    	httpRequest.execute();
			      //sql.execute();sql
			    } catch (IllegalArgumentException e) {
					      
			    }
		        
		      return;
		    } catch (IOException e) {
		      System.err.println(e.getMessage());
		    } catch (Throwable t) {
		      t.printStackTrace();
		    }
		    System.exit(1);
		  }
		
}
