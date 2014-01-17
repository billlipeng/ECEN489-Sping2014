import java.io.*;
import java.net.*;

public class EchoClient {
	public static void main(String[] args) throws Exception {
		String host = "127.0.0.1";
		int port = 5555;
		String input = "";
		
		System.out.print("Enter server IP or leave blank for default (127.0.0.1): ");
		input = System.console().readLine();
		if (input.length() > 0) {
			host = input;
		}
		
		System.out.print("Enter Port or leave blank for default (5555): ");
		input = System.console().readLine();
		if (input.length() > 0) {
			port = Integer.parseInt(input);
		}
		
		System.out.println("Connecting to host " + host + " on port " + Integer.toString(port));
		
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		BufferedReader stdIn = null;
		
		try {
			socket = new Socket(host,port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			String clientInput;
			System.out.print(">>");
			while ((clientInput = stdIn.readLine()) != null) {
				long startTime = System.currentTimeMillis();
				out.println(clientInput);
				String response = in.readLine();
				long endTime = System.currentTimeMillis();
				long duration = endTime - startTime;
                System.out.println("echo (" + duration + " ms): " + response);
                System.out.print(">>");
			}			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		out.close();
        in.close();
        socket.close();
	}
}
