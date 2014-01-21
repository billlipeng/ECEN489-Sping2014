
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;



public class Java2Server {
	public static void main(String[] args) throws IOException {
		
			int portNumber = 8080;
			int queueLength = 10;
		
		ServerSocket server = null;
		try {
		server = new ServerSocket(portNumber, queueLength); 
		Socket connection = server.accept();
		
		
		DataInputStream receiver = new DataInputStream(connection.getInputStream());
		int a = receiver.readInt();
		int b = receiver.readInt();
        System.out.println("I have received: " + a + " and " + b + ".");        
        int c = a + b;
        System.out.println("Attempting to send client correct answer:" + c);
        
        DataOutputStream sender = new DataOutputStream(connection.getOutputStream());
        sender.writeInt(c); //Sending correct answer to client.
        System.out.println("Correct answer sent to user!");
        connection.close();
        System.exit(0);
		
		}
		catch (IOException e){
			System.err.println("Could not listen on port: " + portNumber + ".");
			System.exit(1);
		}
		
	}
}