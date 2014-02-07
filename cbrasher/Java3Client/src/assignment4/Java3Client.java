package assignment4;

import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.*;

public class Java3Client {

public static void main(String[] args) throws UnknownHostException {
		
		
		Scanner user = new Scanner(System.in);
		
		int[] array;
		array = new int[2];

		int port;
		System.out.println("Enter the address of the Hive Mind: \n");
		String address = user.nextLine();
		System.out.println("Enter the port of the Hive Mind: \n");
		port = user.nextInt();
		
		System.out.print("Calling the Hive mind\n");
		try{
			Socket client = new Socket(address ,port); //establishing port number
			System.out.println("We hear the call of the Hive Mind\n");
			
			DataInputStream input = 
					new DataInputStream(client.getInputStream());
			DataOutputStream output = 
	    			new DataOutputStream(client.getOutputStream());

		do  {
			Random random = new Random();
			
			array[0]=random.nextInt(101);
			array[1]=random.nextInt(101);
			
			System.out.println("Sum the following:"+array[0]+"+"+array[1]);
			int answer = user.nextInt();
			
			byte[] num = new byte[8];
			num[7] = (byte) (array[1]);
			num[6] = (byte) (array[1]>>8);
			num[5] = (byte) (array[1]>>16);
			num[4] = (byte) (array[1]>>24);
			num[3] = (byte) (array[0]);
			num[2] = (byte) (array[0]>>8);
			num[1] = (byte) (array[0]>>16);
			num[0] = (byte) (array[0]>>24);
			
			output.write(num);
			
			int sum = input.readInt();
			
			if (sum == answer)
            System.out.println("Hivemind says correct");
			else{
            System.out.println("Hivemind is disappointed");
			}
		}
		while (user.nextLine() != "quit");
		
		if(user.nextLine() == "quit"){
			input.close();
			output.close();
			client.close();
									}
		}
         catch (UnknownHostException e) 
         	{
        	  InetAddress addr = InetAddress.getLocalHost();
             
              String ipAddress = addr.getHostAddress();
            
              System.out.println("IP address of localhost from Java Program: " + ipAddress);
            
              String hostname = addr.getHostName();
              System.out.println("Name of hostname : " + hostname);
            System.out.println("Unknown host"); //If host unknown catch exception
         	} 
         catch (IOException e) 
        	{
            System.out.println("IO Exception"); //For IO exception
            return;
			}
	}
}
