package com.example.basicbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BasicBT extends Activity {
	Handler h;
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_bt);
		
		// this needed to be added to utilize the phone's BT capabilities
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		
		// our button sends stuff
		final Button button = (Button) findViewById(R.id.btnBT);
		final EditText txtInput = (EditText) this.findViewById(R.id.txtInput);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	 
        		 String BTsend = txtInput.getText().toString();
        		 write(BTsend);
            }
        });
		
        
    	h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                case INC:                                                   // if receive massage
                	
                	byte[] readBuf = (byte[]) msg.obj;
                    String strIncom = new String(readBuf, 0, msg.arg1);                 // create string from bytes array
                    sb.append(strIncom);                                                // append string
                    String inc = sb.toString();
                    txtReceive.setText(inc);
                    sb.setLength(0);
                    break;
                }
            }
        };
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.basic_bt, menu);
		return true;
	}

	// -=-=-=-=-=-=-=-=-=-=-=-=-=-
	// Lets Start Doing Stuff Here
	
	
	// Define bt components
	private BluetoothAdapter btAdapter = null;
	private BluetoothSocket btSocket = null;
	private InputStream BTinStream = null;
    private OutputStream BToutStream = null;
	// SPP UUID service 
    //00001101-0000-1000-8000-00805F9B34FB
	private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	  
	// MAC-address of Bluetooth module
	private static String address = "5C:F3:70:02:63:9B";
	
	// attempt to create connections in onResume
    public void onResume(){
    	super.onResume();
    	
    	BluetoothDevice device = btAdapter.getRemoteDevice(address);
    	try {
    		btSocket = device.createRfcommSocketToServiceRecord(uuid);
    	} catch (IOException e) {Log.e("bluetooth", "Could not create Insecure RFComm Connection",e);}
    	
    	//before connecting be sure to turn off discovery
    	btAdapter.cancelDiscovery();
    	//connect
    	try {
    	      btSocket.connect();
    	    } catch (IOException e) {
    	      try {
    	        btSocket.close();
    	      } catch (IOException e2) {Log.e("bluetooth", "Could not connect",e2);}
    	    }
    	//create data stream
    	try {
    	      BToutStream = btSocket.getOutputStream();
    	      BTinStream = btSocket.getInputStream();
    	    } catch (IOException e) {Log.e("bluetooth", "Could not get Ostream",e);}
    }
	
	// -=-=-=-=-=-=-=-=-=-=-=-=-=-
	// Send/Receive

    
	public void write(String message){
		
		byte[] msgBuffer = message.getBytes();
        try {
            BToutStream.write(msgBuffer);
        } catch (IOException e) { Log.e("bluetooth", "Error data send",e); }
		
	}
	

	


	final int INC = 1;

	public void tryReceive(View v){
		byte[] buffer = new byte[256];  // buffer store for the stream
        int bytes; // bytes returned from read()
		while(true){
			try {
                // Read from the InputStream
                bytes = BTinStream.read(buffer);        // Get number of bytes and message in "buffer"
                h.obtainMessage(INC, bytes, -1, buffer).sendToTarget();     // Send to message queue Handler
            } catch (IOException e) { 
                break;
            }
		}
	}
	
	private StringBuilder sb = new StringBuilder();
	final EditText txtReceive = (EditText) this.findViewById(R.id.txtReceive);
	
}
	
	

