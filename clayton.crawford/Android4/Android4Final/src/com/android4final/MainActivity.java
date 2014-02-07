package com.android4final;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button button1;
	private Button button2;
	private TextView textView1;
	private EditText editText1;

	private BluetoothAdapter BTadapter;
	private BluetoothDevice BTdevice;
	private BluetoothSocket BTsocket;

	private DataInputStream in;
	private DataOutputStream out;
	private String send = "";
	private String echo = "";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		textView1 = (TextView) findViewById(R.id.textView1);
		editText1 = (EditText) findViewById(R.id.editText1);
		
		textView1.setTextColor(Color.parseColor("#FFFFFF"));
		textView1.setMovementMethod(new ScrollingMovementMethod());
		editText1.setTextColor(Color.parseColor("#FFFFFF"));
		editText1.setBackgroundColor(Color.parseColor("#888888"));

		BTadapter = BluetoothAdapter.getDefaultAdapter();
		BTadapter.startDiscovery();
		while (true) {
			if (BTadapter.isDiscovering()) {
				BTadapter.cancelDiscovery();
				break;
			}
		}

		BTdevice = BTadapter.getRemoteDevice("60:36:DD:82:C0:4D");
		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

		try {
			BTsocket = BTdevice.createRfcommSocketToServiceRecord(uuid);
			BTsocket.connect();
			in = new DataInputStream(BTsocket.getInputStream());
			out = new DataOutputStream(BTsocket.getOutputStream());
		} catch (IOException e){
			textView1.append(e.getMessage() + "\n");
		}
		button1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editText1.getWindowToken(), 0);
				send = editText1.getText().toString();
				editText1.getText().clear();
				try {
					out.writeUTF(send);
					echo = in.readUTF();
				} catch (IOException e) {
					textView1.append(e.getMessage() + "\n");
				}
				textView1.append(echo + "\n");
			}
		});
		
		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					BTsocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					textView1.append(e.getMessage() + "\n");
				}
				finish();
				System.exit(0);
			}
		});
	}
}
