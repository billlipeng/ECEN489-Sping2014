package com.example.android2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zpartal.commpackets.ClientPacket;
import com.zpartal.commpackets.ServerPacket;

public class MainActivity extends Activity {
	Button generate;
	TextView CurrentQuestion;
	Button Connect;
	EditText IPaddress;
	Button check;
	EditText userinput;
	TextView AnswerReceived;

	public static String ipaddress;
	public int myanswer;
	public int n1,n2;

	private static final int port = 5555;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toast.makeText(this, "Landed in onCreate", Toast.LENGTH_LONG).show();

		generate = (Button) findViewById(R.id.generatenums);
		CurrentQuestion = (TextView) findViewById(R.id.numberdisplay);

		Connect = (Button) findViewById(R.id.saveip);
		IPaddress = (EditText) findViewById(R.id.ipaddress);

		userinput = (EditText) findViewById(R.id.userinput);

		AnswerReceived = (TextView) findViewById(R.id.answerreceived);

		generate.setOnClickListener(NewQuestionClickListener);
		Connect.setOnClickListener(ConnectClickListener);
	

	}



public OnClickListener NewQuestionClickListener = new OnClickListener(){
	@Override
	public void onClick(View v) {

		Random R = new Random();
		int a = R.nextInt(10);
		int b = R.nextInt(10);
		n1 = a;
		n2 = b;
		CurrentQuestion.setText(Integer.toString(a) + " + " + Integer.toString(b));

	}
};
public OnClickListener ConnectClickListener = new OnClickListener(){


	@Override
	public void onClick(View v) {

		ipaddress = IPaddress.getText().toString();
		new Thread(new connect()).start();

	}

};


class connect implements Runnable {

		@Override
		public void run() {

			try{
				InetAddress ip = InetAddress.getByName(ipaddress);
				Socket connection = new Socket(ip,port);
				ObjectOutputStream sender = 
						new ObjectOutputStream(connection.getOutputStream());
				ObjectInputStream receiver = 
						new ObjectInputStream(connection.getInputStream());

		        ClientPacket cp = new ClientPacket("Destoroyah",n1,n2);
		        ServerPacket sp;
		        sender.writeObject(cp);      

		        Object received =  receiver.readObject();
		        sp = (ServerPacket) received;
		        int correctanswer = sp.getResult();
		        connection.close();
		        if (Integer.parseInt(userinput.getText().toString()) == correctanswer)
		        	runOnUiThread(new Runnable() {
			            @Override
			            public void run() {

			     
			            	AnswerReceived.setText("Correct");
			           }
			       });

				else
					runOnUiThread(new Runnable() {
			            @Override
			            public void run() {

			     
			            	AnswerReceived.setText("You Died");
			           }
			       });




				} 

				catch (IOException e){
					e.printStackTrace();
				}
				catch(ClassNotFoundException e){
					e.printStackTrace();
				}

		}
}


}