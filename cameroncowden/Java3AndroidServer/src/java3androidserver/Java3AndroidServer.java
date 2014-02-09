package java3androidserver;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Java3AndroidServer {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Server started.."); 		 
        
        //Configuration        
        int portNumber = 5555;
		int queueLength = 10;
	
		//Declaring scanner
		Scanner userinput = new Scanner(System.in);
		String input = "";
		input = userinput.nextLine();
		while(input != "exit"){
		ServerSocket server = null;
		Socket connection = null;
		//Attempting to create socket and connection.
		
		try {
			
			server = new ServerSocket(portNumber, queueLength); 
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
			new Threader(connection).start();			
			}
				    		    
		    
		}
		System.out.println("Server closed.");	
		userinput.close();
}
}
			


