

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.lang.String;

public class Java2Client
{

	public static void main( String[] args )
	{		
		System.out.print("Enter two integers:");
		Scanner user_in = new Scanner(System.in );
		int num1 = user_in.nextInt();
		int num2 = user_in.nextInt();
		user_in.close();
		
		Socket connection;
		System.out.print("Starting client\n");
		try {
			connection = new Socket("localhost", 5000);	
			if (connection.isConnected())
				System.out.print("Socket found.\n");
			
			DataOutputStream output = new DataOutputStream(connection.getOutputStream());
			DataInputStream input = new DataInputStream(connection.getInputStream());
			
			output.writeInt(num1);
			output.writeInt(num2);	
			System.out.print("Info sent.\n");
			
			System.out.print("Input stream acquired.\n");
			int answer = input.readInt();
			
			System.out.print(num1 + " + " + num2 + " = " + answer);
			
			output.close();
			input.close();
			
			connection.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String address = "127.0.0.1";
		//Socket connection = new Socket(address, 5000);
				
		//String text = new String(InetAddress.getLocalHost().getHostAddress());
		
	
		
	}
}