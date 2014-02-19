package com.example.androidinterface;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.os.AsyncTask;

import com.google.gson.*;

import at.abraxas.amarino.*;

import com.example.samcarey.AndroidPacket1;
import com.example.samcarey.ObjectItem;;

public class MainActivity extends Activity {
	
	ArrayList<AndroidPacket1> dp = new ArrayList<AndroidPacket1>();
	
	//private static final String TEAM1 =  "00:12:09:13:99:42";
	//private static final String TEAM2 =  "00:12:09::";
	//private static final String TEAM3 =  "00:12:09:";
	//private static final String TEAM4 =  "00:12:09::;
	private static final String TEAM5 =  "00:12:09:25:92:";
	
	//const
	private static final String id = "Team 5";
	private static final String Tag = "Project 1";

	boolean flag = false;
	boolean receive = false;
	
	private static final char function = 'f';
	
	Socket connect;
	ObjectOutputStream output;
	ObjectInputStream input;
	String ipaddress;
	int port;
	
	
	private ArduinoConnected arduinoconn = new ArduinoConnected();
	private ArduinoReciever arduinorec = new ArduinoReciever();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		registerReceiver(arduinorec, new IntentFilter(AmarinoIntent.ACTION_RECEIVED));
		registerReceiver(arduinoconn, new IntentFilter(AmarinoIntent.ACTION_CONNECTED));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onStop(){
		super.onStop();
		unregisterReceiver(arduinorec);
		unregisterReceiver(arduinoconn);
	}
	
	//timer
	/*public void connectT1(View v)
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
	}*/
	public void connectT5(View v)
	{
		Amarino.connect(this, TEAM5);
	}
	public void senddata(String address)
	{
		Amarino.sendDataToArduino(this, address, function, "");
	}
	public void disconnect(String address)
	{
		Amarino.disconnect(this, address);
	}
	
	
	public class ArduinoReciever extends BroadcastReceiver{
		private Object time;
		private Object date;
		private Object lat;
		private Object lng;

		@Override
		public void onReceive(Context context, Intent intent){
			
			String data = null;
			String result = null;
			
			final String addr = intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);
			
			final int dataType = intent.getIntExtra(AmarinoIntent.EXTRA_DATA_TYPE,-1);	
			
			if (dataType == AmarinoIntent.STRING_EXTRA){
			
				data = intent.getStringExtra(AmarinoIntent.EXTRA_DATA);
				
				if (data != null){
				flag = true;
				Gson gson = new Gson(); //Confirmation whether androidpacket or objectitem
				AndroidPacket1 sensordata = gson.fromJson(data, AndroidPacket1.class);
				sensordata = new AndroidPacket1.sensor_id(sensordata.getsensor_id())
						.sensor_type(sensordata.getSensor_type())
						.sensor_value(sensordata.getSensor_value());
				disconnect(addr);
				dp.add(sensordata);	
				flag = true;
				receive = false;
				}
				}
			}
		}

	public class ArduinoConnected extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent){
			final String addr = intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);
			
			senddata(addr);
		}
	}
	
	/*Already in the gps 
	public boolean network(){
		ConnectivityManager manger = (ConnectivityManager) 
					getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = manger.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
		boolean blueconn = network.isConnectedOrConnecting();
		if (blueconn == true)
			PlaceholderTextview.setText("BlueTooth is Connecting");
		else
			PlaceholderTextview.setText("BlueTooth is Disconnected");
		
		//NetworkInfo network_e = manger.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		//boolean wificonn = network.isConnected();
	}
	

	public void connection(){
		if(network())
		{
			
		}
	}
	
	
	 public class Server extends AsyncTask<...>{
     	@Override
     	protected void doInBackground(String ... values){
     		try{
     			connect = new Socket(ipaddress, port);
     			//some data packet might be assigned here
     			output = 
     					 ObjectOutputStream(connect.getOutputStream());
     			output.flush();
     			output.writeObject(ObjectItem);
     			output.flush();
     			output.close();
     			
     			connect.close();
     		}
     		catch(Exception e){
     			Log.e("Android Applicatoin","Async broke down =(");
     		}
     	}
     }*/

}
