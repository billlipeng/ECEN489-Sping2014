package com.example.android2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;
import java.util.Random;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	int a;
	int b;
	
	int useranswer;
	
	int sum;
	
	int currentScore=0;
	int highScore=0;
	String ans;

	String ipaddress;
	int port;
	
	private Connection Client;

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
        
        bRandomize.setEnabled(false);
		bEnter.setEnabled(false);
        
        
        bConnect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
       
            	try {
                	ipaddress=etIp.getText().toString();
                	port= Integer.parseInt(etPort.getText().toString());
                	
                	new connectTask().execute("");
        			bRandomize.setEnabled(true);
        			bEnter.setEnabled(true);
        		}  catch (Exception e) {
        			System.out.print(e);
        		} 
           	 
            }
        });

		
		 bRandomize.setOnClickListener(new View.OnClickListener() {

             public void onClick(View v) {
            	Random numRandom = new Random();
            	a = numRandom.nextInt(101);
         		b = numRandom.nextInt(101);
         		
				try {
					Client.sendInt(a,b);
					
				} catch (Exception e) {
				
					System.out.print(e);
				}


         		tvPrompt.setText("What is "+a+" + "+b+"?");
         		
         		

             }
         });

		 bEnter.setOnClickListener(new View.OnClickListener() {

             public void onClick(View v) {
               
            	if(etInput.getText().toString().length()>0 ){
	            	try {
						sum=Client.receiveInt();
					} catch (IOException e) {
					
						System.out.print(e);
					}

	            	useranswer=Integer.parseInt(etInput.getText().toString());
	            	if(useranswer==sum ) {
	            		tvCheck.setText("The Swarm is pleased");
	            		Random numRandom = new Random();
	                	a = numRandom.nextInt(101);
	             		b = numRandom.nextInt(101);
	             		tvPrompt.setText("Sum: "+a+" + "+b+"?");

	             		try {
	             			Client.sendInt(a,b);
	             			
	    				} catch (Exception e) {
	    			
	    					System.out.print(e);
	    				}

	             		currentScore ++;
	             		if(currentScore>highScore)
	             			highScore=currentScore;
	             		
	             		if(currentScore < 0){
	                 		currentScore = 0;
	                 	}
	            	}
	            	else{
	            		tvCheck.setText("You Died!");
	            		currentScore  --;

	            	}
	            	etInput.setText("");
	            	tvCurrentScore.setText("Current Score: "+currentScore);
	            	tvHighScore.setText("High Score: "+highScore);
             }
             }
         });

        

    }
    public class connectTask extends AsyncTask<String,String,Connection> {
   	 
        @Override
        protected Connection doInBackground(String... message) {
 
            Client = new Connection(new Connection.OnMessageReceived() {
                @Override

                public void messageReceived(String message) {
                
                    publishProgress(message);
                }
            });
            Client.run(ipaddress, port);
 
            return null;
        }
 
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
 
        }
    }
   
}