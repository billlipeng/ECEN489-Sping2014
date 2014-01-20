import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String []args){
		try{
			System.out.println("Server initializing...");
			ServerSocket server = new ServerSocket(2015, 1);
			System.out.println("Server waiting...");
			Socket connection = server.accept();
			System.out.println("Server connected!");
			
			ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
			int num1 = input.readInt();
			int num2 = input.readInt();
			
			ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
			output.writeInt(num1 + num2);
			output.flush();
			
			input.close();
			output.close();
			server.close();
		}catch(IOException e){
			System.out.println("IOException");
		}
	}
}
