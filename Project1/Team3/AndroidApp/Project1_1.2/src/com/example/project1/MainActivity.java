package com.example.project1;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;

import com.google.gson.Gson;
import com.zpartal.project1.datapackets.DataPoint;


public class MainActivity extends Activity {
	
	// Setting constants
	private static final String TAG = "Project1";
	private static final char FUNCTION = 's';
	private final String client_id = "Team3";
	
	// Server connection variables
	InetAddress IPaddress;
	int portNumber;
	
	// List with stored data
	ArrayList<DataPoint> dp = new ArrayList<DataPoint>();	
	
	// Setting the bluetooth address of each team 
	private static final String TEAM1_ADDRESS =  "00:12:09:13:99:42";
	private static final String TEAM2_ADDRESS =  "00:15:FF:F2:10:0F";
	private static final String TEAM3_ADDRESS =  "00:12:09:25:92:95";
	private static final String TEAM4_ADDRESS =  "00:12:09:25:92:92";
	private static final String TEAM5_ADDRESS =  "00:12:09:25:96:92";
	
	// Broadcast receivers
	private ArduinoReceiver arduinoReceiver = new ArduinoReceiver();
	private ArduinoConnected arduinoConnected = new ArduinoConnected();
	
	// gson objects
	private Gson gson = new Gson();
	
	// sensor objects
	private DataPoint sensorPoint;
	
	//Definition of date format
	SimpleDateFormat d1 = new SimpleDateFormat("yyyy-mm-dd");
	SimpleDateFormat d2 = new SimpleDateFormat("hh:mm:ss");
	SimpleDateFormat d3 = new SimpleDateFormat("yyyymmdd_hhmmss");
	
	// Time variables
	String run_id; // teamid_yyyymmdd_hhmmss
	String time; // hh:mm:ss
	String date; // yyyy-mm-dd
	
	GPSData gps;
	Timer timer;
	ServerCom server;
	
/**
 * Declarations of application standart functions 
 * (onCreate, onStart, ...)
 * 	  
 */
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//gps initialization
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		gps = new GPSData(locationManager);
		gps.onCreate();

	    //------------------------------------------
	    //TIMER STUFF
	    //tmr = (TextView) findViewById(R.id.tmr);
	    
	    //for gps integration
	    final EditText interval = (EditText) findViewById(R.id.coordInterval);
		
	    
	    //button func
		Button reCoord = (Button) findViewById(R.id.getCoords);
		
		reCoord.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Button reCoord = (Button) view;
	            if (reCoord.getText().equals("Stop")) {
	                timer.stop();
	                reCoord.setText("Start");
	                DPprocess("end");
	            } else {
	            	//------for gps integration
	            	
	            	if (interval.getText().toString().length() > 0)
	            	    timer = new Timer(Integer.parseInt(interval.getText().toString()), 
	            	    		new Callable<Integer>(){
	            	    				public Integer call() {
	            	    						return DPprocess("point");
	            	    				}});
	            	String run_id_info = d3.format(timer.getSQLDate()); // teamid_yyyymmdd_hhmmss
	            	// Defines a new runID every time it start recording
	            	run_id = "team3_"+run_id_info;
	            	//------timerry stuff
	            	timer.start();
	                reCoord.setText("Stop");
	                DPprocess("start");
	            }
			}
		});
	    
	}
	//--------------------------------END OF ONCREATE

	@Override
	protected void onStart() {
		super.onStart();
		// in order to receive broadcasted intents we need to register our receiver
		registerReceiver(arduinoReceiver, new IntentFilter(AmarinoIntent.ACTION_RECEIVED));
		registerReceiver(arduinoConnected, new IntentFilter(AmarinoIntent.ACTION_CONNECTED));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	  /* Request updates at startup */
	  @Override
	  protected void onResume() {
	    super.onResume();
	    gps.onResume();
	  }

	  
	  /* No updates when not open */
	  @Override
	  protected void onPause() {
	    super.onPause();
	    gps.onPause();

	  }
	  
		@Override
		public void onStop(){
			super.onStop();
			// unregistering the registered receiver
			unregisterReceiver(arduinoReceiver);
			unregisterReceiver(arduinoConnected);
		}  
	  
    /**
     * Method to create points from the GPS
     */
	
		  
  public int DPprocess(String attribute) {
		//Time & Date info
	Date d = timer.getSQLDate();
	date = d1.format(d); // yyyy-mm-dd
	time = d2.format(d); // hh:mm:ss
		
	DataPoint dpx = new DataPoint.Builder().client_id(client_id).run_id(run_id).time(time).date(date).latitude(gps.getLat()).longitude(gps.getLng()).attribute(attribute).build();
	dp.add(dpx);
	
	//prints
		Log.d(TAG,dpx.getClient_id());
		Log.d(TAG,dpx.getDate());
		Log.d(TAG,dpx.getTime());
		Log.d(TAG,dpx.getRun_id());
		Log.d(TAG,Double.toString(dpx.getLatitude()));
		Log.d(TAG,Double.toString(dpx.getLongitude()));
		Log.d(TAG,dpx.getAttribute());
		Log.d(TAG,Integer.toString(dp.size()));
	return 0;
}
			  
	/**
	 * Bluetooth connection functions
	 * 
	 */
	
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
	
	public void connectTeam5(View v)
	{
		// Connecting with team 3 arduino
		Amarino.connect(this, TEAM5_ADDRESS);
	}
	
	public void send(String address)
	{
		Amarino.sendDataToArduino(this, address, FUNCTION, "");
	}
	
	public void disconnect(String address)
	{
		Amarino.disconnect(this, address);
	}
	
	
	/**
	 * @author João Marcos
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
				sensorPoint = gson.fromJson(data, DataPoint.class);
				sensorPoint = new DataPoint.Builder().client_id(client_id).run_id(run_id).time(time).date(date).latitude(gps.getLat()).longitude(gps.getLng()).attribute("sensor").sensor_id(sensorPoint.getSensor_id()).sensor_type(sensorPoint.getSensor_type()).sensor_value(sensorPoint.getSensor_value()).build();
				disconnect(address);
				dp.add(sensorPoint);
			}
		}
	}
	
	
	/**
	 * @author João Marcos
	 * Connection feedback
	 */
	public class ArduinoConnected extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// the device address from connected
			final String address = intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);
			send(address);
		}
		
	}

	/**
	 * Server connection and communication
	 *
	 */
	
	// Server Connection
	public void connectToServer(View view) {
		if (isOnline())
		{
			EditText editText = (EditText) findViewById(R.id.edit_IPAddress);
			try{
			IPaddress = InetAddress.getByName(editText.getText().toString());
			}
			catch(UnknownHostException e)
			{
			}
			EditText editText1 = (EditText) findViewById(R.id.edit_PortNumber);
			portNumber = Integer.parseInt(editText1.getText().toString());
			server = new ServerCom(IPaddress, portNumber, dp);
			server.connectToServer();
		}
    }
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
}
