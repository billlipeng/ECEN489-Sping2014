package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import com.zpartal.commpackets.*;


public class java3server {
	
	
	public static void main(String[] args) { 
		
		
		int ans;
		ServerSocket server;
		try {
		server = new ServerSocket(5555);
		while(true) {
			
				
			Socket connection = server.accept();
//			if (connection != null)
//				System.out.println("connection achieved");
			System.out.println("Connection Established");
			ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());
			
			
			
//make ints out of

			
			int x = 0;
			int y = 0;
			
				ClientPacket cp = new ClientPacket("jpbinder",2,2);
				cp = (ClientPacket) ois.readObject();
				x = cp.getNum1();

				y = cp.getNum2();
				String Client = cp.getClientID();

				System.out.println("Received");
				System.out.println(Client);
				System.out.println(x);
				System.out.println("+");
				System.out.println(y);
			
			ObjectOutputStream oos = new ObjectOutputStream(connection.getOutputStream());
			
			
			
			
			
			
			oos.flush();
			
			
			
			
			ans = x + y;
			System.out.println("=");
			System.out.println(ans);
			ServerPacket sp = new ServerPacket("jpbinder",ans);
			
			oos.writeObject(sp);
			
		}
		}
	
		catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}//
		

	}
}
