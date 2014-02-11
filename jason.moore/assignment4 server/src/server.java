import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.IOException;
import java.lang.System;
import java.net.InetAddress;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.DataInputStream;
public class server {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		ServerSocket serversocket = new ServerSocket(5558);
		 
        Socket ss= serversocket.accept();
        DataInputStream DIS= (DataInputStream) ss.getInputStream();
		int firstnum = DIS.readInt();
		int secondnum= DIS.readInt();
		int sum = firstnum + secondnum;
		DataOutputStream DOS= (DataOutputStream) ss.getOutputStream();
		DOS.write(sum);
	}
	
}
