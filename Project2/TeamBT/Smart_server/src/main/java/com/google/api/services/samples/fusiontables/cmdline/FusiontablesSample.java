/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.services.samples.fusiontables.cmdline;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.Fusiontables.Query.Sql;
import com.google.api.services.fusiontables.Fusiontables.Table.Delete;
import com.google.api.services.fusiontables.FusiontablesScopes;
import com.google.api.services.fusiontables.model.Column;
import com.google.api.services.fusiontables.model.Table;
import com.google.api.services.fusiontables.model.TableList;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import jar_project1.Client;
import team2.DataPacket;

public class FusiontablesSample {

  /**
   * Be sure to specify the name of your application. If the application name is {@code null} or
   * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
   */
  private static final String APPLICATION_NAME = "ECEN489-FusionTableDemo/1.0";

  /** Directory to store user credentials. */
  private static final java.io.File DATA_STORE_DIR =
      new java.io.File(System.getProperty("user.home"), ".store/fusion_tables_sample");
  
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

  //-------------------------------------------------------------------------------------
  
  /** Authorizes the installed application to access user's protected data. */
  private static Credential authorize() throws Exception {
    // load client secrets
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
        JSON_FACTORY, new InputStreamReader(
            FusiontablesSample.class.getResourceAsStream("/client_secrets.json")));
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
    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
  }

  //-------------------------------------------------------------------------------------
  //Main PART
  
  
  //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!IMPORTANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  // The Fusion Tables look like slow because Im using the View class to show a lot of infos
  //in the Console. We just need to remove it, and the server will be faster.
  //Just put there because we are in the debugging fase.
  public void Start(DataPacket Packet) {
    try {
     
      //facilities to interface with a website
      httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
     
      // authorization
      Credential credential = authorize();
      
      // set up global FusionTables instance
      fusiontables = new Fusiontables.Builder(
          httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
      
      // run commands
      listTables();
      //THe create is off. We dont need to create every time. If we want to create every
      //time, we just need to change insertData(.adasdasdadsa) and showRows(asasdasd) to:
      //insertData(tableID,longitude,latitude)
      //showRows(tableID)
      
      
      //THe table is not complete, but I already edited it, so I know how to do. Just did not
      //finish it. Too tired today. (it is like a normal sql to update, create, etc...)
  //    String tableId = createTable();     //create a table
      insertData("1A-xFDlbvccPxgQ_5akG_QQDPZm8bArRRWLpmLX9l", Packet);                //insert data into table
      showRows("1A-xFDlbvccPxgQ_5akG_QQDPZm8bArRRWLpmLX9l");                  //not quite working yet
//      deleteTable(tableId);
      // success!
      return;
    } catch (IOException e) {
      System.err.println(e.getMessage());
    } catch (Throwable t) {
      t.printStackTrace();
    }
    System.exit(1);
  }

  /**
   * @param tableId
   * @throws IOException
   */
  private static void showRows(String tableId) throws IOException {
    View.header("Showing Rows From Table");

    Sql sql = fusiontables.query().sql("SELECT time,position,bearing,speed,accel,orientation,rotVec,linAcc,gravity,gyro FROM " + tableId);

    try {
      sql.execute();
    } catch (IllegalArgumentException e) {
      // For google-api-services-fusiontables-v1-rev1-1.7.2-beta this exception will always
      // been thrown.
      // Please see issue 545: JSON response could not be deserialized to Sqlresponse.class
      // http://code.google.com/p/google-api-java-client/issues/detail?id=545
    }
  }

  /** List tables for the authenticated user. */
  private static void listTables() throws IOException {
    View.header("Listing My Tables");

    // Fetch the table list
    Fusiontables.Table.List listTables = fusiontables.table().list();
    TableList tablelist = listTables.execute();

    if (tablelist.getItems() == null || tablelist.getItems().isEmpty()) {
      System.out.println("No tables found!");
      return;
    }

    for (Table table : tablelist.getItems()) {
      View.show(table);
      View.separator();
    }
  }

  
  //Never used because we dont create a new table all the time.
  /** Create a table for the authenticated user. */
  private static String createTable() throws IOException {
    View.header("Create Sample Table");

    // Create a new table
    Table table = new Table();
    table.setName("Aloha");//UUID.randomUUID().toString());
    table.setIsExportable(false);
    table.setDescription("Sample Table");

    // Set columns for new table
    table.setColumns(Arrays.asList(/*new Column().setName("Arroz").setType("STRING"),
    		new Column().setName("Text").setType("STRING"),
            new Column().setName("Number").setType("NUMBER"),
            new Column().setName("Location").setType("LOCATION"),
            new Column().setName("Date").setType("DATETIME")));
    		*/
    	//There will be the final version.	
    	new Column().setName("time").setType("STRING"),
    	new Column().setName("position").setType("LOCATION"),
    	new Column().setName("bearing").setType("STRING"),
    	new Column().setName("speed").setType("STRING"),
    	new Column().setName("accel").setType("STRING"),
   		new Column().setName("orientation").setType("STRING"),
    	new Column().setName("rotVec").setType("STRING"),
   		new Column().setName("linAcc").setType("STRING"),
        new Column().setName("gravity").setType("STRING"),
        new Column().setName("gyro").setType("STRING")));
    // Adds a new column to the table.
    Fusiontables.Table.Insert t = fusiontables.table().insert(table);
    Table r = t.execute();
    View.show(r);

    return r.getTableId();
  }

  /** Inserts a row in the newly created table for the authenticated user. */
  private static void insertData(String tableId, DataPacket Data ) throws IOException {
	  String Locat = Double.toString(Data.getLatitude()) +", " + Double.toString(Data.getLongitude());
	  String accel = Float.toString(Data.getAccelX()) +", " + Float.toString(Data.getAccelY()) +", " + Float.toString(Data.getAccelZ());
	  String orientation = Float.toString(Data.getOrientationA()) +", " + Float.toString(Data.getOrientationB()) +", " + Float.toString(Data.getOrientationC());
	  String rotvec = Float.toString(Data.getRotVecX()) +", " + Float.toString(Data.getRotVecY()) +", " + Float.toString(Data.getRotVecZ())+", 0";
	  String linacc = Float.toString(Data.getLinAccX()) +", " + Float.toString(Data.getLinAccY()) +", " + Float.toString(Data.getLinAccZ());
	  String gravity = Float.toString(Data.getGravityX()) +", " + Float.toString(Data.getGravityY()) +", " + Float.toString(Data.getGravityZ());
	  String gyro = Float.toString(Data.getGyroX()) +", " + Float.toString(Data.getGyroY()) +", " + Float.toString(Data.getGyroZ());
	  String time = Long.toString(Data.getTime());
	  
	  
	  System.out.printf("%s", Locat);
	  Sql sql = fusiontables.query().sql("INSERT INTO " +tableId+ "(time,position,bearing,speed,accel,orientation,rotVec,linAcc,gravity,gyro)"
		  		+ "VALUES ('" +time+"','" +Locat+"','" +Data.getBearing()+"','" +Data.getSpeed()+"','" +accel+"','" +orientation+"','" +rotvec+"','"+linacc+"','"+gravity+"','"+gyro+"')");

	  //	  Sql sql = fusiontables.query().sql("INSERT INTO " +tableId+ "(Arroz,Text,Number,Location,Date)"
	//  		+ "VALUES ('122','ss','4','"+Locat+"','"+new DateTime(new Date())+"')");

    try {
      sql.execute();
    } catch (IllegalArgumentException e) {
      // For google-api-services-fusiontables-v1-rev1-1.7.2-beta this exception will always
      // been thrown.
      // Please see issue 545: JSON response could not be deserialized to Sqlresponse.class
      // http://code.google.com/p/google-api-java-client/issues/detail?id=545
    }
  }

  //Same thing as created tables. Never used.
  /** Deletes a table for the authenticated user. */
  private static void deleteTable(String tableId) throws IOException {
    View.header("Delete Sample Table");
    // Deletes a table
    Delete delete = fusiontables.table().delete(tableId);
    delete.execute();
  }

}



