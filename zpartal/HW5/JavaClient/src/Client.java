	import java.io.*;
	import java.net.*;

public class Client {
		static private ObjectInputStream ois;
		static private ObjectOutputStream oos;
		static private Socket socket;
	
		public static void main(String[] args) throws Exception {
			String host = "127.0.0.1";
			int port = 5555;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			String input = "";
			
			System.out.print("Enter server IP or leave blank for default (127.0.0.1): ");
			input = bufferedReader.readLine();
			if (input.length() > 0) {
				host = input;
			}
			
			System.out.print("Enter Port or leave blank for default (5555): ");
			input = bufferedReader.readLine();
			if (input.length() > 0) {
				port = Integer.parseInt(input);
			}
			
			System.out.println("Connecting to host " + host + " on port " + Integer.toString(port));
			
			
			try {
				socket = new Socket(host,port);
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
				
				ClientPacket cp = new ClientPacket("client", 1,2);
				oos.writeObject((ClientPacket) cp);
				oos.flush();
				
				ServerPacket response = (ServerPacket) ois.readObject();
				System.out.println("ServerID: " + response.getServerID());
				System.out.println("Result: " + response.getResult());				
				
//				String clientInput;
//				System.out.print(">>");
//				while ((clientInput = stdIn.readLine()) != null) {
//					long startTime = System.currentTimeMillis();
//					out.println(clientInput);
//					String response = in.readLine();
//					long endTime = System.currentTimeMillis();
//					long duration = endTime - startTime;
//	                System.out.println("echo (" + duration + " ms): " + response);
//	                System.out.print(">>");
//				}			
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		
			oos.close();
	        ois.close();
	        socket.close();
		}
}
