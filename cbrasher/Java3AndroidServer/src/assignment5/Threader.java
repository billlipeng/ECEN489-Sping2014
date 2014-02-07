package assignment5;

import java.io.*;
import java.net.*;

import com.zpartal.commpackets.*;


public class Threader extends Thread{

	protected Socket socket;
	ClientPacket clientpac;
	ServerPacket serverpac;
	public Threader(Socket clientsocket){
		this.socket = clientsocket;
	}
	
	public void start() {
	
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
		while(true){
			try{
				Object num = input.readObject();
				clientpac = (ClientPacket) num;
				int num1 = clientpac.getNum1();
				int num2 = clientpac.getNum2();
				int sum = num1 + num2;
				serverpac.setServerID("Destoroyah");
				serverpac.setResult(sum);
				output.writeObject(serverpac);
			}
			catch(IOException | ClassNotFoundException e){
				System.out.println(e);
				System.exit(2);
			}
		}
	}

}
