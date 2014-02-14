package com.example.gpsproj;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
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

public class GPSproj extends Activity implements LocationListener {
//GPS DECL
  private TextView latText;
  private TextView longText;
  private LocationManager locationManager;
  private String provider;
  private double lat;
  private double lng;
  
//TIMER DECL
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
        	  int x = seconds/interv;
        	  Calendar cal = new GregorianCalendar();
        	  java.util.Date callTime = cal.getTime();
        	  java.sql.Date sqlDate = new java.sql.Date(callTime.getTime());
        	  DPprocess(sqlDate,x);
          }
          timerHandler.postDelayed(this, 500);
      }
  };
  
  //DATAPOINT DECL
  SimpleDateFormat d1 = new SimpleDateFormat("yyyy-mm-dd");
  SimpleDateFormat d2 = new SimpleDateFormat("hh:mm:ss");
  SimpleDateFormat d3 = new SimpleDateFormat("yyyymmdd_hhmmss");
  
  String client_id = "team3";
  String run_id; // teamid_yyyymmdd_hhmmss
  String time; // hh:mm:ss
  String date; // yyyy-mm-dd
  
  ArrayList<DataPoint> dp = new ArrayList<DataPoint>();
  
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //----------------------------------------------------
    //GPS STUFF
    setContentView(R.layout.activity_gpsproj);
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
            	String run_id_info = d3.format(sqlDate); // teamid_yyyymmdd_hhmmss
            	String run_id = "team3_"+run_id_info;
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
  }

  /* No updates when not open */
  @Override
  protected void onPause() {
    super.onPause();
    locationManager.removeUpdates(this);

  }

  @Override
  public void onLocationChanged(Location location) {
    lat = (double) (location.getLatitude());
    lng = (double) (location.getLongitude());
    

    latText.setText(String.valueOf(lat));
    longText.setText(String.valueOf(lng));
    
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
  

//--------------------------------------------
//DataPoint Stuff

public void DPprocess(Date d, int i) {
	//Time & Date info
	
	String date = d1.format(d); // yyyy-mm-dd
	String time = d2.format(d); // hh:mm:ss
	
	
	/*
	//prints
	System.out.println(client_id);
	System.out.println(date);
	System.out.println(time);
	System.out.println(run_id);
	System.out.println(lat);
	System.out.println(lng);
	*/
	
	
	
	DataPoint dpx = new DataPoint.Builder().client_id(client_id).run_id(run_id).time(time).date(date).latitude(lat).longitude(lng).build();
	dp.add(dpx);
	
	//prints
		System.out.println(dpx.getClient_id());
		System.out.println(dpx.getDate());
		System.out.println(dpx.getTime());
//		System.out.println(dpx.getRun_id());
		System.out.println(dpx.getLatitude());
		System.out.println(dpx.getLongitude());
	
}
  
} 




