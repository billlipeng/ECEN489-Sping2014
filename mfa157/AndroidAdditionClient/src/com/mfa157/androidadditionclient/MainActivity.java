package com.mfa157.androidadditionclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	int num1;
	int num2;
	int input;
	int total;
	int currentScore=0;
	int highScore=0;
	String ans;
	
	String IP;
	int Port;
	private TCPClient mTcpClient;
	
	Socket connection;
	DataOutputStream output1;
	DataInputStream input1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final EditText etInput = (EditText) findViewById(R.id.etInput);
        final EditText etIp = (EditText) findViewById(R.id.etIp);
        final EditText etPort = (EditText) findViewById(R.id.etPort);
        final TextView tvPrompt = (TextView) findViewById(R.id.tvPrompt);
        final TextView tvCheck = (TextView) findViewById(R.id.tvCheck);
        final TextView tvCurrentScore = (TextView) findViewById(R.id.tvCurrentScore);
        final TextView tvHighScore = (TextView) findViewById(R.id.tvHighScore);
        final Button bEnter = (Button) findViewById(R.id.bEnter);
        final Button bRandomize = (Button) findViewById(R.id.bRandomize);
        final Button bConnect = (Button) findViewById(R.id.bConnect);
        Log.e("Android Addition Client", "Connecting...");
        bRandomize.setEnabled(false);
		bEnter.setEnabled(false);
        
        
        bConnect.setOnClickListener(new View.OnClickListener() {
			 
            public void onClick(View v) {
                // Perform action on click
            
            	try {
                	IP=etIp.getText().toString();
                	Port= Integer.parseInt(etPort.getText().toString());
                	new connectTask().execute("");
        			Log.e("Android Addition Client", "Connected!");
        			bRandomize.setEnabled(true);
        			bEnter.setEnabled(true);
        		}  catch (Exception e) {
        			// TODO Auto-generated catch block
        			Log.e("Android Addition Client", "Error Connecting]", e);
        		} 
           	 
            }
        });
		
		//choose new numbers and display them on the tvPrompt textview
		 bRandomize.setOnClickListener(new View.OnClickListener() {
			 
             public void onClick(View v) {
                 // Perform action on click
            	Random numRandom = new Random();
            	num1 = numRandom.nextInt(10);
         		num2 = numRandom.nextInt(10);
         		
				try {
					mTcpClient.sendInt(num1,num2);
					//mTcpClient.sendInt(num1);
         			//mTcpClient.sendInt(num2);
					//total=mTcpClient.receiveInt();
					Log.e("Android Addition Client", String.valueOf(total));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("Android Addition Client", "error getting value");
				}
				
				
         		tvPrompt.setText("What is "+num1+" + "+num2+"?");
         		//total=num1+num2;
            	 
             }
         });
		 
		 bEnter.setOnClickListener(new View.OnClickListener() {
			 
             public void onClick(View v) {
                 // Perform action on click
            	if(etInput.getText().toString().length()>0 ){
	            	try {
						total=mTcpClient.receiveInt();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            	
	            	input=Integer.parseInt(etInput.getText().toString());
	            	if(input==total ) {
	            		tvCheck.setText("Correct! Try Again!");
	            		Random numRandom = new Random();
	                	num1 = numRandom.nextInt(10);
	             		num2 = numRandom.nextInt(10);
	             		tvPrompt.setText("What is "+num1+" + "+num2+"?");
	             		
	             		try {
	             			mTcpClient.sendInt(num1,num2);
	             			//mTcpClient.sendInt(num2);
	    					//total=mTcpClient.receiveInt();
	    				} catch (Exception e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	    				
	             		//total=num1+num2;
	             		currentScore +=1;
	             		if(currentScore>highScore)
	             			highScore=currentScore;
	            	}
	            	else{
	            		tvCheck.setText("Incorrect! Try Again!");
	            		currentScore=0;
	            		
	            	}
	            	etInput.setText("");
	            	tvCurrentScore.setText("Current Score: "+currentScore);
	            	tvHighScore.setText("High Score: "+highScore);
             }
             }
         });
		 
        

    }
    public class connectTask extends AsyncTask<String,String,TCPClient> {
   	 
        @Override
        protected TCPClient doInBackground(String... message) {
 
            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run(IP, Port);
 
            return null;
        }
 
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
 
            //in the arrayList we add the messaged received from server
           // arrayList.add(values[0]);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
           // mAdapter.notifyDataSetChanged();
        }
    }
   
}