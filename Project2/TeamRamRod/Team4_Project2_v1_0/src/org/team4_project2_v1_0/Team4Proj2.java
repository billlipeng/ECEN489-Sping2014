package org.team4_project2_v1_0;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.net.*;
import java.io.*;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import android.os.*;
import android.speech.RecognizerIntent;
import android.util.*;

import java.lang.*;

import org.team4_project2_v1_0.*;



public class Team4Proj2 extends Activity{
	ObjectOutputStream os;
	ObjectInputStream is;
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
	
	private Button btnSpeak;
	private SensorManager manager;
	private SensorEventListener listener;
	private LocationListener locationListener;
	private LocationManager locationManager;
	private TextView AcceX,AcceY,AcceZ;
	private TextView GyroX,GyroY,GyroZ;
	private TextView OrientationA,OrientationP,OrientationR;
	private TextView LinAccX,LinAccY,LinAccZ;
	private TextView GravityX,GravityY,GravityZ;
	private TextView RotVecX,RotVecY,RotVecZ,RotVecC;
	
	private TextView lattxt,lngtxt,timetxt,speedtxt,bearingtxt;
	
	private EditText etIP,etPort;
	
	private float acceX,acceY,acceZ;
	private float gyroX,gyroY,gyroZ;
	private float orientationA,orientationP,orientationR;
	private float rotVecX,rotVecY,rotVecZ,rotVecC;
	private float linAccX,linAccY,linAccZ;
	private float gravityX,gravityY,gravityZ;
	
	private long time;
	private double lat;
	private double lng;
	private double bearing;
	private double speed;
	
	private boolean check ;
	
	int port;
	String hostip;
	Socket clientSocket;
	
	ArrayList<Float> Acce_X = new ArrayList<Float>();
	ArrayList<Float> Acce_Y = new ArrayList<Float>();
	ArrayList<Float> Acce_Z = new ArrayList<Float>();
	
	ArrayList<Float> Gyro_X = new ArrayList<Float>();
	ArrayList<Float> Gyro_Y = new ArrayList<Float>();
	ArrayList<Float> Gyro_Z = new ArrayList<Float>();
	
	ArrayList<Float> Orientation_A = new ArrayList<Float>();
	ArrayList<Float> Orientation_P = new ArrayList<Float>();
	ArrayList<Float> Orientation_R = new ArrayList<Float>();
	
	ArrayList<Float> LinAcc_X = new ArrayList<Float>();
	ArrayList<Float> LinAcc_Y = new ArrayList<Float>();
	ArrayList<Float> LinAcc_Z = new ArrayList<Float>();
	
	ArrayList<Float> Gravity_X = new ArrayList<Float>();
	ArrayList<Float> Gravity_Y = new ArrayList<Float>();
	ArrayList<Float> Gravity_Z = new ArrayList<Float>();
	
	ArrayList<Float> RotVec_X = new ArrayList<Float>();
	ArrayList<Float> RotVec_Y = new ArrayList<Float>();
	ArrayList<Float> RotVec_Z = new ArrayList<Float>();
	ArrayList<Float> RotVec_C = new ArrayList<Float>();
	
	ArrayList<Long> Time = new ArrayList<Long>();
	ArrayList<Double> Latitude = new ArrayList<Double>();
	ArrayList<Double> Longitude = new ArrayList<Double>();
	ArrayList<Double> Speed = new ArrayList<Double>();
	ArrayList<Double> Bearing = new ArrayList<Double>();
	
