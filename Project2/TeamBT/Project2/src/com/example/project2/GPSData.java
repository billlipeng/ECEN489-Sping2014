package com.example.project2;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GPSData implements LocationListener {
	  private static final String TAG = "GPSData";
	  private LocationManager locationManager;
	  private String provider;
	  private double lat;
	  private double lng;
	  private double bearing;
	  private double speed;
	  
	  public double getBearing() {
		return bearing;
	}

	public void setBearing(double bearing) {
		this.bearing = bearing;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void onCreate(){
		
	    Criteria criteria = new Criteria();
	    Log.d(TAG, criteria.toString());
	    provider = locationManager.getBestProvider(criteria, false);
	    Location location = null;
	    
	    try{
	        location = locationManager.getLastKnownLocation(provider);
	        } finally {if (location == null) Log.e("location", "manager error" );}
	    
	    if (location != null) {
	        Log.d(TAG, "Provider " + provider + " has been selected.");
	        onLocationChanged(location);
	      }
	  }
	  
	  public GPSData(LocationManager locationManager) {
		super();
		this.locationManager = locationManager;
	}

	public void onPause(){
		  locationManager.requestLocationUpdates(provider, 400, 1, this);
	  }
	  
	  public void onResume(){
		  locationManager.removeUpdates(this);
	  }
	  
	  @Override
	  public void onLocationChanged(Location location) {
	    lat = (double) (location.getLatitude());
	    lng = (double) (location.getLongitude());
	    bearing = location.getBearing();
	    speed = (double) location.getSpeed();
	  }
	  @Override
	  public void onProviderEnabled(String provider) {

	  }
	  
	  @Override
	  public void onProviderDisabled(String provider) {
		  
	  }
	  
	  @Override
	  public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	  	// TODO Auto-generated method stub
	  	
	  }
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	  
}
