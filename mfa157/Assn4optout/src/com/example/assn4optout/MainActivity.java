package com.example.assn4optout;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;

public class MainActivity extends Activity {

	BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String status = null;
		if (bluetooth.isEnabled()) {
		    String mydeviceaddress = bluetooth.getAddress();
		    String mydevicename = bluetooth.getName();
		    int state = bluetooth.getState();
		    status = mydevicename + " : " + mydeviceaddress + " : " + state;
		}
		else
		{
			status = "Bluetooth is not Enabled";
		}
		Toast.makeText(this, status, Toast.LENGTH_LONG).show();
		
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
