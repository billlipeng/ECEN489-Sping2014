package client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.Scanner;



public class java3client {
	
	
	public static void main(String[] args)  { try {
		//----------------------
		// INPUT ADDRESS HERE:
		String address = "192.168.1.5";
		//----------------------
		
		Socket connection = new Socket(address,5555);
		
		// scan the input prompt
		
		// random number gen
				Random generator = new Random();
		// define the two numbers to be added (between 1 and 10)
				int x = generator.nextInt(10) + 1;
				int y = generator.nextInt(10) + 1;
		
		//create an outstream

		
		DataInputStream dis = new DataInputStream(connection.getInputStream());
		DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
		
		//put in array to send
		ByteBuffer bb = ByteBuffer.allocate(8); 
	    bb.putInt(x);
	    bb.putInt(y);
		byte[] b = bb.array();
	    
		System.out.println(x + " + " + y + "= ?");
		
		Scanner input = new Scanner(System.in);
		int ans = input.nextInt();
		input.close();
		
		System.out.println("Sending");
		dos.write(b);
		
		System.out.println("Sent");
		
		int correct = dis.readInt();
		System.out.println(ans);
		
		
		
		if (ans == correct) {
			System.out.println("CORRECT YOU WIN $$$$$");
		}
		else {
			System.out.println("YOU DIED");
		}
		
		connection.close();
		
	}
	
	catch ( IOException e ) {
		
		e.printStackTrace();
	}
	}
}