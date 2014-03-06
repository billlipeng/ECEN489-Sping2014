package com.jdterrell.p2;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.FusiontablesScopes;
import com.google.api.services.fusiontables.model.Column;
import com.google.api.services.fusiontables.model.Table;
import com.google.api.services.fusiontables.model.TableList;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Main class for the Fusion Tables API command line sample.
 * Demonstrates how to make an authenticated API call using OAuth 2 helper classes.
 */
public class FusionTable {

    /**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "ECEN 489 Project One";

    /** Directory to store user credentials. */
    private static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.home"), ".store/fusiontables_sample");

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    @SuppressWarnings("unused")
    private static Fusiontables client;

    /** Authorizes the installed application to access user's protected data. */
    private static Credential authorize() throws Exception {
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(FusionTable.class.getResourceAsStream("/client_secrets.json")));
        if (clientSecrets.getDetails().getClientId().startsWith("Enter") ||
                clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            System.out.println(
                    "Overwrite the src/main/resources/client_secrets.json file with the client secrets file "
                            + "you downloaded from the Quickstart tool or manually enter your Client ID and Secret "
                            + "from https://code.google.com/apis/console/?api=fusiontables#project:42539874137 "
                            + "into src/main/resources/client_secrets.json");
            System.exit(1);
        }

        // Set up authorization code flow.
        // Ask for only the permissions you need. Asking for more permissions will
        // reduce the number of users who finish the process for giving you access
        // to their accounts. It will also increase the amount of effort you will
        // have to spend explaining to users what you are doing with their data.
        // Here we are listing all of the available scopes. You should remove scopes
        // that you are not actually using.
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

    public static void main(String[] args) {
        try {
            // initialize the transport
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            // initialize the data store factory
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

            // authorization
            Credential credential = authorize();

            // set up global Fusiontables instance
            client = new Fusiontables.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();

            System.out.println("Success! Now add code here.");
//            createTable();
            listTables();


        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(1);
    }

    /** Create a table for the authenticated user. */
    private static String createTable() throws IOException {
//        FusionView.header("Create Sample Table");

        // Create a new table
        Table table = new Table();
        table.setName("ECEN489-ProjectOne-Table");
        table.setIsExportable(false);
        table.setDescription("Sample Table");

        // Set columns for new table
        table.setColumns(Arrays.asList(new Column().setName("Text").setType("STRING"),
                new Column().setName("Number").setType("NUMBER"),
                new Column().setName("Location").setType("LOCATION"),
                new Column().setName("Date").setType("DATETIME")));

        // Adds a new column to the table.
        Fusiontables.Table.Insert t = client.table().insert(table);
        Table r = t.execute();

//        FusionView.show(r);

        return r.getTableId();
    }

    /** List tables for the authenticated user. */
    private static void listTables() throws IOException {
 //       FusionView.header("Listing My Tables");

        // Fetch the table list
        Fusiontables.Table.List listTables = client.table().list();
        TableList tablelist = listTables.execute();

        if (tablelist.getItems() == null || tablelist.getItems().isEmpty()) {
            System.out.println("No tables found!");
            return;
        }

        for (Table table : tablelist.getItems()) {
//            FusionView.show(table);
//            FusionView.separator();
        }
    }

}
