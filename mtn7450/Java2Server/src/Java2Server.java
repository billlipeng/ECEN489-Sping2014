import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Java2Server {

	public static void main( String[] args )
	{
		
		ServerSocket server;
		System.out.print("Starting server.\n");
		try {
			server = new ServerSocket(5000, 1);
			Socket connection = server.accept();

			DataInputStream input = new DataInputStream(connection.getInputStream());
			DataOutputStream output = new DataOutputStream(connection.getOutputStream());
			
			System.out.print("Reading...\n");
			int num1 = input.readInt();
			int num2 = input.readInt();
			System.out.print(num1);
			System.out.print(", " + num2);
			
			System.out.print("Returning sum...");
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
