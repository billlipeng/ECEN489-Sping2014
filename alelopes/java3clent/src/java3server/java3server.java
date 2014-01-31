package java3server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class java3server extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField enterField; // inputs message from user
    private JTextArea displayArea; // display information to user
    private DataOutputStream output; // output stream to client
    private DataInputStream input; // input stream from client	
	private ServerSocket server; // server socket
	private Socket connection; // connection to client
	int a,b;
	
	public java3server()
	 {
	 super( "java3server" );
	
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
			server = new ServerSocket( 5555, 100 ); // create ServerSocket
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
		
		output = new DataOutputStream( connection.getOutputStream() );
		output.flush();
	    input = new DataInputStream( connection.getInputStream() );
	} // end method getStreams
	
	
	private void processConnection() throws IOException
	{
		//String message = "Connection successful";
		setTextFieldEditable( true );
		boolean i=true;
		do // process messages sent from client
		{
			try {
				byte [] getfromuser = new byte[8];
				input.read(getfromuser);
				displayMessage("\nValues:");
				int Var1,Var2;
				Var1=getfromuser[3] & 0xFF |
			            (getfromuser[2] & 0xFF) << 8 |
			            (getfromuser[1] & 0xFF) << 16 |
			            (getfromuser[0] & 0xFF) << 24;
				Var2=getfromuser[7] & 0xFF |
			            (getfromuser[6] & 0xFF) << 8 |
			            (getfromuser[5] & 0xFF) << 16 |
			            (getfromuser[4] & 0xFF) << 24;
								
				displayMessage(Integer.toString(Var1));
				displayMessage(" and ");
				displayMessage(Integer.toString(Var2));
				int Calculate;
				Calculate=Var1+Var2;
				//System.out.printf("\nVALOR %d", Calculate);

				sendData (Integer.toString(Calculate));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
		} while (i);
		
	} // end method processConnection
	
	
	
	private void closeConnection()
	{
		displayMessage( "\nTerminating connection\n" );
		setTextFieldEditable( false ); // disable enterField
		try
		{
	//		output.close(); // close output stream
	//		input.close(); // close input stream
			connection.close(); // close socket
		}
		catch ( IOException ioException )
		{
			ioException.printStackTrace();
		} // end catch
	} // end method closeConnection
	
	
	
	
	
	private void sendData( String message )
	{
		//System.out.printf("Da string %s", message);

		try // send object to client
		{
			a= Integer.parseInt(message);							
		/*	ByteBuffer buf = ByteBuffer.allocate(4);
			buf.putInt(a);
			byte [] firstinteger = buf.array();
			b= 0;
			ByteBuffer buf2 = ByteBuffer.allocate(4);
			buf2.putInt(b);
			byte [] secondinteger = buf2.array();
			ByteBuffer bufend = ByteBuffer.allocate(firstinteger.length+secondinteger.length);
			bufend.put(firstinteger);
			bufend.put(secondinteger);
			byte [] buffinal = bufend.array();
		*/	output.writeInt(a);
			output.flush();
			
	//		output.writeObject( "\n" + message );
	//		output.flush(); // flush output to client
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
