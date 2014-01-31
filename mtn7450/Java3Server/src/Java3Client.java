import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.lang.String;

public class Java3Client
{

	public static void main( String[] args )
	{		
		int num1 = (int)Math.round(Math.random()*9);
		int num2 = (int)Math.round(Math.random()*9);
		
		Socket connection;
		System.out.print("Starting client\n");
		try {
			connection = new Socket("localhost", 5555);	
			if (connection.isConnected())
				System.out.print("Socket found.\n");
			
			DataOutputStream output = new DataOutputStream(connection.getOutputStream());
			DataInputStream input = new DataInputStream(connection.getInputStream());
			
			ByteBuffer buffer =  ByteBuffer.allocate(8).putInt(num1);
			buffer.putInt(num2);
			byte[] byte_array = buffer.array();	
			
			output.write(byte_array);
			System.out.print("Info sent.\n");
			
			System.out.print("Input stream acquired.\n");
			int answer = input.readInt();
			
			System.out.print(num1 + " + " + num2 + " = " + answer);
			
			output.close();
			input.close();
			
			connection.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String address = "127.0.0.1";
		//Socket connection = new Socket(address, 5000);
				
		//String text = new String(InetAddress.getLocalHost().getHostAddress());
		
	
		
	}
}