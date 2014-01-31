package java3client;



import java.io.EOFException;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;



public class java3client extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField enterField; // enters information from user
	private JTextArea displayArea; // display information to user
	private DataOutputStream output; // output stream to server
	private DataInputStream input; // input stream from server
	private String message = ""; // message from server
	private String chatServer; // host server for this application
	private Socket client; // socket to communicate with server
	int changeab=0;
	int a;
	int b;
	
	
	public java3client( String host )
	{
		super( "java3client" );
	
		chatServer = host; // set server to which this client connects
	
		enterField = new JTextField(); // create enterField
		enterField.setEditable( false );
		enterField.addActionListener(
		new ActionListener()
		{
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
	    setVisible( true ); // show window
	} // end Client constructor
	
	
	
	public void runClient()
	{
		try // connect to server, get streams, process connection
		{
			connectToServer(); // create a Socket to make connection
			getStreams(); // get the input and output streams
			processConnection(); // process connection
		} // end try
		catch ( EOFException eofException )
		{
			displayMessage( "\nClient terminated connection" );
		} // end catch
		catch ( IOException ioException )
		{
			ioException.printStackTrace();
		} // end catch
		finally
		{
			closeConnection(); // close connection
		} // end finally
	} // end method runClient
	// connect to server
	private void connectToServer() throws IOException
	{
//		displayMessage( "Attempting connection\n" );
		client = new Socket( InetAddress.getByName( chatServer ), 5555 );
	// create Socket to make connection to server
	displayMessage( "Connected to: " + client.getInetAddress().getHostName() );
	 } // end method connectToServer
	
	// get streams to send and receive data
	private void getStreams() throws IOException
	{
		displayMessage( "\nGoing...\n" );
		
		output = new DataOutputStream( client.getOutputStream() );
		output.flush();
	    input = new DataInputStream( client.getInputStream() );

	} // end method getStreams
	
	
	
	private void processConnection() throws IOException
	{
		setTextFieldEditable( true );
		do // process messages sent from server
		{
			try // read message and display it
			{
				//byte [] getfromuser = new byte[8];
				int a=input.readInt();		
				//System.out.printf("VALOR FINAL CHEGOU %d", getfromuser[3]);
				displayMessage("\nValue of Sum:");
				displayMessage(Integer.toString(a));
				
			
			} // end try
			catch ( Exception processpart )
			{
				displayMessage( "\nUnknown object type received" );
			} // end catch
		} while ( !message.equals( "SERVER>>> TERMINATE" ) );
	} // end method processConnection
	
	// close streams and socket
	private void closeConnection()
	{
		displayMessage( "\nClosing connection" );
		setTextFieldEditable( false ); // disable enterField
		try
		{
			output.close(); // close output stream
			input.close(); // close input stream
			client.close(); // close socket
		} // end try
		catch ( IOException ioException )
		{
			ioException.printStackTrace();
		} // end catch
	} // end method closeConnection

	
	private void sendData( String message )
	{
		try // send object to server
		{
			if (changeab==0){
				a= Integer.parseInt(message);
				changeab=1;
				}
				else if (changeab==1){
					
				ByteBuffer buf = ByteBuffer.allocate(4);
				buf.putInt(a);
				byte [] firstinteger = buf.array();
				b=Integer.parseInt(message);
				ByteBuffer buf2 = ByteBuffer.allocate(4);
				buf2.putInt(b);
				changeab=0;
				byte [] secondinteger = buf2.array();
				ByteBuffer bufend = ByteBuffer.allocate(firstinteger.length+secondinteger.length);
				bufend.put(firstinteger);
				bufend.put(secondinteger);
				byte [] buffinal = bufend.array();
				output.write(buffinal);
				output.flush();
				}
		} // end try
		catch ( IOException ioException )
		{
			displayArea.append( "\nError writing object" );
		} // end catch
	} // end method sendData
	
	// manipulates displayArea in the event-dispatch thread
	private void displayMessage( final String messageToDisplay )
	{
		SwingUtilities.invokeLater(
		new Runnable()
		{
			public void run() // updates displayArea
			{
				displayArea.append( messageToDisplay );
			} // end method run
		} // end anonymous inner class
	    ); // end call to SwingUtilities.invokeLater
	} // end method displayMessage
	
	// manipulates enterField in the event-dispatch thread
	private void setTextFieldEditable( final boolean editable )
	{
		SwingUtilities.invokeLater(
		new Runnable()
		{
			public void run() // sets enterField's editability
			{
				enterField.setEditable( editable );
			} // end method run
		} // end anonymous inner class
		); // end call to SwingUtilities.invokeLater
	} // end method setTextFieldEditable
		
	
	
		
}

