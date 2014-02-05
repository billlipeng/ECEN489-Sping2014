package com.example.android2;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zpartal.commpackets.ClientPacket;
import com.zpartal.commpackets.ServerPacket;

public class MainActivity extends Activity {

	final String IP_ADDRESS = "10.201.219.57";
	int guess,
		a,
		b,
		result;
	Socket connection;
	boolean flagTransmission;
	ObjectOutputStream output;
	ObjectInputStream input;
//	ClientPacket sendPacket = new ClientPacket("Joao",0,0);
//	ServerPacket receivePacket = new ServerPacket(null, 0);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button b1 = (Button) findViewById(R.id.button1);
		b1.setEnabled(false);
		Button b2 = (Button) findViewById(R.id.button_disconnect);
		b2.setEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void connect(View view) {
		if (isOnline())
		{
			EditText editText = (EditText) findViewById(R.id.IPAddress);
			String address = editText.getText().toString();
			ConnectServer serverConnection = new ConnectServer();
			serverConnection.execute(address);
			Button b1 = (Button) findViewById(R.id.button1);
			b1.setEnabled(true);
			Button b2 = (Button) findViewById(R.id.button_disconnect);
			b2.setEnabled(true);
			Button b3 = (Button) findViewById(R.id.button_connect);
			b3.setEnabled(false);
			newQuestion();
			new SendMessage().execute();
		}
    }
	
	public void send(View view) {
		EditText editText = (EditText) findViewById(R.id.edit_number);
		guess = Integer.parseInt(editText.getText().toString());
		flagTransmission = true;
		new SendMessage().execute();
		while (flagTransmission);
		TextView editText1 = (TextView) findViewById(R.id.Result);
		if (result == guess)//receivePacket.getResult() == guess)
			editText1.setText("Correct Answer!");
		else
			editText1.setText("Wrong Answer!");
		newQuestion();	
	}
	
	public void disconnect(View view) {
		//Setting buttons allowances
		new CloseConnection().execute();
		Button b1 = (Button) findViewById(R.id.button1);
		b1.setEnabled(false);
		Button b2 = (Button) findViewById(R.id.button_disconnect);
		b2.setEnabled(false);
		Button b3 = (Button) findViewById(R.id.button_connect);
		b3.setEnabled(true);

    }
	
	@SuppressLint("DefaultLocale")
	public void newQuestion() {
		Random r = new Random();
/*		sendPacket.setNum1(0 + r.nextInt(11));
		sendPacket.setNum2(0 + r.nextInt(11));*/
		a = 0 + r.nextInt(11);
		b = 0 + r.nextInt(11);
        TextView editText = (TextView) findViewById(R.id.Question);
        String question = String.format("%d + %d = ?", a,b);//sendPacket.getNum1(),sendPacket.getNum2());
        editText.setText(question);
		}
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}

	private class ConnectServer extends AsyncTask<String, Void, Void>{
	
		@Override
		protected Void doInBackground (String ... params){

			try
			{
				// Creating connection
				InetAddress address = InetAddress.getByName( params[0] );
				connection = new Socket( address , 5555);
				Log.d(params[0], "Socket Created");
				// Creating stream Objects
				output =	new ObjectOutputStream(connection.getOutputStream());
				output.flush();
				input =  new ObjectInputStream(connection.getInputStream());
			}
			catch( IOException ioException )
			{
				ioException.printStackTrace();
			}
			return null;
		}
	}
	
	
	private class SendMessage extends AsyncTask<Void, Void, Void>{
		
		@SuppressLint("DefaultLocale")
		@Override
		protected Void doInBackground (Void ... params){

			try
			{
				ClientPacket sendPacket = new ClientPacket("Joao",a,b);
				String message = String.format("Num1 = %d, Num2 = %d", sendPacket.getNum1(), sendPacket.getNum2());
				Log.d(IP_ADDRESS, message);
				output.flush();
				output.writeObject(sendPacket);
				output.flush();
				
				try
				{
					ServerPacket receivePacket = (ServerPacket) input.readObject();
					String message1 = String.format("Result = %d", receivePacket.getResult());
					Log.d(IP_ADDRESS, message1);
					result = receivePacket.getResult();
					flagTransmission = false;
				}
				catch (ClassNotFoundException e)
				{
				}
			}
			catch( IOException ioException )
			{
				ioException.printStackTrace();
			}
			return null;
		}
	}
	
	private class CloseConnection extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected Void doInBackground (Void ... params){
			// Closing connection
			try
			{
				input.close();
				output.close();
				connection.close();
			}
	    	catch( IOException ioException )
	    	{
	    		ioException.printStackTrace();
	    	} 
			return null;
		}
	}
}