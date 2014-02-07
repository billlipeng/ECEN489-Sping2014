package org.toming90.socketandroid;

import java.io.*;
import java.net.*;
import java.util.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.*;

import com.zpartal.commpackets.*;


public class MainActivity extends Activity {
	ObjectOutputStream os;
	ObjectInputStream is;
	int port = 5555;
	Socket clientSocket;
	int num1;
	int num2;
	int MyAnswer;
	String hostIP;
	 EditText  IP;    
	 EditText Num1;
	 EditText Num2;
	 EditText Guess;
	 TextView text;
	 Button btCon;
	 Button btDiscon;
	 Button btSend;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		
	}
	
	public void connect(View v){
		btCon = (Button) findViewById(R.id.connectbutton);
		btSend = (Button) findViewById(R.id.sendbutton);
		btDiscon = (Button) findViewById(R.id.discon);
		btCon.setEnabled(true);
		btSend.setEnabled(false);
		btDiscon.setEnabled(false);
		IP = (EditText) findViewById(R.id.ip); 
	    hostIP = IP.getText().toString();
	    
	    new AsyncTask<Void, Void, String> (){
	    protected String doInBackground(Void... params) {
	    	String showtext;
	    	try
			{	
		    	clientSocket = new Socket(hostIP,port);
		    	os = new ObjectOutputStream(clientSocket.getOutputStream());
		    	is = new ObjectInputStream(clientSocket.getInputStream());
		    }
			catch (UnknownHostException e)
			{
		    	e.printStackTrace();
		    	//text.setText("Connection Failed");
		    }
			catch (IOException e)
			{
		    	e.printStackTrace();
		    	//text.setText("Connection Failed");
		    }
		    
		    showtext = "Connection Established!";		
		    		return showtext;
	    }
	    
	    protected void onPostExecute(String showtext) {
	    	text = (TextView) findViewById(R.id.display);
	    	text.setText("Connection Established!");
	    	btCon.setEnabled(false);
			btSend.setEnabled(true);
			btDiscon.setEnabled(true);
	    }
	    
	    }.execute();
	 
	}
	
	

	
	
	public void Close(View v){
		btCon = (Button) findViewById(R.id.connectbutton);
		btSend = (Button) findViewById(R.id.sendbutton);
		btDiscon = (Button) findViewById(R.id.discon);
		
		 new AsyncTask<Void, Void, Void> (){
			    protected Void doInBackground(Void... params) {
			    	try
					{
						
						is.close();
						os.close();
						clientSocket.close();
						
					}
					catch (Exception e)
					{
						e.getStackTrace();
							
					}
			    	return null;
			    }
		 
			    protected void onPostExecute(Void a) {
			    	text.setText("Connection Closed!");
			    	btCon.setEnabled(true);
					btSend.setEnabled(false);
					btDiscon.setEnabled(false);
			    }
	   		 }.execute();
	}
	
	
	
	public void MessageProcess(View v){
		btCon = (Button) findViewById(R.id.connectbutton);
		btSend = (Button) findViewById(R.id.sendbutton);
		btDiscon = (Button) findViewById(R.id.discon);
		
		Num1 = (EditText) findViewById(R.id.num1);
		Num2 = (EditText) findViewById(R.id.num2);
		Guess = (EditText) findViewById(R.id.ans);
		
		num1 = Integer.parseInt(Num1.getText().toString());
		num2 = Integer.parseInt(Num2.getText().toString());
		MyAnswer = Integer.parseInt(Guess.getText().toString());
		//int CorrectAnswer = num1 + num2;
		
		new AsyncTask<Void, Void, Integer> (){
			
			
			@Override
			protected Integer doInBackground(Void... params) {
				ClientPacket Send = new ClientPacket("Yuanxing", num1, num2);
				Integer result = null;
				try 
				{
					
					os.writeObject(Send);
					os.flush();
					ServerPacket Receive = (ServerPacket) is.readObject();
					result = Receive.getResult();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				} 
				
				return result;
			
			}
			
			@Override
			protected void onPostExecute(Integer result) {
				super.onPostExecute(result);
				btCon.setEnabled(false);
				btSend.setEnabled(true);
				btDiscon.setEnabled(true);
				text = (TextView) findViewById(R.id.display);
				if (result == MyAnswer)
					text.setText("Correct Answer!");
				else
					text.setText("Wrong Answer!");
			}
			
		}.execute();    
	}
	
	

			
			
			

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
	

}
