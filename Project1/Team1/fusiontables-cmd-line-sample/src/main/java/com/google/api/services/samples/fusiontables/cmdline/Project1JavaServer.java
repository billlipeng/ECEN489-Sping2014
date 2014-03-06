package com.google.api.services.samples.fusiontables.cmdline;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Project1JavaServer {

	public static void main(String[] args) throws IOException{
		//Begin
		System.out.println("Server started..");
		//Configuration
		ServerSocket server = null;
		Socket connection = null;
		
		//Declaring scanner
		Scanner userinput = new Scanner(System.in);
		
		//Setting port
		System.out.println("Please enter desired port to listen on.");
		int portNumber = 0;
		portNumber = userinput.nextInt();
		userinput.close();
		//Operations
		
		try {	
			server = new ServerSocket(portNumber); 
			System.out.println("Waiting for a connection.");	
			}		
			catch (IOException e){
				System.err.println("Could not listen on port: " + portNumber + ".");
				System.exit(1);
			}
		while(true){
			try{
			connection = server.accept();
			System.out.println("A client has connected.");	
			}
					catch (IOException e) {
						server.close();
		                System.err.println("I/O error: " + e);
		            }
			//Data Do:
			new DataThreader(connection).start();
			 
			}
		//End / Cleanup
	}
}
