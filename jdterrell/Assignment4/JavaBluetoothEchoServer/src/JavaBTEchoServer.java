import java.io.*;
import javax.bluetooth.*;
import javax.microedition.io.*;

public class JavaBTEchoServer {
	public final UUID uuid = new UUID ("b0d042fe8a3c11e3b91bd231feb1dc81", false);
	public String name = "Java Server";
	public final String url = "btspp://localhost:" + uuid + ";name=" + name + ";authenticate=false;encrypt=false;" ;
	
	StreamConnectionNotifier service = null;
	StreamConnection conn = null;

	public JavaBTEchoServer() {
		try {
			System.out.println("Initializing Server...");
			service = (StreamConnectionNotifier) Connector.open(url);
			conn = (StreamConnection) service.acceptAndOpen();
			System.out.println("Client connected...");
			System.out.println("Waiting for client input...");
			DataInputStream in = new DataInputStream(conn.openInputStream());
			DataOutputStream out = new DataOutputStream(conn.openOutputStream());
			while (true) {
				String received = in.readUTF();
				System.out.println("Received from Client: " + received);
				out.writeUTF("Echo: " + received);
				if (received.equals("close") | received.equals("Close") | received.equals("exit") | received.equals("Exit")) {
					System.exit(0);
				}
				else if (received.equals("Jeff is awesome")) {
					System.out.println("YESSIR!");
				}
					
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		JavaBTEchoServer javaBTEchoServer = new JavaBTEchoServer();
	}
}
