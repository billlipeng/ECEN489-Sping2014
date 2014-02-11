package assignment5;

import java.io.*;
import java.net.*;
import java.lang.*;

import com.zpartal.commpackets.*;


public class Threader extends Thread{

	protected Socket socket;
	ClientPacket clientpac;
	ServerPacket serverpac;
	int a = 0;
	int b = 0;
	public Threader(Socket clientsocket){
		this.socket = clientsocket;
	}
	
	public void run() {
	
		ObjectInputStream input = null;
		ObjectOutputStream output = null;
		
		try{
			input = 
				new ObjectInputStream(socket.getInputStream());
			output = 
	    		new ObjectOutputStream(socket.getOutputStream());
		}
		catch(IOException e){
			System.out.println(e);
			System.exit(1);
		}
		try{
		while(true){
			    clientpac = (ClientPacket) input.readObject();
			    a = clientpac.getNum1();
			    b = clientpac.getNum2();
			    String clientID = clientpac.getClientID();
				System.out.println("Client ID: " +clientID);
				System.out.println("Number1: " + a + " " + "Number2: " + b);
				System.out.println("Sending Answer");
				serverpac = new ServerPacket("Destoroyah", clientpac.getNum1()+clientpac.getNum2());
				output.writeObject(serverpac);
				System.out.println("Answer Sent:" + (a+b) );
			}
		}
			catch(IOException e){
				System.out.println(e);
			}
			catch(ClassNotFoundException e){
				System.out.println(e);
				System.exit(2);
		}
		try{
			input.close();
			output.close();
			socket.close();
		}
		catch(IOException e){
			System.out.println(e);
		}
	}
}
