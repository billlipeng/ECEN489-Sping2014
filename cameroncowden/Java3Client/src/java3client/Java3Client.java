package java3client;

import java.util.*;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class Java3Client {

	public static void main(String[] args) throws IOException {
		
		 System.out.println("Welcome to the adding machine! Enter 'exit' at any time to end program.");
         //Declaring scanner
         Scanner userinput = new Scanner(System.in);
         //Search for IP's
        // System.out.println("Searching for IP's..");
         //checkHosts("192.168.0");
                     
         //Configuration
         String ipaddress;
         int port;
         System.out.println("Please enter IP address of server.");
         ipaddress = userinput.nextLine();
         System.out.println("Please enter port of server.");
         port = userinput.nextInt();
         System.out.println("Attempting connection to server.");
         try{
                 Socket connection = new Socket(ipaddress,port);
                 System.out.println("Socket created ...");
                 DataOutputStream sender = new DataOutputStream(connection.getOutputStream());
                 DataInputStream receiver = new DataInputStream(connection.getInputStream());
                 System.out.println("IO Streams established ...");
         
                 int[] values;
                 values = new int[2];
         
	         //Declaring client loop for continuous operation	         
	         while (userinput.nextLine() != "exit"){
	                 System.out.println("Welcome to the adding quiz!");
	                 System.out.println("Here is your question:");
	                 Random R = new Random(); //Creating random number generator.
	                 values[0] = R.nextInt(101); //Finding first random number between 0 and 100.
	                 values[1] = R.nextInt(101); //Finding second random number between 0 and 100.
	                 System.out.println("What is the sum of " + values[0] + " and " + values[1] + "?");
	                 int UserAnswer = userinput.nextInt();
	                 System.out.println("Asking the server for the correct answer...");
	                 
	                 //convert int array into byte array
	                 //Taken from: 
	                 //http://stackoverflow.com/questions/1936857/convert-integer-into-byte-array-java
	                 //ByteBuffer buff = ByteBuffer.allocate(8);
	                 //b.order(ByteOrder.BIG_ENDIAN);	                          
	                 //buff.putInt(1,values[0]).putInt(2,values[1]);
	                 //byte[] result = buff.array(); 	                
	                 //System.out.println(result);
	                 
	                 //Convert int array into byte array
	                 byte[] buff = new byte[8];
	                 buff[7] = (byte) (values[1]);
	                 buff[6] = (byte) (values[1]>>8);
	                 buff[5] = (byte) (values[1]>>16);
	                 buff[4] = (byte) (values[1]>>24);
	                 buff[3] = (byte) (values[0]);
	                 buff[2] = (byte) (values[0]>>8);
	                 buff[1] = (byte) (values[0]>>16);
	                 buff[0] = (byte) (values[0]>>24);
	                 //send byte array to server.
	                 
	                 //sender.write(result[0]);
	                 //sender.write(result[1]);
	                 sender.write(buff);
	                 System.out.println(Arrays.toString(buff));
	                 //sender.writeInt(values[0]); //Send first int to the server.
	                 //sender.writeInt(values[1]); //Send second int to the server.
	                 
	                 //sender.write(values);
	                 //listen for response
	                 int correctanswer = receiver.readInt();             
	                 System.out.println("Correct answer received from the server: " + correctanswer);
	                 //checking if user was correct
	                 if (UserAnswer == correctanswer)
	                         System.out.println("You got it correct!");
	                 else
	                         System.out.println("Unfortunately you're wrong!");
	         }
	         	connection.close(); //Close the connection.
         }
         catch (IOException e){
                 System.err.println("Failed to reach server.");
                 System.exit(1);
         }
         userinput.close(); //Tidying things up
         
 System.exit(0);


	}

	
	public static void checkHosts(String subnet) throws IOException{
		   int timeout=1000;
		   for (int i=1;i<254;i++){
		       String host=subnet + "." + i;
		       if (InetAddress.getByName(host).isReachable(timeout)){
		           System.out.println(host + " is reachable");
		       }
		   }
		}
}
