package ecen489.mark.android2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.zpartal.commpackets.*;
import android.os.AsyncTask;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	TextView textOutput;
	int num1;
	int num2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textOutput = (TextView) findViewById(R.id.textOut);
	}
	

	public void connect(View view)
	{
		EditText text1 = (EditText) findViewById(R.id.ipText);
		String ip_addr = text1.getText().toString();
		new talkToServer().execute(ip_addr);
	}
	
	
	 private class talkToServer extends AsyncTask<String,Void,Integer>
	{
		@Override
		protected Integer doInBackground(String... ip_addr) {
			Socket connection = null;
			num1 = (int)Math.round(Math.random()*9);
			num2 = (int)Math.round(Math.random()*9);
			int answer = -1;
		
			//System.out.print("test\n");
			//System.out.print("IP: " + getLocalIpAddress());
			//textOutput.setText(ip_addr);
			//System.out.print(ip_addr);
			try {
				System.out.println("opening socket...");
				connection = new Socket(ip_addr[0], 6123);	//10.201.193.83
				if (connection.isConnected())
					System.out.print("Socket found.\n");
				
				ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
				ObjectInputStream input = new ObjectInputStream(connection.getInputStream());	
				
				ClientPacket cp = new ClientPacket("cp", num1, num2);
				
				cp.setNum1(num1);
				cp.setNum2(num2);
				output.writeObject(cp);
				//output.write(byte_array);
				output.writeInt(num1);
				output.writeInt(num2);
				System.out.print("Info sent.\n");
				
				System.out.print("Input stream acquired.\n");
				ServerPacket sp = (ServerPacket) input.readObject();
				
				answer = sp.getResult();
				
				output.close();
				input.close();
				
				connection.close();
				
			} catch (UnknownHostException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return answer;
		 }
		
		 @Override
	        protected void onPostExecute(Integer result) {	 	
			 	textOutput.setText(num1 + " + " + num2 + " = " + result.toString());
	        }

	
		
	}

}
