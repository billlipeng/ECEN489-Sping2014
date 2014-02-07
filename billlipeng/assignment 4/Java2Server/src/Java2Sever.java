import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;  
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;  
import java.net.Socket;  
public class Java2Sever 
{	
	public Java2Sever() throws IOException 
	{  
		try
		 {
			 ServerSocket server = new ServerSocket(5555);
			 System.out.println("Server established...\nPort number: 5555");

			 while(true){
				 //waiting for connection
				 Socket connection = server.accept();
				 System.out.println("New socket established...");

				 
				 //manage I/O streams
				 OutputStream os = connection.getOutputStream();
				 DataOutputStream dos = new DataOutputStream(os);
				 InputStream is = connection.getInputStream();
				 DataInputStream dis = new DataInputStream(is);
				 
				 //perform processing
				 byte[] bs = new byte[8];
				 dis.read(bs);
				 int a = ((bs[0]&0xFF)<<24) + ((bs[1]&0xFF)<<16) + ((bs[2]&0xFF)<<8) + (bs[3]&0xFF);
				 int b = ((bs[4]&0xFF)<<24) + ((bs[5]&0xFF)<<16) + ((bs[6]&0xFF)<<8) + (bs[7]&0xFF);
				 
				 int sum = a+b;
				 System.out.println("Server: a="+a+" b="+b + "sum=" + sum);

				 //write back to data output stream
				 dos.writeInt(sum);
				 
				 //close connection
				 connection.close();
			 }
			 
		 }catch(Exception e)
		 {
			 System.out.println("Error in Java2Server()");
			 e.printStackTrace();
		 }
	}  
	  
	public static void main(String[] args) 
	{  
	try {  
	    	new Java2Sever();  
	    }catch (IOException e) 
	    {  
	    	e.printStackTrace();  
	    }  
	}  
	 
}
