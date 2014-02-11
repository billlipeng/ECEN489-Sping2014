package com.example.android2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.zpartal.commpackets.ClientPacket;
import com.zpartal.commpackets.ServerPacket;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.Android3.MESSAGE";
	
	public int num1 = 0;
	public int num2 = 0;
	public int result  = 0;
	private int port = 5555;
	private String ip = "10.201.41.194";

	public void sendMessage(View view) {
		if (netCheck()){
			display("Client waiting...");
			new Send().execute();
		}else{
			display("Error");
		}
	}
	
	public void checkAnswer(){
		EditText editText = (EditText) findViewById(R.id.edit_message);
	    String message = editText.getText().toString();
	    try{
	    	int answer = Integer.parseInt(message);
	    	if (answer == result)
		    	display("Correct!");
		    else
		    	display("Incorrect!");
	    }catch(NumberFormatException e){
	    	display("Your answer is garbage!");
	    }
	}
	
	private class Send extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... args) {
        	try{
        		ClientPacket clientPacket = new ClientPacket("1", num1, num2);
        		Socket connection = new Socket(ip, port);
        		display("Client connected!");
    			
    			ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
    			output.writeObject(clientPacket);
    			output.flush();
    			
    			ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
    			ServerPacket serverPacket = (ServerPacket) input.readObject();
    			result = serverPacket.getResult();
    			checkAnswer();
    			
    			input.close();
    			output.close();
    			connection.close();
    		} catch(IOException e){
    		} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}finally{}
			return null;
        }
    }
	
	public void generateQuestion(View view) { 
	    Random rand = new Random();
		num1 = rand.nextInt(10);
		num2 = rand.nextInt(10);
		//sum = num1 + num2;
		display("What is " + num1 + "+" + num2 + "?");  
	}
	
	public boolean netCheck(){
		ConnectivityManager connMgr = (ConnectivityManager) 
				getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
	}
	
	public void display(String message){
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		intent.putExtra(EXTRA_MESSAGE, message);
	    startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
