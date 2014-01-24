package eclipseSimpleServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;



public class Java2Client {
	
	
	
	public static void main(String[] args)  { try {
		
		Socket connection = new Socket(InetAddress.getByName(null),5557);
		
		// scan the input prompt
		
		System.out.println("Enter two integers you wish to sum.");
		Scanner input = new Scanner(System.in);
		int x = input.nextInt();
		int y = input.nextInt();
		input.close();
		
		
		//create an outstream

		
		DataInputStream dis = new DataInputStream(connection.getInputStream());
		DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
		
		System.out.println("Sending");
		dos.writeInt(x);
		dos.writeInt(y);
		System.out.println("Sent");
		
		int ans = dis.readInt();
		System.out.println(ans);
		
		connection.close();
		
	}
	
	catch ( IOException e ) {
		
		e.printStackTrace();
	}
	}
}