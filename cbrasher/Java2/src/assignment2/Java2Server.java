package assignment2;


import java.net.*;
import java.io.*;

public class Java2Server 
{
public static void main(String[] args) 
	{

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
			int sum;
			Integer numb1 =  new Integer(fromClient.readLine());
			Integer numb2 =  new Integer(fromClient.readLine());
			System.out.println("The following numbers have been revecieved by the server\n: " + numb1.intValue()+ " " +numb2.intValue());
			sum = numb1 + numb2;
			toClient.println("Skynet has calculated the sum to be: " + sum);
			serverSocket.close();
			}
		}
		catch(IOException ex)
			{
			System.err.println(ex);
			}
	}
}