package com.jdterrell.p2;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.testing.http.HttpTesting;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.FusiontablesScopes;
import com.google.api.client.googleapis.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.api.services.fusiontables.Fusiontables.*;
import com.jdterrell.p2.AndroidPacket2;


public class FusionTableHandler implements Runnable {
    private static final String APPLICATION_NAME = "ECEN 489 Project Two";
    private static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.home"), ".store/fusiontables_sample");
    private static FileDataStoreFactory dataStoreFactory;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport httpTransport;

    @SuppressWarnings("unused")
    private static Fusiontables client;

    private Config config = new Config();
    private ArrayList<AndroidPacket2> data = null;

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
    
// https://www.google.com/fusiontables/DataSource?docid=1kMI1PaLFXtXuP06qk9SX7nARPeLehs1Jw5agd1M
    }

    public FusionTableHandler(ArrayList<AndroidPacket2> _dataset) {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            Credential credential = authorize();
            client = new Fusiontables.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
            this.data = _dataset;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            if (data.size() <= 500) {
                String sql = createMultipleInsertSQL(data);
                HttpContent content = ByteArrayContent.fromString(null, "sql=" + sql);
                HttpRequest httpRequest = client.getRequestFactory().buildPostRequest(new GenericUrl("https://www.googleapis.com/fusiontables/v1/query"), content);
                httpRequest.execute();
            }
            else {
                ArrayList<List<AndroidPacket2>> listolists = new ArrayList<List<AndroidPacket2>>();
                int binSize = 500;
                int numBins = (data.size() + binSize - 1) / binSize;
                for (int i = 0; i < numBins; i++) {
                    int start = i*binSize;
                    int end = Math.min(start + binSize, data.size());
                    listolists.add(data.subList(start, end));
                }
                for (List<AndroidPacket2> dps : listolists) {
                    String sql = createMultipleInsertSQL((new ArrayList<AndroidPacket2>(dps)));
                    HttpContent content = ByteArrayContent.fromString(null, "sql=" + sql);
                    HttpRequest httpRequest = client.getRequestFactory().buildPostRequest(new GenericUrl("https://www.googleapis.com/fusiontables/v1/query"), content);
                    httpRequest.execute();
                    Thread.sleep(1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String packetToString(AndroidPacket2 packet){
    	return
    		"'" + packet.time + "', " +	
     	 		  packet.latitude + ", " +
     	 		  packet.longitude + ", " +
     	 		  packet.bearing + ", " +
     	 		  packet.speed + ", " +
     	 		  packet.accel_x + ", " +
     	 		  packet.accel_y + ", " +
     	 		  packet.accel_z + "y " +
     	 		  packet.gyro_x + "', " +
     	 		  packet.gyro_y + "', " +
     	 		  packet.gyro_z + ", " +
     	 		  packet.orientation_A + ", " +	
     	 		  packet.orientation_P + ", " +
     	 		  packet.orientation_R + ", " +
     	 		  packet.rotVec_X + "', " +
     	 		  packet.rotVec_Y + "', " +
     	 		  packet.rotVec_Z + ", " +
     	 		  packet.rotVec_C + ", " +
     	 		  packet.linACC_X + ", " +
     	 		  packet.linACC_Y + ", " +
     	 		  packet.linACC_Z + ", " +
     	 		  packet.gravity_X + ", " +
     	 		  packet.gravity_Y + ", " +     	 		  
     	 		  packet.gravity_Z + "' ";
    }
    
    
    // Helper function to generate sql for adding a AndroidPacket2 to the db
    public String createInsertSQL(AndroidPacket2 dp) {
        //  INSERT INTO Customers (CustomerName, ContactName, Address, City, PostalCode, Country)
        //  VALUES ('Cardinal','Tom B. Erichsen','Skagen 21','Stavanger','4006','Norway');
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(config.FUSION_TABLE_NAME);
        query.append(" (");
        String sep = "";
        for (String s : config.COLUMN_NAMES) {
            query.append(sep);
            query.append(s);
            sep = ",";
        }
        query.append(") VALUES (");
        query.append(packetToString(dp));
        query.append(");");
        return query.toString();
    }

    // Helper function to generate sql for adding a AndroidPacket2 to the db
    public String createMultipleInsertSQL(ArrayList<AndroidPacket2> dps) {
        StringBuilder query = new StringBuilder();
        for (AndroidPacket2 dp : dps) {
            query.append(createInsertSQL(dp));
        }
        System.out.println(query);
        return query.toString();
    }
}