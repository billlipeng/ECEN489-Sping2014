package assignment2;

import java.net.*;
import java.io.*;
import java.util.*;

public class Java2Server 
{
public static void main(String[] args) {

	try
	{
		ServerSocket serverSocket = new ServerSocket(4444);
		Socket clientSocket = serverSocket.accept();

		BufferedReader fromClient = new BufferedReader(
                	new InputStreamReader(clientSocket.getInputStream()));
        	PrintWriter toClient = 
			new PrintWriter(clientSocket.getOutputStream(), true);

		while (true) 
		{
			Integer numb1 =  new Integer(fromClient.readLine());
			Integer numb2 =  new Integer(fromClient.readLine());
			System.out.println("received: " + numb1.intValue() + numb2.intValue());
			toClient.println("The Sum is: "+numb1.intValue()+numb2.intValue());
		}
	}
	catch(IOException ex)
	{
		System.err.println(ex);
	}

    }
}