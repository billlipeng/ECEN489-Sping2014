
import java.io.*;
import java.net.*;

public class Server {
	static private ServerSocket server;
	
	public static void main(String[] args) throws Exception {
		// Port configuration
		int port = 5555;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		
		System.out.print("Enter Port or leave blank for default (5555): ");
		input = bufferedReader.readLine();
		if (input.length() > 0) {
			port = Integer.parseInt(input);
		}
		
		System.out.println("Configureing server on port " + Integer.toString(port) + "...");
		
		try {
			server = new ServerSocket(port);
			// Worker threads are launched upon acceptance of client connection
			while(true) {
				Worker worker;
				worker = new Worker(server.accept());
				System.out.println("New Connection...");
				Thread t = new Thread(worker);
				t.start();				
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}