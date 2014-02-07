import java.net.*;
import java.io.*;
import com.zpartal.commpackets.*;

public class Java2Sever {

	ObjectInputStream ois;
	ObjectOutputStream oos;

	public Java2Sever() throws IOException{
		ServerSocket server = null;
		Socket socket = null;
		
		try {
			server = new ServerSocket(5555);
			ClientPacket packet;
			ServerPacket response;
			while(true){
				System.out.println("Server established...");
				socket = server.accept();
				ois = new ObjectInputStream(socket.getInputStream());
				oos = new ObjectOutputStream(socket.getOutputStream());
				while (true) {
					packet = (ClientPacket) ois.readObject();
					System.out.println("Request ID: " + packet.getClientID() + " a = " + packet.getNum1() + " b = " + packet.getNum2());
	                int sum = packet.getNum1() + packet.getNum2();
					System.out.println("Sum = " + sum);
					response = new ServerPacket("Server",sum);
					oos.writeObject(response);
					System.out.println("Sending back...");					
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
		} 
	}

	public static void main(String[] args) throws IOException {
		new Java2Sever();
	}
}