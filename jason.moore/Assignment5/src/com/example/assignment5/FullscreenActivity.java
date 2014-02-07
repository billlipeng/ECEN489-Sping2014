package com.example.assignment5;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import com.example.assignment5.util.SystemUiHider;
import com.zpartal.commpackets.ClientPacket;
import com.zpartal.commpackets.ServerPacket;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
	public ClientPacket packet;
	private EditText Num1;
	private EditText Num2;
	private EditText Num3;
	private TextView MT;
	private Button Send;
	public Double number1;
	public Double number2;
	public Double number3;
	public ServerPacket sp;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);
		Num1 = (EditText) findViewById(R.id.editText1);
		Num2 = (EditText) findViewById(R.id.editText2);
		Num3 = (EditText) findViewById(R.id.editText3);
		Send = (Button)   findViewById(R.id.button1);
		MT= (TextView) findViewById(R.id.textView1);

	
}
	public void onSend(View view){
		number1= Double.valueOf(Num1.getText().toString());
		number2= Double.valueOf(Num2.getText().toString());
		number3= Double.valueOf(Num3.getText().toString());
		packet.setNum1(number1.intValue());
		packet.setNum2(number2.intValue());
		try {
			sp= new passpacket().execute(packet).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int a= sp.getResult();
		if (a==number3){
			MT.setText("Correct, answer is "+a);
		}
		else {
			MT.setText("Incorrect, answer is "+a);
		}
		}
	
	private class passpacket extends AsyncTask<ClientPacket, Void, ServerPacket>	 {
		

		@SuppressWarnings("unused")
		protected ServerPacket doInBackground(ClientPacket...packet) {
			try {
				ServerPacket s = null;
				Socket commsock = new Socket("192.168.1.101",2001);
				ObjectOutputStream OOS = new ObjectOutputStream(commsock.getOutputStream());
				ObjectInputStream OIS = new ObjectInputStream(commsock.getInputStream());
				OOS.writeObject(packet);
				try {
					s= (ServerPacket) OIS.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        commsock.close();
				return s;
			} 
		 catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}



		
		
		
	}
}
