package com.Team1.finalapp2;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zpartal.finalproject.datapackets.DataPoint;
import com.zpartal.finalproject.datapackets.DataPoint.Builder;

public class MainActivity extends Activity implements LocationListener {
	
	// base, calibration are the strings for SSID when calibrating 
	
	Boolean beginButtonPressed = false;
	Boolean stopStreamButtonPressed = false;
	Boolean cal1Pressed = false;
	Boolean cal2Pressed = false;
	
	// SQLite variables
	SQLiteDatabase dbStreamingData;
	SQLiteDatabase dbPointsData;
	
	// app elements
	Button calPoint1Button;
	Button calPoint2Button;
	Button dataPointButton;
	Button connectButton;
	Button beginMeasurementButton;
	TextView outputTextView;
	EditText ipEditText;
	EditText portNumEditText;
	
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
	
	// RSSI variables
	private ConnectivityManager cm;
	private NetworkInfo ni;
	private WifiManager wm;
	private int RSSI;
	private String SSID;
	
	ArrayList<DataPoint> dp = new ArrayList<DataPoint>();
	DataPoint point;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// intitialize app elements
		calPoint1Button = (Button) findViewById(R.id.calPoint1Button);
		calPoint2Button = (Button) findViewById(R.id.calPoint2Button);
		dataPointButton = (Button) findViewById(R.id.dataPointButton);
		connectButton = (Button) findViewById(R.id.connectButton);
		beginMeasurementButton = (Button) findViewById(R.id.beginMeasurementsButton);
		outputTextView = (TextView) findViewById(R.id.outputTextView);
		ipEditText = (EditText) findViewById(R.id.ipEditText);
		portNumEditText = (EditText) findViewById(R.id.portNumEditText);
		outputTextView.setMovementMethod(new ScrollingMovementMethod());
		
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
			onLocationChanged(location);
		}
		
		// database setup
		dbPointsData = openOrCreateDatabase("TestDataRSSI.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		dbStreamingData = openOrCreateDatabase("StreamingDataRSSI.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		
		dbPointsData.setVersion(3);
		dbPointsData.setLocale(Locale.getDefault());
		dbStreamingData.setVersion(3);
		dbStreamingData.setLocale(Locale.getDefault());
		
		final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS data (time REAL, device_id STRING,"
				 + "ssid STRING, rssi REAL, "
				 + "longitude REAL, latitude REAL);";
		dbPointsData.execSQL(CREATE_TABLE);
		dbStreamingData.execSQL(CREATE_TABLE);
		
		// setup button click event handlers
		
		beginMeasurementButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (beginMeasurementButton.getText().equals("Begin Streaming Measurement")) {
					outputTextView.setText("Taking Streaming Measurements");
					beginButtonPressed = true;
					stopStreamButtonPressed = false;
					beginMeasurementButton.setText("Stop Stream");
				} else {
					outputTextView.setText("Streaming Stopped");
					stopStreamButtonPressed = true;
					beginButtonPressed = false;
				}
			}
		});
		
		connectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (connectButton.getText().equals("Connect To Server")) {
					connectButton.setText("Disconnect From Server");
					outputTextView.setText("Connect to Server");
					
					IP = ipEditText.getText().toString().trim();
					Port = Integer.parseInt(portNumEditText.getText().toString());
					
					AsyncCreateConnection createConnection = new AsyncCreateConnection();
					createConnection.execute();
					
				} else {
					connectButton.setText("Connect To Server");
					outputTextView.setText("Disconnect From Server");
				}
			}
		});
		
		calPoint1Button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				beginButtonPressed = false;
				stopStreamButtonPressed = false;
				cal1Pressed = true;
				cal2Pressed = false;
			}
		});
		
		calPoint2Button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				beginButtonPressed = false;
				stopStreamButtonPressed = false;
				cal1Pressed = false;
				cal2Pressed = true;
			}
		});
		
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
				SSID + "\nDevice ID:\t" + deviceID);
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
	
	protected void onResume() {
		super.onResume();
		lm.requestLocationUpdates(provider, 400, 0, this);
	}

	protected void onPause() {
		super.onPause();
		lm.removeUpdates(this);
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
	
	public DataPoint takeMeasurements() {
		getRSSIandSSID();
		Builder build = new Builder();
		build.latitude(lat);
		build.longitude(lon);
		build.time(time);
		build.rssi(RSSI);
		build.ssid(SSID);
		build.device_id(deviceID);
		point = new DataPoint(build);
		dp.add(point);
		return point;
		//addToMeasurementsDatabase(point);
	}
	
	public void addToMeasurementsDatabase (DataPoint dp) {
		ContentValues data = new ContentValues();
		data.put("time", dp.getTime());
		data.put("device_id", dp.getDevice_id());
		data.put("ssid", dp.getSsid());
		data.put("rssi", dp.getRssi());
		data.put("longitude", dp.getLongitude());
		data.put("latitude", dp.getLatitude());
		dbPointsData.insertOrThrow("data", null, data);
	}
	
	public void addToStreamingDatabase(DataPoint dp) {
		ContentValues data = new ContentValues();
		data.put("time", dp.getTime());
		data.put("device_id", dp.getDevice_id());
		data.put("ssid", dp.getSsid());
		data.put("rssi", dp.getRssi());
		data.put("longitude", dp.getLongitude());
		data.put("latitude", dp.getLatitude());
		dbStreamingData.insertOrThrow("data", null, data);
	}
	
	public class AsyncCreateConnection extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try {
				socket = new Socket(InetAddress.getByName(IP), Port);
				out = new ObjectOutputStream(socket.getOutputStream());
				
				while (true) {
					android.os.SystemClock.sleep(1000); // one second wait
					DataPoint point = takeMeasurements();
					Builder build = new Builder();
					build.latitude(point.getLatitude());
					build.longitude(point.getLongitude());
					build.time(point.getTime());
					build.rssi(point.getRssi());
					build.device_id(point.getDevice_id());
					if (beginButtonPressed) {
						out.writeObject(point);
						out.flush();
						addToStreamingDatabase(point);
					}
					if (cal1Pressed) {
						build.ssid("base");
						DataPoint dp = new DataPoint(build);
						out.writeObject(dp);
						out.flush();
						cal1Pressed = false;
					}
					if (cal2Pressed) {
						build.ssid("calibration");
						DataPoint dp = new DataPoint(build);
						out.writeObject(dp);
						out.flush();
						cal2Pressed = false;
					}
					if (stopStreamButtonPressed) {
						beginButtonPressed = false;
						break;
					}
				}
			} catch (IOException e) {
				outputTextView.append(e.getMessage());
			}
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				outputTextView.append(e.getMessage());
			}
			return null;
		}
	}
	
	
	
}
