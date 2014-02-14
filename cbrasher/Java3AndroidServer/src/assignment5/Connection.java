package assignment5;

import java.io.*;
import java.net.*;

import com.zpartal.commpackets.*;

public class Connection implements Runnable {
	
	protected Socket client = null;
	ObjectInputStream input;
	ObjectOutputStream output;
	
	int a = 0;
	int b = 0;
	
	ClientPacket incoming;
	ServerPacket outgoing;
	boolean connected=false;
	
	public Connection(Socket client){
		super();
		this.client = client;
	}
	
	public void run() {
		try{
		input = 
				new ObjectInputStream(client.getInputStream());
		output =
				new ObjectOutputStream(client.getOutputStream());
		
		while(true){
			incoming = (ClientPacket) input.readObject();
			a = incoming.getNum1();
			b = incoming.getNum2();
			String ClientID = incoming.getClientID();
			System.out.println("Client ID: " + ClientID);
			System.out.println("Recevied:"+ a + "+" + b);
			System.out.println("Total:"+(a+b));
			System.out.println("Swarm sending Total to client");
			outgoing = new ServerPacket("Destoroyah", incoming.getNum1()+incoming.getNum2());
			output.writeObject(outgoing);
		}
		}
		catch(IOException e){
			System.out.println("Client disconnected unexpectedly");
		}
		catch(ClassNotFoundException p){
			System.out.println(p);
		}
		try{
			output.close();
			input.close();
			System.out.println("Client Disconnected");
			client.close();
		}
		catch(IOException e){
			System.out.println(e);
		}
	}
	

}
