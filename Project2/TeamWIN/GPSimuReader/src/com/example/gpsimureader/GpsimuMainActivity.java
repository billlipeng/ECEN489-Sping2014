package com.example.gpsimureader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GpsimuMainActivity extends Activity implements LocationListener, SensorEventListener {
//GPS DECL
  private TextView latText;
  private TextView longText;
  private LocationManager locationManager;
  private String provider;
  private SensorManager sensorManager;
  
  private double lat;
  private double lng;
  
//OTHER DECL
  private double bear;
  private double spd;
  private double acX;
  private double acY;
  private double acZ;
  private double orA;
  private double orP;
  private double orR;
  private double rvX;
  private double rvY;
  private double rvZ;
  private double rvC;
  private double laX;
  private double laY;
  private double laZ;
  private double gX;
  private double gY;
  private double gZ;
  private double gyX;
  private double gyY;
  private double gyZ;
  
//TIMER DECL
  private long time;
  public long strtTime = 0;
	public String name = "tmr1";
	TextView tmr;
	Handler timerHandler = new Handler();
	public int interv = -1;
  Runnable timerRunnable = new Runnable() {
  	@Override
      public void run() {
          long millis = System.currentTimeMillis() - strtTime;
          int seconds = (int) (millis / 1000);
          int minutes = seconds / 60;
          seconds = seconds % 60;

          tmr.setText(String.format("%d:%02d", minutes, seconds));
          if(seconds%interv == 0)
          {
        	  //GET ALL VALUES

        	  DPprocess();
          }
          timerHandler.postDelayed(this, 500);
      }
  };
  
  //DATAPOINT DECL
  SimpleDateFormat d2 = new SimpleDateFormat("hh:mm:ss");
  
  
  ArrayList<DataPoint> dp = new ArrayList<DataPoint>();
  
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //----------------------------------------------------
    //SENSOR STUFF
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    
    //GPS STUFF
    setContentView(R.layout.activity_gpsimu_main);
    latText = (TextView) findViewById(R.id.latText);
    longText = (TextView) findViewById(R.id.longText);
    
    // Get the location manager
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    Criteria criteria = new Criteria();
    provider = locationManager.getBestProvider(criteria, false);
    Location location = null;
//
    try{
    location = locationManager.getLastKnownLocation(provider);
    } finally {if (location == null) Log.e("location", "manager error" );}

    if (location != null) {
      System.out.println("Provider " + provider + " has been selected.");
      onLocationChanged(location);
    } else {
      latText.setText("Location not available");
      longText.setText("Location not available");
    }
    //------------------------------------------
    //TIMER STUFF
    tmr = (TextView) findViewById(R.id.tmr);
    
    //for gps integration
    final EditText interval = (EditText) findViewById(R.id.coordInterval);
	
    
    //button func
	Button reCoord = (Button) findViewById(R.id.getCoords);
	
	reCoord.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			Button reCoord = (Button) view;
            if (reCoord.getText().equals("stop")) {
                timerHandler.removeCallbacks(timerRunnable);
                reCoord.setText("recoord");
            } else {
            	//------for gps integration
            	
            	if (interval.getText().toString().length() > 0)
            	    interv = Integer.parseInt(interval.getText().toString());
            	Calendar cal1 = new GregorianCalendar();
            	java.util.Date callTime1 = cal1.getTime();
            	java.sql.Date sqlDate = new java.sql.Date(callTime1.getTime());

            	//------timerry stuff
            	//System.out.println(interv);
                strtTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
                reCoord.setText("stop");
            }
		}
	});
    
	
	
	
  }//--------------------------------END OF ONCREATE

  /* Request updates at startup */
  @Override
  protected void onResume() {
    super.onResume();
    locationManager.requestLocationUpdates(provider, 400, 1, this);
    sensorManager.registerListener(this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL);
    sensorManager.registerListener(this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
            SensorManager.SENSOR_DELAY_NORMAL);
    sensorManager.registerListener(this,
            sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
            SensorManager.SENSOR_DELAY_NORMAL);
    sensorManager.registerListener(this,
            sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
            SensorManager.SENSOR_DELAY_NORMAL);
    sensorManager.registerListener(this,
            sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
            SensorManager.SENSOR_DELAY_NORMAL);
    sensorManager.registerListener(this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_NORMAL);
  }

  /* No updates when not open */
  @Override
  protected void onPause() {
    super.onPause();
    locationManager.removeUpdates(this);
    sensorManager.unregisterListener(this);
  }

  @Override
  public void onLocationChanged(Location location) {
    lat = (double) (location.getLatitude());
    lng = (double) (location.getLongitude());
    time = location.getTime();
    bear = (double) (location.getBearing());
    spd = (double) (location.getSpeed());

    latText.setText(String.valueOf(lat));
    longText.setText(String.valueOf(lng));
    
    //System.out.println("does it get this far again?");
	
  }

  //SENSOR CHANGE
  @SuppressWarnings("deprecation")
