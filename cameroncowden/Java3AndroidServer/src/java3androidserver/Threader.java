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
			System.err.println("Something done goofed in the thread.");
		}
		
		while(true){
			try{
			//listening for packets and sending back the answer
				//Object received =  receiver.readObject();
				//cp = (ClientPacket) received;
				ClientPacket cp = (ClientPacket) receiver.readObject();
				sp = new ServerPacket("Cameron", cp.getNum1()+cp.getNum2());
				//int n1 = cp.getNum1();
				//int n2 = cp.getNum2();
				//int sum = n1 + n2;
				//sp.setServerID("Cameron");
				//sp.setResult(sum);
				sender.writeObject(sp);
				System.out.println("Correct answer sent to user!");
			}
			catch(ClassNotFoundException e){
				System.err.println("Class not found...");
			}
			catch(IOException e){
				System.err.println("IO exception here..");
			}
			
		}
		
	
	
	}
	


}



