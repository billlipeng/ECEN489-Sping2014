package com.example.android4client2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zpartal.commpackets.*;

public class Client extends Activity {

	public static String ipAddress;
	private EditText address;
	private TextView sum;
	private EditText guess;
	private TextView Soln;
	public ObjectInputStream ois;
	public ObjectOutputStream oos;
	public int x;
	public int y;
	public int correct;
	public String RightWrong = "hi";
	public String stringOut;
	Socket connection;
	
	ServerPacket sp = new ServerPacket("jpbinder",x);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);
		address = (EditText) findViewById(R.id.address);
		sum = (TextView) findViewById(R.id.sum);
		guess = (EditText) findViewById(R.id.guess);
		Soln = (TextView) findViewById(R.id.Soln);
		sumMaker();
		
		
		
		//button listeners
		final Button Check = (Button) findViewById(R.id.Check);
	    Check.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	
	        	//
	        	
	        	//check answer
	  
	        	
	        	stringOut = answerRes();
				Soln.setText(stringOut);
	        	
	        }
	        private String answerRes() {
				String guessSol = guess.getText().toString();
				int guessSolInt = Integer.parseInt(guessSol);
				
				//put in packet to send in async
				
				
					
					
					
					
					if(correct == guessSolInt) {
						RightWrong = "Right";
					}
					else
					{
						RightWrong = "Wrong";
					}
				
				
				if(RightWrong=="hi")
				{
					RightWrong = "Wrongs";
					
				}
				return RightWrong;
			}
	    });
	
	    final Button ansBtn = (Button) findViewById(R.id.ansBtn);
	    ansBtn.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	
	        	
	        	
	        	//check answer
	        	new connect().execute();
	        	
	        	
	        	
	        }

			
			


	    });
	    
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.client, menu);
		return true;
	}

	
	void sumMaker() {
		// random number gen
		Random generator = new Random();
		// define the two numbers to be added (between 1 and 10)
		x = generator.nextInt(10) + 1;
		y = generator.nextInt(10) + 1;
		
		
		String summation = x + " + " + y + "= ?";
		sum.setText(summation);

	}
	
	
	private class connect extends AsyncTask<Void, Void, Integer> {
		
		
		

		@Override
		protected Integer doInBackground(Void... params) {

			//connection
				//
        		String ipAddress = address.getText().toString();
//					ipAddress = "192.168.1.9";
				
				
					System.out.println(ipAddress);
					
					try {
						connection = new Socket(ipAddress,5555);
						ObjectOutputStream oos = new ObjectOutputStream(connection.getOutputStream());
						
						oos.flush();
						//
						ClientPacket cp = new ClientPacket("jpbinder",x,y);
						cp.setNum1(x);
						cp.setNum2(y);
						
						
						oos.writeObject(cp);
						
						
						ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());
						
						
						
							sp = (ServerPacket) ois.readObject();

							correct = sp.getResult();
							
							connection.close();
							
						
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					
					
					
					
				 
				
				
				
				
				
				
					
					
			return null;

		}
	}
		
					
			
	}
	






