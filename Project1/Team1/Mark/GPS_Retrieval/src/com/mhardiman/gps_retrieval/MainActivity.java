package com.mhardiman.gps_retrieval;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.location.*;

public class MainActivity extends Activity implements LocationListener{

	TextView coordsText;
	LocationManager lm;
	String provider;
	Location loc;
	Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() 
    {
    	 @Override
         public void run() {
             getCoordinates();
             timerHandler.postDelayed(this, 5000);
    	 }
    };
    	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		coordsText = (TextView) findViewById(R.id.coordsText);
		lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		provider = lm.getBestProvider(new Criteria(), false);
		loc = lm.getLastKnownLocation(provider);
		if (loc != null)
		{
			System.out.println("GPS connected\n");
		}
		else
			System.out.println("null\n");

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
	
	@SuppressLint("NewApi")
	public void getCoordinates()
	{
	  	lm.requestSingleUpdate(provider, this, null);
	}
	
	/*@Override
	  protected void onResume() {
	    super.onResume();
	    //lm.requestLocationUpdates(provider, 400, 1, this);
	  }

	  // Remove the locationlistener updates when Activity is paused 
	  @Override
	  protected void onPause() {
	    super.onPause();
	    lm.removeUpdates(this);
	  }*/
	  

	@Override
	  public void onLocationChanged(Location loc) {
		double latitude = loc.getLatitude();
		double longitude = loc.getLongitude();
		coordsText.setText(String.valueOf(latitude) + ", " + String.valueOf(longitude));
		System.out.print("onLocationChanged");
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
