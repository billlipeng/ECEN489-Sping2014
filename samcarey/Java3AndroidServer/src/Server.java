import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.zpartal.commpackets.*;

public class Server {
	public static void main(String []args){
		try{
			String serverID = "server1";
			while(true){
			System.out.println("Server initializing...");
			ServerSocket server = new ServerSocket(5555, 1);
			System.out.println("Server waiting...");
			Socket connection = server.accept();
			System.out.println("Server connected!");
			
			ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
			
			ClientPacket clientPacket = (ClientPacket) input.readObject();
			System.out.println("Halfway");
			int num1 = clientPacket.getNum1();
			int num2 = clientPacket.getNum2();
			int result = num1 + num2;
			
			ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
			ServerPacket serverPacket = new ServerPacket(serverID, result);
			output.writeObject(serverPacket);
			output.flush();
			System.out.println("Finished");
			
			input.close();
			output.close();
			server.close();
			}
		}catch(IOException e){
			System.out.println("IOException");
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException");
		}
	}
}
