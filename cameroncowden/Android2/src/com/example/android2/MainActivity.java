package com.example.android2;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.util.*;

import java.net.*;
import java.io.*;
import java.util.*;

import com.zpartal.commpackets.*;

public class MainActivity extends Activity {
	
	//Declaring Buttons and Text fields
	Button CheckNetwork;
	TextView NetworkStatus;
	
	Button NewQuestion;
	TextView CurrentQuestion;
	
	Button Connect;
	EditText IPaddress;
	
	TextView ConnectionStatus;
	
	Button Send;
	EditText userinput;
	
	TextView AnswerReceived;
	
	public static String ipaddress;
	public int myanswer;
	public int n1,n2;
	
	//private Socket connection;
	private static final int port = 5555;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Toast.makeText(this, "Landed in onCreate", Toast.LENGTH_LONG).show();
		
		//Linking up Buttons and text fields 
		CheckNetwork = (Button) findViewById(R.id.checknetwork);
		NetworkStatus = (TextView) findViewById(R.id.networkstate);
		
		NewQuestion = (Button) findViewById(R.id.generatenums);
		CurrentQuestion = (TextView) findViewById(R.id.numberdisplay);
		
		Connect = (Button) findViewById(R.id.saveip);
		IPaddress = (EditText) findViewById(R.id.ipaddress);
		
		ConnectionStatus = (TextView) findViewById(R.id.connectionstatus);
		
		Send = (Button) findViewById(R.id.sendanswer);
		userinput = (EditText) findViewById(R.id.userinput);
		
		AnswerReceived = (TextView) findViewById(R.id.answerreceived);
		
		//Linking Buttons to their functions		
		CheckNetwork.setOnClickListener(CheckNetworkClickListener);
		NewQuestion.setOnClickListener(NewQuestionClickListener);
		Connect.setOnClickListener(ConnectClickListener);
		Send.setOnClickListener(SendClickListener);
		
	}
	
	

public OnClickListener CheckNetworkClickListener = new OnClickListener(){
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ConnectivityManager connMgr = (ConnectivityManager) 
		        getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		boolean isWifiConn = networkInfo.isConnected();
		if (isWifiConn == true)
			NetworkStatus.setText("Wifi is up!");
		else
			NetworkStatus.setText("Wifi is down :(");
	}	
	
};
public OnClickListener NewQuestionClickListener = new OnClickListener(){
	@Override
	public void onClick(View v) {
		
		Random R = new Random();
		int a = R.nextInt(100);
		int b = R.nextInt(100);
		n1 = a;
		n2 = b;
		CurrentQuestion.setText(Integer.toString(a) + " + " + Integer.toString(b));
		
	}
};
public OnClickListener ConnectClickListener = new OnClickListener(){
	
	
	@Override
	public void onClick(View v) {
				
		ipaddress = IPaddress.getText().toString();
		//Connect to server
		new Thread(new connect()).start();
		
	}
	
};
public OnClickListener SendClickListener = new OnClickListener(){

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//Send ints to server.
		//int correctanswer = receiver.readInt();
	}
	
};



class connect implements Runnable {
	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			try{
				InetAddress ip = InetAddress.getByName(ipaddress);
				Socket connection = new Socket(ip,port);
				ObjectOutputStream sender = new ObjectOutputStream(connection.getOutputStream());
				ObjectInputStream receiver = new ObjectInputStream(connection.getInputStream());
		        
		        ClientPacket cp = new ClientPacket("Test",n1,n2);
		        ServerPacket sp;
		        sender.writeObject(cp);      
		        
		        Object received =  receiver.readObject();
		        sp = (ServerPacket) received;
		        int correctanswer = sp.getResult();
		        
		        if (Integer.parseInt(userinput.getText().toString()) == correctanswer)
					AnswerReceived.setText("CORRECT!");
				else
					AnswerReceived.setText("WRONG!");
					
					
				} 
				
				catch (IOException e){
					//output error message
					e.printStackTrace();
				}
				catch(ClassNotFoundException e){
					e.printStackTrace();
				}
			
		}
}


}