	int acc1,acc2,acc3,gyr1,gyr2,gyr3,ori1,ori2,ori3,
	rv1,rv2,rv3,rv4,la1,la2,la3,g1,g2,g3;
	int la,lo,sp,be,ti = 0;
	
	
	Handler handler;
	
	
	Runnable runnable = new Runnable(){
		
		@Override
		public void run(){
			AcceX.setText("acceX = "+ acceX);
			AcceY.setText("acceY = "+ acceY);
			AcceZ.setText("acceZ = "+ acceZ);
			
			GyroX.setText("gyroX = "+ gyroX);
			GyroY.setText("gyroY = "+ gyroY);
			GyroZ.setText("gyroZ = "+ gyroZ);
			
			OrientationA.setText("orientationA = "+ orientationA);
			OrientationP.setText("orientationP = "+ orientationP);
			OrientationR.setText("orientationR = "+ orientationR);
			
			RotVecX.setText("rotVecX = "+ rotVecX);
			RotVecY.setText("rotVecY = "+ rotVecY);
			RotVecZ.setText("rotVecZ = "+ rotVecZ);
			RotVecC.setText("rotVecC = "+ rotVecC);
			
			LinAccX.setText("linAccX = "+ linAccX);
			LinAccY.setText("linAccY = "+ linAccY);
			LinAccZ.setText("linAccZ = "+ linAccZ);
			
			GravityX.setText("gravityX = "+ gravityX);
			GravityY.setText("gravityY = "+ gravityY);
			GravityZ.setText("gravityZ = "+ gravityZ);
			
			timetxt.setText("time = "+ time);
			lattxt.setText("latitude = "+ lat);
			lngtxt.setText("longitude = "+ lng);
			bearingtxt.setText("bearing = "+ bearing);
			speedtxt.setText("speed = "+ speed);
			
			handler.postDelayed(this,2000);
			
		}
	};
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team4_proj2);
		etIP = (EditText) findViewById(R.id.ip);
		etPort = (EditText) findViewById(R.id.port);
		
		btnSpeak = (Button) findViewById(R.id.speak);
	
		AcceX = (TextView) findViewById(R.id.acceX); 
		AcceY = (TextView) findViewById(R.id.acceY);
		AcceZ = (TextView) findViewById(R.id.acceZ);
		
		GyroX = (TextView) findViewById(R.id.gyroX);
		GyroY = (TextView) findViewById(R.id.gyroY);
		GyroZ = (TextView) findViewById(R.id.gyroZ);
		
		OrientationA = (TextView) findViewById(R.id.orientationA);
		OrientationP = (TextView) findViewById(R.id.orientationP);
		OrientationR = (TextView) findViewById(R.id.orientationR);
		
		RotVecX = (TextView) findViewById(R.id.rotVecX);
		RotVecY = (TextView) findViewById(R.id.rotVecY);
		RotVecZ = (TextView) findViewById(R.id.rotVecZ);
		RotVecC = (TextView) findViewById(R.id.rotVecC);
		
		LinAccX = (TextView) findViewById(R.id.linAccX);
		LinAccY = (TextView) findViewById(R.id.linAccY);
		LinAccZ = (TextView) findViewById(R.id.linAccZ);
		
		GravityX = (TextView) findViewById(R.id.gravityX);
		GravityY = (TextView) findViewById(R.id.gravityY);
		GravityZ = (TextView) findViewById(R.id.gravityZ);
		
		lattxt = (TextView) findViewById(R.id.lat);
		lngtxt = (TextView) findViewById(R.id.lng);
		timetxt = (TextView) findViewById(R.id.time);
		bearingtxt = (TextView) findViewById(R.id.bearing);
		speedtxt = (TextView) findViewById(R.id.speed);
		
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0 ){
			btnSpeak.setEnabled(false);
			btnSpeak.setText("Voice recognizer not present");
		}
		
		manager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		listener = new SensorEventListener(){
			
			public void onAccuracyChanged(Sensor arg0, int arg1) {
				
		    }
			
			@SuppressWarnings("deprecation")
			public void onSensorChanged(SensorEvent event){
				Sensor sensor = event.sensor;
				try{
					if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
				
					
					acceX = event.values[0];
					acceY = event.values[1];
					acceZ = event.values[2];
					
					Acce_X.add(acceX);
					Acce_Y.add(acceY);
					Acce_Z.add(acceZ);
					
					
				}
				else if(sensor.getType() == Sensor.TYPE_GYROSCOPE){
					gyroX = event.values[0];
					gyroY = event.values[1];
					gyroZ = event.values[2];
					
					Gyro_X.add(gyroX);
					Gyro_Y.add(gyroY);
					Gyro_Z.add(gyroZ);
				}
				else if (sensor.getType() == Sensor.TYPE_ORIENTATION){
					orientationA = event.values[0];
					orientationP = event.values[1];
					orientationR = event.values[2];
					
					Orientation_A.add(orientationA);
					Orientation_P.add(orientationP);
					Orientation_R.add(orientationR);
					
					
				}
				else if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
					linAccX = event.values[0];
					linAccY = event.values[1];
					linAccZ = event.values[2];
					
					LinAcc_X.add(linAccX);
					LinAcc_Y.add(linAccY);
					LinAcc_Z.add(linAccZ);
					
				}
				else if (sensor.getType() == Sensor.TYPE_GRAVITY){
					gravityX = event.values[0];
					gravityY = event.values[1];
					gravityZ = event.values[2];
					
					Gravity_X.add(gravityX);
					Gravity_Y.add(gravityY);
					Gravity_Z.add(gravityZ);
				}
				else if (sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
					rotVecX = event.values[0];
					rotVecY = event.values[1];
					rotVecZ = event.values[2];
					rotVecC = event.values[3];
					
					RotVec_X.add(rotVecX);
					RotVec_Y.add(rotVecY);
					RotVec_Z.add(rotVecZ);
					RotVec_C.add(rotVecC);
					
				}
				}catch(Exception e){
					e.printStackTrace();
				}
