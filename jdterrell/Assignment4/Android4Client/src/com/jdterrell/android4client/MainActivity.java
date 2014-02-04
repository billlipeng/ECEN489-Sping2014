package com.jdterrell.android4client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.*;
//import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
//	private static final String TAG = "AndroidClient";
	private BluetoothAdapter mBtAdapter;
	public BluetoothDevice mDevice;
	private BluetoothSocket mSock;
	private DataInputStream in;
	private DataOutputStream out;
	private Button button;
	private TextView echoText;
	private EditText textSend;	
	private String message = "Hello";
	private String reply = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button = (Button) findViewById(R.id.button_send);
		echoText = (TextView) findViewById(R.id.text_echo);
		textSend = (EditText) findViewById(R.id.text_send);
		connectBluetooth();
	}

	private void connectBluetooth() {
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		mBtAdapter.startDiscovery();
		while(true){
			if (mBtAdapter.isDiscovering() == true) {
                mBtAdapter.cancelDiscovery();
                break;
            }
		}
		mDevice = mBtAdapter.getRemoteDevice("00:02:72:B3:1A:06");
		UUID uuid = UUID.fromString("b0d042fe-8a3c-11e3-b91b-d231feb1dc81");
		try {
			mSock = mDevice.createRfcommSocketToServiceRecord(uuid);
			mSock.connect();
			in = new DataInputStream(mSock.getInputStream());
			out = new DataOutputStream(mSock.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				message = textSend.getText().toString();
				try {
					out.writeUTF(message);
					reply = in.readUTF();
				} catch (IOException e) {
					e.printStackTrace();
				}
				echoText.setText(reply);				
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
