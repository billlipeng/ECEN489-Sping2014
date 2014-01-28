package com.jdterrell.wifiinfo;

import java.net.InetAddress;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

public class WifiMonitor extends Activity {

	TextView textConnected, textIPAddress, textSSID, textMAC, textSpeed, textRSSI;
	 
	   /** Called when the activity is first created. */
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_wifi_monitor);

	       textConnected = (TextView)findViewById(R.id.status);
	       textIPAddress = (TextView)findViewById(R.id.ip);  
	       textSSID = (TextView)findViewById(R.id.ssid);
	       textMAC = (TextView)findViewById(R.id.mac);
	       textSpeed = (TextView)findViewById(R.id.speed);
	       textRSSI = (TextView)findViewById(R.id.rssi);

	       DisplayWifiState();
	      
	       this.registerReceiver(this.myWifiReceiver,
	         new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

	   }
	  
	   private BroadcastReceiver myWifiReceiver
	   = new BroadcastReceiver(){

	  @Override
	  public void onReceive(Context arg0, Intent arg1) {

		   @SuppressWarnings("deprecation")
		NetworkInfo networkInfo = (NetworkInfo) arg1.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		   if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
			   DisplayWifiState();
	   }
	  }};
	  
	   private void DisplayWifiState(){
	    
		    ConnectivityManager myConnManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		    NetworkInfo myNetworkInfo = myConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		    WifiManager myWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		    WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
	  
		    textMAC.setText(myWifiInfo.getMacAddress());
	  
		    if (myNetworkInfo.isConnected()){
			      
			     textConnected.setText(" CONNECTED");	//make this green
			     textConnected.setTextColor(Color.GREEN);			      
			     
			     int ip = myWifiInfo.getIpAddress();
			      
			     String ipString = String.format("%d.%d.%d.%d",
			    		 (ip & 0xff),
			    		 (ip >> 8 & 0xff),
			    		 (ip >> 16 & 0xff),
			    		 (ip >> 24 & 0xff));
			     
			     textIPAddress.setText(ipString);
			      
			     textSSID.setText(" " + myWifiInfo.getSSID());
			     textSpeed.setText(" " + String.valueOf(myWifiInfo.getLinkSpeed()) + " " + WifiInfo.LINK_SPEED_UNITS);
			     textRSSI.setText(" " + String.valueOf(myWifiInfo.getRssi()));
		    }
		    else{
			     textConnected.setText(" DISCONNECTED");	//make this red
			     textConnected.setTextColor(Color.RED);
			     textIPAddress.setText("---");
			     textSSID.setText("---");
			     textSpeed.setText("---");
			     textRSSI.setText("---");
		    }
	    
	   	}
	}
