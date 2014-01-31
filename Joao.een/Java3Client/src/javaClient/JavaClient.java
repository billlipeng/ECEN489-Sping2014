package javaClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.Scanner;

public class JavaClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner inputClient = new Scanner(System.in);
		int a ,
	    b ,
	    c ,
	    guess,
	    port = 5555;
	    String address;
	    
	    System.out.printf("Enter the server address: ");
	    address = inputClient.nextLine();
	    
	    Random r = new Random();
	    a = 0 + r.nextInt(101);
	    b = 0 + r.nextInt(101);
	    
	    System.out.printf("What is the result of " + a + "+" + b + "?");
	    guess = inputClient.nextInt();
	try
	{
			System.out.println("Connecting with server...");
			Socket connection = new Socket( InetAddress.getByName( address ), port);
			System.out.println("Connection successful.");
			// Creating stream Objects
			DataOutputStream output =	new DataOutputStream( connection.getOutputStream() );
			output.flush();
			DataInputStream input =  new DataInputStream( connection.getInputStream() );
			// Setting integer to a byte array of 8 positions
			byte [] buffer_out = new byte[] {
					(byte)(b >>> 54),
		            (byte)(b >>> 48),
		            (byte)(b >>> 40),
		            (byte)(b >>> 32),
					(byte)(a >>> 24),
		            (byte)(a >>> 16),
		            (byte)(a >>> 8),
		            (byte)a};
			output.write(buffer_out);
			output.flush();
			// Receiving answer
			byte[] buffer_in = new byte[4];
			input.read(buffer_in);
			c = ByteBuffer.wrap(buffer_in).getInt();
			System.out.printf("%d + %d = %d\n" ,a,b,c);
			if ( c == guess)
				System.out.printf("Correct Answer!!");
			else
				System.out.printf("Wrong Answer! :(");
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
