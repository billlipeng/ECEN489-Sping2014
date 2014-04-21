package com.example.bestteam;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;




import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {

	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 0; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
    protected LocationManager locationManager;
	public GPSConection task1;
	double posa=0, posb=0;
	int flag=0;//used for connection
	public int flag2=0;
	private ObjectOutputStream output; // output stream to server
	private ObjectInputStream input; // input stream from server
	public Socket socket;
	Handler mHandler = new Handler();

	
	MySQLiteHelper db1 = new MySQLiteHelper(this);


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
	      
	      
	      locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	              locationManager.requestLocationUpdates(
	                      LocationManager.GPS_PROVIDER,
	                      MINIMUM_TIME_BETWEEN_UPDATES,
	                      MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
	                      new MyLocationListener()
	              );

	      
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

	   
	   
	   public void stop(View view){
			TextView tv = (TextView)findViewById(R.id.TextView1);

			tv.setText("STOP: "+WifiDataList.size());
		  	flag=2;
		  	flag2=0; //1 for Automatic, 0 for manual
			socketConection task2 = new socketConection();
			
			task2.execute("a");
	   }
	   
		  public void change (View view){
			  Intent intent = new Intent(this, SQLActivity.class);
			  startActivity(intent);

			  

		  }
	   
	   public void store (View view) throws InterruptedException{
	     //   Log.i("MainActivity", Integer.toString(rssivalue));
	
	        Log.i("MainActivity","vaientrarthread");

	      //	task1 = new GPSConection();
	      //	task1.execute("a");
	
		  	flag=1;
		  	flag2=0;
			TextView tv = (TextView)findViewById(R.id.TextView1);

			tv.setText(Integer.toString(WifiDataList.size()));
		//	if (flag2==0)
		//	{	
				socketConection task2 = new socketConection();
		
				task2.execute("a");
			//}	
		 //  }while (flag==1);
	   }
	   public void send (View view){

			flag=0;//will start a new conection
			flag2=0;
			TextView tv = (TextView)findViewById(R.id.TextView1);

			tv.setText(Integer.toString(WifiDataList.size()));
			socketConection task2 = new socketConection();
			task2.execute("a");

	   }

		public class GPSConection extends AsyncTask<String, Void, String> {
			
			
			
			@Override
			protected String doInBackground(String... nothing) {
				
		

					return "A";

					
				}				 				  
			}

		Runnable codeToRun = new Runnable() {
		    @Override
		    public void run() {
				TextView tv = (TextView)findViewById(R.id.TextView1);

				tv.setText(Integer.toString(WifiDataList.size()));
		    }
		};
		


	    private class MyLocationListener implements LocationListener {

	        public void onLocationChanged(Location location) {
	         //   String message = String.format(
	        //            "New Location \n Longitude: %1$s \n Latitude: %2$s",
	        	try{
	                    posa=location.getLongitude(); 
	                    posb=location.getLatitude();
	        			Log.i("AsyncTask", "SHOW NEW "+Double.toString(posa));
	        	}
	        	finally{}
	         //   );
	            //Toast.makeText(LbsGeocodingActivity.this, message, Toast.LENGTH_LONG).show();
	        }

	        public void onStatusChanged(String s, int i, Bundle b) {
	            //Toast.makeText(LbsGeocodingActivity.this, "Provider status changed",
	              //      Toast.LENGTH_LONG).show();
	        }

	        public void onProviderDisabled(String s) {
	          //  Toast.makeText(LbsGeocodingActivity.this,
	            //        "Provider disabled by the user. GPS turned off",
	        //            Toast.LENGTH_LONG).show();
	        }

	        public void onProviderEnabled(String s) {
	    //        Toast.makeText(LbsGeocodingActivity.this,
	              //      "Provider enabled by the user. GPS turned on",
	      //              Toast.LENGTH_LONG).show();
	        }

	    }
	    		  



		  private class socketConection extends AsyncTask<String, Void, String> {

				@Override
				protected String doInBackground(String... urls) {
					//flag2=1;
			        int rssivalue; 
			        Location location;
					Conection UserPack = new Conection(0.0,0.0,0);
					EditText editText1;
					String IP;
					String going;
					String ret = null;
				//	while (true){
					     if (flag2==0){   

					
			        rssivalue=mainWifiObj.getConnectionInfo().getRssi();
					UserPack.setLatitude(posa);
					UserPack.setLongitude(posb);
			        db1.addBook(new Book(Integer.toString(rssivalue),Double.toString(posa),Double.toString(posb)));
				//	db.addBook(new Book(Double.toString(posa),Double.toString(posb)));

					UserPack.setRIIS(rssivalue);
					WifiDataList.add(UserPack);
					Log.i("MainActivity","vaientrarthread");			
					final int SERVER_PORT = 10000;
					DatagramSocket socket;
					
				    Log.i("AsyncTask","Rodando");	  
				    Criteria criteria = new Criteria();

				    String bestProvider = locationManager.getBestProvider(criteria, false);
				    location = locationManager.getLastKnownLocation(bestProvider);

				//	location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			   
					if (location != null) {

						posa=location.getLatitude(); //Store in variable that will go to sqlite
						posb=location.getLongitude();//Store in variable that will go to sqlite
					}
					Log.i("AsyncTask", "SHOW"+Double.toString(posa));

	
				
					
					editText1 = (EditText) findViewById(R.id.Edit1);
				    IP = editText1.getText().toString();
					//10.200.214.90
					//  final String SERVER_ADDRESS = "10.200.214.90";//public ip of my server
					Log.i("AsyncTask", "doInBackground: Creating socket");                       
					try {
						 InetAddress serverAddr = InetAddress.getByName(IP);
				            socket = new DatagramSocket();
				            if (flag==0) {
					            //Preparing the packet
				            	int i=WifiDataList.size();

				            	going = "2,2,"+Integer.toString(WifiDataList.get(i-1).getRIIS());

					     // Need this for rescue now it is test      
				            	//for(int i=0;i<WifiDataList.size();i++){
						          //  going = Double.toString(WifiDataList.get(i).getLatitude())+","+Double.toString(WifiDataList.get(i).getLongitude())+","+Integer.toString(WifiDataList.get(i).getRIIS());
						           
						            byte[] buf = going.getBytes();
						            DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, SERVER_PORT);
					            
					            //Sending the packet
			//		            Log.i("AsyncTask", String.format("Sending: '%s' to %s:%s", new String(buf), IP, SERVER_PORT));
					            socket.send(packet);
					            Log.i("AsyncTask", "Packet sent.");
							     // Need this for rescue now it is test      

					            //      }
				            }
				            else if (flag==1){
				            	int i=WifiDataList.size();
					            going = Double.toString(WifiDataList.get(i-1).getLatitude())+","+Double.toString(WifiDataList.get(i-1).getLongitude())+","+Integer.toString(WifiDataList.get(i-1).getRIIS());
					            byte[] buf = going.getBytes();
					            DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, SERVER_PORT);
					            
					            //Sending the packet
					            Log.i("AsyncTask", String.format("Sending: '%s' to %s:%s", new String(buf), IP, SERVER_PORT));
					            socket.send(packet);
					            Log.i("AsyncTask", "Packet sent.");
				            }
				            else if (flag==2){
				            	int i=WifiDataList.size();

				            	String going1 = "1,1,"+Integer.toString(WifiDataList.get(i-1).getRIIS());

				            	//String going1 = "1,1,1";
					            byte[] buf = going1.getBytes();
					            DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, SERVER_PORT);
					            
					            //Sending the packet
					            Log.i("AsyncTask", String.format("Sending: '%s' to %s:%s", new String(buf), IP, SERVER_PORT));
					            socket.send(packet);
					            Log.i("AsyncTask", "Packet sent. " + flag);
				            }
				         
				  		    mHandler.postDelayed(codeToRun, 100);
						//	return "A";

				       //     Thread.sleep(5000);

						}
				//	
				         catch (Exception e) {
				            Log.i("AsyncTask", "Client error", e);
	
				        }

					}
	//		}
						//this flag in here determines when the socket will be created again. So, after
						//Initializing a connection, we set flag to 1. And every time we enter the
						//Thread again, it will skip this part of creation. If wanted to start another
						//connection, just set flag=0 outside the Thread. flag is outside the thread, and
						//can be used everywhere inside the MainActivity.
				/*		if (flag==0){ 
							socket = new Socket();
							Log.i("AsyncTask", "try Create");

							SocketAddress sockad = new InetSocketAddress("10.201.62.13", 5555); 
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
*/
						return ret;

				}

			}


	}