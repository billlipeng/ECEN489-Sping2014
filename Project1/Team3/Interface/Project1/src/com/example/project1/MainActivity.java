package com.example.project1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;

import com.google.gson.Gson;


public class MainActivity extends Activity {
	
	private static final String TAG = "Project1";
	private static final char FUNCTION = 's';
	
	Socket connection;
	ObjectOutputStream output;
	ObjectInputStream input;
	
	ArrayList<DataPoint> dataTable = new ArrayList<DataPoint>();	
	
	// Setting the bluetooth address of each team 
	private static final String TEAM1_ADDRESS =  "00:12:09:25:92:95";
	private static final String TEAM2_ADDRESS =  "00:12:09:25:92:95";
	private static final String TEAM3_ADDRESS =  "00:12:09:25:92:95";
	private static final String TEAM4_ADDRESS =  "00:12:09:25:92:95";
	private static final String TEAM5_ADDRESS =  "00:12:09:25:92:95";
	
	private ArduinoReceiver arduinoReceiver = new ArduinoReceiver();
	
	// gson objects
	private Gson gson = new Gson();
	
	// sensor objects
	private DataPoint sensor1;
	private DataPoint sensor2;
	private DataPoint sensor3;
	private DataPoint sensor4;
	private DataPoint sensor5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// in order to receive broadcasted intents we need to register our receiver
		registerReceiver(arduinoReceiver, new IntentFilter(AmarinoIntent.ACTION_RECEIVED));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void connectTeam1(View v)
	{
		// Connecting with team 1 arduino
		Amarino.connect(this, TEAM1_ADDRESS);
		Amarino.sendDataToArduino(this, TEAM1_ADDRESS, FUNCTION, "");
	}
	
	public void connectTeam2(View v)
	{
		// Connecting with team 2 arduino
		Amarino.connect(this, TEAM2_ADDRESS);
		Amarino.sendDataToArduino(this, TEAM2_ADDRESS, FUNCTION, "");
	}
	
	public void connectTeam3(View v)
	{
		// Connecting with team 3 arduino
		Amarino.connect(this, TEAM3_ADDRESS);
		Amarino.sendDataToArduino(this, TEAM3_ADDRESS, FUNCTION, "");
	}
	
	public void connectTeam4(View v)
	{
		// Connecting with team 3 arduino
		Amarino.connect(this, TEAM4_ADDRESS);
		Amarino.sendDataToArduino(this, TEAM4_ADDRESS, FUNCTION, "");
	}
	
	public void connectTeam5(View v)
	{
		// Connecting with team 3 arduino
		Amarino.connect(this, TEAM5_ADDRESS);
		Amarino.sendDataToArduino(this, TEAM5_ADDRESS, FUNCTION, "");
	}
	
	public void send(String address)
	{
		Amarino.sendDataToArduino(this, address, FUNCTION, "");
	}
	
	public void disconnect(String address)
	{
		Amarino.disconnect(this, address);
	}
	
	@Override
	public void onStop(){
		super.onStop();
		// unregistering the registered receiver
		unregisterReceiver(arduinoReceiver);
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
				if (address.equals(TEAM1_ADDRESS))
				{
					sensor1 = gson.fromJson(data, DataPoint.class);
					disconnect(TEAM1_ADDRESS);
				}
				if (address.equals(TEAM2_ADDRESS))
				{
					sensor2 = gson.fromJson(data, DataPoint.class);
					disconnect(TEAM2_ADDRESS);
				}
				if (address.equals(TEAM3_ADDRESS))
				{
					sensor3 = gson.fromJson(data, DataPoint.class);
					Log.d(TAG, sensor3.getSensor_id());
					Log.d(TAG, sensor3.getSensor_type());
					disconnect(TEAM3_ADDRESS);
				}
				if (address.equals(TEAM4_ADDRESS))
				{
					sensor4 = gson.fromJson(data, DataPoint.class);
					disconnect(TEAM4_ADDRESS);
				}
				if (address.equals(TEAM5_ADDRESS))
				{
					sensor5 = gson.fromJson(data, DataPoint.class);
					disconnect(TEAM5_ADDRESS);
				}
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
			
			if (address.equals(TEAM1_ADDRESS))
			{
				send(TEAM1_ADDRESS);
			}
			if (address.equals(TEAM2_ADDRESS))
			{
				send(TEAM2_ADDRESS);
			}
			if (address.equals(TEAM3_ADDRESS))
			{
				send(TEAM3_ADDRESS);
			}
			if (address.equals(TEAM4_ADDRESS))
			{
				send(TEAM4_ADDRESS);
			}
			if (address.equals(TEAM5_ADDRESS))
			{
				send(TEAM5_ADDRESS);
			}
		}
		
	}
	
	/**
	 * Server connection and communication
	 * @param view
	 */
	
	// Server Connection
	public void connectToServer(View view) {
		if (isOnline())
		{
			EditText editText = (EditText) findViewById(R.id.edit_IPAddress);
			String address = editText.getText().toString();
			EditText editText1 = (EditText) findViewById(R.id.edit_PortNumber);
			String portNumber = editText1.getText().toString();
			ConnectServer serverConnection = new ConnectServer();
			serverConnection.execute(address, portNumber);
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
				InetAddress address = InetAddress.getByName( params[0] );
				int portNumber = Integer.parseInt(params[1]);
				connection = new Socket( address , portNumber);
				Log.d(TAG, "Socket Created");
				// Creating stream Objects
				output =	new ObjectOutputStream(connection.getOutputStream());
				input =  new ObjectInputStream(connection.getInputStream());
				output.flush();
				output.writeObject(dataTable);
				Log.d(TAG, "Data sended to Server");
			}
			catch( IOException ioException )
			{
				ioException.printStackTrace();
			}
			return null;
		}
	}

}
