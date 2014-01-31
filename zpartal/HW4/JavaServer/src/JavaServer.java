import java.io.*;
import javax.bluetooth.*;
import javax.microedition.io.*;

public class JavaServer {
	public final UUID uuid = new UUID ("b0d042fe8a3c11e3b91bd231feb1dc81", false);
	public String name = "Java Server";
	public final String url = "btspp://localhost:" + uuid + ";name=" + name + ";authenticate=false;encrypt=false;" ;
	
	StreamConnectionNotifier service = null;
	StreamConnection conn = null;
	
	public JavaServer() {
		try {
			System.out.println("Initializing Server...");
			service = (StreamConnectionNotifier) Connector.open(url);
			conn = (StreamConnection) service.acceptAndOpen();
			System.out.println("Client connected...");
			DataInputStream in = new DataInputStream(conn.openInputStream());
			DataOutputStream out = new DataOutputStream(conn.openOutputStream());
			while (true) {
				String received = in.readUTF();
				System.out.println("Received: " + received);
				out.writeUTF("Echo: " + received);
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}	
	public static void main(String[] args) {
		JavaServer javaserver = new JavaServer();
	}
}
