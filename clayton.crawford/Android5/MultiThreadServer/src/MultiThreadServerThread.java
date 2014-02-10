import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.zpartal.commpackets.ClientPacket;
import com.zpartal.commpackets.ServerPacket;


public class MultiThreadServerThread implements Runnable{

	Socket socket = null;
	ObjectInputStream in;
	ObjectOutputStream out;
	int addend1 = 0;
	int addend2 = 0;
	String clientID = "";
	ClientPacket clientPacket = null;
	ServerPacket serverPacket = null;
	
	MultiThreadServerThread(Socket clientSocket) {
		super();
		this.socket = clientSocket;
	}
	
	public void run() {
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			while (true) {
				clientPacket = (ClientPacket) in.readObject();
				addend1 = clientPacket.getNum1();
				addend2 = clientPacket.getNum2();
				clientID = clientPacket.getClientID();
				System.out.println("Client " + clientID + ": " + addend1 + " + " + addend2 + " = " + (addend1 + addend2));
				serverPacket = new ServerPacket("clayton.crawford", addend1 + addend2);
				out.writeObject((ServerPacket)serverPacket);
				out.flush();
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		finally {
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
