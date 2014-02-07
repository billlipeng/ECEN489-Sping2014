package assignment4;

import java.net.*;
import java.nio.*;
import java.util.Scanner;
import java.io.*;
import java.lang.*;


public class Java3Server {

	public static void main(String[] args) {
		
		System.out.println("Awakening Hive Mind\n");
		
		ServerSocket server = null;
		int port = 5555;
		Scanner user = new Scanner(System.in);
		
		
		try{
			
			server = new ServerSocket(port);
			System.out.print("The Swarm has found the port\n");
			Socket clientSocket = server.accept();
			
			DataInputStream input = 
					new DataInputStream(clientSocket.getInputStream());
			DataOutputStream output = 
	    			new DataOutputStream(clientSocket.getOutputStream()); 
			
				byte[] num = new byte[8];
				input.read(num);
				int a = ByteBuffer.wrap(num).getInt();
				int b = ByteBuffer.wrap(num, 4, 4).getInt();
				int sum = a+b;
				System.out.println("The following numbers have been revecieved by the server\n: " + a+ " " +b+"\n");
				System.out.println("The Great Hive Mind has found the answer.\n");
				output.writeInt(sum);
				
				input.close();
				output.close();
				server.close();
		}
			catch (IOException e){
				System.out.println(e);
								}
		
	}
}
