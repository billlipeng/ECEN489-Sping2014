package java2Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class Java2Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int a ,
		    b ,
		    c = 1,
		    port = 555,
		    clients = 2;
		try
		{
			ServerSocket server = new ServerSocket(port, clients);
			while(c !=0)
			{
				System.out.println("Waiting for connection");
				Socket connection = server.accept();
				System.out.println("Connection successful.");
				ObjectOutputStream output =	new ObjectOutputStream( connection.getOutputStream() );
				output.flush();
				ObjectInputStream input =  new ObjectInputStream( connection.getInputStream() );
				try
				{
					System.out.println("Test");
					a = (Integer) input.readObject();
					b = (Integer) input.readObject();
					System.out.println("Test");
					c = a + b;
					output.writeObject(c);
					output.flush();
				}
				catch (ClassNotFoundException classNotFoundException)
				{
					System.out.println("Transmission error.");
				}
				input.close();
				output.close();
				connection.close();
			}
			server.close();
		}
		catch( IOException ioException )
		{
			ioException.printStackTrace();
		}
	}

}
