package com.example.android2;

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

import android.os.Bundle;
import android.app.Activity;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {
	
	EditText num1;
	EditText num2;
	EditText sum;
	EditText message;
	Button generate;
	Button compute;
	Button connect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		num1 = (EditText) findViewById(R.id.editText1);
		num2 = (EditText) findViewById(R.id.editText2);
		sum  = (EditText) findViewById(R.id.editText3);
		message = (EditText) findViewById(R.id.editText4);
		generate = (Button) findViewById(R.id.button2);
		compute = (Button) findViewById(R.id.button3);
		connect = (Button) findViewById(R.id.button1);
		
		generate.setOnClickListener(new ClickButton());
		compute.setOnClickListener(new ClickButton1());
		connect.setOnClickListener(new ClickButton2());
		
	}
	Random random = new Random();
	int a = random.nextInt(9);
	int b = random.nextInt(9);
	public int total;
	ByteBuffer integers = ByteBuffer.allocate(8).putInt(a).putInt(b);
	public byte[] byte_int = integers.array();
	
private class ClickButton implements Button.OnClickListener{
			
			public void onClick(View v){
				
			num1.setText(Integer.toString(a));		
			num2.setText(Integer.toString(b));
			
			}
		}
private class ClickButton1 implements Button.OnClickListener
		{
			
			public void onClick(View v){
			
			int x = Integer.parseInt(sum.getText().toString());
			int total1 = a+b;
			try {
				if (x == total1)
					message.setText("Correct");
				else
					message.setText("Incorrect");

				}
			finally{
				
					}
			}
		}
public class ClickButton2 implements Button.OnClickListener 
{
	
	public void onClick(View v){
		Socket connection;
		System.out.print("Connection to the Swarm.\n");
		try {
			connection = new Socket("10.0.2.2", 5559);	
			if (connection.isConnected())
				System.out.print("Hive Mind Located: Awaiting to send orders\n");

			DataOutputStream output = new DataOutputStream(connection.getOutputStream());
			DataInputStream input = new DataInputStream(connection.getInputStream());
			
			output.write(byte_int);
			
			int total = input.readInt();
			System.out.print(total);
			
			output.close();
			input.close();
			connection.close();
		}
		 catch (UnknownHostException e) {

				e.printStackTrace();
			 }
			 catch (IOException e) {
				 
				e.printStackTrace();
			}
		
			}
		}
}
