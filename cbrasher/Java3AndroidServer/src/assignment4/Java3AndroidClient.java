package assignment4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.*;

public class Java3AndroidClient {
	

	public static void main(String[] args) { 
		
		 Random random = new Random();
		 int a = random.nextInt(9);
		 int b = random.nextInt(9);
		
		int sum1;
		System.out.print(a+" "+b+"\n");
	 System.out.print( "Enter sum: " );
		Scanner falcon = new Scanner( System.in );
		sum1 = falcon.nextInt();
		
		falcon.close();
		
		
		Socket connection;
		System.out.print("Connecting to the Swarm.\n");
		try {
			
			connection = new Socket("localhost", 5559);	
			if (connection.isConnected())
				System.out.print("Hive Mind Located: Awaiting to send orders\n");

			DataOutputStream output = new DataOutputStream(connection.getOutputStream());
			DataInputStream input = new DataInputStream(connection.getInputStream());
			
			 ByteBuffer integers = ByteBuffer.allocate(8).putInt(a).putInt(b);
			 byte[] byte_int = integers.array();
			
			output.write(byte_int);
			
			int total = input.read();
			
			if ( total == sum1 )
				System.out.printf("That is correct\n"+"%d == %d\n", total, sum1 );
			else {
				System.out.print( "Incorrect, please try again\n" );
			
			output.close();
			
			connection.close();
			}
		}
		 catch (UnknownHostException e) {

				e.printStackTrace();
			 }
			 catch (IOException e) {
				 
					e.printStackTrace();
			}
		
	}

}
