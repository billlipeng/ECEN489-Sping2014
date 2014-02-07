package com.example.androidclienttest;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.regex.Pattern;

import com.zpartal.commpackets.ClientPacket;
import com.zpartal.commpackets.ServerPacket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	/** Called when the activity is first created. */
	private Button new_question,check_answer;
	private TextView text_qestion,text_result,text_addr;
	private EditText user_answer,ip_address;
	public 	String result;

	
	private String ip = "192.168.1.108";
	//private String ip = "127.0.0.1";
	
	static private ObjectOutputStream oos = null;	
	static private ObjectInputStream ois = null;

	static private Socket client = new Socket(); 

	private int a = 1 , b = 2;
	private int sum = 4;
	private boolean ConnectFlag = false;
	private String TAG = "Client4";
	
	//public static Socket client;
	@Override
	public void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new_question = (Button)findViewById(R.id.new_question);
		check_answer = (Button)findViewById(R.id.check_answer);
		
		user_answer = (EditText)findViewById(R.id.user_answer);
		ip_address = (EditText)findViewById(R.id.ip_address);

		text_addr = (TextView)findViewById(R.id.t0);
		text_qestion = (TextView)findViewById(R.id.t1);
		text_result = (TextView)findViewById(R.id.t2);
		
		text_addr.setText("Please input IP address: ");
		new_question.setOnClickListener(this);
		check_answer.setOnClickListener(this);
	}
	
	public void onClick(View v){
		
		switch (v.getId()){
		
			case R.id.new_question:
				a = (int)(Math.random()*50);
				b = (int)(Math.random()*50);
				ConnectServer setup = new ConnectServer();
				setup.execute();
    			Log.d(TAG,"Start");
				text_qestion.setText("Calculate: "+ a  + "+" + b + " = ");
				break;
			
			case R.id.check_answer:	
    			Log.d(TAG,"Check answer");
    			if(user_answer.getText().toString().length()>0)
    			{
    				if (isNumeric(user_answer.getText().toString())){
    					result = (Integer.parseInt(user_answer.getText().toString()) == sum) ? "correct!": "incorrect.";
    					text_result.setText("Your answer is "+ result);
    				}
    				else
    					text_result.setText("Please input legal format!");
    			}
				break;
		}
	}
	
	private static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	} 
	
	private class ConnectServer extends AsyncTask<Void, Void, Void> {
	    @Override
		protected Void doInBackground(Void... arg0) {
	    	InetAddress ServerAddr;
			try{
				if(ConnectFlag == false)
				{
					if(ip_address.getText().toString().length()>0)
						{ip = ip_address.getText().toString();}		
					ServerAddr = InetAddress.getByName(ip);
	    			Log.d(TAG,ip);
					client = new Socket(ServerAddr, 5555);
					OutputStream os = client.getOutputStream();
					oos = new ObjectOutputStream(os);
					InputStream is = client.getInputStream();
					ois = new ObjectInputStream(is);
					if(client.isConnected())
					{
						ConnectFlag = true;
						Log.d(TAG,"Socket Connected");
					}
				 
				}
				// Check if Client connect
				
				if(ConnectFlag == true)
				{
					ClientPacket packet = new ClientPacket("billlipeng",a,b);
					if(packet != null && oos != null)
					{
						Log.d(TAG,"Sending to Packet...");
						oos.writeObject((ClientPacket) packet);	
						oos.flush(); // Clean Output stream data
						oos.reset();
					}
					ServerPacket response;
	    			response = (ServerPacket) ois.readObject();// Get server feedback
	    			if(response != null)
	    			{
	        			sum = response.getResult();
		    			Log.d("sum=",Integer.toString(sum));
	    			}
				}
			} catch (IOException e){
					e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return null;
	     }
	}
}