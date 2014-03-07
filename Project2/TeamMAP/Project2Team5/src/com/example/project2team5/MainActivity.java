package com.example.project2team5;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.zpartal.project2.datapackets.DataPoint;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	public ListView mList;
	
	String TAG = "TeamMAP";
	private LocationManager locationManager;
	private LocationListener locationListener;
	private SensorManager mSensorManager;
	private Sensor sAccel;
	private Sensor sOrient;
	private Sensor sRotation;
	private Sensor sLinAcc;
	private Sensor sGravity;
	private Sensor sGyro;
	private Sensor sMag;
	
	int interval = 10000;
	long initialTime=0;
	int port = 5555;
	String hostip = "10.201.57.216";
	Socket clientSocket;
	ObjectOutputStream os;
	ObjectInputStream is;
	
	boolean firstpoint=true;
	
	long time;
	double latitude ;
	double longitude ;
	double bearing ;
	double speed ;
	double accelX ;
	double accelY ;
	double accelZ ;
	double orientationA ;
	double orientationB ;
	double orientationC ;
	double rotVecX ;
	double rotVecY ;
	double rotVecZ ;
	double rotVecC;
	double linAccX ;
	double linAccY ;
	double linAccZ ;
	double gravityX ;
	double gravityY ;
	double gravityZ ;
	double gyroX ;
	double gyroY ;
	double gyroZ ;
	
	ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();
	
	TextView tvTime, tvLocation, tvLinAcc, tvRot, tvGyro, tvGrav;
	Button bStart, bStop, bSend, bReset, bVoice;
	private EditText etIP;
	private EditText etPort;
	

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvTime = (TextView) findViewById(R.id.tvTime);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvLinAcc = (TextView) findViewById(R.id.tvLinAcc);
		tvRot = (TextView) findViewById(R.id.tvRot);
