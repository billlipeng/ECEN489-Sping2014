package com.example.project1_team2_v1_2;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.mfa157.project1packet.Project1Packet;









import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;

public class Team2Proj extends Activity {
	ObjectOutputStream os;
	ObjectInputStream is;
	private Button btnRec, btnEnd, btnGPS, btnSend,btnReset;
	private String DEVICE_ADDRESS ;

	
	private List<String> DeviceAddr_List = new ArrayList<String>();
	private TextView show,text, tvData;
	
	private Spinner team_spinner;
	private ArrayAdapter<CharSequence> adapter;
	private LocationManager locationManager;
	private EditText etIP;
	private EditText etPort;
	private TextView txt;
	boolean GPS_status = true;
	boolean receive_status = false;
	boolean sensor_flag = false;
	double lat, lng;
	String v1,v2;
	String timestamp, date,runtime;
	double v3;
	int i = 0;
	int startcheck = 0;
	int check = 0;
	boolean End;
	LocationListener locationListener;
	
	
	int port ;
	String hostip;
	Socket clientSocket;
	
	
	 ArduinoReceiver arduinoReceiver = new ArduinoReceiver();
	ArrayList<Double> sensorValue = new ArrayList<Double>();
	ArrayList<String> sensorType = new ArrayList<String>();
	ArrayList<String> sensorID = new ArrayList<String>();
	ArrayList<String> time_stamp = new ArrayList<String>();
	ArrayList<String> time_date = new ArrayList<String>();
	ArrayList<String> latitude = new ArrayList<String>();
	ArrayList<String> longitude = new ArrayList<String>();

	ArrayList <String> ClientID = new ArrayList<String>();
	ArrayList <String> RunID = new ArrayList<String>();
	ArrayList <String> Attribute = new ArrayList<String>();
	
	private static final String TAG = "Team2P1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team2_proj);
		etIP = (EditText) findViewById(R.id.etIP);
		etPort = (EditText) findViewById(R.id.etPort);
		tvData = (TextView) findViewById(R.id.tvData);
		DeviceAddr_List.add("00:12:09:13:99:42");
		DeviceAddr_List.add("00:15:FF:F2:10:0F");
		DeviceAddr_List.add("00:12:09:25:92:95");
		DeviceAddr_List.add("00:12:09:25:92:92");
		DeviceAddr_List.add("00:12:09:25:96:92");
		text = (TextView) findViewById(R.id.text);
		team_spinner = (Spinner) findViewById(R.id.bluetooth_address);
		adapter = ArrayAdapter.createFromResource(
				this, R.array.BlueToothAddress, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		team_spinner.setAdapter(adapter);
		
		team_spinner.setOnItemSelectedListener(new OnItemSelectedListenerImp());
	}

	
	
