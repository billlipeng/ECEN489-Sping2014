package com.mfa157.assn3optout;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import 	android.net.wifi.WifiManager;
import android.database.sqlite.*;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Public MainActivity(Context context){
			MySQLiteHelper dbhelper = new MySQLiteHelper(context);
			SQLiteDatabase db=dbhelper.getWritableDatabase();
		}
		 //final String TableName="RSSI readings";
		 //final String DATABASE_NAME = "Assignment3optout.db";
		 //final String DATABASE_CREATE = "create table "+TableName +"( ID integer primary key autoincrement, RSSI text not null);";
		Button bStart = (Button)findViewById(R.id.bStart);
		
		
		
		bStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				TextView tvCurrentData = (TextView)findViewById(R.id.tvCurrentData);
				ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo Info = cm.getActiveNetworkInfo();
				
				 if (Info == null || !Info.isConnectedOrConnecting()) {
				        tvCurrentData.setText("WIFI CONNECTION: No connection");
				    } else {
				        int netType = Info.getType();
				        int netSubtype = Info.getSubtype();

				        if (netType == ConnectivityManager.TYPE_WIFI) {
				        	WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
				            int rssi = wifiManager.getConnectionInfo().getRssi();
				            tvCurrentData.setText("Current RSSI: "+rssi);

				        } 
				    }
				
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
