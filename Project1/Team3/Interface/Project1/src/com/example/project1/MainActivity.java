package com.example.project1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;

import com.google.gson.Gson;
import com.zpartal.project1.datapackets.DataPoint;


public class MainActivity extends Activity implements LocationListener{
	
	// Setting constants
	private static final String TAG = "Project1";
	private static final char FUNCTION = 's';
	private final String client_id = "Team3";
	
	// Server connection variables
	Socket connection;
	ObjectOutputStream output;
	ObjectInputStream input;
	InetAddress IPaddress;
	int portNumber;
	
	// List with stored data
	ArrayList<DataPoint> dp = new ArrayList<DataPoint>();	
	
	// Setting the bluetooth address of each team 
	private static final String TEAM1_ADDRESS =  "00:12:09:13:99:42";
	private static final String TEAM2_ADDRESS =  "00:12:09:25:92:95";
	private static final String TEAM3_ADDRESS =  "00:12:09:25:92:95";
	private static final String TEAM4_ADDRESS =  "00:12:09:25:92:95";
	private static final String TEAM5_ADDRESS =  "00:12:09:25:92:95";
	
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
	
	// GPS variables
//	  private TextView latText;
//	  private TextView longText;
	  private LocationManager locationManager;
	  private String provider;
	  private double lat;
	  private double lng;
	
/**
 * Declarations of application standart functions 
 * (onCreate, onStart, ...)
 * 	  
 */
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	    // Get the location manager
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    
	    Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    Location location = null;
	    
	    try{
	        location = locationManager.getLastKnownLocation(provider);
	        } finally {if (location == null) Log.e("location", "manager error" );}
	    
	    if (location != null) {
	        Log.d(TAG, "Provider " + provider + " has been selected.");
	        onLocationChanged(location);
	      } //else {
//	        latText.setText("Location not available");
//	        longText.setText("Location not available");
//	      }
	    
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
	                timerHandler.removeCallbacks(timerRunnable);
	                reCoord.setText("Start");
	            	timeDate("start");
	            } else {
	            	//------for gps integration
	            	
	            	if (interval.getText().toString().length() > 0)
	            	    interv = Integer.parseInt(interval.getText().toString());
	            	Calendar cal1 = new GregorianCalendar();
	            	java.util.Date callTime1 = cal1.getTime();
	            	java.sql.Date sqlDate = new java.sql.Date(callTime1.getTime());
	            	String run_id_info = d3.format(sqlDate); // teamid_yyyymmdd_hhmmss
	            	// Defines a new runID every time it start recording
	            	run_id = "team3_"+run_id_info;
	            	//------timerry stuff
	            	//System.out.println(interv);
	                strtTime = System.currentTimeMillis();
	                timerHandler.postDelayed(timerRunnable, 0);
	                reCoord.setText("Stop");
	                timeDate("end");
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
	    locationManager.requestLocationUpdates(provider, 400, 1, this);
	  }
	
	  
	  /* No updates when not open */
	  @Override
	  protected void onPause() {
	    super.onPause();
	    locationManager.removeUpdates(this);

	  }
	  
		@Override
		public void onStop(){
			super.onStop();
			// unregistering the registered receiver
			unregisterReceiver(arduinoReceiver);
			unregisterReceiver(arduinoConnected);
		}
	  
	  
	/**
	 * Timer declaration
	 * 
	 */
	
	//TIMER Variables
	public long strtTime = 0;
//	public String name = "tmr1";
//	TextView tmr;
	Handler timerHandler = new Handler();
	public int interv = -1;
	
	  Runnable timerRunnable = new Runnable() {
		  	@Override
		      public void run() {
		          long millis = System.currentTimeMillis() - strtTime;
		          int seconds = (int) (millis / 1000);
		          //int minutes = seconds / 60;
		          seconds = seconds % 60;

		          //tmr.setText(String.format("%d:%02d", minutes, seconds));
		          if(seconds%interv == 0)
		          {
		        	  timeDate("point");
		          }
		          timerHandler.postDelayed(this, 500);
		      }
		  };
		  
		  public void timeDate(String attribute)
		  {
        	  Calendar cal = new GregorianCalendar();
        	  java.util.Date callTime = cal.getTime();
        	  java.sql.Date sqlDate = new java.sql.Date(callTime.getTime());
        	  DPprocess(sqlDate, attribute);
		  }
	
	/**
	 * Method for updating GPS coordinates
	 * 	  
	 */
	  @Override
	  public void onLocationChanged(Location location) {
	    lat = (double) (location.getLatitude());
	    lng = (double) (location.getLongitude());
	    

//	    latText.setText(String.valueOf(lat));
//	    longText.setText(String.valueOf(lng));
	    
	    //System.out.println("does it get this far again?");
		
	  }
	  
	  @Override
	  public void onProviderEnabled(String provider) {
	    Toast.makeText(this, "Enabled new provider " + provider,
	        Toast.LENGTH_SHORT).show();

	  }
	  
	  @Override
	  public void onProviderDisabled(String provider) {
	    Toast.makeText(this, "Disabled provider " + provider,
	        Toast.LENGTH_SHORT).show();
	  }
	  
	  @Override
	  public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	  	// TODO Auto-generated method stub
	  	
	  }
	  
	  
    /**
     * Method to create points from the GPS
     */
	
		  
  public void DPprocess(Date d, String attribute) {
		//Time & Date info
	
	date = d1.format(d); // yyyy-mm-dd
	time = d2.format(d); // hh:mm:ss
	
	
	/*
	//prints
	System.out.println(client_id);
	System.out.println(date);
	System.out.println(time);
	System.out.println(run_id);
	System.out.println(lat);
	System.out.println(lng);
	*/
		
	DataPoint dpx = new DataPoint.Builder().client_id(client_id).run_id(run_id).time(time).date(date).latitude(lat).longitude(lng).attribute(attribute).build();
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
				sensorPoint = new DataPoint.Builder().client_id(client_id).run_id(run_id).time(time).date(date).latitude(lat).longitude(lng).attribute("sensor").sensor_id(sensorPoint.getSensor_id()).sensor_type(sensorPoint.getSensor_type()).sensor_value(sensorPoint.getSensor_value()).build();
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
			ConnectServer serverConnection = new ConnectServer();
			serverConnection.execute();
			new ConnectServer().execute();
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
	
	private class ConnectServer extends AsyncTask<String, Void, Void>{
		
		@Override
		protected Void doInBackground (String ... params){

			try
			{
				// Creating connection
				//InetAddress address = InetAddress.getByName( params[0] );
				//int portNumber = Integer.parseInt(params[1]);
				connection = new Socket( IPaddress , portNumber);
				Log.d(TAG, "Socket Created");
				// Creating stream Objects
				output = new ObjectOutputStream(connection.getOutputStream());
				Log.d(TAG, "Object Stream Created");
				Log.d(TAG,Integer.toString(dp.size()));
				output.flush();
				output.writeObject(dp);
				output.flush();
				Log.d(TAG, "Data sended to Server");
				output.close();
				connection.close();
				Log.d(TAG, "Socket Closed");
			}
			catch( IOException ioException)
			{
				ioException.printStackTrace();
			}
			return null;
		}
	}

}
