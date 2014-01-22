package Java2Server;


import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;




import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class Java2Server extends JFrame {
	
	private JTextField enterField; // inputs message from user
    private JTextArea displayArea; // display information to user
    private ObjectOutputStream output; // output stream to client
    private ObjectInputStream input; // input stream from client	
	private int counter = 1; // counter of number of connections
	private ServerSocket server; // server socket
	private Socket connection; // connection to client
	
	
	public Java2Server()
	 {
	 super( "Java2Server" );
	
	 enterField = new JTextField(); // create enterField
	 enterField.setEditable( false );
	 enterField.addActionListener(
	 new ActionListener()
	 {
	// send message to client
		 public void actionPerformed( ActionEvent event )
		 {
			 sendData( event.getActionCommand() );
			 enterField.setText( "" );
		 } // end method actionPerformed
	  } // end anonymous inner class
	  ); // end call to addActionListener
		
     add( enterField, BorderLayout.NORTH );
		
     displayArea = new JTextArea(); // create displayArea
   	 add( new JScrollPane( displayArea ), BorderLayout.CENTER );
   	 
   	 setSize( 300, 150 ); // set size of window
   	 setVisible(true);
	 } // end Server constructor
	
	public void runServer()
	{
		try // set up server to receive connections; process connections
		{
			server = new ServerSocket( 12345, 100 ); // create ServerSocket
			displayMessage( "\nServer Started" );
			while ( true )
			{
				try
				{
					waitForConnection(); // wait for a connection
					getStreams(); // get input & output streams
					processConnection(); // process connection
				} // end try
				catch ( EOFException eofException )
				{
				} // end catch
				finally
				{
					closeConnection(); // close connection
					++counter;
				} // end finally
			} // end while
		} // end try
		catch ( IOException ioException )
		{
			ioException.printStackTrace();
		} // end catch
	} // end method runServer
	
	private void waitForConnection() throws IOException
	{
		displayMessage( "\nWaiting for connection\n" );
		connection = server.accept(); // allow server to accept connection
	}
	
	private void getStreams() throws IOException
	{
		displayMessage( "\nGoing...\n" );
		output = new ObjectOutputStream( connection.getOutputStream() );
		sendData("What is the sum of 3+5?");
		output.flush();
		input = new ObjectInputStream( connection.getInputStream() );

	} // end method getStreams
	
	
	private void processConnection() throws IOException
	{
		String message = "Connection successful";
		setTextFieldEditable( true );
		
		do // process messages sent from client
		{
			try {
				message = ( String ) input.readObject();
				displayMessage( "\nA\n"+message);
				if (message.equals("CLIENT>>> 8")){
					sendData("Congrats! You are right\n");
					output.flush();					
				}
				else{
					sendData("Wrong! Go back to school or try again\n");
					output.flush();	
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} while ( !message.equals( "CLIENT>>> TERMINATE" ) );
		
	} // end method processConnection
	
	
	
	private void closeConnection()
	{
		displayMessage( "\nTerminating connection\n" );
		setTextFieldEditable( false ); // disable enterField
		try
		{
			output.close(); // close output stream
			input.close(); // close input stream
			connection.close(); // close socket
		}
		catch ( IOException ioException )
		{
			ioException.printStackTrace();
		} // end catch
	} // end method closeConnection
	
	
	
	
	
	private void sendData( String message )
	{
		try // send object to client
		{
			output.writeObject( "\n" + message );
			output.flush(); // flush output to client
		} // end try
		catch ( IOException ioException )
		{
			displayArea.append( "\nError writing object" );
		} // end catch
	} // end method sendData

	private void displayMessage( final String messageToDisplay )
	{
		SwingUtilities.invokeLater(
		new Runnable()
		{
			public void run() // updates displayArea
			{
				displayArea.append( messageToDisplay ); // append message
			} // end method run
		} // end anonymous inner class
		); // end call to SwingUtilities.invokeLater
	} // end method displayMessage
	
	
	
	
	private void setTextFieldEditable( final boolean editable )
	{
		SwingUtilities.invokeLater(
		new Runnable()
		{
			public void run() // sets enterField's editability
			{
				enterField.setEditable( editable );
			} // end method run
		} // end inner class
	); // end call to SwingUtilities.invokeLater
	}


}
