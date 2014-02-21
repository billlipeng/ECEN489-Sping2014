package com.mhardiman.gps_retrieval;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Set;

import com.google.gson.Gson;
	
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.location.*;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;

public class MainActivity extends Activity{

	final static int PICK_ADDRESS_REQUEST = 1;
	BluetoothDevice blueArray[];
	public dataRow currentData;
	boolean waitingOnGPS=false;
	TextView ipText;
	TextView portText;
	Button sendText;
	Button readSensor;
	myLocationListener myLL;
	String DEVICE_ADDRESS = "";// =  "00:12:09:13:99:42"; 
	ArduinoReceiver arduinoReceiver = new ArduinoReceiver();
	BluetoothAdapter adapter;
	Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() 
    {
    	 @Override
         public void run() {
             myLL.getCoordinates(false);
            
             //coordsText.setText(String.valueOf(myLL.latitude) + ", " + String.valueOf(myLL.longitude));
             timerHandler.postDelayed(this, 5000);
    	 }
    };
    	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		readSensor = (Button)findViewById(R.id.getReading);
		ipText = (TextView)findViewById(R.id.ipText);
		portText = (TextView)findViewById(R.id.portText);
		myLL = new myLocationListener(this, (ListView)findViewById(R.id.dataText));
		adapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	public void toggle(View view)
	{
		  Button toggleBtn = (Button)view;
	      if (toggleBtn.getText().equals("Stop")) {
	          timerHandler.removeCallbacks(timerRunnable);
	          toggleBtn.setText("Start");
	      } else {
	          timerHandler.postDelayed(timerRunnable, 0);
	          toggleBtn.setText("Stop");
	      }
	}
	
	public void sendDataPress(View view)
	{
		String ip = ipText.getText().toString();
		String port = portText.getText().toString();
		if (ip.length()<=7 | port.length() < 4)
			Toast.makeText(this, "Please enter IP and port information.", Toast.LENGTH_SHORT).show();
		else
			myLL.sendData(ipText.getText().toString(), portText.getText().toString());
		
			
	}

	public void getReading(View view)
	{
		  Button toggleBtn = (Button)view;
	      if (toggleBtn.getText().equals("Connect")) 
	      {
	    	  Set<BluetoothDevice> blueSet = adapter.getBondedDevices();
	    	  blueArray = blueSet.toArray(new BluetoothDevice[blueSet.size()]);
	    	  String deviceNames[] = new String[blueArray.length];
	    	  for (int i=0; i<blueArray.length; i++)
	    	  {
	    		  deviceNames[i] = blueArray[i].getName();
	    	  }
	    	  Intent intent = new Intent(this, ChooseBluetooth.class);
	    	  intent.putExtra("deviceList", deviceNames);
	    	  startActivityForResult(intent, PICK_ADDRESS_REQUEST);
	    	  
	      } 
	      else 
	      {
		myLL.getCoordinates(true);
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 's', 's');
	      }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==1 & resultCode==RESULT_OK)
		{
			int index = data.getIntExtra("deviceIndex",0);
			DEVICE_ADDRESS = blueArray[index].getAddress();
			Amarino.connect(this, DEVICE_ADDRESS);
	        readSensor.setText("Read from\n" + blueArray[index].getName());
		}
	}
	
	public void disconnect(View view)
	{
		Amarino.disconnect(this, DEVICE_ADDRESS);
		readSensor.setText("Connect");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		// in order to receive broadcasted intents we need to register our receiver
		registerReceiver(arduinoReceiver, new IntentFilter(AmarinoIntent.ACTION_RECEIVED));
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		// if you connect in onStart() you must not forget to disconnect when your app is closed
		//Amarino.disconnect(this, DEVICE_ADDRESS);
		
		// do never forget to unregister a registered receiver
		unregisterReceiver(arduinoReceiver);
	}
	
	
	public class ArduinoReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String data = null;
			
			// the device address from which the data was sent, we don't need it here but to demonstrate how you retrieve it
			//final String address = intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);
			
			// the type of data which is added to the intent
			final int dataType = intent.getIntExtra(AmarinoIntent.EXTRA_DATA_TYPE, -1);
			//String devices = intent.getStringExtra(AmarinoIntent.EXTRA_CONNECTED_DEVICE_ADDRESSES);
			//System.out.println(devices);
			
			if (dataType == AmarinoIntent.STRING_EXTRA){
				data = intent.getStringExtra(AmarinoIntent.EXTRA_DATA);
				
				if (data != null)
				{
					Gson gson = new Gson();

					sensorData reading = gson.fromJson(data, sensorData.class);
					myLL.setSensorReading(reading);
				}				
			}
		}
	}

}


