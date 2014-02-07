import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.IOException;
import java.lang.System;
import java.net.InetAddress;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
public class primary {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
Scanner sc = new Scanner(System.in);
InetAddress ipaddress = null;
int port= 5558;
System.out.println("Enter first number");
int firstnum= sc.nextInt();
System.out.println("Enter second number");
int secondnum=sc.nextInt();
System.out.println("Enter guess number");
int guess=sc.nextInt();

//System.out.println("Enter ip address");
//ipaddress = sc.nextLine();
		Socket socket = new Socket("192.168.1.1",port);
		ByteArrayOutputStream BAOS = (ByteArrayOutputStream) socket.getOutputStream();
		InputStream DIS =  socket.getInputStream();
		BAOS.write(0);
		BAOS.write(0);
		BAOS.write(0);
		BAOS.write(firstnum);
		BAOS.write(0);
		BAOS.write(0);
		BAOS.write(0);
		BAOS.write(secondnum);
		BAOS.flush();
		
		int sum= DIS.read();
		
		if(sum==guess){
			System.out.println("You are correct");
		}
		else{
			System.out.println("You are incorrect");
		}
		socket.close();
		sc.close();
		
		
	}

}
