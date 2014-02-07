package com.zpartal.androidclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zpartal.commpackets.*;

public class MainActivity extends Activity {
	
	private String TAG = "AndroidClient";
	
	private Button bConnect;
	private Button bRequest;
	private EditText etClientID;
	private EditText etIP;
	private EditText etPort;
	private EditText etInt1;
	private EditText etInt2;
	private TextView tvResponse;	
	
	static private Socket socket; 
	
	private int SERVER_PORT = 5555;
	private String SERVER_IP = "192.168.1.105";
	private String CLIENT_ID = "client";
	
	private ObjectInputStream ois;
	private ObjectOutputStream oos;	
	
	private int int1 = 0;
	private int int2 = 1;
	private int result = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		configureInitialUI();		
	}
	
	public void connectClient(View view) {
		configureConnectUI();	
		if (etClientID.getText().toString().length() > 0) {
			CLIENT_ID = etClientID.getText().toString();
		}
		if (etPort.getText().toString().length() > 0) {
			SERVER_PORT = Integer.parseInt(etPort.getText().toString());
		}
		if (etIP.getText().toString().length() > 0) {
			SERVER_IP = etIP.getText().toString();
		}		
		
//		Toast.makeText(getApplicationContext(),"Your message.", Toast.LENGTH_SHORT).show();
		Log.d(TAG,"ID: " + CLIENT_ID + ", IP: " + SERVER_IP + ", PORT: " + SERVER_PORT);
		
		AsyncCreateSocket createSocket = new AsyncCreateSocket();
		createSocket.execute();
	}
	
	public void sendRequest(View view) {
		int1 = Integer.parseInt(etInt1.getText().toString());
		int2 = Integer.parseInt(etInt2.getText().toString());
		AsyncSendRequest sendReq = new AsyncSendRequest();
		sendReq.execute();
		
		// Hide keyboard on click
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etInt2.getWindowToken(), 0);
	}
	
	// Async task called on each request
	private class AsyncSendRequest extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... arg0) {
			ClientPacket cp = new ClientPacket(CLIENT_ID, int1,int2);
			try {
				oos.writeObject((ClientPacket) cp);
				ServerPacket response = (ServerPacket) ois.readObject();
				Log.d(TAG, response.getServerID());
				result = response.getResult();					
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}		
			return null;
		}		
		@Override
	    protected void onPostExecute(String res) {
			tvResponse.setText("Response: " + String.valueOf(result));
		}
	}
	
	// Async task will create socket and input/output object streams
	private class AsyncCreateSocket extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... arg0) {
			InetAddress serverAddr;
			try {
				serverAddr = InetAddress.getByName(SERVER_IP);
				socket = new Socket(serverAddr, SERVER_PORT);
				Log.d(TAG, "Socket Created");
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			return null;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void configureInitialUI() {
		bConnect = (Button) findViewById(R.id.ButtonConnect);
		bRequest = (Button) findViewById(R.id.buttonSend);
		etClientID = (EditText) findViewById(R.id.ETClientID);
		etIP = (EditText) findViewById(R.id.ETIP);
		etPort = (EditText) findViewById(R.id.ETPort);
		etInt1 = (EditText) findViewById(R.id.editTextInt1);
		etInt2 = (EditText) findViewById(R.id.editTextIn2);	
		tvResponse = (TextView) findViewById(R.id.TVResponse);
	}
	
	private void configureConnectUI() {
		bConnect.setEnabled(false);
	    etClientID.setEnabled(false);
	    etIP.setEnabled(false);
	    etPort.setEnabled(false);	
	    
	    bRequest.setEnabled(true);
		etInt1.setEnabled(true);
		etInt2.setEnabled(true);	
	}
}
