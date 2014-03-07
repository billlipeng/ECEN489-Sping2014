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
import com.sun.corba.se.spi.logging.CORBALogDomains;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.api.services.fusiontables.Fusiontables.*;

public class FusionUpload implements Runnable {

    private String FUSION_TABLE_NAME = "1wBCyK6GHyFahftu8kGymu4nFmPHTZhKmB_N2e7xr";
    private ArrayList<String> COLUMN_NAMES = new ArrayList<String>();

    private static final String APPLICATION_NAME = "ECEN 489 Project Two";
    private static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.home"), ".store/fusiontables_sample");
    private static FileDataStoreFactory dataStoreFactory;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport httpTransport;

    @SuppressWarnings("unused")
    private static Fusiontables client;

    private ArrayList<DataPoint> dataset = null;

    private static Credential authorize() throws Exception {
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(FusionTable.class.getResourceAsStream("/client_secrets.json")));
        if (clientSecrets.getDetails().getClientId().startsWith("Enter") ||
                clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            System.out.println("Fix client.json");
            System.exit(1);
        }

        Set<String> scopes = new HashSet<String>();
        scopes.add(FusiontablesScopes.FUSIONTABLES);
        scopes.add(FusiontablesScopes.FUSIONTABLES_READONLY);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, scopes)
                .setDataStoreFactory(dataStoreFactory)
                .build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public FusionUpload(ArrayList<DataPoint> _dataset) {
        COLUMN_NAMES.add("time");
        COLUMN_NAMES.add("longitude");
        COLUMN_NAMES.add("latitude");
        COLUMN_NAMES.add("bearing");
        COLUMN_NAMES.add("speed");
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            Credential credential = authorize();
            client = new Fusiontables.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
            this.dataset = _dataset;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void run() {
        Fusiontables.Query.Sql delSQL = null;
        try {
            delSQL = client.query().sql("DELETE FROM " + FUSION_TABLE_NAME);
            delSQL.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (dataset.size() <= 500) {
                // https://groups.google.com/forum/#!topic/fusion-tables-users-group/CqhjtdhHea4
                String sql = createMultipleInsertSQL(dataset);
                HttpContent content = ByteArrayContent.fromString(null, "sql=" + sql);
                HttpRequest httpRequest = client.getRequestFactory().buildPostRequest(new GenericUrl("https://www.googleapis.com/fusiontables/v1/query"), content);
                httpRequest.execute();
            }
            else {
                ArrayList<List<DataPoint>> listolists = new ArrayList<List<DataPoint>>();
                int binSize = 500;
                int numBins = (dataset.size() + binSize - 1) / binSize;
                for (int i = 0; i < numBins; i++) {
                    int start = i*binSize;
                    int end = Math.min(start + binSize, dataset.size());
                    listolists.add(dataset.subList(start, end));
                }
                for (List<DataPoint> dps : listolists) {
                    String sql = createMultipleInsertSQL((new ArrayList<DataPoint>(dps)));
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

    // Helper function to generate sql for adding a datapoint to the db
    public String createInsertSQL(DataPoint dp) {
        //  INSERT INTO Customers (CustomerName, ContactName, Address, City, PostalCode, Country)
        //  VALUES ('Cardinal','Tom B. Erichsen','Skagen 21','Stavanger','4006','Norway');
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(FUSION_TABLE_NAME);
        query.append(" (");
        String sep = "";
        for (String s : COLUMN_NAMES) {
            query.append(sep);
            query.append(s);
            sep = ",";
        }
        query.append(") VALUES (");
        query.append(dp.toString());
        query.append(");");
        return query.toString();
    }

    // Helper function to generate sql for adding a datapoint to the db
    public String createMultipleInsertSQL(ArrayList<DataPoint> dps) {
        StringBuilder query = new StringBuilder();
        for (DataPoint dp : dps) {
            query.append(createInsertSQL(dp));
        }
        return query.toString();
    }
}
