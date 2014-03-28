package com.mfa157.rssigatheringteam3;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import 	android.net.wifi.WifiManager;
import android.database.Cursor;
import android.database.sqlite.*;

public class MainActivity extends Activity {
	
	private LocationManager locationManager;
	private LocationListener locationListener;
	MySQLiteHelper dbhelper;
	SQLiteDatabase db;
	Button bStart, bStop, bCollect, bView, bClear;
	String TAG = "RSSI app";
	double latitude, longitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dbhelper = new MySQLiteHelper(getApplicationContext());
		db=dbhelper.getWritableDatabase();
	}
	
	public void Start(View v) {
		bStart= (Button) findViewById(R.id.bStart);
		final TextView tvStatus = (TextView)findViewById(R.id.tvStatus);		
		final TextView tvData = (TextView)findViewById(R.id.tvData);	
		 tvStatus.setText("Status: Activating GPS...");
		locationListener = new LocationListener(){
			@Override
			public void onStatusChanged(String provider,
					int status, Bundle extras) {
				Log.i(TAG, "onStatusChanged");
			}

			@Override
			public void onLocationChanged(Location location) {
				tvStatus.setText("Status: GPS lock acquired");
					if (location != null) {
						latitude = location.getLatitude();
						longitude = location.getLongitude();							
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
		bStop= (Button) findViewById(R.id.bStop);
		final TextView tvStatus = (TextView)findViewById(R.id.tvStatus);
		
		try{
			locationManager.removeUpdates(locationListener);
			tvStatus.setText("Status: GPS stopped  " );
			}
			catch(Exception e){
			Log.e(TAG, "Error stopping gps", e);
			}

	}
	
	public void Collect(View v) {
		bCollect= (Button) findViewById(R.id.bCollect);
		TextView tvStatus = (TextView)findViewById(R.id.tvStatus);
		final TextView tvData = (TextView)findViewById(R.id.tvData);
		
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo Info = cm.getActiveNetworkInfo();
		
		 if (Info == null || !Info.isConnectedOrConnecting()) {
		        tvStatus.setText("Status: WIFI CONNECTION: No connection");
		    } else {
		        int netType = Info.getType();
		        int netSubtype = Info.getSubtype();

		        if (netType == ConnectivityManager.TYPE_WIFI) {
		        	WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		            int rssi = wifiManager.getConnectionInfo().getRssi();
		            
		            Cursor c = db.rawQuery("SELECT MAX(ID) FROM RSSI_data", null);
		            int id= (c.moveToFirst() ? c.getInt(0) : 0)+1;
		            db.execSQL("INSERT INTO RSSI_data (ID, RSSI, latitude, longitude) VALUES ("+id+","+rssi+","+latitude+", "+longitude+" );");
		            tvData.setText("Data: RSSI: "+rssi+" Points: "+id);
		        } 
		    }
	}
	
	public void View(View v) {
		bView= (Button) findViewById(R.id.bView);
		Log.i("OPEN DB", "Viewing data");
		// TODO Auto-generated method stub
		try{
		Intent dataView = new Intent(getApplicationContext(), DatabaseView.class);
		startActivity(dataView);	
		}catch (Exception e){
			Log.d("Open DB", e.getMessage());
		}
	}
	public void Clear(View v) {
		bClear= (Button) findViewById(R.id.bClear);
		db.execSQL("DELETE FROM RSSI_data;");

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

