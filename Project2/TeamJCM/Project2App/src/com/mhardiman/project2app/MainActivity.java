package com.mhardiman.project2app;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
	
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.speech.RecognizerIntent;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.*;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;


public class MainActivity extends Activity{

	private static final int REQUEST_CODE = 1111;
	Button speechButton;
	TextView runningText;
	boolean running = false;
	myListener myListen;
	TextView ipText;
	TextView portText;
	Handler timerHandler = new Handler();
	int index = 0;
    Runnable timerRunnable = new Runnable() 
    {
    	 @Override
         public void run() {
             timerHandler.postDelayed(this, 10000);
             System.out.println(index++);
            	 myListen.getCoordinates((index-1)%1==0);

    	 }
    };
    	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		runningText = (TextView)findViewById(R.id.runningText);
		ipText = (TextView)findViewById(R.id.ipText);
		portText = (TextView)findViewById(R.id.portText);
		
		SensorManager mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		
		myListen = new myListener(this, (ListView)findViewById(R.id.dataText), mSensorManager);
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
	    RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0) {
			speechButton.setEnabled(false);
			Toast.makeText(getApplicationContext(), "Recognizer Not Found",
			Toast.LENGTH_SHORT).show();
		} 	  
	}
	
	@SuppressLint("InlinedApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		  if (requestCode == REQUEST_CODE & resultCode == RESULT_OK) {
			  ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			  for (int i=0; i<matches.size(); i++)
			  {
				  String currentPhrase = matches.get(i);
			  	if ((!running && currentPhrase.toLowerCase().contains("start")) | (running && currentPhrase.toLowerCase().contains("stop")))
			  	{
			  		toggle();
			  		break;
			  	}
			  	else if (currentPhrase.toLowerCase().contains("send"))
			  	{
			  		String ip = ipText.getText().toString();
					String port = portText.getText().toString();
					if (ip.length()<=7 | port.length() < 4)
						Toast.makeText(this, "Please enter IP and port information.", Toast.LENGTH_SHORT).show();
					else
						myListen.sendData(ipText.getText().toString(), portText.getText().toString());
					break;
			  	}
			  	
			  }
			   
		  	}
			super.onActivityResult(requestCode, resultCode, data);
		}
	
	public void getSpeech(View view)
	{
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
		RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start, stop, send");
		startActivityForResult(intent, REQUEST_CODE);
	}
	
	public void toggle()
	{
	      if (running) {
	          timerHandler.removeCallbacks(timerRunnable);
	          runningText.setText("Stopped");
	      } else {
	          timerHandler.postDelayed(timerRunnable, 0);
	          runningText.setText("Running");
	      }
	  	running = !running;
	}
	
	//public void sendDataPress(View view)
	//{
	//		myLL.sendData();
	//}

	/*public void getReading(View view)
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
	}*/
	
}


class myListener implements LocationListener, SensorEventListener{
	 private SensorManager mSensorManager;
     private Sensor sAccelerometer;
     private Sensor sGyro;
     private Sensor sGravity;
     private Sensor sLinAccel;
     private boolean hasGyro = false;
     private Sensor sRotateVec;
     
     private boolean gotSensor = true;
	 private boolean takeReadings = false;
     private boolean enableSensors = true;
     private boolean hasSpeed = false;
     private boolean hasBearing = false;
     
     private imu currentData;
     
