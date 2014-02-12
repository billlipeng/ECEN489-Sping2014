package com.mhardiman.gps_retrieval;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.location.*;

public class MainActivity extends Activity implements LocationListener{

	TextView coordsText;
	LocationManager lm;
	String provider;
	Location loc;
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
			//onLocationChanged(loc);
		}
		else
			System.out.println("null\n");

	}
	
	@SuppressLint("NewApi")
	public void getCoordinates(View view)
	{
		//System.out.println("test\n");
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
