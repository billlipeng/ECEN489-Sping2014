package com.mfa157.rssigatheringteam3;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.FinalPhaseOnePacket.FinalProjectPhaseOnePacket1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
	ObjectOutputStream os;
	ObjectInputStream is;
	int port;
	String hostip;
	Socket clientSocket;
	
	private LocationManager locationManager;
	private LocationListener locationListener;
	MySQLiteHelper dbhelper;
	SQLiteDatabase db;
	Button bStart, bStop, bCollect, bView, bClear, bSend;
	RadioButton rbCollection, rbStream;
	EditText etIP, etPort;
	String TAG = "RSSI app";
	double latitude, longitude;
	ArrayList<Double> lat_list = new ArrayList<Double>();
	ArrayList<Double> long_list = new ArrayList<Double>();
	ArrayList<Double> rssi_list = new ArrayList<Double>();
	
	//Array with 6 values, 0 and 1 are antenna lat and long, 2 and 3 are start point lat and long, 4 and 5 are end point lat and long
	double[] streamArray = {0,0,0,0,0,0}; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bStart= (Button) findViewById(R.id.bStart);
		bStop= (Button) findViewById(R.id.bStop);
		bCollect= (Button) findViewById(R.id.bCollect);
		bView= (Button) findViewById(R.id.bView);
		bClear= (Button) findViewById(R.id.bClear);
		bSend= (Button) findViewById(R.id.bSend);
		
		rbCollection= (RadioButton) findViewById(R.id.rbCollection);
		rbStream= (RadioButton) findViewById(R.id.rbStream);
		
		bCollect.setEnabled(false);
		dbhelper = new MySQLiteHelper(getApplicationContext());
		db=dbhelper.getWritableDatabase();
	}
	public void Mode(View v) {
		if(rbCollection.isChecked()){
			bCollect.setText("Collect");
			bView.setText("View Data");
			bClear.setText("Clear Data");
			
			bCollect.setEnabled(false);
			bView.setEnabled(true);
			bClear.setEnabled(true);
			
		}
		else if(rbStream.isChecked()){
			bCollect.setText("Store Antenna Pos");
			bView.setText("Get Start Point");
			bClear.setText("Get End Point");
			
			bCollect.setEnabled(false);
			bView.setEnabled(false);
			bClear.setEnabled(false);
		}
	
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
				bCollect.setEnabled(true);
				bView.setEnabled(true);
				bClear.setEnabled(true);
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
				if(rbCollection.isChecked()){
					bCollect.setEnabled(false);
					bView.setEnabled(true);
					bClear.setEnabled(true);
				}
				else if(rbStream.isChecked()){
					bCollect.setEnabled(false);
					bView.setEnabled(false);
					bClear.setEnabled(false);
				}
			}
			catch(Exception e){
			Log.e(TAG, "Error stopping gps", e);
			}
		
		

	}
	
	//Collect rssi or store antenna position
	public void Collect(View v) {
		bCollect= (Button) findViewById(R.id.bCollect);
		TextView tvStatus = (TextView)findViewById(R.id.tvStatus);
		TextView tvData = (TextView)findViewById(R.id.tvData);
		
		if(rbCollection.isChecked()){
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
			            
			            rssi_list.add((double)rssi);
			            lat_list.add(latitude);
			            long_list.add(longitude);
			        } 
			    }
		}
		//in Stream mode, store antenna starting position
		else if(rbStream.isChecked()){
			streamArray[0]=latitude;
			streamArray[1]=longitude;
			tvStatus.setText("Status: Antenna position recorded");
			tvData.setText("lat: "+streamArray[0]+" lon: "+streamArray[1]);
		}
	}
	//view db or get start position
	public void View(View v) {
		bView= (Button) findViewById(R.id.bView);
		TextView tvStatus = (TextView)findViewById(R.id.tvStatus);
		TextView tvData = (TextView)findViewById(R.id.tvData);
		
		if(rbCollection.isChecked()){
			Log.i("OPEN DB", "Viewing data");
			// TODO Auto-generated method stub
			try{
			Intent dataView = new Intent(getApplicationContext(), DatabaseView.class);
			startActivity(dataView);	
			}catch (Exception e){
				Log.d("Open DB", e.getMessage());
			}
		}
		//in stream mode, get start position
		else if(rbStream.isChecked()){
			streamArray[2]=latitude;
			streamArray[3]=longitude;
			tvStatus.setText("Status: Start position recorded");
			tvData.setText("lat: "+streamArray[2]+" lon: "+streamArray[3]);
		}
	}
	//clear data or get end position
	public void Clear(View v) {
		bClear= (Button) findViewById(R.id.bClear);
		TextView tvStatus = (TextView)findViewById(R.id.tvStatus);
		TextView tvData = (TextView)findViewById(R.id.tvData);
		
		if(rbCollection.isChecked()){
			db.execSQL("DELETE FROM RSSI_data;");
			rssi_list.clear();
			lat_list.clear();
			long_list.clear();
		}
		else if(rbStream.isChecked()){
			streamArray[4]=latitude;
			streamArray[5]=longitude;
			tvStatus.setText("Status: End position recorded");
			tvData.setText("lat: "+streamArray[4]+" lon: "+streamArray[5]);
		}

	}
	
	public void Send(View v) {
		bSend= (Button) findViewById(R.id.bSend);
		etPort= (EditText) findViewById(R.id.etPort);
		etIP= (EditText) findViewById(R.id.etIP);
		
		hostip = etIP.getText().toString();
		port = Integer.parseInt(etPort.getText().toString());
		
		 new AsyncTask<Void, Void, Void> (){
			 protected Void doInBackground(Void... params) {
				 if(rbCollection.isChecked()){
				    try {
				    	Double[] latitude_array = (Double[])lat_list.toArray(new Double[lat_list.size()]);
						Double[] longitude_array = (Double[])long_list.toArray(new Double[long_list.size()]);
						Double[] rssi_array = (Double[])rssi_list.toArray(new Double[rssi_list.size()]);
			
						
						Log.i(TAG,"Connecting");
						clientSocket = new Socket(hostip,port);
						os = new ObjectOutputStream(clientSocket.getOutputStream());
						FinalProjectPhaseOnePacket1 Send = new FinalProjectPhaseOnePacket1(latitude_array, longitude_array, rssi_array);
						os.writeObject(Send);
						Log.i(TAG, "Data Sent!");
						os.flush();
						os.close();
						clientSocket.close();
				    
				    } catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				 }
				 else if(rbStream.isChecked()){
					 try{
							
							Log.i(TAG,"Connecting");
							clientSocket = new Socket(hostip,port);
							os = new ObjectOutputStream(clientSocket.getOutputStream());
							os.writeObject(streamArray);
							Log.i(TAG, "Data Sent!");
							os.flush();
							os.close();
							clientSocket.close();
					    
					    } catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					 
				 }
			    
			    return null; }
			 
			 protected void onPostExecute(Void result) {
				// show.setText("DATA SENT!");
			 };
			 
		 }.execute();

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