//		tvGrav = (TextView) findViewById(R.id.tvGrav);
		tvGyro = (TextView) findViewById(R.id.tvGyro);
		etIP = (EditText) findViewById(R.id.etIP);
		etPort = (EditText) findViewById(R.id.etPort);
	
			
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    sAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sOrient = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    sRotation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
	    sLinAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
	    sGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
	    sGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
	    sMag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
	   
		
		
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
		 	accelX = event.values[0];
		 	accelY = event.values[1];
			accelZ = event.values[2];
			//tvLinAcc.setText("LinX: "+temp1+" \n LinY: "+temp2+"\n LinZ: "+temp3);
	    }
		if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
		 	rotVecX = event.values[0];
		 	rotVecY = event.values[1];
		 	rotVecZ = event.values[2];
		 	//rotVecC = event.values[3];
			//tvRot.setText("RotX: "+rotVecX+" \n RotY: "+rotVecY+"\n RotZ: "+rotVecZ);
	    }
		 if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
			 	linAccX = event.values[0];
			 	linAccY = event.values[1];
			 	linAccZ = event.values[2];
				//tvLinAcc.setText("LinX: "+linAccX+" \n LinY: "+linAccY+"\n LinZ: "+linAccZ);
		    }
		 if(event.sensor.getType() == Sensor.TYPE_GRAVITY){
			 	gravityX = event.values[0];
			 	gravityY = event.values[1];
			 	gravityZ = event.values[2];
				//tvGrav.setText("GravX: "+gravityX+" \n GravY: "+gravityY+"\n GravZ: "+gravityZ);
		    }
		 if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
		    	gyroX = event.values[0];
		    	gyroY = event.values[1];
		    	gyroZ = event.values[2];
			//	tvGyro.setText("GyroX: "+gyroX+" \n GyroY: "+gyroY+"\n GyroZ: "+gyroZ);
		    }
		 
		 if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
             orientationA = event.values[0];
             orientationB = event.values[1];
             orientationC = event.values[2];
     }
		//SensorManager.getRotationMatrix(rotVec, incl, gravity, geomag);
		// SensorManager.getOrientation
		 //getRotation()
		
		//Log.i("Team Map", "getting data");
		//tvLinAcc.setText("LinX: "+temp1+" \n LinY: "+temp2+"\n LinZ: "+temp3);
		//tvRot.setText("RotX: "+temp1+" \n RotY: "+temp2+"\n RotZ: "+temp3);
	}

	public void Start(View v) {
		bStart= (Button) findViewById(R.id.bStart);
		Log.i(TAG, "Starting location gathering");
		locationListener = new LocationListener(){
				@Override
				public void onStatusChanged(String provider,
						int status, Bundle extras) {
					Log.i(TAG, "onStatusChanged");
				}

				@Override
				public void onLocationChanged(Location location) {
					
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
							bearing = location.getBearing();
							speed = location.getSpeed();
							time = location.getTime();
							
							tvTime.setText("Time: "+String.valueOf((time-initialTime)/1000)+" Points: "+dataPoints.size());
							tvLocation.setText("Lat:" +latitude+" Lng: "+longitude);
							tvLinAcc.setText("Brng:" +bearing+" Spd: "+speed);
							Log.i(TAG, "Time: "+time+ " latitud: "+latitude+" longitude: "+longitude);
							Log.i(TAG, "bearing: "+bearing+ " speed: "+speed);
							
							if(firstpoint){
								firstpoint=false;
								initialTime=time;
								//tvTime.setText("First point thrown aside. Start walking when data appears");
							}
							else if(time-initialTime>=interval){
								dataPoints.add(new DataPoint.Builder().time(time).latitude(latitude).longitude(longitude).bearing(bearing).speed(speed).rotVecX(rotVecX).rotVecY(rotVecY).rotVecZ(rotVecZ).orientationA(orientationA).orientationP(orientationB).orientationR(orientationC).accelX(accelX).accelY(accelY).accelZ(accelZ).linAccX(linAccX).linAccY(linAccY).linAccZ(linAccZ).gyroX(gyroX).gyroY(gyroY).gyroZ(gyroZ).gravityX(gravityX).gravityY(gravityY).gravityZ(gravityZ).build());
								//tvTime.setText("Time: "+String.valueOf((time-initialTime)/1000)+" Points: "+dataPoints.size());
								//tvRot.setText("RotX: "+(int) (360*rotVecX)+" \n RotY: "+(int) (360*rotVecY)+"\n RotZ: "+(int)(360*rotVecZ));
								//tvLinAcc.setText("OriA: "+orientationA+" \n OriB: "+orientationB+"\n OriC: "+orientationC);
								//tvGyro.setText("GyroX: "+gyroX+" \n GyroY: "+gyroY+"\n GyroZ: "+gyroZ);
								initialTime=time;

							}
							
									
						}

						else {
							//double lat = 2;
							//double lng = 2;
							
						}
				

				}

				@Override
				public void onProviderEnabled(String provider) {
					Log.i(TAG, "onProviderEnabled");
				}

				@Override
				public void onProviderDisabled(String provider) {
					Log.i(TAG, "onProviderDisabled");
				}
			};

		// while(GPS_status){

				locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 1000, 0,
						locationListener);
	}
	
	public void Stop(View v) {
		bStop = (Button) findViewById(R.id.bStop);
		locationManager.removeUpdates(locationListener);
		firstpoint=true;
		tvTime.setText("Updates stopped   Points: "+dataPoints.size());
		
	}
	public void Reset(View v) {
		bReset= (Button) findViewById(R.id.bReset);
		dataPoints = new ArrayList<DataPoint>();
		tvTime.setText("Data Reset");
		
	}
	
	public void Voice(View v) {
		bVoice= (Button) findViewById(R.id.bVoice);
	    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	    intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
	        "Speech recognition demo");
	    startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
	        ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	        for(int i=0; i<matches.size(); i++)
	        	Log.i(TAG, matches.get(i));
	        
	        if (matches.contains("start")) {
	            Start(bStart);
	        }
	        else if (matches.contains("stop")) {
	            Stop(bStop);
	        }
	        else if (matches.contains("reset")) {
	            Reset(bReset);
	        }
	        else if (matches.contains("send")) {
	            Send(bSend);
	        }
	    }
	}
	      //  mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	  protected void onResume() {
	    super.onResume();
	    mSensorManager.registerListener(this, sAccel, SensorManager.SENSOR_DELAY_NORMAL);
	    mSensorManager.registerListener(this, sRotation, SensorManager.SENSOR_DELAY_NORMAL);
	    mSensorManager.registerListener(this, sLinAcc, SensorManager.SENSOR_DELAY_NORMAL);
	    mSensorManager.registerListener(this, sGravity, SensorManager.SENSOR_DELAY_NORMAL);
	    mSensorManager.registerListener(this, sGyro, SensorManager.SENSOR_DELAY_NORMAL);
	    mSensorManager.registerListener(this, sMag, SensorManager.SENSOR_DELAY_NORMAL);
	    mSensorManager.registerListener(this, sOrient, SensorManager.SENSOR_DELAY_NORMAL);
	  }

	  @Override
	  protected void onPause() {
	    super.onPause();
	    mSensorManager.unregisterListener(this);
	  }
	  
	  public void Send(View v){
		  Log.d(TAG, "Sending");
		  
			bSend = (Button) findViewById(R.id.bSend);
			hostip = etIP.getText().toString();
			//port =Integer.parseInt(etIP.getText().toString());

			
			 Log.d(TAG, "Starting async");
			 new AsyncTask<Void, Void, Void> (){
				 protected Void doInBackground(Void... params) {
				    try {

						
						
						Log.i(TAG,"Connecting");
						//for(int i=0; i<dataPoints.size(); i++)
						//	Log.i(TAG, String.valueOf(dataPoints.get(i).getTime()));
						clientSocket = new Socket(hostip,port);
						os = new ObjectOutputStream(clientSocket.getOutputStream());
						os.writeObject(dataPoints);
						Log.i(TAG, "Data Sent!");
						os.flush();
						os.close();
						clientSocket.close();
				    
				    } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					   
				    
				    
				    return null; }
			 }.execute();

			
		}



}
