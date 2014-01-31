package java3server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.nio.*;

public class Java3Server {

	public static void main(String[] args) throws IOException {
		System.out.println("Server started.."); 
		
		//Declaring scanner
        Scanner userinput = new Scanner(System.in);
        //String input = "";
        //Configuration
        
		int portNumber, queueLength = 10;
		System.out.println("Please enter port number to listen on.");
		portNumber = userinput.nextInt();
	
		ServerSocket server = null;
		//Attempting to create socket and connection.
		try {
			
			server = new ServerSocket(portNumber, queueLength); 
			System.out.println("Waiting for a connection.");			
			
			//while(input != "exit"){
			Socket connection = server.accept();
			
			DataInputStream receiver = new DataInputStream(connection.getInputStream());
			DataOutputStream sender = new DataOutputStream(connection.getOutputStream());
			//Receiving values
			//int[] values;
			//values = new int[2];
			//for(int i = 0; i < 2; i++){
				//values[i] = receiver.read();				
			//}
			byte[] arriving = new byte[8];
			receiver.read(arriving);
			//int a = values[0];
			//int b = values[1];
			
			int a = ByteBuffer.wrap(arriving).getInt();
			int b = ByteBuffer.wrap(arriving, 4, 4).getInt();
		    System.out.println("I have received: " + a + " and " + b + "."); 
		    int c = a + b;    		   
		    System.out.println("Attempting to send client correct answer:" + c);
		    		   
		    sender.writeInt(c); //Sending correct answer to client.
		    System.out.println("Correct answer sent to user!");
		    System.out.println("Enter any key to continue, type exit to quit.");
		    //input = userinput.nextLine();
		   // if (input == "exit") connection.close();
			//}
		    
		    
		    System.exit(0);
		    userinput.close();
			}
		
	catch (IOException e){
		System.err.println("Could not listen on port: " + portNumber + ".");
		System.exit(1);
	}	

	}

}
