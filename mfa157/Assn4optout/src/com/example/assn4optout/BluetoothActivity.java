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
	ConnectThread btconnection;
	ConnectedThread btcom;
	int REQUEST_ENABLE_BT = 1;
	UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	String MAC="C4:85:08:46:A0:4F";
	
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
		TextView tvEcho = (TextView) findViewById(R.id.tvEcho);
		EditText etInput = (EditText) findViewById(R.id.etInput);
		
		 tvMac.setText("Mac: "+MAC);
		 
		mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(MAC);
		System.out.println("MAC is "+MAC);
		System.out.println("Creating Connection");
		ConnectThread btconnection = new ConnectThread(mBluetoothDevice);
		//btconnection.cancel();
		btconnection.run();
		
		
		
		bSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				btcom.run();
			}
		});
	
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
		private class ConnectThread extends Thread {
		    private final BluetoothSocket mmSocket;
		    private final BluetoothDevice mmDevice;
		 
		    public ConnectThread(BluetoothDevice device) {
		        // Use a temporary object that is later assigned to mmSocket,
		        // because mmSocket is final
		        BluetoothSocket tmp = null;
		        mmDevice = device;
		 
		        // Get a BluetoothSocket to connect with the given BluetoothDevice
		        try {
		        	System.out.println("Creating connection");
		            // MY_UUID is the app's UUID string, also used by the server code
		            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
		            tmp.connect();
		            System.out.println("Done!");
		        } catch (IOException e) { }
		        mmSocket = tmp;
		    }
		 
		    public void run() {
		        // Cancel discovery because it will slow down the connection
		        mBluetoothAdapter.cancelDiscovery();
		 
		        try {
		            // Connect the device through the socket. This will block
		            // until it succeeds or throws an exception
		    		System.out.println("Connecting");
		            mmSocket.connect();
		    		System.out.println("Connected!");
		    		btcom = new ConnectedThread(mmSocket);

		        } catch (IOException connectException) {
		            // Unable to connect; close the socket and get out
		        	System.out.println("Failed!");
		        	System.out.println(connectException.getMessage());
		            try {
		                mmSocket.close();
		                System.out.println("Socket closed");
		            } catch (IOException closeException) { }
		            return;
		        }
		 
		        // Do work to manage the connection (in a separate thread)
		        //manageConnectedSocket(mmSocket);
		    }
		 
		    // Will cancel an in-progress connection, and close the socket 
		    public void cancel() {
		        try {
		        	System.out.println("Closing connection");
		            mmSocket.close();
		        } catch (IOException e) { }
		    }
		}
		
		private class ConnectedThread extends Thread {
		    private final BluetoothSocket mmSocket;
		    private final DataInputStream mmInStream;
		    private final DataOutputStream mmOutStream;
		 
		    public ConnectedThread(BluetoothSocket socket) {
		        mmSocket = socket;
		        DataInputStream tmpIn = null;
		        DataOutputStream tmpOut = null;
		 
		        // Get the input and output streams, using temp objects because
		        // member streams are final
		        try {
		            tmpIn = new DataInputStream(mmSocket.getInputStream());
		            tmpOut = new DataOutputStream(mmSocket.getOutputStream());
		        } catch (IOException e) { }
		 
		        mmInStream = tmpIn;
		        mmOutStream = tmpOut;
		    }
		 
		    public void run() {
		        int bytes; // bytes returned from read()
		 
		        // Keep listening to the InputStream until an exception occurs
		        while (true) {
		            try {
		            	mmOutStream.writeUTF(output);
						input = mmInStream.readUTF();

		            } catch (IOException e) {
		                break;
		            }
		        }
		    }
		 
		    // Call this from the main activity to send data to the remote device 
		    public void write(byte[] bytes) {
		        try {
		            mmOutStream.write(bytes);
		        } catch (IOException e) { }
		    }
		 
		    // Call this from the main activity to shutdown the connection 
		    public void cancel() {
		        try {
		            mmSocket.close();
		        } catch (IOException e) { }
		    }
		}
		
}
