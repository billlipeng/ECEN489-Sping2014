import java.io.*;
import java.net.*;

public class EchoServer {
	public static void main(String[] args) throws Exception {
		int port = 5555;
		String input = "";
		
		System.out.print("Enter Port or leave blank for default (5555): ");
		input = System.console().readLine();
		if (input.length() > 0) {
			port = Integer.parseInt(input);
		}
		
		System.out.println("Configureing server on port " + Integer.toString(port));
		
		ServerSocket server = null;
		Socket client = null;
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			server = new ServerSocket(port);
			client = server.accept();
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			String serverinput;
			while ((serverinput = in.readLine()) != null) {
				out.println(serverinput);
				System.out.println("Server echo'd: " + serverinput);
			}			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		out.close();
        in.close();
        client.close();

	}
}
