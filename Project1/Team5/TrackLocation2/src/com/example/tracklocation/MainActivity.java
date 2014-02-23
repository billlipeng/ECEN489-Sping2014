package com.example.tracklocation;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.samcarey.AndroidPacket1;
import com.example.samcarey.ObjectItem;
import com.example.samcarey.SensorData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import com.google.gson.*;
import at.abraxas.amarino.*;

public class MainActivity extends FragmentActivity implements
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener {

	//Info
	private Integer port = 5555;
	private String ip = "10.200.213.22";
	String teamid = "team5";
	String run_id;
	public static final String PREFS_NAME = "MyPrefsFile";
	
	
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	
	// gson objects
		private Gson gson = new Gson();
	
	private static final char FUNCTION = 's';
	// Setting the bluetooth address of each team 
		private static final String TEAM1_ADDRESS =  "00:12:09:13:99:42";
		private static final String TEAM2_ADDRESS =  "00:15:FF:F2:10:0F";
		private static final String TEAM3_ADDRESS =  "00:12:09:25:92:95";
		private static final String TEAM4_ADDRESS =  "00:12:09:25:92:92";
		private static final String TEAM5_ADDRESS =  "00:12:09:25:96:92";

		// Broadcast receivers
		private ArduinoReceiver arduinoReceiver = new ArduinoReceiver();
		private ArduinoConnected arduinoConnected = new ArduinoConnected();
		
		@Override
		protected void onStart() {
			super.onStart();
			// in order to receive broadcasted intents we need to register our receiver
			registerReceiver(arduinoReceiver, new IntentFilter(AmarinoIntent.ACTION_RECEIVED));
			registerReceiver(arduinoConnected, new IntentFilter(AmarinoIntent.ACTION_CONNECTED));
		}
		@Override
		public void onStop(){
			super.onStop();
			// unregistering the registered receiver
			unregisterReceiver(arduinoReceiver);
			unregisterReceiver(arduinoConnected);
		}
		
		/**
		 * Bluetooth connection functions
		 * 
		 */
/*
		public void connectTeam1(View v)
		{
			// Connecting with team 1 arduino
			Amarino.connect(this, TEAM1_ADDRESS);
		}

		public void connectTeam2(View v)
		{
			// Connecting with team 2 arduino
			Amarino.connect(this, TEAM2_ADDRESS);
		}

		public void connectTeam3(View v)
		{
			// Connecting with team 3 arduino
			Amarino.connect(this, TEAM3_ADDRESS);
		}

		public void connectTeam4(View v)
		{
			// Connecting with team 3 arduino
			Amarino.connect(this, TEAM4_ADDRESS);
		}
*/
		public void connectTeam5(View v)
		{
			// Connecting with team 3 arduino
			Amarino.connect(this, TEAM5_ADDRESS);
		}

		public void send(String address)
		{
			//Amarino.connect(this, TEAM5_ADDRESS);
			Amarino.sendDataToArduino(this, address, FUNCTION, "");
		}

		public void disconnect(String address)
		{
			Amarino.disconnect(this, address);
		}


		/**
		 * @author
		 * setting the broadcast receiver to receive data from the arduino
		 */
		public class ArduinoReceiver extends BroadcastReceiver {

			@Override
			public void onReceive(Context context, Intent intent) {
				String data = null;

				// the device address from which the data was sent, we don't need it here but to demonstrate how you retrieve it
				final String address = intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);

				// the type of data which is added to the intent
				final int dataType = intent.getIntExtra(AmarinoIntent.EXTRA_DATA_TYPE, -1);

				// Receiving the string from Amarino
				if (dataType == AmarinoIntent.STRING_EXTRA){
					// Receiving in the respective object, one for each sensor
					data = intent.getStringExtra(AmarinoIntent.EXTRA_DATA);
					sensorData = gson.fromJson(data, SensorData.class);
					disconnect(address);
					newSensorData = true;
				}
			}
		}


		/**
		 * @author
		 */
		public class ArduinoConnected extends BroadcastReceiver {
			@Override
			public void onReceive(Context context, Intent intent) {
				// the device address from connected
				final String address = intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);

				send(address);
			}

		}

		public void readSensor1(View view){
			send(TEAM1_ADDRESS);
		}
		
		public void readSensor2(View view){
			send(TEAM2_ADDRESS);
		}
		
		public void readSensor3(View view){
			send(TEAM3_ADDRESS);
		}
		
		public void readSensor4(View view){
			send(TEAM4_ADDRESS);
		}
		
		public void readSensor5(View view){
			send(TEAM5_ADDRESS);
		}
		
		
	
	
	
	
	
	
	
	
	
	
	private final static int INTERVAL = 1000 * 5; // 5 seconds
	
	public final static String EXTRA_MESSAGE = "com.example.tracklocation.MESSAGE";
	
	AlertDialog alertDialogStores;
	LocationClient locationClient;
	ArrayList<ObjectItem> locations;
	ArrayList<AndroidPacket1> data;
	int count = 1;
	boolean first = true;
	Handler handler;
	Boolean begun = false;
	Boolean newSensorData = false;
	SensorData sensorData;
	String addr;
	Boolean startPoint = false;
	Boolean endPoint = false;
	
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	/*
	private static final String TEAM1 =  "00:12:09:13:99:42";
	private static final String TEAM2 =  "00:15:FF:F2:10:0F";
	private static final String TEAM3 =  "00:12:09:25:92:95";
	private static final String TEAM4 =  "00:12:09:25:92:92";
	private static final String TEAM5 =  "00:12:09:25:96:92";  
	*/
	/*private static final String id = "Team 5";
	private static final String Tag = "Project 1";*/

	boolean receive = false;
	//private static final char function = 's';
	/*
	private ArduinoConnected arduinoconn = new ArduinoConnected(); //need this
	private ArduinoReciever arduinorec = new ArduinoReciever(); //need this
	*/
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		if (servicesConnected()){
			setContentView(R.layout.activity_main);
		}else{
			display("GPS OFFLINE");
		}
		
		if (first){
			handler = new Handler();
			data = new ArrayList<AndroidPacket1>();
			begun = false;
			locationClient = new LocationClient(this, this, this);
			locationClient.connect();
			first = false;
		}
		
		 View.OnClickListener handler = new View.OnClickListener(){
			 public void onClick(View v) {
				 //switch (v.getId()) {
				 if (R.id.button1 == v.getId()){
					 showPopUp();
				 }
			 }
		 };
		 findViewById(R.id.button1).setOnClickListener(handler);
		 
		 /*
		// Restore preferences
	       SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	       String ip = settings.getString("ip", "ip");
	       port = settings.getInt("port", port);
	       */
		 
	}
	/*
	@Override
	protected void onStart(){ //need this 
		super.onStart();
		registerReceiver(arduinorec, new IntentFilter(AmarinoIntent.ACTION_RECEIVED));
		registerReceiver(arduinoconn, new IntentFilter(AmarinoIntent.ACTION_CONNECTED));
		Amarino.connect(this, "00:12:09:25:96:92");
		//display("startingAmarino...");
	}
	
	@Override
	public void onStop(){ //need this
		super.onStop();
		unregisterReceiver(arduinorec);
		unregisterReceiver(arduinoconn);
		Amarino.disconnect(this, "00:12:09:25:96:92");
	}
	
	//Connect the address of the device it sees
	public void connectT1(View v) //ignore other teams for now
	{
		Amarino.connect(this, TEAM1);
	}public void connectT2(View v)
	{
		Amarino.connect(this, TEAM2);
	}public void connectT3(View v)
	{
		Amarino.connect(this, TEAM3);
	}
	public void connectT4(View v)
	{
		Amarino.connect(this, TEAM4);
	}
	public void connectT5(View v)
	{
		Amarino.connect(this, TEAM5);
	}
	public void senddata(String address)
	{
		Amarino.sendDataToArduino(this, "00:12:09:25:96:92", function, "");
	}
	public void disconnect(String address)
	{
		Amarino.disconnect(this, address);
	}
	
	public class ArduinoReciever extends BroadcastReceiver { //need this
		@Override
		public void onReceive(Context context, Intent intent){
			final String action = intent.getAction();
			final String addr = intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);
			
			display("0");
			if (AmarinoIntent.ACTION_CONNECTED.equals(action)){ 
				// connection has been established 
				display("1");
				} 
				else if (AmarinoIntent.ACTION_DISCONNECTED.equals(action)){ 
				// disconnected from a device
					display("2");
				} 
				else if (AmarinoIntent.ACTION_CONNECTION_FAILED.equals(action)){ 
				// connection attempt was not successful 
					display("3");
				} 
				else if (AmarinoIntent.ACTION_PAIRING_REQUESTED.equals(action)){ 
				// a notification message to pair the device has popped up 
					display("4");
				}
			
			
			
			//display("Sensor Data?");
			String jsonString = null;
			//String result = null;
			
			//final String addr = intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);
			
			final int dataType = intent.getIntExtra(AmarinoIntent.EXTRA_DATA_TYPE,-1);	
			
			if (dataType == AmarinoIntent.STRING_EXTRA){
			
				jsonString = intent.getStringExtra(AmarinoIntent.EXTRA_DATA);
				
				if (jsonString != null){
					//display("Sensor Data!");
					receive = true;
					Gson gson = new Gson();
					sensorData = gson.fromJson(jsonString, SensorData.class);
					//AndroidPacket1 sensordata = gson.fromJson(data, AndroidPacket1.class); //
					//sensordata = sensordata.getsensor_id().getsensor_type().getsensor_value(); //sensordata parse data into respective fields here
					//dp.add(sensordata);	
					newSensorData = true;
					receive = false;
				}
			}
		}
	}

	public class ArduinoConnected extends BroadcastReceiver{ //need this
		@Override
		public void onReceive(Context context, Intent intent){
			addr = intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);
			senddata(addr);
		}
	}
*/
	
	
	public void beginCollection(View view){
		//display("Beginning");
		startPoint = true;
		data = new ArrayList<AndroidPacket1>();
		begun = true;
		run_id = run_id();
		LogLoop.run();
		//display("OH NOES!");
	}

	
	Runnable LogLoop = new Runnable(){
		@Override 
	     public void run() {
	          logData();
	          handler.postDelayed(this, INTERVAL);
	     }
	};
	
	public String attribute(){
		if (startPoint){
			startPoint = false;
			return "start";
		}else if(endPoint){
			endPoint = false;
			return "end";
		}else if (newSensorData){
			return "sensor";
		}else{
			return "point";
		}
	}
	
	public String sensor_id(){
		if (newSensorData)
			return sensorData.sensor_id;
		else
			return null;
	}
	
	public String sensor_type(){
		if (newSensorData)
			return sensorData.sensor_type;
		else
			return null;
	}
	
	public Double sensor_value(){
		if (newSensorData)
			return sensorData.sensor_value;
		else
			return 0.0;
	}
	
	public Double bearing(){
		return 0.0;
	}
	
	public Double speed(){
		return 0.0;
	}
	
	public Double altitude(){
		return 0.0;
	}
	
	public String run_id(){
		Calendar calendar = Calendar.getInstance();
		String run_id = teamid + "_" + 
				calendar.get(Calendar.YEAR) +
				format(calendar.get(Calendar.MONTH)) +
				format(calendar.get(Calendar.DAY_OF_MONTH)) + "_" +
				format(calendar.get(Calendar.HOUR_OF_DAY)) +
				format(calendar.get(Calendar.MINUTE)) +
				format(calendar.get(Calendar.SECOND));
		return run_id;
	}
	
	public String timestamp(){
		Calendar calendar = Calendar.getInstance();
		String timestamp =	format(calendar.get(Calendar.HOUR_OF_DAY)) + "-" +
						  	format(calendar.get(Calendar.MINUTE)) + "-" +
						  	format(calendar.get(Calendar.SECOND));
		return timestamp;
	}
	
	public String date(){
		Calendar calendar = Calendar.getInstance();
		String date =	format(calendar.get(Calendar.YEAR)) + "-" +
						format(calendar.get(Calendar.MONTH)) + "-" +
						format(calendar.get(Calendar.DAY_OF_MONTH));
		return date;
	}
	
	public String format(Integer num){
		if (num < 10) return ("0"+ num);
		else return num.toString();
	}
	
	public void logData(){
		Location currentLocation = locationClient.getLastLocation();
		
		AndroidPacket1 packet = 
				new AndroidPacket1(
						run_id(),
						timestamp(),
						date(),
						attribute(),
						currentLocation.getLatitude(),
						currentLocation.getLongitude(),
						sensor_id(),
						sensor_type(),
						sensor_value(),
						bearing(),
						speed(),
						altitude());
		if (newSensorData) newSensorData = false;
		data.add(packet);
		begun = true;
		//Integer integer = data.size();
		//(integer.toString());
	}
	
	public void showPopUp(){
		if (begun){
			locations = new ArrayList<ObjectItem>();
			
			for (int i = 0 ; i < data.size(); ++i){
				locations.add(new ObjectItem(count, data.get(i).latitude,data.get(i).longitude));
				count++;
			}
			
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
	}

	public void sendMessage(View view) {
		if (begun){
			handler.removeCallbacks(LogLoop);
			if (netCheck()){
				//display("Client waiting...");
				endPoint = true;
				logData();
				endPoint = false;
				new Send().execute();
			}else{
				display("Error");
			}
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
        		EditText editText = (EditText) findViewById(R.id.editText1);
        		String text = editText.getText().toString();
        		if (text == "Server Port") text = port.toString();
        	    port = Integer.parseInt(text);
        		editText = (EditText) findViewById(R.id.editText2);
        		text = editText.getText().toString();
        		if (text == "Server IP") text = ip;
        		ip = text;
        		Socket connection = new Socket(ip, port);
        		//display("Client connected!");
    			
    			ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
    			output.writeObject(data);
    			output.flush();
    			//Integer integer = data.size();
    			//display(integer.toString());
    			output.close();
    			connection.close();
    			//display("Data sent");
    			data = new ArrayList<AndroidPacket1>();
    			begun = false;
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
            int requestCode, int resultCode, Intent data2) {
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