class myLocationListener implements LocationListener{
	private Time now;
	private double latitude;
	private double longitude;
	private LocationManager lm;
	private String provider;
	private Location loc;
	private Context mainContext;
	private boolean isSensorEntry=false;
	private boolean gotSensorValue=false;
	private boolean gotGPS=false;
	private dataRow currentData;
	private ArrayList<dataRow> rowArray = new ArrayList<dataRow>(); //trimToSize() at the end
	private ListView dataText;
	private ArrayList<String> displayData = new ArrayList<String>(1);
	public myLocationListener (Context mainCntxt, ListView dataTxt)
	{
		mainContext = mainCntxt;
		dataText = dataTxt;
		lm = (LocationManager)mainContext.getSystemService(Context.LOCATION_SERVICE);
		provider = lm.getBestProvider(new Criteria(), false);
		loc = lm.getLastKnownLocation(provider);
		if (loc != null)
		{
			System.out.println("GPS connected\n");
		}
		else
			System.out.println("null\n");
	}
	@SuppressLint("NewApi")
	public void getCoordinates(boolean isSensorReading)
	{
		isSensorEntry = isSensorReading;
	  	lm.requestSingleUpdate(provider, this, null);
	}
	
	public void setSensorReading(sensorData reading)
	{
		if (currentData == null)
		{
			currentData = new dataRow();
		}
		currentData.sensorValue = reading.getSensorValue();
		currentData.sensorID = reading.getSensorID();
		currentData.sensorType = reading.getSensorType();
		gotSensorValue = true;
		if (gotGPS)
		{
			//add to vector
			makeEntry();
		}
	}
	
	@Override
	  public void onLocationChanged(Location loc) {
		latitude = loc.getLatitude();
		longitude = loc.getLongitude();
		if (currentData == null)
		{
			currentData = new dataRow();
		}
		currentData.latitude = latitude;
		currentData.longitude = longitude;

			gotGPS = true;
		if (!isSensorEntry | gotSensorValue)//if we don't need to wait on a sensor reading
		{
			//add to vector
			makeEntry();
		}

	  }
	
	private void makeEntry()
	{

		now = new Time();
		now.setToNow();
		currentData.date = Integer.toString(now.month + 1 ) + "/" 
				+ Integer.toString(now.monthDay) + "/" 
				+ Integer.toString(now.year);
		currentData.time = Integer.toString(now.hour) + ":" 
				+ Integer.toString(now.minute) + ":" 
				+ Integer.toString(now.second);
		gotGPS = false;
		gotSensorValue = false;
		if (rowArray.size()==0)
			currentData.attribute = "Start";
		rowArray.add(currentData);
		displayList();
        currentData = new dataRow();
	}
	
	public void displayList()
	{
		displayData.add(Double.toString(currentData.latitude) + ", " + Double.toString(currentData.longitude)
				+ "; " + currentData.sensorType + ": " + Float.toString(currentData.sensorValue));
    dataText.setAdapter(new ArrayAdapter<String>(mainContext, android.R.layout.simple_list_item_1, displayData));
	}
	
	public void sendData(String ip, String port)
	{
		//disable buttons while sending data
		int rowSize = rowArray.size();
		if (rowSize != 0)
		{
			dataRow lastRow = rowArray.get(rowSize-1);
			lastRow.attribute = "Stop";
			rowArray.remove(rowSize-1);
			rowArray.add(lastRow);
			new talkToServer().execute(ip, port);
		}
		else
			Toast.makeText(mainContext, "No data to send.", Toast.LENGTH_SHORT).show();
		
	}
	
	 private class talkToServer extends AsyncTask<String,Void,Void>
		{
			@Override
			protected Void doInBackground(String... ip_addr) {
				Socket connection = null;
				try {
					System.out.println("opening socket...");
					
					connection = new Socket(ip_addr[0], Integer.valueOf(ip_addr[1]));
					
					ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
					output.writeObject(rowArray);
					output.flush();
					
					output.close();
					
					connection.close();
					
				} catch (UnknownHostException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			
			 }
		}
		

	  @Override
	  public void onStatusChanged(String provider, int status, Bundle extras) {
	    // TODO Auto-generated method stub

	  }

	  @Override
	  public void onProviderEnabled(String provider) {

	  }

	  @Override
	  public void onProviderDisabled(String provider) {

	  }

}



	



		


	



