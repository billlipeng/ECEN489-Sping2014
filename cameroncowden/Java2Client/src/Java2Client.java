import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.Random;
public class Java2Client {

	public static void main(String[] args) throws IOException {
		
		Random R = new Random(); //Creating random number generator.
		int a = R.nextInt(101); //Finding first random number between 0 and 100.
		int b = R.nextInt(101); //Finding second random number between 0 and 100.
		System.out.println("Welcome to the adding quiz!");
		System.out.println("What is the sum of " + a + " and " + b + "?");
		
			//Sending the integers to the server//
		try {
		//Declaring socket to use:
		Socket connection = new Socket("localhost", 8080);
		System.out.println("Socket created.");
		//Attempting to send ints to server.
		
	        DataOutputStream sender = new DataOutputStream(connection.getOutputStream());
	        sender.writeInt(a); //Send first int to the server.
	        sender.writeInt(b); //Send second int to the server.
	        
	    //Attempting to receive answer from server.
	        DataInputStream receiver = new DataInputStream(connection.getInputStream());
	        int correctanswer = receiver.readInt();	     
	        System.out.println("Correct answer received from the server!");
	  
	    //Prompting user for answer
	        System.out.println("What do you think it is??");
	        Scanner userinput = new Scanner(System.in);
			int UserAnswer = userinput.nextInt();
			
			if (UserAnswer == correctanswer)
				System.out.println("You got it correct!");
			else
				System.out.println("Unfortunately you're wrong!");
			 //Closing things up.
			userinput.close(); //Tidying things up
	        connection.close(); //Close the connection.
	        System.exit(0);
	        
		} catch (IOException e){
			System.err.println("Failed to reach server.");
			System.exit(1);
		}
	}

}
