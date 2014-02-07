import java.net.*;

import com.zpartal.commpackets.*;

import java.io.*;
import java.util.Scanner;

public class mysocketserver {

	ObjectInputStream is;
	ObjectOutputStream os;
	int num1;
	int num2;

	public void run() {
		byte[] accept = new byte[8];
		ServerSocket serversocket = null;
		Socket socket = null;
		
		try {
			ClientPacket client;
			ServerPacket server;
			serversocket = new ServerSocket(5555);
			while(true){
			socket = serversocket.accept();
			try {
				is = new ObjectInputStream(socket.getInputStream());
				os = new ObjectOutputStream(socket.getOutputStream());
				Scanner scanner = new Scanner(System.in);
				while (true) {
					client = (ClientPacket) is.readObject();
					num1 = client.getNum1();
					num2 = client.getNum2();
					String clientID = client.getClientID();
					System.out.println("ClientID" + clientID);
					System.out.println("Number1: " +num1);
					System.out.println("Number2: " +num2);
					server = new ServerPacket("Yuanxing",num1+num2);
					os.writeObject(server);
					System.out.println("Sum = " +(num1+num2));
					
					System.out.println("You got two numbers: " + num1 + " and "
							+ num2);
					int sum = num1 + num2;

					System.out.println(" Their sum is:" + sum);
					System.out.println("The result is now sending to the client.......");
					
					//is.close();
					//os.close();
					//socket.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}}
		} catch (Exception e) {
			e.printStackTrace();

		} 
			
		
	}

	public static void main(String[] args) {
		mysocketserver ss = new mysocketserver();
		ss.run();
	}
}