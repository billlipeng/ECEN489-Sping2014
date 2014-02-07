package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;


public class java3server {
	//to parse the input
	static int fromByteArray(byte[] bytes) {
	     return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	}
	
	public static void main(String[] args) { try {
		byte[] a = new byte[8];
		
		int ans;
		ServerSocket server;
		server = new ServerSocket(5555,1);
		while(true) {
			
			Socket connection = server.accept();
			
			DataInputStream dis = new DataInputStream(connection.getInputStream());
			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
			
			//make ints out of
			int sum = 0;
			int[] ia = {0,0};
			dis.read(a);

			byte temp;
			byte[] xbyte = new byte[4]; 
			for (int i = 0; i < 4; i++)
			  {
			     temp = a[i];
			     xbyte[i] = temp;
			  }
			
			
			int x = fromByteArray(xbyte);
			
			byte[] ybyte = new byte[4];
			for (int i = 4; i < 8; i++)
			  {
			     temp = a[i];
			     ybyte[i-4] = temp;
			  }
			int y = fromByteArray(ybyte);
			
			
			ans = x + y;
			
			
			System.out.println("Received");
			System.out.println(x);
			System.out.println(y);
			
			dos.writeInt(ans);
			
		}
		}
		catch ( IOException e ) {
			
			e.printStackTrace();
		}
		
		

		
	}
}