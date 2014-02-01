import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import com.zpartal.commpackets.*;

public class Worker implements Runnable {
	
	private Socket client;	
	static private ObjectInputStream ois;
	static private ObjectOutputStream oos;
		
	public Worker(Socket client) {
		super();
		this.client = client;
	}
	
	@Override
	public void run() {
		try {
			oos = new ObjectOutputStream(client.getOutputStream());
			ois = new ObjectInputStream(client.getInputStream());
			
			while(true) {
				ClientPacket cp = (ClientPacket) ois.readObject();
				System.out.print("Request from: " + cp.getClientID());
				System.out.println("; Data: " + cp.getNum1() + ", " + cp.getNum2());
				ServerPacket response = new ServerPacket("zpartal", cp.getNum1() + cp.getNum2());
				oos.flush();
				oos.writeObject(response);
				
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			oos.close();
			ois.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
