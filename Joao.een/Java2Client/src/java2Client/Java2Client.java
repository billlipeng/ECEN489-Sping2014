package java2Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Java2Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner inputClient = new Scanner(System.in);
		int a ,
	    b ,
	    c ,
	    port = 555;
	    String address;
	    
	    
	    System.out.printf("Enter the server address: ");
	    address = inputClient.nextLine();
	    System.out.printf("Enter first number: ");
	    a = inputClient.nextInt();
	    System.out.printf("Enter second number: ");
	    b = inputClient.nextInt();
	try
	{
			System.out.println("Connecting with server...");
			Socket connection = new Socket( InetAddress.getByName( address ), port);
			System.out.println("Connection successful.");
			ObjectOutputStream output =	new ObjectOutputStream( connection.getOutputStream() );
			output.flush();
			ObjectInputStream input =  new ObjectInputStream( connection.getInputStream() );
			try
			{
				output.writeObject(a);
				output.flush();
				output.writeObject(b);
				output.flush();
				c = (Integer) input.readObject();
				System.out.printf("%d + %d = %d\n" ,a,b,c);
			}
			catch (ClassNotFoundException classNotFoundException)
			{
				System.out.println("Transmission error.");
			}
			input.close();
			output.close();
			connection.close();
			inputClient.close();
	}
	catch( IOException ioException )
	{
		ioException.printStackTrace();
	}
  }
}
