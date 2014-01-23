package assignment2;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class Java2Client extends Applet implements ActionListener
{
	TextField numbField;
	TextField numbField1;
	Label display;
	Label display1;
	Socket socket;
	  public void init() {

	        try {
	            socket = new Socket("localhost",4444); //establishing port number
	        } catch (UnknownHostException e) {
	            System.out.println("Unknown host"); //
	        } catch (IOException e) {
	            System.out.println("IO Exception");
	            return;
	        }

	        numbField = new TextField(6); //setting up the applet
	        add(numbField);
	        numbField1 = new TextField(6);
	        add(numbField1);

	        Button button = new Button("Send");
	        add(button);
	    button.addActionListener(this);
	        Button button1 = new Button("Send");
		button1.addActionListener(this);

		display = new Label("No Number");
		add(display);
		display1 = new Label("No Number");
		add(display1);

	        validate();
	    }

	    public void actionPerformed(ActionEvent e)
	    {
	        int numb1 = 0;
	        int numb2 = 0;
		String numbStr = null;
		String numbStr1 = null;
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
	            		numbStr1 = fromServer.readLine();
	        	} 
			catch (IOException ex) 
			{
	            		System.out.println("Applet receive failed:");
	        	}

	        	display.setText(numbStr);
	           }
	    }
	}
