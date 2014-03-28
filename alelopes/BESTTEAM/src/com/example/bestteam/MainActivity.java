package com.example.bestteam;




import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


public class MainActivity extends Activity {
	   
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 0; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
	public LocationManager locationManager;
	public GPSConection task1;
	double posa, posb;
	int flag=0;//used for connection

	private ObjectOutputStream output; // output stream to server
	private ObjectInputStream input; // input stream from server
	public Socket socket;
	
	
	   public ArrayList<Conection> WifiDataList = new ArrayList();
	   WifiManager mainWifiObj;
	   WifiScanReceiver wifiReciever;
	   ListView list;
	   String wifis[];
	   public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_main);
	      list = (ListView)findViewById(R.id.listView1);
	      mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);	      
	      mainWifiObj.setWifiEnabled(true);
	      	      	      
	      wifiReciever = new WifiScanReceiver();
	      mainWifiObj.startScan();
	   }



	   protected void onResume() {
	      registerReceiver(wifiReciever, new IntentFilter(
	      WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	      super.onResume();
	   }

	   class WifiScanReceiver extends BroadcastReceiver {
	      @SuppressLint("UseValueOf")
	      public void onReceive(Context c, Intent intent) {
	         List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
	         wifis = new String[wifiScanList.size()];
	         for(int i = 0; i < wifiScanList.size(); i++){
	            wifis[i] = ((wifiScanList.get(i)).toString());
	         }
	         list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
	         android.R.layout.simple_list_item_1,wifis));
	      }
	   }
	   
	   public void result(View view){
			EditText editText = (EditText) findViewById(R.id.Edit);
			String networkSSID = editText.getText().toString();
			//String networkPass = "12345678";

			List<WifiConfiguration> list = mainWifiObj.getConfiguredNetworks();
			for( WifiConfiguration i : list ) {
			    if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
			    	mainWifiObj.disconnect();
			    	mainWifiObj.enableNetwork(i.networkId, true);
			    	mainWifiObj.reconnect();               

			         break;
			    }           
			 }	    
	   }
	   
	   public void store (View view){
	        int rssivalue = mainWifiObj.getConnectionInfo().getRssi();
	        Log.i("MainActivity", Integer.toString(rssivalue));
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

	    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
			MINIMUM_TIME_BETWEEN_UPDATES,MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
			Log.i("MainActivity","vaientrarthread");			        
	    	task1 = new GPSConection();
			//the a means nothing.
			task1.execute("a");
			try{
			Thread.sleep(100);
			}
			catch (Exception e){
			}
			
			Conection UserPack = new Conection(0.0,0.0,0);
			UserPack.setLatitude(posa);
			UserPack.setLongitude(posb);
			UserPack.setRIIS(rssivalue);
			WifiDataList.add(UserPack);
			Log.i("MainActivity","vaientrarthread");			        

	   }
	   
	   public void send (View view){
	   
			flag=0;//will start a new conection
			socketConection task2 = new socketConection();

			task2.execute("a");

	   }
	   
		private class GPSConection extends AsyncTask<String, Void, String> {

			@Override
			protected String doInBackground(String... nothing) {
					Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					
					posa=location.getLatitude(); //Store in variable that will go to sqlite
					posb=location.getLongitude();//Store in variable that will go to sqlite
					Log.i("AsyncTask", Double.toString(posa));

					return "A";

				}				 				  
			}		
		
		private class MyLocationListener implements LocationListener {

			public void onLocationChanged(Location location) {
			}

			public void onStatusChanged(String s, int i, Bundle b) {
			}

			public void onProviderDisabled(String s) {
			}

			public void onProviderEnabled(String s) {
			}
		}


	   
		
		

		private class socketConection extends AsyncTask<String, Void, String> {


			@Override
			protected String doInBackground(String... urls) {

				Log.i("AsyncTask", "doInBackground: Creating socket");                       
				try {
					//this flag in here determines when the socket will be created again. So, after
					//Initializing a connection, we set flag to 1. And every time we enter the
					//Thread again, it will skip this part of creation. If wanted to start another
					//connection, just set flag=0 outside the Thread. flag is outside the thread, and
					//can be used everywhere inside the MainActivity.
					if (flag==0){ 
						socket = new Socket();
						Log.i("AsyncTask", "try Create");
						
						SocketAddress sockad = new InetSocketAddress("10.201.222.183", 5555); 
						socket.connect(sockad);
						Log.i("AsyncTask", "Could Create");
						output = new ObjectOutputStream(socket.getOutputStream());
					}

					//tests if conected
					if (socket.isConnected()){
							Log.i("AsyncTask", "try Send again");
							Log.i("AsyncTask", "Is Connected");

							output.writeObject(WifiDataList);
							output.flush();
							output.reset();
							Thread.sleep(500);
						

					}

					//after the for loop, I close the conection. (Dont need the flag 1, because it
					//is closed, but I used this because I dont feel that it is interesting
					// to send anymore to the server, sinse I already did it. We can create a 
					//button StartOver, or see other solution
					flag=1;
					output.close();
					input.close();
					socket.close();

				}

				catch (Exception e){
					Log.i("AsyncTask", "Coudnt Create");

				}
				Log.i("AsyncTask", "Ending");
				return null;

			}

		}

	   
	}
