import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Java3Server {

	public static void main( String[] args )
	{
		
		ServerSocket server;
		System.out.print("Starting server.\n");
		try {
			server = new ServerSocket(5555, 1);
			Socket connection = server.accept();

			DataInputStream input = new DataInputStream(connection.getInputStream());
			DataOutputStream output = new DataOutputStream(connection.getOutputStream());
			
			System.out.print("Reading...\n");
			byte[] byte_array = new byte[8];
			input.read(byte_array);
			
			int num1 = ((int)byte_array[3]&0xFF) + (((int)byte_array[2]&0xFF)<<8) + (((int)byte_array[1]&0xFF)<<16) + (((int)byte_array[0]&0xFF)<<24);
			int num2 = ((int)byte_array[7]&0xFF) + (((int)byte_array[6]&0xFF)<<8) + (((int)byte_array[5]&0xFF)<<16) + (((int)byte_array[4]&0xFF)<<24);

			System.out.print("Received: " + num1 + ", " + num2 + "\n");
			
			System.out.print("Returning sum...\n");
			output.writeInt(num1+num2);
			
			input.close();
			output.close();
			
			connection.close();
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
