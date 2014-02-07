package com.example.assn4optout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothActivity extends Activity {

	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	BluetoothDevice mBluetoothDevice;
	BluetoothSocket mSocket;
    DataInputStream mInStream;
    DataOutputStream mOutStream;

    
	int REQUEST_ENABLE_BT = 1;
	UUID MY_UUID = UUID.fromString("b0d042fe-8a3c-11e3-b91b-d231feb1dc81");
	String MAC="00:19:15:66:9E:A2";
	//String MAC="C4:85:08:46:A0:4F";
	
	String output="";
	String input="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_activity);
	//	Intent i = getIntent();
	//	MAC = i.getStringExtra("MAC");
		
		Button bSend= (Button) findViewById(R.id.bSend);	
		TextView tvMac = (TextView) findViewById(R.id.tvMac);
		
		
		
		 tvMac.setText("Mac: "+MAC);
		 
		mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(MAC);
		System.out.println("MAC is "+MAC);
		System.out.println("Creating Connection");

		
	 


	 
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	        	System.out.println("Creating connection");
	            // MY_UUID is the app's UUID string, also used by the server code
	            mSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
	            mSocket.connect();
	            System.out.println("Done!");
	        } catch (IOException e) { }

	    

	        // Cancel discovery because it will slow down the connection
	        mBluetoothAdapter.cancelDiscovery();
	     
	        DataInputStream tmpIn = null;
	        DataOutputStream tmpOut = null;
	 
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            tmpIn = new DataInputStream(mSocket.getInputStream());
	            tmpOut = new DataOutputStream(mSocket.getOutputStream());
	        } catch (IOException e) { }
	 
	        mInStream = tmpIn;
	        mOutStream = tmpOut;
		
		bSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TextView tvEcho = (TextView) findViewById(R.id.tvEcho);
				EditText etInput = (EditText) findViewById(R.id.etInput);
				 // Keep listening to the InputStream until an exception occurs
		            try {
		            	output = etInput.getText().toString();
		            	mOutStream.writeUTF(output);
						input = mInStream.readUTF();
						tvEcho.setText(input);
						etInput.setText("");

		            } catch (IOException e) {
		                
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



	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	/*
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		try {
			mSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	

	

		
}

