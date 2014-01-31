package javaserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.*;

public class Server {
		
		private DataOutputStream output; // Output stream
		private DataInputStream input; // Output stream
		private ServerSocket server; // Server Socket
		private Socket connection; // Connection to Client
		private int count = 1; // Number of connections
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
		output =	new DataOutputStream( connection.getOutputStream() );
		output.flush(); // flushing the buffer to send header information
		// Creating the input stream variable
		input = new DataInputStream(connection.getInputStream()); 
	} // End createStreams
	
	public void process() throws IOException
	{
		// buffer will store the values coming from the client
		byte [] buffer_in = new byte[8];
		
		// Receiving data
		input.read(buffer_in);
		a = ByteBuffer.wrap(buffer_in).getInt();
		b = ByteBuffer.wrap(buffer_in, 4, 4).getInt();
		// Calculating response
		c = a + b;
		// Sending response back
		byte [] buffer_out = new byte []
				{ 
				(byte)(c >>> 24),
	            (byte)(c >>> 16),
	            (byte)(c >>> 8),
	            (byte)c
	            };
		output.write(buffer_out);		
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
