package com.mfa157.androidadditionclient;

import android.util.Log;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import com.zpartal.commpackets.ClientPacket;
import com.zpartal.commpackets.ServerPacket;
 
public class TCPClient {
 byte[] buffer= new byte[1];
    private String serverMessage;
    int result;
   // public static final String SERVERIP = "192.168.1.133"; //your computer IP address
   // public static final int SERVERPORT = 12345;
    
   // public static final String SERVERIP = "192.168.1.2"; //your computer IP address
   // public static final int SERVERPORT = 1000;
    
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    
    ObjectOutputStream out;
    ObjectInputStream in;
 
    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }
 
    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     * @throws IOException 
     */
    public void sendInt(int num1, int num2)  {
        if (out != null) {
    	
            
            try {
                ClientPacket send = new ClientPacket("Miguel", num1,num2);
				out.writeObject(send); //send total back to client
				 out.flush();
				//serverMessage=in.readInt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("TCPClient", "error getting value",e);
			}
      
           
        }
        	
    	}
    
    /**
     * receives message from server
     * @param message text entered by client
     * @throws IOException 
     */
    public int receiveInt() throws IOException {
     	
           return result;

    }
 
    public void stopClient(){
        mRun = false;
    }
 
    public void run(String SERVERIP, int SERVERPORT) {
 
        mRun = true;
 
        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
 
            Log.e("TCP Client", "C: Connecting...");
 
            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVERPORT);
 
            try {
 
                //send the message to the server
                out = new ObjectOutputStream( socket.getOutputStream() );
 
                Log.e("TCP Client", "C: Sent.");
 
                Log.e("TCP Client", "C: Done.");
 
                //receive the message which the server sends back
                in = new ObjectInputStream( socket.getInputStream() );
                                //in this while the client listens for the messages sent by the server
               
                while (mRun) {
                	ServerPacket receive = (ServerPacket)  in.readObject();
                	result = receive.getResult();
                    serverMessage = "Server ID: " + receive.getServerID() + "   Sum: " + receive.getResult();
                    //Log.e("TCP Client", String.valueOf(serverMessage));
                    /*
                    if ( serverMessage !=-1 && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = -1;
                    */
                }
                
 
                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
 
 
            } catch (Exception e) {
 
                Log.e("TCP", "S: Error", e);
 
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }
 
        } catch (Exception e) {
 
            Log.e("TCP", "C: Error", e);
 
        }
 
    }
 
    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String serverMessage);
    }
}
