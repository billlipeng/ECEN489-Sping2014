package com.team1.finalapp;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
//import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.team1.finalapp.DataPoint.Builder;

public class FinalAppMainActivity extends Activity implements LocationListener {
	
	// SQLite variables
	//SQLiteDatabase db;

	// GPS Variables
	private LocationManager lm;
	private String provider;
	private double lat;
	private double lon;
	
	private long time;
	private String deviceID = "device1";
	
	// server variables
	private String IP;
	private int Port;
	static private Socket socket;
	private ObjectOutputStream out;
	
	// app elements
	TextView outputTextView;
	TextView ipTextView;
	TextView portTextView;
	Button beginButton;
	Button connectButton;
	EditText ipEditText;
	EditText portEditText;
	
	// RSSI variables
	private ConnectivityManager cm;
	private NetworkInfo ni;
	private WifiManager wm;
	private int RSSI;
	private String SSID;
	
	ArrayList<DataPoint> dp = new ArrayList<DataPoint>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final_app_main);
		
		beginButton = (Button) findViewById(R.id.beginButton);
		connectButton = (Button) findViewById(R.id.connectButton);
		outputTextView = (TextView) findViewById(R.id.outputTextView);
		ipEditText = (EditText) findViewById(R.id.ipEditText);
		portEditText = (EditText) findViewById(R.id.portEditText);
		ipTextView = (TextView) findViewById(R.id.ipTextView);
		portTextView = (TextView) findViewById(R.id.portTextView);
		
		outputTextView.setTextColor(Color.parseColor("#FFFFFF"));
		ipEditText.setTextColor(Color.parseColor("#FFFFFF"));
		portEditText.setTextColor(Color.parseColor("#FFFFFF"));
		ipTextView.setTextColor(Color.parseColor("#FFFFFF"));
		portTextView.setTextColor(Color.parseColor("#FFFFFF"));
		connectButton.setTextColor(Color.parseColor("#FFFFFF"));
		beginButton.setTextColor(Color.parseColor("#FFFFFF"));
		
		beginButton.setVisibility(View.INVISIBLE);
		
		// GPS setup
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = lm.getBestProvider(criteria, false);
		Location location = null;
		
		try {
			location = lm.getLastKnownLocation(provider);
		} finally {
			if (location == null)
				Log.e("location", "manager error");
		}
		
		if (location != null) {
			System.out.println("Provider" + provider + " has been selected.");
			onLocationChanged(location);
		}
		
		// connect to server when connect button pushed
		connectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				IP = ipEditText.getText().toString().trim();
				Port = Integer.parseInt(portEditText.getText().toString());
				
				AsyncCreateConnection createConnection = new AsyncCreateConnection();
				createConnection.execute();
			}
		});
	}
	
	public void getRSSIandSSID() {
		// WiFi RSSI setup
		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		ni = cm.getActiveNetworkInfo();
		
		if (ni == null || !(ni.isConnectedOrConnecting())) 
			outputTextView.setText("Error: WiFi Not Connected!");
		else {
			if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
				wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
				RSSI = wm.getConnectionInfo().getRssi();
				SSID = wm.getConnectionInfo().getSSID();
			}
		}
	}
	
	public class AsyncCreateConnection extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try {
				socket = new Socket(InetAddress.getByName(IP), Port);
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				for (int i = 0; i < dp.size(); ++i) {
					out.writeObject((DataPoint)dp.get(i));
					out.flush();
				}
			} catch (IOException e) {
				outputTextView.append(e.getMessage());
			}
			return null;
		}
	}
	
	protected void onResume() {
		super.onResume();
		lm.requestLocationUpdates(provider, 400, 0, this);
	}
	
	protected void onPause() {
		super.onPause();
		lm.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		lat = (double) (location.getLatitude());
		lon = (double) (location.getLongitude());
		time = (long) location.getTime();
		getRSSIandSSID();
		outputTextView.setText("Latitude:\t" + lat + "\nLongitude:\t" + 
							   lon + "\nTime:\t" + 
							   time + "\nRSSI:\t" + 
							   RSSI + "\nSSID:\t" +
							   SSID + "\nDevice ID:\t" +
							   deviceID);
		Builder build = new Builder();
		build.latitude(lat);
		build.longitude(lon);
		build.time(time);
		build.rssi(RSSI);
		build.ssid(null);
		build.device_id(deviceID);
		DataPoint data = new DataPoint(build);
		dp.add(data);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	

}
