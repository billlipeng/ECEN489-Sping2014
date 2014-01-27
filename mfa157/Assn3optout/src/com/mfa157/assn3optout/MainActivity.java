package com.mfa157.assn3optout;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import 	android.net.wifi.WifiManager;
import android.database.Cursor;
import android.database.sqlite.*;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	
	    final MySQLiteHelper dbhelper = new MySQLiteHelper(getApplicationContext());
		final SQLiteDatabase db=dbhelper.getWritableDatabase();
	
		 //final String TableName="RSSI readings";
		 //final String DATABASE_NAME = "Assignment3optout.db";
		 //final String DATABASE_CREATE = "create table "+TableName +"( ID integer primary key autoincrement, RSSI text not null);";
		Button bStart = (Button)findViewById(R.id.bStart);
		Button bData = (Button)findViewById(R.id.bData);
		Button bClear = (Button)findViewById(R.id.bClear);
		
		
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
				            Cursor c = db.rawQuery("SELECT MAX(ID) FROM RSSI_data", null);
				            int id= (c.moveToFirst() ? c.getInt(0) : 0)+1;
				            db.execSQL("INSERT INTO RSSI_data (ID, RSSI) VALUES ("+id+","+rssi+");");

				        } 
				    }
				
			}
		});
		
		bData.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent dataView = new Intent(getApplicationContext(), DatabaseView.class);
				startActivity(dataView);				
			}
		});
			
		bClear.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db.execSQL("DELETE FROM RSSI_data;");
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
