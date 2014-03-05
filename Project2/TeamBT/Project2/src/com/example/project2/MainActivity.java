package com.example.project2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import team2.DataPacket;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

	// Setting constants
	private static final String TAG = "Project2";
	private static final int PORT_NUMBER = 545;
	private static final int REQUEST_CODE = 1234;
	
	//Definition of date format
	SimpleDateFormat d1 = new SimpleDateFormat("yyyy-mm-dd");
	SimpleDateFormat d2 = new SimpleDateFormat("hh:mm:ss");
	SimpleDateFormat d3 = new SimpleDateFormat("yyyymmdd_hhmmss");
	
	//Flag for running
	boolean running = false;
	
	// List with stored data
	ArrayList<DataPacket> dp = new ArrayList<DataPacket>();
	
	// Server connection variables
	InetAddress IPaddress;
	int portNumber;
	
	// Sensors
	private SensorManager mSensorManager;
	private Sensor accel;
	private Sensor gyro;
	private Sensor rotation;
	private Sensor  linearAcc;
	private Sensor gravity;
	private Sensor geomagnetic;
	
	//sensor data
	private float gyro_data[] = new float[3];
	private float accel_data[] = new float[3];
	private float orientation_data[] = new float[3];
	private float gravity_data[] = new float[3];
	private float rot_data[] = new float[3];
	private float linAcc_data[] = new float[3];
	
	private float rotationMatrix[] = new float[9];
	
	// Time variables
	String run_id; // teamid_yyyymmdd_hhmmss
	long time; // hh:mm:ss
	String date; // yyyy-mm-dd
	GPSData gps;
	Timer timer;
	ServerCom server;
	Button speakButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//sensors set up
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		Log.d(TAG, mSensorManager.toString());
		
		accel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		gyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		rotation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		linearAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		gravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		geomagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		speakButton = (Button) findViewById(R.id.speakButton);
		
		//gps initialization
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		gps = new GPSData(locationManager);
		gps.onCreate();
		
		//Speech Recognition set up
		
		  // Disable button if no recognition service is present
		
		  PackageManager pm = getPackageManager();
		  List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
		    RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		  if (activities.size() == 0) 
		  {
		   speakButton.setEnabled(false);
		   Toast.makeText(getApplicationContext(), "Recognizer Not Found",
		     Toast.LENGTH_SHORT).show();
		  }
		  speakButton.setOnClickListener(new View.OnClickListener() {
			   @Override
			   public void onClick(View v) {
			    startVoiceRecognitionActivity();
			   }
			  });
	}
	
	 private void startVoiceRecognitionActivity() {
		  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
		    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		  intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
		    "Voice Recognition");
		  startActivityForResult(intent, REQUEST_CODE);
		 }

	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
	   ArrayList<String> matches = data
	     .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	   voiceCommand(matches.get(0));
	  }
	  super.onActivityResult(requestCode, resultCode, data);
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
	    //gps.onResume();
	    mSensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
	    mSensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL);
	    mSensorManager.registerListener(this, rotation, SensorManager.SENSOR_DELAY_NORMAL);
	    mSensorManager.registerListener(this, linearAcc, SensorManager.SENSOR_DELAY_NORMAL);
	    mSensorManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_NORMAL);
	    mSensorManager.registerListener(this, geomagnetic, SensorManager.SENSOR_DELAY_NORMAL);
	  }
	  
	  @Override
	  protected void onPause() {
	    super.onPause();
	    //gps.onPause();
	    //mSensorManager.unregisterListener(this);
	  }
	  
	  @Override
	  public void onStop(){
		  super.onStop();
		  mSensorManager.unregisterListener(this);
	  }
	  
	    /**
	     * Method to create points from the GPS
	     */
	  
	  
    //------------------------------------------
    //TIMER STUFF
    //tmr = (TextView) findViewById(R.id.tmr);
    
		
	public void voiceCommand(String Command) {
		// TODO Auto-generated method stub
		TextView editTxt = (TextView) findViewById(R.id.textView1);
		Log.d(TAG,"Command: " + Command + " " + Boolean.toString(running));
        if (Command.equals("stop") && running) 
        {
        	running = false;
        	editTxt.setText("Stopped");
            timer.stop();
            DPprocess();
        } 
        else  if (Command.equals("start"))
        {
        	//------for gps integration
        	Log.d(TAG,"Started running");
        	running = true;
        	editTxt.setText("Running");
        	timer = new Timer(10, new Callable<Integer>(){
        	    				public Integer call() {
        	    						return DPprocess();
        	    				}});
        	//------timerry stuff
        	timer.start();
            DPprocess();
        }
        else  if (Command.equals("send"))
        {
        	this.connectToServer();
        }
        else  if (Command.equals("reset"))
        {
        	dp.clear();
        }
	}
	
	/**
	 * getting sensor data
	 * 
	 * 
	 */
			  
	@Override
	public final void onAccuracyChanged(Sensor sensor, int accuracy){
		
	}
	
	@Override
	  public final void onSensorChanged(SensorEvent event) {
	    // The light sensor returns a single value.
	    // Many sensors return 3 values, one for each axis.
		float magData[] = new float[3];
		
	    if(event.sensor.getType() == accel.getType())
	    {
	    	accel_data = event.values;
	    }
	    else if(event.sensor.getType() == gyro.getType())
	    {
	    	gyro_data = event.values;
	    }
	    else if(event.sensor.getType() == rotation.getType())
	    {
	    	rot_data = event.values;
	    }
	    else if(event.sensor.getType() == linearAcc.getType())
	    {
	    	linAcc_data = event.values;
	    }
	    else
	    	{
	    		if(event.sensor.getType() == gravity.getType())
			    {
			    	gravity_data = event.values;
			    }
	    		else if (event.sensor.getType() == geomagnetic.getType())
	    		{
	    			magData = event.values;
	    		}
	    		
	    		SensorManager.getRotationMatrix(rotationMatrix, null, gravity_data, magData);
	    	}
	  }
	
	
	  public int DPprocess() {
			//Time & Date info
		Date d = timer.getSQLDate();
		date = d1.format(d); // yyyy-mm-dd
		time = System.currentTimeMillis();
			//new DataPacket(time, longitude, latitude, bearing, speed, accelX, accelY, accelZ, orientationA, orientationB, orientationC, rotVecX, rotVecY, rotVecZ, linAccX, linAccY, linAccZ, gravityX, gravityY, gravityZ, gyroX, gyroY, gyroZ)

		//float fake1 = 0;
		//double fake2 = 0;
		SensorManager.getOrientation(rotationMatrix, orientation_data);
		DataPacket dpx = new DataPacket(time, gps.getLng(), gps.getLat(),gps.getBearing(), gps.getSpeed(), accel_data[0], accel_data[1], accel_data[2], orientation_data[0] , orientation_data[1], orientation_data[2], rot_data[0], rot_data[1], rot_data[2], linAcc_data[0], linAcc_data[1], linAcc_data[2], gravity_data[0], gravity_data[1], gravity_data[2], gyro_data[0], gyro_data[1], gyro_data[2]);
		dp.add(dpx);
		
		//prints
//			Log.d(TAG,Float.toString(dpx.getAccelX()));
//			Log.d(TAG,Float.toString(dpx.getGravityX()));
//			Log.d(TAG,Float.toString(dpx.getGyroX()));
			Log.d(TAG,Double.toString(dpx.getBearing()));
			Log.d(TAG,Double.toString(dpx.getLongitude()));
			Log.d(TAG,Integer.toString(dp.size()));
		return 0;
	}
	  
	// Server Connection
	public void connectToServer() {
		if (isOnline())
		{
			EditText editText = (EditText) findViewById(R.id.edit_IPAddress);
			try{
			IPaddress = InetAddress.getByName(editText.getText().toString());
			}
			catch(UnknownHostException e)
			{
			}
			portNumber = PORT_NUMBER;
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
