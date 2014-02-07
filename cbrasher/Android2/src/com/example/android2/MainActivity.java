package com.example.android2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import com.zpartal.commpackets.*;

import android.net.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {


	TextView message;
	EditText sum;
	Button check;
	Button send;
	int a;
	int b;
	int total;
	int x;
	int c = 15; //test input for message output
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sum  = (EditText) findViewById(R.id.sum);
		
		Random random = new Random();
		a = random.nextInt(9);
		b = random.nextInt(9);
		
		EditText int1 = (EditText) findViewById(R.id.num1);
		int1.setText(Integer.toString(a));
		EditText int2 = (EditText) findViewById(R.id.num2);
		int2.setText(Integer.toString(b));
		
		check = (Button)findViewById(R.id.check);
		send = (Button)findViewById(R.id.send);
		
		//check.setOnClickListener(new ClickButton());
		//send.setOnClickListener(new ClickButton1());
		
	}

	public void connect(View view)
	{
		EditText address = (EditText) findViewById(R.id.ipadr);
		String ipaddress = address.getText().toString();
		new talkToServer().execute(ipaddress);
	}
	
	
	 private class talkToServer extends AsyncTask<String,Void,Integer>
	{
		@SuppressWarnings("unused")
		protected void onPreExectue(){
			
		}
		
		protected Integer doInBackground(String... ipaddress) {
			Socket connection = null;

			try {
				connection = new Socket(ipaddress[0], 6123);	//192.168.0.100
				if (connection.isConnected())
					System.out.print("Connection established \n");
				
				ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
				ObjectInputStream input = new ObjectInputStream(connection.getInputStream());	
				
				ClientPacket cp = new ClientPacket("cp", a, b);
				
				cp.setNum1(a);
				cp.setNum2(b);
				output.writeObject(cp);
				//output.write(byte_array);
				output.writeInt(a);
				output.writeInt(b);
				
				ServerPacket sp = (ServerPacket) input.readObject();
				
				total = sp.getResult();
				
				
				output.close();
				input.close();
				
				connection.close();
				
			} 
			catch (UnknownHostException e ) 
			{
				e.printStackTrace();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			} catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			return total;
		 }
		
		
		 @Override
	        protected void onPostExecute(Integer result) {

			 message = (TextView) findViewById(R.id.message);
			x = Integer.parseInt(sum.getText().toString());
							
			if	(x == total){
			message.setText("The Force is Strong with this One");
				        	}
			else{
			message.setText("All your Bases Belong to Me");
				}
			 
					}
	}
}
