package java3androidserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.zpartal.commpackets.ClientPacket;
import com.zpartal.commpackets.ServerPacket;

public class Threader extends Thread {
	protected Socket socket;
	ClientPacket cp;
	ServerPacket sp;
	public Threader(Socket client){
		this.socket = client;
	}
	public void run(){
		
		ObjectOutputStream sender = null;
		ObjectInputStream receiver = null;
		
		try{
			sender = new ObjectOutputStream(socket.getOutputStream());
			receiver = new ObjectInputStream(socket.getInputStream());					
		}
		catch(IOException e){
			System.err.println("Could not create I/O Streams..!");
		}
		
		
			try{
				while(true){
			//listening for packets and sending back the answer
				
				ClientPacket cp = (ClientPacket) receiver.readObject();
				sp = new ServerPacket("Cameron", cp.getNum1()+cp.getNum2());
				sender.writeObject(sp);
				System.out.println("Correct answer sent to user!");
				
				//System.exit(0);
			}
			}
			catch(ClassNotFoundException e){
				System.err.println("Class not found...");
			}
			catch(IOException e){
				
				System.out.println("A client disconnected!");
			}
			try{
				socket.close();
				System.out.println("Socket terminated.");
			}
			catch(IOException e){
				System.err.println("Couldn't close the socket!");
			}
			
		}
		
	
	
	}	




