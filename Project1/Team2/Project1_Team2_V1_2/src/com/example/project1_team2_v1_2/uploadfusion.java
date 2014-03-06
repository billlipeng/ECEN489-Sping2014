package com.example.project1_team2_v1_2;



import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.Fusiontables.Query.Sql;
import com.google.api.services.fusiontables.FusiontablesScopes;


public class uploadfusion {

	
	public static final String APPLICATION_NAME = "ecen-489-team-2";
	public static final String SERVICE_URL = "https://www.google.com/fusiontables/api/query";
	public static FileDataStoreFactory dataStoreFactory;
	public static HttpTransport httpTransport;
	public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	public static Fusiontables fusiontables;
	public static final java.io.File DATA_STORE_DIR =
		      new java.io.File(System.getProperty("user.home"),".store/fusiontable");
	
	private static Credential authorize() throws Exception {
	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
	        JSON_FACTORY, new InputStreamReader(
	            uploadfusion.class.getResourceAsStream("/client_secrets.json")));
	  	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        httpTransport, JSON_FACTORY, clientSecrets,
	        Collections.singleton(FusiontablesScopes.FUSIONTABLES)).setDataStoreFactory(
	        dataStoreFactory).build();
	    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	  }
	
	
//	public void update(String[] ClientID, String[] RunID, String[] Timestamp, String[] Date, String[] Latitude, String[] Longitude, String[] SensorID, String[] SensorType, Double[] SensorValue, String[] Attribute){
//
//		String tableId= "1qTs6ydcfsj1nnYZy9oKKaPnIXZLstaJCH-yhIFE";
//		
//		try {
//		      httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//		      dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
//		      Credential credential = authorize();
//		      fusiontables = new Fusiontables.Builder(
//		          httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();	
//		      
//		      for(int iter=0;iter < ClientID.length ; iter++)
//		      {
//		      Sql sql = fusiontables.query().sql("INSERT INTO " + tableId + " (ClientID,RunID,Timestamp,Date,Latitude,Longitude,SensorID,SensorType,SensorValue,Attribute) "
//				        + "VALUES ('" + ClientID[iter] + "', '" + RunID[iter] + "', '" + Timestamp[iter] + "', '" + Date[iter] + "', " + Latitude[iter] + ", " + Longitude[iter] + ", '" + SensorID[iter] + "', '" + SensorType[iter] + "', " + SensorValue[iter] + ", '" + Attribute[iter] + "')");
//		      
//				    try {
//				      sql.execute();
//				    } catch (IllegalArgumentException e) {
//				      
//				    }
//		      }
	public void update(String[] Timestamp, String[] Date, String[] Latitude, String[] Longitude, String[] SensorID, String[] SensorType, Double[] SensorValue){

		String tableId= "1qTs6ydcfsj1nnYZy9oKKaPnIXZLstaJCH-yhIFE";
		
		try {
		      httpTransport =  AndroidHttp.newCompatibleTransport();
		      dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
		      Credential credential = authorize();
		      fusiontables = new Fusiontables.Builder(
		          httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();	
		      
		      for(int iter=0;iter < Timestamp.length ; iter++)
		      {
		      Sql sql = fusiontables.query().sql("INSERT INTO " + tableId + " (Timestamp,Date,Latitude,Longitude,SensorID,SensorType,SensorValue) "
				        + "VALUES ('" + Timestamp[iter] + "', '" + Date[iter] + "', '" + Latitude[iter] + "', '" + Longitude[iter] + "', '" + SensorID[iter] + "', '" + SensorType[iter] + "', " + SensorValue[iter] + ")");
		      
				    try {
				      sql.execute();
				    } catch (IllegalArgumentException e) {
				      
				    }
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