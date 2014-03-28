package project;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.example.bestteam.Conection;



public class ServerMain extends JFrame {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//SQLITE VARIABLES
	  private static Connection c;
	  private static String filepath = "C:\\Users\\Alexandre\\Downloads\\adt-bundle-windows-x86_64-20131030\\sdk\\tools\\";
	  private static String sql, sql2;
	  private static Statement query;
	  //WINDOW VARIABLES DONT CHANGE
	  private JTextField enterField; // inputs message from user
	  private JTextArea displayArea; // display information to user

	  //SERVER VARIABLES
	  private ObjectInputStream input; // input stream from client	
	  private ServerSocket server; // server socket
	  private Socket connection; // connection to client
	  
	  //MOST IMPORTANT VARIABLE
	  private ArrayList<Conection> received;
	  
		
	  public ServerMain(){ //FOR THE WINDOW DONT CHANGE
		     
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
						
	  }
	    private void sendData( String message )
    	{} // end method sendData

	  
	    
	    
	    
	    
	  public void SQLINSERT() { 
		  //METHOD TO INSERT INTO THE SQLITE
		  
			try{
				Class.forName("org.sqlite.JDBC");
				//Change the filepath, and .db to yours sqlite table name and filepath
				c = DriverManager.getConnection("jdbc:sqlite:"+filepath+"test.db");
			}
			catch(Exception e){
				e.printStackTrace();
				System.out.printf("chegou 1");
			}
		  	//this is the part to send to the SQLite. every 10 times, it will send the lat and
			//logitude values too.
		  	String RSSItoString;
		  	String longitudetoString;
		  	String LatitudetoString;
		  		
		  	System.out.printf("Array size %d\n",received.size());
		  	
				for (int interator=0;interator<received.size();interator++){	
					
					longitudetoString = Double.toString(received.get(interator).getLongitude());
					LatitudetoString=Double.toString(received.get(interator).getLatitude());
					RSSItoString=Double.toString(received.get(interator).getRIIS());
					
					
					//I DONT KNOW IF WE WILL MERGE THE XYZ gravity, linAcc, gyro, etc. They said yes, but didnt
					//write it on github. here, it will add a new Latitude and Longitude every 100 seconds (every 10 values) 
						sql =	"INSERT INTO tabletest(RSSI,latitude,longitude" +
				                ")VALUES ('"+RSSItoString+"','"+LatitudetoString+"','"+longitudetoString+"')";
					
					try{
						query = c.createStatement();

				         query.executeUpdate(sql);
				         query.close();

						}
					catch(SQLException el){el.printStackTrace();System.out.printf("chegou 2");}				
				}

				}
						
	  
	  
	  
	  
	  public void FUSIONTABLE(){

	  }
	  

	  
	  public void startServer(){

			try // set up server to receive connections; process connections
			{
	
				server = new ServerSocket(5555, 100); // create ServerSocket
				displayMessage( "\nServer Started" );
				
				while(true){
					try
					{

						displayMessage( "\nWaiting for connection\n" );
						connection = server.accept(); // allow server to accept connection
					    input = new ObjectInputStream( connection.getInputStream() );
							try {
	
								displayMessage( "\ntrying to get...\n" );
	
								received = (ArrayList<Conection>) input.readObject();
								displayMessage("\nGot");			
							}
							catch (Exception e) {
								
								displayMessage( "\nClient Closed\n" );
								input.close(); // close input stream
								break;
							}
							finally
							{

								input.close(); // close input stream
								connection.close(); // close socket	
								SQLINSERT();
								FUSIONTABLE();

							} // end finally
							  
					} // end try
					catch ( EOFException eofException )
					{		
					} // end catch

				}
				 // end while
			} // end try
			catch ( IOException ioException )
			{
				ioException.printStackTrace();
			} // end catch
			
			
	  }





	  private void displayMessage( final String messageToDisplay )
		//This is the display part. DONT CHANGE AGAIN.
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



}