//				while (check){
//					Longitude.add(0.0);
//					Latitude.add(0.0);
//					Time.add((long) 0);
//					Bearing.add(0.0);
//					Speed.add(0.0);
//					
//					String time1 = Long.toString(time);
//					String lat1 = Double.toString(lat);
//					String lng1 = Double.toString(lng);
//					String bearing1 = Double.toString(bearing);
//					String speed1 = Double.toString(speed);
//					
//					Log.i("Time0",time1);
//					Log.i("Time0",lat1);
//					Log.i("Time0",lng1);
//					Log.i("Time0",bearing1);
//					Log.i("Time0",speed1);
//				}
			}
			
		};
		
		locationListener = new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				check = false;
				try{
					if(location != null){
						
						lat = location.getLatitude();
						lng = location.getLongitude();
						time = location.getTime();
						bearing = location.getBearing();
						speed = location.getSpeed();
						
						Longitude.add(lng);
						Latitude.add(lat);
						Time.add(time);
						Bearing.add(bearing);
						Speed.add(speed);
						String time1 = Long.toString(time);
						String lat1 = Double.toString(lat);
						String lng1 = Double.toString(lng);
						String bearing1 = Double.toString(bearing);
						String speed1 = Double.toString(speed);
						
						Log.i("Time1",time1);
						Log.i("Time1",lat1);
						Log.i("Time1",lng1);
						Log.i("Time1",bearing1);
						Log.i("Time1",speed1);
						
						
					}
					else{
						lat = 1.0;
						lng = 1.0;
						time = 1;
						bearing = 10000.0;
						speed = 10000.0;
						
						Longitude.add(lng);
						Latitude.add(lat);
						Time.add(time);
						Bearing.add(bearing);
						Speed.add(speed);
						
						String time1 = Long.toString(time);
						String lat1 = Double.toString(lat);
						String lng1 = Double.toString(lng);
						String bearing1 = Double.toString(bearing);
						String speed1 = Double.toString(speed);
						Log.i("Time2",time1);
						Log.i("Time2",lat1);
						Log.i("Time2",lng1);
						Log.i("Time2",bearing1);
						Log.i("Time2",speed1);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		handler = new Handler();
		
	}
	
	
	public void speak(View v){
		if(v.getId() == R.id.speak){
			startVoiceRecognitionActivity();
		}
	}	
	
	
	public void start(){
	check = true;
	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),0);
	manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), 0);
	manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), 0);
	manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_GRAVITY), 0);
	manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_ORIENTATION), 0);
	manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), 0);
	
	
	handler.postDelayed(runnable,2000);
		
	}
	
	private void startVoiceRecognitionActivity() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "SPEAKING");
		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		if(requestCode == VOICE_RECOGNITION_REQUEST_CODE){
			if(resultCode == RESULT_OK){
				ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				if (matches.get(0).contains("start")){
					start();
					btnSpeak.setText("Speak!");
				}
				else if (matches.get(0).contains("stop")){
					stop();
					btnSpeak.setText("Speak!");
				}
				else if (matches.get(0).contains("send")){
					send();
					btnSpeak.setText("Speak!");
				}
				else if (matches.get(0).contains("reset")){
					reset();
					btnSpeak.setText("Speak!");
				}
				else
				{
					btnSpeak.setText("no match,please click and speak again");
				}
			}
		}
	}
	
	
	public void reset(){
		//check = true;
		//btnReset = (Button) findViewById(R.id.reset);
		Latitude.clear();
		Longitude.clear();
		Time.clear();
		Bearing.clear();
		Speed.clear();
		Acce_X.clear();
		Acce_Y.clear();
		Acce_Z.clear();
		Gyro_X.clear();
		Gyro_Y.clear();
		Gyro_Z.clear();
		LinAcc_X.clear();
		LinAcc_Y.clear();
		LinAcc_Z.clear();
		RotVec_X.clear();
		RotVec_Y.clear();
		RotVec_Z.clear();
		RotVec_C.clear();
		Gravity_X.clear();
		Gravity_Y.clear();
		Gravity_Z.clear();
		Orientation_A.clear();
		Orientation_P.clear();
		Orientation_R.clear();
		//Attribute.clear();
	}
	
	
	
	public void send(){
		hostip = etIP.getText().toString();
		port = Integer.parseInt(etPort.getText().toString());
		
		new AsyncTask<Void,Void,Void> (){
			protected Void doInBackground(Void... params){
				try{
					int size_lat = Latitude.size();
					int size_lng = Longitude.size();
					int size_bearing = Bearing.size();
					int size_speed = Speed.size();
					int size_time = Time.size();
					String size1 = Integer.toString(size_time);
					Log.i("size1", size1);
					String size2 = Integer.toString(size_lat);
					Log.i("size2", size2);
					String size3 = Integer.toString(size_bearing);
					Log.i("size3", size3);
					
					
					int size_acceX = Acce_X.size();
					int size_acceY = Acce_Y.size();
					int size_acceZ = Acce_Z.size();
					
					int size_orientationA = Orientation_A.size();
					int size_orientationP = Orientation_P.size();
					int size_orientationR = Orientation_R.size();
					
					int size_rotVecX = RotVec_X.size();
					int size_rotVecY = RotVec_Y.size();
					int size_rotVecZ = RotVec_Z.size();
					int size_rotVecC = RotVec_C.size();
					
					int size_linAccX = LinAcc_X.size();
					int size_linAccY = LinAcc_Y.size();
					int size_linAccZ = LinAcc_Z.size();
					
					int size_gyroX = Gyro_X.size();
					int size_gyroY = Gyro_Y.size();
					int size_gyroZ = Gyro_Z.size();
					
					int size_gravityX = Gravity_X.size();
					int size_gravityY = Gravity_Y.size();
					int size_gravityZ = Gravity_Z.size();
					
					Double[] lat_array = (Double[])Latitude.toArray(new Double[size_lat]);
					Double[] lng_array = (Double[])Longitude.toArray(new Double[size_lng]);
					Double[] bearing_array = (Double[])Bearing.toArray(new Double[size_bearing]);
					Double[] speed_array = (Double[])Speed.toArray(new Double[size_speed]);
					Long[] time_array = (Long[])Time.toArray(new Long[size_time]);
					
					Float[] acceX_array = (Float[])Acce_X.toArray(new Float[size_acceX]);
					Float[] acceY_array = (Float[])Acce_Y.toArray(new Float[size_acceY]);
					Float[] acceZ_array = (Float[])Acce_Z.toArray(new Float[size_acceZ]);
					
					Float[] orientationA_array = (Float[])Orientation_A.toArray(new Float[size_orientationA]);
					Float[] orientationP_array = (Float[])Orientation_P.toArray(new Float[size_orientationP]);
					Float[] orientationR_array = (Float[])Orientation_R.toArray(new Float[size_orientationR]);
					
					Float[] rotVecX_array = (Float[])RotVec_X.toArray(new Float[size_rotVecX]);
					Float[] rotVecY_array = (Float[])RotVec_Y.toArray(new Float[size_rotVecY]);
					Float[] rotVecZ_array = (Float[])RotVec_Z.toArray(new Float[size_rotVecZ]);
					Float[] rotVecC_array = (Float[])RotVec_C.toArray(new Float[size_rotVecC]);
					
					Float[] linAccX_array = (Float[])LinAcc_X.toArray(new Float[size_linAccX]);
					Float[] linAccY_array = (Float[])LinAcc_Y.toArray(new Float[size_linAccY]);
					Float[] linAccZ_array = (Float[])LinAcc_Z.toArray(new Float[size_linAccZ]);
					
					Float[] gyroX_array = (Float[])Gyro_X.toArray(new Float[size_gyroX]);
					Float[] gyroY_array = (Float[])Gyro_Y.toArray(new Float[size_gyroY]);
					Float[] gyroZ_array = (Float[])Gyro_Z.toArray(new Float[size_gyroZ]);
					
					Float[] gravityX_array = (Float[])Gravity_X.toArray(new Float[size_gravityX]);
					Float[] gravityY_array = (Float[])Gravity_Y.toArray(new Float[size_gravityY]);
					Float[] gravityZ_array = (Float[])Gravity_Z.toArray(new Float[size_gravityZ]);
					
					clientSocket = new Socket(hostip,port);
					os = new ObjectOutputStream(clientSocket.getOutputStream());
					Project2Packet Send = new Project2Packet
							(time_array, lat_array, lng_array,bearing_array , speed_array,
							acceX_array, acceY_array, acceZ_array,
							gyroX_array, gyroY_array, gyroZ_array, 
							orientationA_array, orientationR_array,orientationP_array, 
							rotVecX_array, rotVecY_array, rotVecZ_array, rotVecC_array,
							linAccX_array, linAccY_array,linAccZ_array, 
							gravityX_array, gravityY_array, gravityZ_array);
					os.writeObject(Send);
					os.flush();
					os.close();
					clientSocket.close();
					
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				
				return null;
				
			}
			
			
		}.execute();
		
	}
	
	protected void onResume(){
		super.onResume();
	}
	
	protected void onPause(){
		super.onPause();
		//manager.unregisterListener(listener);
	}
	
	public void stop(){
		manager.unregisterListener(listener);
		locationManager.removeUpdates(locationListener);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team4_proj2, menu);
		return true;
	}

	

}
