package com.example.project1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.Log;

import com.zpartal.project1.datapackets.DataPoint;

public class ServerCom{
	private static final String TAG = "Server Communication";
	
	// Server connection variables
	Socket connection;
	ObjectOutputStream output;
	ObjectInputStream input;
	private InetAddress IPaddress;
	private int portNumber;
	private ArrayList<DataPoint> dp; // Object to be send to the server
	private boolean flag = true;
	
	
	
	public ServerCom(InetAddress iPaddress, int portNumber,
			ArrayList<DataPoint> dp) {
		super();
		IPaddress = iPaddress;
		this.portNumber = portNumber;
		this.dp = dp;
	}


	// Server Connection
	public void connectToServer() {
		Log.d(TAG, "test2");
		ConnectServer serverConnection = new ConnectServer();
		serverConnection.execute();
		flag = true;
		new ConnectServer().execute();
    }
	
	
	private class ConnectServer extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected Void doInBackground (Void ...voids){

			try
			{
				if(flag){
				// Due to error of being executed twice
				flag = false;
				// Creating connection
				connection = new Socket( IPaddress , portNumber);
				Log.d(TAG, "Socket Created");
				// Creating stream Objects
				output = new ObjectOutputStream(connection.getOutputStream());
				Log.d(TAG, "Object Stream Created");
				Log.d(TAG,Integer.toString(dp.size()));
				output.flush();
				output.writeObject(dp);
				Log.d(TAG, "Data sended to Server");
				output.close();
				connection.close();
				Log.d(TAG, "Socket Closed");
				}
			}
			catch( IOException ioException)
			{
				ioException.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void v) {
		}
	}
}
