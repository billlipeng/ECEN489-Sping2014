import java.net.*;
import java.io.*;
import java.util.Scanner;

public class mysocketserver {
	
//	ServerSocket serversocket = null;
//	Socket socket = null;
//	DataInputStream is = null;
//	DataOutputStream os = null;
	
	public static void main(String[] args) {
		byte[] accept= new byte[8];
	 try {
		 ServerSocket serversocket = new ServerSocket(5555);
		 Socket socket = serversocket.accept();
		 try {
			 DataInputStream is = new DataInputStream(socket.getInputStream());
			 DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			 Scanner scanner = new Scanner(System.in);
			 while(true) {
				 
				 int NumberOfbytes = is.read(accept);
				 int num1 = ((accept[0]& 0xFF)<<24)+((accept[1]& 0xFF)<<16)+
						 ((accept[2]& 0xFF)<<8)+(accept[3]& 0xFF);
				 int num2 = ((accept[4]& 0xFF)<<24) +((accept[5]& 0xFF)<<16)+
						 ((accept[6]& 0xFF)<<8)+(accept[7]& 0xFF);
				 System.out.println("You got two numbers: "+num1+" and "+num2);
				 int sum = num1+num2;
			
				 System.out.println("Their sum is:"+ sum);
				 System.out.println("The result is now sending to the client.......");
//				 byte[] send = new byte[4];
//				 send[3]= (byte) (sum);
//				 send[2]= (byte) ((sum)>>8);
//				 send[1]= (byte) ((sum)>>16);
//				 send[0]= (byte) ((sum)>>24);
//				 os.write(send);
				 
				 os.writeInt(sum);
				 
//				 System.out.println(accept);
//				 String send = scanner.nextLine();
//				 System.out.println("Server:" + send);
//				 os.writeUTF("Server:"+ send);
			 }
		 }finally  {
			 socket.close();
		 }
		
	}catch (IOException e){
		e.printStackTrace();
	}
		
		
		
		
	}
}		