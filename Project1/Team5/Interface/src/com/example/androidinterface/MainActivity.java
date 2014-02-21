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
	
	//Need Amarino Library in Build Path 
	//http://www.amarino-toolkit.net/index.php/download.html
	
	ArrayList<AndroidPacket1> dp = new ArrayList<AndroidPacket1>();
	
	private static final String TEAM1 =  "00:12:09:13:99:42";
	private static final String TEAM2 =  "00:15:FF:F2:10:0F";
	private static final String TEAM3 =  "00:12:09:25:92:95";
	private static final String TEAM4 =  "00:12:09:25:92:92";
	private static final String TEAM5 =  "00:12:09:25:96:92"; //address of device connecting to 
	// need this from the teensy
	/*private static final String id = "Team 5";
	private static final String Tag = "Project 1";*/

	boolean receive = false;

	private static final char function = 'f';
	
	/*Socket connect;
	ObjectOutputStream output;
	ObjectInputStream input;
	String ipaddress;
	int port;*/
	
	
	private ArduinoConnected arduinoconn = new ArduinoConnected(); //need this
	private ArduinoReciever arduinorec = new ArduinoReciever(); //need this

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onStart(){ //need this 
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
	public void onStop(){ //need this
		super.onStop();
		unregisterReceiver(arduinorec);
		unregisterReceiver(arduinoconn);
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
		Amarino.sendDataToArduino(this, address, function, "");
	}
	public void disconnect(String address)
	{
		Amarino.disconnect(this, address);
	}
	
	
	public class ArduinoReciever extends BroadcastReceiver{ //need this
	

		@Override
		public void onReceive(Context context, Intent intent){
			
			String data = null;
			String result = null;
			
			final String addr = intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);
			
			final int dataType = intent.getIntExtra(AmarinoIntent.EXTRA_DATA_TYPE,-1);	
			
			if (dataType == AmarinoIntent.STRING_EXTRA){
			
				data = intent.getStringExtra(AmarinoIntent.EXTRA_DATA);
				
				if (data != null){
			
				receive = true;
				Gson gson = new Gson();
				
				AndroidPacket1 sensordata = gson.fromJson(data, AndroidPacket1.class); //
				sensordata = sensordata.getsensor_id().getsensor_type().getsensor_value(); //sensordata parse data into respective fields here
				dp.add(sensordata);	
			
				receive = false;
				}
				}
			}
		}

	public class ArduinoConnected extends BroadcastReceiver{ //need this
		@Override
		public void onReceive(Context context, Intent intent){
			final String addr = intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);
			
			senddata(addr);
		}
	}

}
