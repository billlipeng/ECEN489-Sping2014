package com.example.android2;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import android.util.Log;

import com.zpartal.commpackets.ClientPacket;
import com.zpartal.commpackets.ServerPacket;
 
public class Connection {
 byte[] buffer= new byte[1];
    private String serverMessage;
    int result;
  
    
    private OnMessageReceived MessageL = null;
    private boolean Runner = false;
    
    ObjectOutputStream output;
    ObjectInputStream input;
 
    public Connection(OnMessageReceived listener) {
        MessageL = listener;
    }

    public void sendInt(int num1, int num2)  {
        if (output != null) {
    	
            
            try {
                ClientPacket send = new ClientPacket("Destoroyah", num1,num2);
				output.writeObject(send); 
				output.flush();
			} catch (IOException e) {
				
				System.out.print(e);
			}
      
           
        }
        	
    	}
    
    public int receiveInt() throws IOException {
     	
           return result;

    }
 
    public void stopClient(){
        Runner = false;
    }
 
    public void run(String SERVERIP, int SERVERPORT) {
 
        Runner = true;
 
        try {
        
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
 
            Socket socket = new Socket(serverAddr, SERVERPORT);
 
            try {
            	
                output = new ObjectOutputStream( socket.getOutputStream() );
 
 
           
                input = new ObjectInputStream( socket.getInputStream() );
                             
               
                while (Runner) {
                	ServerPacket receive = (ServerPacket)  input.readObject();
                	result = receive.getResult();
                    serverMessage = "Server ID: " + receive.getServerID() + "   Sum: " + receive.getResult();
                    Log.e("Server ID:" + receive.getServerID() + "Result: " + receive.getResult(), SERVERIP);
                }

            } catch (Exception e) {
 
            	System.out.print(e);
 
            } finally {
  
                socket.close();
            }
 
        } catch (Exception e) {
 
        	System.out.print(e);
 
        }
 
    }
 
    public interface OnMessageReceived {
        public void messageReceived(String serverMessage);
    }
}