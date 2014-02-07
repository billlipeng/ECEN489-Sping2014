import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class EchoServerForAndroidClient {
	
	public static void main(String[] args) throws IOException {

		try {

			StreamConnectionNotifier service = (StreamConnectionNotifier) Connector.open("btspp://localhost:" + new UUID("0000110100001000800000805F9B34FB", false).toString() + ";name=helloService");

			StreamConnection conn = (StreamConnection) service.acceptAndOpen();
			System.out.println("Connected");
			String input = "";
			
			DataInputStream in = new DataInputStream(conn.openInputStream());
			DataOutputStream out = new DataOutputStream(conn.openOutputStream());
			while(!input.contains("close")) {
				input = in.readUTF();	      // Read from client
				out.writeUTF("Echo: " + input);	  // Send Echo to client
				System.out.println("Client: " + input);
			}
			conn.close();
			service.close();

		} catch (IOException e) {	System.err.print(e.toString());   }
	}

}