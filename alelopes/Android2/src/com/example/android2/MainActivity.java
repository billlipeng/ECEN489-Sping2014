package com.example.android2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.zpartal.commpackets.ClientPacket;
import com.zpartal.commpackets.ServerPacket;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	

	private ObjectOutputStream output; // output stream to server
	private ObjectInputStream input; // input stream from server
	public Socket socket;
	int d,a,b;
	String ip;

	int flag=0;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	  private class socketConection extends AsyncTask<String, Void, String> {
	
		  
		  @Override
		    protected String doInBackground(String... urls) {
			  
            Log.i("AsyncTask", "doInBackground: Creating socket");                       
		    try {
	            if (flag==0){
	            	socket = new Socket();
		            Log.i("AsyncTask", "try Create");
		            Log.i("AsyncTask",ip);
		            SocketAddress sockad = new InetSocketAddress(ip, 5555); 
			    	socket.connect(sockad);
			        Log.i("AsyncTask", "Could Create");
		    	 	output = new ObjectOutputStream(socket.getOutputStream());
			    	input = new ObjectInputStream(socket.getInputStream());
		            }
	          //  flag=2;
		     //  do {
		    	   
		    //	   if (flag==2){
			    	if (socket.isConnected()){

			       
				    	Log.i("AsyncTask", "Is Connected");
	
						ClientPacket sendObj = new ClientPacket("Alex",a,b);
	
				    	output.writeObject(sendObj);
				    	output.flush();
				    	output.reset();
				    	ServerPacket recobj = (ServerPacket) input.readObject();
					d=recobj.getResult();		    		
			    	}
			    	flag=1;
		    	//   }
		     //   }while (true);	
		    }
		    
		    catch (Exception e){
	            Log.i("AsyncTask", "Coudnt Create");

		    }
		    
		    
		  	  
		    Log.i("AsyncTask", "Ending");
	  		return null;
		      	
		    }
	
	  }
	
	
	
	
	public void conect (View view){
	

		EditText editText = (EditText) findViewById(R.id.value1);
		String num1 = editText.getText().toString();
		a=Integer.parseInt(num1);
		EditText editText2 = (EditText) findViewById(R.id.value2);
		String num2 = editText2.getText().toString();
		b=Integer.parseInt(num2);
		
		EditText editText3 = (EditText) findViewById(R.id.value0);
		ip = editText3.getText().toString();
		
		
    	Log.i("MainActivity", "EANEGO");

		socketConection task = new socketConection();
		task.execute(ip);
	    	Log.i("MainActivity", "at");


		}
	
	public void result (View view){
		Log.i("MainActivity","SAIU!");
		TextView tv = (TextView)findViewById(R.id.TextView);  
		tv.setText(Integer.toString(d));
	
	}
	
	public void cancel (View view){
		try {
			socket.close();
			output.close();
			input.close();
			flag=0;
			Log.i("MainActivity","canceling...");

		}
		catch (Exception e){
			Log.i("MainActivity","Error to cancel!");
		}
	}
	

}
