import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

  public class EchoServer{
  
  public static void main(String[] args) {
	    UUID uuid = new UUID ("b0d042fe8a3c11e3b91bd231feb1dc81", false);
		String name = "Java Server";
		final String url = "btspp://localhost:" + uuid + ";name=" + name + ";authenticate=false;encrypt=false;" ;
		
		StreamConnectionNotifier service = null;
		StreamConnection conn = null;
		
			try {
				System.out.println("Starting Server...");
				service = (StreamConnectionNotifier) Connector.open(url);
				System.out.println("Waiting for clients to connect");
				conn = (StreamConnection) service.acceptAndOpen();
				System.out.println("Client connected! Waiting for data");
				DataInputStream in = new DataInputStream(conn.openInputStream());
				DataOutputStream out = new DataOutputStream(conn.openOutputStream());
				while (true) {
					String received = in.readUTF();
					System.out.println( received);
					out.writeUTF(received);
				}			
			} catch (IOException e) {
				e.printStackTrace();
			}		
			
  }
}