     private LocationManager lm;
     private boolean gotGPS = false;
     private String provider;
     private Location loc;
     private Context mainContext;
     private ArrayList<imu> rowArray = new ArrayList<imu>(); //trimToSize() at the end
     private ListView dataText;
     private ArrayList<String> displayData = new ArrayList<String>(1);
     @SuppressLint("InlinedApi")
	public myListener (Context mainCntxt, ListView dataTxt, SensorManager sensorM)
     {
    	 currentData = new imu();
    	 mainContext = mainCntxt;
    	 dataText = dataTxt;
    	 lm = (LocationManager)mainContext.getSystemService(Context.LOCATION_SERVICE);
    	 provider = LocationManager.GPS_PROVIDER;
    	 loc = lm.getLastKnownLocation(provider);
    	 if (loc != null)
    	 {
    		 System.out.println("GPS connected\n");
		}	
		else
		System.out.println("null\n");
		
		if (enableSensors)
		{	
			mSensorManager = sensorM;
			sAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
			sRotateVec = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
			sLinAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
			sGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
	
			if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)==null)
			{
		    	 Toast.makeText(mainContext, "Gyroscope unvailable.", Toast.LENGTH_LONG).show();	
		    	 hasGyro = false;
			}
			else
				hasGyro = true;
		}
     }
     
     @SuppressLint("NewApi")
     public void getCoordinates(boolean takeReadings)
     {
    	 this.takeReadings = takeReadings;
    	 lm.requestSingleUpdate(provider, this, null);
    	 
    	 if (enableSensors & takeReadings)
    	 {
	    	 mSensorManager.registerListener(this, sAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	    	 //mSensorManager.registerListener(this, sRotateVec, SensorManager.SENSOR_DELAY_NORMAL);
	    	 //if (hasGyro)
	    	//	 mSensorManager.registerListener(this, sGyro, SensorManager.SENSOR_DELAY_NORMAL);
	    	 //mSensorManager.registerListener(this, sLinAccel, SensorManager.SENSOR_DELAY_NORMAL);
	    	 //mSensorManager.registerListener(this, sGravity, SensorManager.SENSOR_DELAY_NORMAL);
    	 }
    	 System.out.println("GPS requested.");
     }
	
	
     @Override
     public void onLocationChanged(Location loc) {
    	 if (takeReadings)
    	 {
	    	 System.out.println("Gps acquired.");
	    	 gotGPS = true;
	    	 currentData.latitude = loc.getLatitude();
	    	 currentData.longitude = loc.getLongitude();
	    	 currentData.speed = loc.getSpeed();
	    	 System.out.println(loc.getSpeed() + ", " + loc.getBearing());
	    	 hasSpeed = loc.hasSpeed();
	    	 hasBearing = loc.hasBearing();
	    	 System.out.println(hasSpeed + ", " + hasBearing);
	    	 currentData.bearing = loc.getBearing();
	    	 currentData.time = loc.getTime();
	    	 
	    	 if (gotSensor | !enableSensors)
	    		 makeEntry();
    	 }
     }
	
	
     public void sendData(String ip, String port)
     {
    	 //disable buttons while sending data
    	 int rowSize = rowArray.size();
    	 if (rowSize != 0)
    	 {
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

     public void onAccuracyChanged(Sensor sensor, int accuracy) {
     }

     public void onSensorChanged(SensorEvent event) {
    	 Sensor thisSensor = event.sensor;
    	 System.out.println("Sensor event.");

    	 float [] values = event.values;
    	 switch(thisSensor.getType())
    	 {
    	 	case Sensor.TYPE_ACCELEROMETER:
    	 		System.out.println("Accelerometer data acquired.");
    	 		currentData.accelX = values[0];
    	 		currentData.accelY = values[1];
    	 		currentData.accelZ = values[2];
    	 		mSensorManager.unregisterListener(this);
    	 		if (hasGyro)
    	 			mSensorManager.registerListener(this, sGyro, SensorManager.SENSOR_DELAY_NORMAL);
    	 		else
    	 			mSensorManager.registerListener(this, sRotateVec, SensorManager.SENSOR_DELAY_NORMAL);
    	 		break;
    	 	case Sensor.TYPE_GYROSCOPE:
    	 		System.out.println("Gyroscope data acquired.");
    	 		currentData.gyroX = values[0];
    	 		currentData.gyroY = values[1];
    	 		currentData.gyroZ = values[2];
    	 		mSensorManager.unregisterListener(this);
    	 		mSensorManager.registerListener(this, sRotateVec, SensorManager.SENSOR_DELAY_NORMAL);
    	 		break;
    	 	case Sensor.TYPE_ROTATION_VECTOR:
    	 		System.out.println("Rotation vector data acquired.");
    	 		currentData.rotVecX = values[0];
    	 		currentData.rotVecY = values[1];
    	 		currentData.rotVecZ = values[2];
    	 		//float orientValues[] = new float[3];
    	 		//SensorManager.getOrientation(values, orientValues);
    	 		currentData.orientationA = 0.0f;//orientValues[0];
    	 		currentData.orientationB = 0.0f;//orientValues[1];
    	 		currentData.orientationC = 0.0f;//orientValues[2];	
    	 		mSensorManager.unregisterListener(this);
    	 		mSensorManager.registerListener(this, sLinAccel, SensorManager.SENSOR_DELAY_NORMAL);
    	 		break;
    	 	case Sensor.TYPE_LINEAR_ACCELERATION:
    	 		System.out.println("Linear acceleration data acquired.");
    	 		currentData.linAccX = values[0];
    	 		currentData.linAccY = values[1];
    	 		currentData.linAccZ = values[2];
    	 		mSensorManager.unregisterListener(this);
    	 		mSensorManager.registerListener(this, sGravity, SensorManager.SENSOR_DELAY_NORMAL);
    	 		break;
    	 	case Sensor.TYPE_GRAVITY:
    	 		System.out.println("Gravity data acquired.");
    	 		currentData.gravityX = values[0];
    	 		currentData.gravityY = values[1];
    	 		currentData.gravityZ = values[2];
    	 		 System.out.println("Got all sensors");
        		 gotSensor = true;
        		 mSensorManager.unregisterListener(this);
        		 if (gotGPS)    			 
        			 makeEntry();
    	 		break;
    	 		
    	 }
     }
     
     private void makeEntry(){
    	 gotGPS = false;
    	 rowArray.add(currentData);
    	 displayData.add(Double.toString(currentData.latitude) + ", " + Double.toString(currentData.longitude)
				+ ";  " + Double.toString(currentData.speed) + ", " + Double.toString(currentData.bearing) + ", " + Boolean.toString(hasSpeed) + ", " + Boolean.toString(hasBearing));
    	 dataText.setAdapter(new ArrayAdapter<String>(mainContext, android.R.layout.simple_list_item_1, displayData));	
    	 currentData = new imu();
     }

}



	



		


	



