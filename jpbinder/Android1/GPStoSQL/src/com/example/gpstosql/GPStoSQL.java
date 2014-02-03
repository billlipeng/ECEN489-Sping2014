package com.example.gpstosql;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GPStoSQL extends Activity implements LocationListener {

  private TextView latText;
  private TextView longText;
  private LocationManager locationManager;
  private String provider;
  private int lat;
  private int lng;
  private SQLwork SQL;
  
  
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
   
    SQL = new SQLwork(this);
    
    setContentView(R.layout.activity_gps_to_sql);
    latText = (TextView) findViewById(R.id.latText);
    longText = (TextView) findViewById(R.id.longText);
    
    // Get the location manager
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    Criteria criteria = new Criteria();
    provider = locationManager.getBestProvider(criteria, false);
    Location location = null;

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

    //button listener
    final Button button = (Button) findViewById(R.id.SQLbtn);
    button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	updatetbl();

        }
    });
    
  
	ListView TblView = (ListView) findViewById(R.id.list1);
    getTbl();
  }

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
    SQL.newTbl();
  }

  @Override
  public void onLocationChanged(Location location) {
    lat = (int) (location.getLatitude());
    lng = (int) (location.getLongitude());

    latText.setText(String.valueOf(lat));
    longText.setText(String.valueOf(lng));
    
    System.out.println("does it get this far again?");
	
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
  
  public void getTbl() {
	  //allow to display table
  }
  
  public void updateSQL(int latit, int longit) {
	  SQL.coordsToDB(latit,longit);
	  System.out.println("coords added to db");
  }
  public void updatetbl() {
	  updateSQL(lat,lng);
  }
@Override
public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	// TODO Auto-generated method stub
	
}
  
  
} 


