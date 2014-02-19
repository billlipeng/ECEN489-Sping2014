package com.example.tracklocation;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import com.example.samcarey.*;

public class MainActivity extends FragmentActivity implements
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener {
	
	//Server info
	private int port = 5555;
	private String ip = "10.200.210.215";
	
	public final static String EXTRA_MESSAGE = "com.example.tracklocation.MESSAGE";
	
	AlertDialog alertDialogStores;
	LocationClient locationClient;
	ArrayList<ObjectItem> locations;
	int count = 1;
	boolean first = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		if (servicesConnected()){
			setContentView(R.layout.activity_main);
		}else{
			display("GPS OFFLINE");
		}
		if (first){
			locations = new ArrayList<ObjectItem>();
			locationClient = new LocationClient(this, this, this);
			locationClient.connect();
			first = false;
		}
		
		 View.OnClickListener handler = new View.OnClickListener(){
			 public void onClick(View v) {
				 switch (v.getId()) {
				 case R.id.button1:
					 showPopUp();
					 break;
				 }
			 }
		 };
		 findViewById(R.id.button1).setOnClickListener(handler);
	}
	
	
	
	public void showPopUp(){
		Location currentLocation = locationClient.getLastLocation();
		
		locations.add(new ObjectItem(count, currentLocation.getLatitude(),currentLocation.getLongitude()));
		count++;
		ObjectItem[] location = new ObjectItem[1];
		
		ArrayAdapterItem adapter = new ArrayAdapterItem(this, R.layout.list_view_row_item, locations.toArray(location) );
		ListView listViewItems = new ListView(this);
		listViewItems.setAdapter(adapter);
		listViewItems.setOnItemClickListener(new OnItemClickListenerListViewItem());
		alertDialogStores = new AlertDialog.Builder(MainActivity.this)
			.setView(listViewItems)
			.setTitle("Location")
			.show();
	}

	public void sendMessage(View view) {
		if (netCheck()){
			display("Client waiting...");
			new Send().execute();
		}else{
			display("Error");
		}
	}
	
	public boolean netCheck(){
		ConnectivityManager connMgr = (ConnectivityManager) 
				getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
	}
	
	private class Send extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... args) {
        	try{
        		Socket connection = new Socket(ip, port);
        		display("Client connected!");
    			
    			ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
    			//ObjectItem bam = locations;
    			output.writeObject(locations);
    			output.flush();
    			
    			output.close();
    			connection.close();
    			display("Locations sent");
    		} catch(IOException e){
    		}finally{}
			return null;
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

    }
    
    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }
    
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showErrorDialog(connectionResult.getErrorCode());
        }
    }
	
	public void showErrorDialog(int code){}
	
	
    // Global constants
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
                    break;
                }
        }
     }
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
            // Get the error code
        	int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    errorCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getSupportFragmentManager(),
                        "Location Updates");
            }
            return false;
        }
    }
    
    public void display(String message){
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		intent.putExtra(EXTRA_MESSAGE, message);
	    startActivity(intent);
	}
}