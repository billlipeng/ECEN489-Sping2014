package assignment4;

import java.net.*;
import java.io.*;
import java.lang.*;

public class Java3AndroidServer {
		
public static void main(String[] args) {
	
	
	ServerSocket server;
	
	try{
		server = new ServerSocket(5559);
		Socket clientSocket = server.accept();
		
		DataInputStream input = 
				new DataInputStream(clientSocket.getInputStream());
		DataOutputStream output = 
    			new DataOutputStream(clientSocket.getOutputStream()); 
		
		System.out.print("The Swarm is looking for the holy port 5555\n");
		while (true) 
			{
			byte[] byte_int = new byte[8];
			input.read(byte_int);
			int a =  ((int)byte_int[3]&0xFF) + (((int)byte_int[2]&0xFF)<<8) + (((int)byte_int[1]&0xFF)<<16) + (((int)byte_int[0]&0xFF)<<24);
			int b =  ((int)byte_int[7]&0xFF) + (((int)byte_int[6]&0xFF)<<8) + (((int)byte_int[5]&0xFF)<<16) + (((int)byte_int[4]&0xFF)<<24);
			int sum = a+b;
			System.out.println("The following numbers have been revecieved by the server\n: " + a+ " " +b+"\n");
			System.out.println("The Great Hive Mind has found the answer.\n");
			output.writeInt(a+b);
			
			input.close();
			output.close();
			server.close();
			}
		}
		catch (IOException e){
			System.out.println(e);
							}

	}
}
