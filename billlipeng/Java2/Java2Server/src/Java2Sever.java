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
			 ServerSocket server = new ServerSocket(8887);
			 while(true){
				 //waiting for connection
				 Socket connection = server.accept();
				 
				 //manage I/O streams
				 OutputStream os = connection.getOutputStream();
				 DataOutputStream dos = new DataOutputStream(os);
				 InputStream is = connection.getInputStream();
				 DataInputStream dis = new DataInputStream(is);
				 
				 //perform processing
				 String str;
				 str = dis.readUTF();
				 int a =0, b = 0;
				 a = Integer.parseInt(str.substring(0,str.indexOf(" ")));
				 b = Integer.parseInt(str.substring(str.indexOf(" ")+1));
				 System.out.println("Server: a="+a+" b="+b);
				 int sum = a+b;
				 //write back to data output stream
				 dos.writeUTF(sum+"");
				 
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
