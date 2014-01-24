package assignment2;

import java.applet.Applet; //importing Applet
import java.awt.*; 
import java.awt.event.*;
import java.net.*; //importing for server
import java.io.*; //importing for server

public class Java2Client extends Applet implements ActionListener //So Applet can read values and send them
{
	TextField numbField; //Creating fields to insert integer
	TextField numbField1;
	Label display; //Creating label for sum value to return
	Socket socket; 
	  public void init() {
		  System.out.print( "Limit number entries to six integers.\n" );
		  System.out.print( "You may need to expand the applet after sending your integers.\nThis will reformat the applet and allow you to see your sum.\n");
		  System.out.print("You may resubmit numbers for new sums as long as the applet is open'\nHowever avoid opening mulitple isntances as this can cause an error.");
	        try {
	            socket = new Socket("localhost",4444); //establishing port number
	        } catch (UnknownHostException e) {
	            System.out.println("Unknown host"); //If host unknown catch exception
	        } catch (IOException e) {
	            System.out.println("IO Exception"); //For IO exception
	            return;
	        }

	        numbField = new TextField(6); //setting numbfield for 6 chars
	        add(numbField);
	        numbField1 = new TextField(6); 
	        add(numbField1);

	        Button button = new Button("Send"); //Creating Button to send data
	        add(button);
	    button.addActionListener(this);

		display = new Label("No Number"); //Initial Message to establish baseline
		add(display);
		

	        validate();
	    }

	    public void actionPerformed(ActionEvent e)
	    {
	        int numb1 = 0; //create int for user to place integers in
	        int numb2 = 0;
		String numbStr = null;
		BufferedReader fromServer = null;
		PrintWriter toServer = null;

	        String actionCommand = e.getActionCommand();
	        if (e.getSource() instanceof Button)
	            if (actionCommand.equals("Send"))
	            {
	       		try 
			{
	            		numb1 = Integer.parseInt(numbField.getText());
	            		numb2 = Integer.parseInt(numbField1.getText());
	        	} 
			catch (NumberFormatException ex) 
			{
	            		System.out.println("Number Format Exception");
	        	}

			try 
			{
				fromServer = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
				toServer = 
					new PrintWriter(socket.getOutputStream(), true);
			}
			catch (IOException ex) 
			{
	            		System.out.println("IO Exception");
	        	}
		
	        	toServer.println(numb1);
	        	toServer.println(numb2);

	        	try 
			{
	            		numbStr = fromServer.readLine();
	        	} 
			catch (IOException ex) 
			{
	            		System.out.println("The Applet recieved no info");
	        	}

	        	display.setText(numbStr);
	           }
	    }
	}