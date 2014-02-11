import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {
	public static void main(String []args){
		String id = "Server: ";
		try{
			byte[] bytes = new byte[8];
			int num1 = 0;
			int num2 = 0;
			int sum = 0;
			
			System.out.println(id + "initializing...");
			ServerSocket server = new ServerSocket(5555, 1);
			System.out.println(id + "waiting...");
			Socket connection = server.accept();
			System.out.println(id + "connected!");
			
			DataInputStream input = new DataInputStream(connection.getInputStream());
			System.out.println(id + "reading data...");
			input.read(bytes);
			
			System.out.println(id + "data: " + Arrays.toString(bytes));
			
			num1 += (int) (bytes[0]&0xFF << 24);
			num1 += (int) (bytes[1]&0xFF << 16);
			num1 += (int) (bytes[2]&0xFF <<  8);
			num1 += (int) (bytes[3]&0xFF);
			System.out.println(id + "num1: " + num1);
			
			num2 += (int) (bytes[4]&0xFF << 24);
			num2 += (int) (bytes[5]&0xFF << 16);
			num2 += (int) (bytes[6]&0xFF <<  8);
			num2 += (int) (bytes[7]&0xFF);
			System.out.println(id + "num2: " + num2);
			
			sum = num1 + num2;
			System.out.println(id + "sum: " + sum);
			
			DataOutputStream output = new DataOutputStream(connection.getOutputStream());
			output.writeInt(sum);
			output.flush();
			
			input.close();
			output.close();
			connection.close();
			server.close();
		}catch(IOException e){
			System.out.println(id + "IOException");
		}
	}
}
