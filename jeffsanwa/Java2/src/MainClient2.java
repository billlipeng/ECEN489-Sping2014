import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class MainClient2 {
	public static String IP_ADDRESS = "192.168.169.132"; //just go to the loopback
	public static int port = 3333; //this needs to match the port of the server
	public static String fromServer = "";
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			
			
			    Socket clientSocket = new Socket(IP_ADDRESS, port);
			    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			    
			  fromServer = in.readLine();
			  System.out.println(fromServer);
			  
			  while(true){
				  System.out.println("Type command to server");


				  Scanner consoleIn = new Scanner(System.in);//could be dangerous, but we'll leave it
				  String s = consoleIn.next();
				  System.out.println(s);
				  out.println(s);

				  String temp = in.readLine();
				  System.out.println(temp);
				  if(temp.contains("Bye")){
					  consoleIn.close();
					  clientSocket.close();
					  break;
				  }
			  }
			    
			    
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