public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
    	getAccelerometer(event);
    }
    else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
    	getRotVec(event);
    }
    else if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
    	getLinAcc(event);
    }
    else if (event.sensor.getType() == Sensor.TYPE_GRAVITY){
    	getGravity(event);
    }
    else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
    	getGyro(event);
    }
    else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION){
    	getOrientation(event);
    }
    
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
//-----------------------------------------------
//SENSOR STUFF
private void getAccelerometer(SensorEvent event) {
    float[] values = event.values;
    // Movement
    acX = (double) values[0];
    acY = (double) values[1];
    acZ = (double) values[2];
}

private void getOrientation(SensorEvent event) {
    float[] values = event.values;
    // Movement
    orA = (double) values[0];
    orP = (double) values[1];
    orR = (double) values[2];
}

private void getRotVec(SensorEvent event) {
    float[] values = event.values;
    // Movement
    rvX = (double) values[0];
    rvY = (double) values[1];
    rvZ = (double) values[2];
//    rvC = (double) values[3]; 
}

private void getLinAcc(SensorEvent event) {
    float[] values = event.values;
    // Movement
    laX = (double) values[0];
    laY = (double) values[1];
    laZ = (double) values[2];
}

private void getGravity(SensorEvent event) {
    float[] values = event.values;
    // Movement
    gX = (double) values[0];
    gY = (double) values[1];
    gZ = (double) values[2];
}

private void getGyro(SensorEvent event) {
    float[] values = event.values;
    // Movement
    gyX = (double) values[0];
    gyY = (double) values[1];
    gyZ = (double) values[2];
}
//--------------------------------------------
//DataPoint Stuff

public void DPprocess() {
	//GET ALL VALUES
	
	
	
	
	DataPoint dpx = new DataPoint.Builder().time(time).latitude(lat).longitude(lng).bearing(bear).speed(spd).accelX(acX).accelY(acY).accelZ(acZ).orientationA(orA).orientationP(orP).orientationR(orR).rotVecX(rvX).rotVecY(rvY).rotVecZ(rvZ).rotVecC(rvC).linAccX(laX).linAccY(laY).linAccZ(laZ).gravityX(gX).gravityY(gY).gravityZ(gZ).gyroX(gyX).gyroY(gyY).gyroZ(gyZ).build();
	dp.add(dpx);
	
	//prints

		System.out.println("-----------------------");
		System.out.println("time: " + dpx.getTime());
		System.out.println("lat: " + dpx.getLatitude());
		System.out.println("long: " + dpx.getLongitude());
		System.out.println("bear: " + dpx.getBearing());
		System.out.println("speed: " + dpx.getSpeed());
		System.out.println("accX: " + dpx.getAccelX());
		System.out.println("accY: " + dpx.getAccelY());
		System.out.println("accZ: " + dpx.getAccelZ());
		System.out.println("orX: " + dpx.getOrientationA());
		System.out.println("orY: " + dpx.getOrientationP());
		System.out.println("orZ: " + dpx.getOrientationR());
		System.out.println("rvX: " + dpx.getRotVecX());
		System.out.println("rvY: " + dpx.getRotVecY());
		System.out.println("rvZ: " + dpx.getRotVecZ());
		System.out.println("rvC: " + dpx.getRotVecC());
		System.out.println("laX: " + dpx.getLinAccX());
		System.out.println("laY: " + dpx.getLinAccY());
		System.out.println("laZ: " + dpx.getLinAccZ());
		System.out.println("gX: " + dpx.getGravityX());
		System.out.println("gY: " + dpx.getGravityY());
		System.out.println("gZ: " + dpx.getGravityZ());
		System.out.println("gyX: " + dpx.getGyroX());
		System.out.println("gyY: " + dpx.getGyroY());
		System.out.println("gyZ: " + dpx.getGyroZ());
		
}

@Override
public void onAccuracyChanged(Sensor arg0, int arg1) {
	// TODO Auto-generated method stub
	
}
  
} 




