package javaserver;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.zpartal.commpackets.ClientPacket;
import com.zpartal.commpackets.ServerPacket;

public class Server {
		
		private ObjectOutputStream output; // Output stream
		private ObjectInputStream input; // Output stream
		private ServerSocket server; // Server Socket
		private Socket connection; // Connection to Client
		private int count = 1; // Number of connections
		private ClientPacket cp;
		private ServerPacket sp;
		private int a;
		private int b;
		private int c;
		
	public void runServer()
	{
		try
		{
			server = new ServerSocket(5555, 10);
			while(true)
			{
				try
				{
					waitConnection();
					createStreams();
					process();
				} // End of try
				catch (EOFException eofException)
				{
					System.out.println( "\nServer terminated connection" );
				} // end catch
				finally
				{
					closeConnection(); //Close connection
					++count;
				} // end finally
			}// end while
		}// end of try
		catch(IOException ioException)
		{
			ioException.printStackTrace();
		}// end catch
	}// end of runServer
	
	// Method for waiting for connections with the client
	public void waitConnection() throws IOException
	{
			System.out.println("Waiting for connection"); 
			connection = server.accept(); // wait for the client connection
			// Shows the number of the connection and the ip address of the client
			System.out.println("Connection " + count + " received from:" + connection.getInetAddress().getHostName());
	} // End of method waitConnection
	
	public void createStreams() throws IOException
	{
		// Creating the output stream variable
		output =	new ObjectOutputStream( connection.getOutputStream() );
		output.flush(); // flushing the buffer to send header information
		// Creating the input stream variable
		input = new ObjectInputStream(connection.getInputStream()); 
	} // End createStreams
	
	public void process() throws IOException
	{
		while(true){
		// buffer will store the values coming from the client
		cp =  new ClientPacket("client", 0, 0);
		try
		{
		// Receiving data
		cp = (ClientPacket) input.readObject();
		a = cp.getNum1();
		b = cp.getNum2();
		// Calculating response
		c = a + b;
		// Sending response back
		sp = new ServerPacket("Joao Server", c);
		output.writeObject(sp);
		output.flush();
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Class not found.");
		}
		}
	} // End of process method
	
	
	public void closeConnection()
	{
		System.out.println("Finishing connection");
		try
		{
			output.close();
			input.close();
			connection.close();
		} // end try
		catch(IOException ioException)
		{
			ioException.printStackTrace();
		} // end catch
	}// end closeConnection;
}