	private class OnItemSelectedListenerImp implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view,int position,long id){
			show = (TextView) findViewById(R.id.show);
			check++;
			String str = "The Blue Tooth Address you will connect is: ";
			if(check>1){
			String value = parent.getItemAtPosition(position).toString();
			show.setText(value);
			if (value.equals("Team1_Address"))
			{
				DEVICE_ADDRESS = DeviceAddr_List.get(0);
				show.setText(str+DEVICE_ADDRESS);
			}
			
			else if (value.equals("Team2_Address"))
			{
				DEVICE_ADDRESS = DeviceAddr_List.get(1);
				show.setText(str+DEVICE_ADDRESS);
			}
			
			else if (value.equals("Team3_Address"))
			{
				DEVICE_ADDRESS = DeviceAddr_List.get(2);
				show.setText(str+DEVICE_ADDRESS);
			}
			else if (value.equals("Team4_Address"))
			{
				DEVICE_ADDRESS = DeviceAddr_List.get(3);
				show.setText(str+DEVICE_ADDRESS);
			}
			else
			{
				DEVICE_ADDRESS = DeviceAddr_List.get(4);
				show.setText(str+DEVICE_ADDRESS);
			}
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	}
		
	
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
	}
		
	protected void onStart() {
		super.onStart();
		registerReceiver(arduinoReceiver, new IntentFilter(AmarinoIntent.ACTION_RECEIVED));

	}

	protected void onStop() {
		super.onStop();
		 unregisterReceiver(arduinoReceiver);
	}
	
	
	public void reset(View v){
		btnReset = (Button) findViewById(R.id.reset);
		latitude.clear();
		longitude.clear();
		time_date.clear();
		time_stamp.clear();
		sensorType.clear();
		sensorValue.clear();
		sensorID.clear();
		ClientID.clear();
		RunID.clear();
		Attribute.clear();
		show.setText("Reset the data!");
		tvData.setText("Points:"+latitude.size()+" Time: "+timestamp);
		
	}

	public void gps(View v) {
		startcheck = 0;
		
		btnGPS = (Button) findViewById(R.id.start);
		locationListener = new LocationListener(){
				@Override
				public void onStatusChanged(String provider,
						int status, Bundle extras) {
					Log.i(TAG, "onStatusChanged");

				}

				@Override
				public void onLocationChanged(Location location) {
					
						try {
							if (location != null) {
							
							if (startcheck<1)
							{
								Attribute.add("Start Point");
								SimpleDateFormat ts = new SimpleDateFormat("HH:mm:ss");
								runtime = ts.format(new Date());
								startcheck++;
								Log.i("TAG","Start Point");
							}
							
							else
							{
								Attribute.add("Point");
								Log.i("TAG","Data Point");
							}
							ClientID.add("Team2");
							lat = location.getLatitude();
							lng = location.getLongitude();
							sensorType.add(null);
							sensorID.add(null);
							sensorValue.add(null);
							latitude.add(String.valueOf(lat));
							longitude.add(String.valueOf(lng));
							SimpleDateFormat ts = new SimpleDateFormat("HH:mm:ss");
							timestamp = ts.format(new Date());
							SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
							date = dt.format(new Date());
							String temp = "Team2_" + date +" _ "+ runtime;  
							
							time_stamp.add(timestamp);
							time_date.add(date);
							RunID.add(temp);
							Log.i(TAG, String.valueOf(lat) + " " + String.valueOf(lng)+" "+v1+" "+v2+" "+v3);
							show.setText("Collecting GPS Data");
							tvData.setText("Points:"+latitude.size()+" Time: "+timestamp);
						}

						else {
							double lat = 1;
							double lng = 1;
							Log.i(TAG, String.valueOf(lat) + " " + String.valueOf(lng)+" "+v1+" "+v2+" "+v3);
						}
						}catch ( Exception e ){
							e.printStackTrace();
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
						LocationManager.GPS_PROVIDER, 3000, 0,
						locationListener);
				show.setText("Collecting GPS Data");
	}

	
	
	
	
	 public void receive(View v){
	 btnRec = (Button) findViewById(R.id.btn1);
	 
	 
	 receive_status = true;
	 
	 new AsyncTask<Void, Void, Void> (){
//	 protected void onPreExecute(){
//	 
//	 }
	 protected Void doInBackground(Void... params) {
	 char flag = 'A';
	 String message = "Hello";
	 try{
	 Amarino.sendDataToArduino(Team2Proj.this, DEVICE_ADDRESS, flag, message);
	 }catch (Exception e){
	 e.printStackTrace();
	 }
	 return null;
	 }
	
	 }.execute();
	 }

	

	public void end(View v) {
		btnEnd = (Button) findViewById(R.id.end);
		
		locationManager.removeUpdates(locationListener);
			ClientID.add("Team2");
			Attribute.add("End Point");
			sensorType.add(null);
			sensorID.add(null);
			sensorValue.add(null);
			latitude.add(String.valueOf(lat));
			longitude.add(String.valueOf(lng));
			SimpleDateFormat ts = new SimpleDateFormat("HH:mm:ss");
			timestamp = ts.format(new Date());
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
			date = dt.format(new Date());
			String temp = "Team2_" + date +" _ "+ runtime; 
			RunID.add(temp);
			time_stamp.add(timestamp);
			time_date.add(date);
			
			Log.i("TAG","End Point");
			tvData.setText("Points:"+latitude.size()+" Time: "+timestamp);
		show.setText("Stop Collecting GPS Information");
		
		
	}

	public class ArduinoReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String data = null;
			String result = null;

			// final String address =
			// intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);
			// Log.d("receive",address);
			final int dataType = intent.getIntExtra(
					AmarinoIntent.EXTRA_DATA_TYPE, -1);
			while (receive_status) {
				if (dataType == AmarinoIntent.STRING_EXTRA) {
					try {
						data = intent.getStringExtra(AmarinoIntent.EXTRA_DATA);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (data != null) {
						sensor_flag = true;
						// Log.d("data0",data);
						Gson gson = new Gson();
						//double lat, lng;
						JsonData obj = gson.fromJson(data, JsonData.class);
						v1 = obj.sensor_id;
						v2 = obj.sensor_type;
						v3 = obj.sensor_value;
//						v1="hi";
//						v2="hello";
//						v3= "haha";
						sensorID.add(v1);
						sensorType.add(v2);
						sensorValue.add(v3);
						
						ClientID.add("Team2");
						latitude.add(String.valueOf(lat));
						longitude.add(String.valueOf(lng));
						Attribute.add("Sensor");
						SimpleDateFormat ts = new SimpleDateFormat("HH:mm:ss");
						timestamp = ts.format(new Date());
						SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
						date = dt.format(new Date());
						String temp = "Team2_" + date +" _ "+ runtime; 
						RunID.add(temp);
						time_stamp.add(timestamp);
						time_date.add(date);
						Log.i("TAG","Sensor");
						Log.i(TAG, String.valueOf(lat) + " " + String.valueOf(lng)+" "+v1+" "+v2+" "+v3);
						
						tvData.setText("Points:"+latitude.size()+" Time: "+timestamp);
						receive_status = false;
						show.setText("Sensor Data Received!");
						
					}

				}
			}
		}
	}
	
	
//	ArrayList<Double> a = new ArrayList<Double>();
//	a.add(1.1);
//	a.add(2.1);
//	a.add(3.1);
//	System.out.println(a.get(1).getClass());
//	int size = a.size();
//	Double[] array = (Double[])a.toArray(new Double[size]);
//	for (int i=0 ; i<size;i++){
//	System.out.println(array[i]);}
//	
	public void send(View v){
		btnSend = (Button) findViewById(R.id.send);
		hostip = etIP.getText().toString();
		port = Integer.parseInt(etPort.getText().toString());
		//String[] lat_array = (String[])latitude.toArray(new String[size_lat]);
		//String[] lng_array = (String[])longitude.toArray(new String[size_lng]);
		//String[] sensorType_array = (String[])sensorType.toArray(new String[size_sensorType]);
		//String[] sensorID_array = (String[])sensorID.toArray(new String[size_sensorID]);
		//Double[] sensorValue_array = (Double[])sensorValue.toArray(new Double[size_sensorValue]);
		//String[] ts_array = (String[])time_stamp.toArray(new String[size_ts]);
		//String[] dt_array = (String[])time_date.toArray(new String[size_dt]);
		
		
		 new AsyncTask<Void, Void, Void> (){
			 protected Void doInBackground(Void... params) {
			    try {
			    	
			    	int size_lat = latitude.size();
					int size_lng = longitude.size();
					int size_sensorType = sensorType.size();
					int size_sensorID = sensorID.size();
					int size_sensorValue = sensorValue.size();
					int size_ts = time_stamp.size();
					int size_dt = time_date.size();
					int size_ClientID = ClientID.size();
					int size_RunID = RunID.size();
					int size_Attribute = Attribute.size();
					
			    	String[] lat_array = (String[])latitude.toArray(new String[size_lat]);
					String[] lng_array = (String[])longitude.toArray(new String[size_lng]);
					String[] sensorType_array = (String[])sensorType.toArray(new String[size_sensorType]);
					String[] sensorID_array = (String[])sensorID.toArray(new String[size_sensorID]);
					Double[] sensorValue_array = (Double[])sensorValue.toArray(new Double[size_sensorValue]);
					String[] ts_array = (String[])time_stamp.toArray(new String[size_ts]);
					String[] dt_array = (String[])time_date.toArray(new String[size_dt]);
					String[] ClientID_array = (String[])ClientID.toArray(new String[size_ClientID]);
					String[] RunID_array = (String[])RunID.toArray(new String[size_RunID]);
					String[] Attribute_array = (String[])Attribute.toArray(new String[size_Attribute]);
					
					Log.i(TAG,"Connecting");
					clientSocket = new Socket(hostip,port);
					os = new ObjectOutputStream(clientSocket.getOutputStream());
					Project1Packet Send = new Project1Packet(sensorValue_array, sensorID_array, sensorType_array, lat_array, lng_array, ts_array, dt_array, ClientID_array, RunID_array,Attribute_array);
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
				   
			    
			    
			    return null; }
			 
			 protected void onPostExecute(Void result) {
				 show.setText("DATA SENT!");
			 };
			 
		 }.execute();
			
		 tvData.setText("Points:"+latitude.size()+" Time: "+timestamp);

		
	}

	
	
	
	
	
	
	
	private class JsonData {
		public String getSensor_id() {
			return sensor_id;
		}

		public void setSensor_id(String sensor_id) {
			this.sensor_id = sensor_id;
		}

		public String getSensor_type() {
			return sensor_type;
		}

		public void setSensor_type(String sensor_type) {
			this.sensor_type = sensor_type;
		}

		public double getSensor_value() {
			return sensor_value;
		}

		public void setSensor_value(double sensor_value) {
			this.sensor_value = sensor_value;
		}

		

		String sensor_id;
		String sensor_type;
		double sensor_value;
		
		// public String toString(){
		// return "team:" + team + "\tsensor:"+ sensor +"\tvalue:"+ value;
		// }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team2_proj, menu);
		return true;
	}



	

	

}
