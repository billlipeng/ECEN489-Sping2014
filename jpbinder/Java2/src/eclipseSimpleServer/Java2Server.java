package eclipseSimpleServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Java2Server {
	public static void main(String[] args) { try {
		int x = 0;
		int y = 0;
		
		int ans;
		ServerSocket server;
		server = new ServerSocket(5557,1);
		while(true) {
			
			Socket connection = server.accept();
			
			DataInputStream dis = new DataInputStream(connection.getInputStream());
			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
			
			x = dis.readInt();
			y = dis.readInt();
			System.out.println("Received");

			System.out.println(x);
			System.out.println(y);
					
			
			ans = x+y;
			
			dos.writeInt(ans);
			
		}
		}
		catch ( IOException e ) {
			
			e.printStackTrace();
		}
		
		

		
	}
}