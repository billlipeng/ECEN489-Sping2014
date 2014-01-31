package com.example.assn4optout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

public class MainActivity extends Activity {

	//BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	//int REQUEST_ENABLE_BT = 1;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final ListView deviceList= (ListView) findViewById(R.id.deviceList);	
		
		/*
		ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		deviceList.setAdapter(mArrayAdapter);
		
		System.out.println("Check BT enabled");
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		
		String status = null;
		if (mBluetoothAdapter.isEnabled()) {
			System.out.println("Getting BT data");
		    String mydeviceaddress = mBluetoothAdapter.getAddress();
		    String mydevicename = mBluetoothAdapter.getName();
		    int state = mBluetoothAdapter.getState();
		    status = mydevicename + " : " + mydeviceaddress + " : " + state;
		}
		else
		{
			status = "Bluetooth is not Enabled";
		}
		Toast.makeText(this, status, Toast.LENGTH_LONG).show();
		
		*/
		
		//Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		//if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		   // for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		    	//System.out.println("Connecting to BT");
				Intent i = new Intent(getApplicationContext(), BluetoothActivity.class);
				//i.putExtra("MAC", device.getAddress());
				startActivity(i);
		    //	mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
		    //	break;
		  // }
		//}
		
		
		
	
		}

	

}
