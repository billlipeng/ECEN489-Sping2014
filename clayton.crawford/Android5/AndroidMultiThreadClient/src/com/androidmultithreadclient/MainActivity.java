package com.androidmultithreadclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zpartal.commpackets.ClientPacket;
import com.zpartal.commpackets.ServerPacket;

public class MainActivity extends Activity {
	
	private Button connectButton; // submit button
	private Button addButton; // send button
	private TextView outputText; // output text view
	private EditText ipBox; // ip
	private EditText portBox; // port
	private EditText num1Box; // num1
	private EditText num2Box; // num1
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private String IP;
	private int Port;
	private int num1;
	private int num2;
	private int sum;
	private String errors = "";
	
	static private Socket socket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupUI();
		
		connectButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				IP = ipBox.getText().toString().trim();
				Port = Integer.parseInt(portBox.getText().toString());
				outputText.append("Connect Button Pressed!\n");
				outputText.append("IP: " + IP + " | Port: " + Port + "\n" );
				ipBox.getText().clear();
				portBox.getText().clear();
				
				ipBox.setEnabled(false);
				portBox.setEnabled(false);
				connectButton.setEnabled(false);
				
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(ipBox.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(portBox.getWindowToken(), 0);
				
				AsyncCreateConnection createConnection = new AsyncCreateConnection();
				createConnection.execute();
			}
		});
		
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				num1 = Integer.parseInt(num1Box.getText().toString());
				num2 = Integer.parseInt(num2Box.getText().toString());
				outputText.append("Add Button Pressed!\n");
				outputText.append("Num1: " + num1 + " | Num2: " + num2 + "\n" );
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(num1Box.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(num2Box.getWindowToken(), 0);
				num1Box.getText().clear();
				num2Box.getText().clear();
				AsyncSendMessage sendMessage = new AsyncSendMessage();
				sendMessage.execute();
			}
		});
	}

	private void setupUI() {
		connectButton = (Button) findViewById(R.id.connectButton);
		addButton = (Button) findViewById(R.id.addButton);
		outputText = (TextView) findViewById(R.id.outputText);
		num1Box = (EditText) findViewById(R.id.num1Box);
		num2Box = (EditText) findViewById(R.id.num2Box);
		ipBox = (EditText) findViewById(R.id.ipBox);
		portBox = (EditText) findViewById(R.id.portBox);
		
		outputText.setTextColor(Color.parseColor("#FFFFFF"));
		outputText.setMovementMethod(new ScrollingMovementMethod());
		ipBox.setHintTextColor(Color.parseColor("#FFFFFF"));
		portBox.setHintTextColor(Color.parseColor("#FFFFFF"));
		num1Box.setHintTextColor(Color.parseColor("#FFFFFF"));
		num2Box.setHintTextColor(Color.parseColor("#FFFFFF"));
		ipBox.setTextColor(Color.parseColor("#FFFFFF"));
		portBox.setTextColor(Color.parseColor("#FFFFFF"));
		num1Box.setTextColor(Color.parseColor("#FFFFFF"));
		num2Box.setTextColor(Color.parseColor("#FFFFFF"));
	}
	
	public class AsyncCreateConnection extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... arg0) {
			try {
				socket = new Socket(InetAddress.getByName(IP), Port);
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				in = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				errors = e.getMessage();
			}
			return null;
		}
		
		protected void onPreExecute(Void result) {
			outputText.append("Establishing Connection to Server...\n");
		}
		
		protected void onPostExecute(Void result) {
			if(socket.isBound())
				outputText.append("Connected to" + socket.getInetAddress().toString());
			outputText.append(errors + "\n");
			errors = "";
		}
	}
	
	public class AsyncSendMessage extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... arg0) {
			try {
				ClientPacket clientPacket = new ClientPacket("clayton.crawford", num1, num2);
				out.writeObject((ClientPacket) clientPacket);
				out.flush();
				ServerPacket echo = (ServerPacket) in.readObject();
				sum = echo.getResult();
			} catch (IOException e) {
				errors = e.getMessage() + "\n";
			} catch (ClassNotFoundException e) {
				errors = errors + e.getMessage();
			}
			return null;
		}
		
		protected void onPostExecute(Void result) {
			outputText.append("Sum: " + String.valueOf(sum));
			outputText.append(errors + "\n");
			errors = "";
		}
	}

}
