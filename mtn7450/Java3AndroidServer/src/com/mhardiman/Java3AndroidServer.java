package com.mhardiman;

import com.zpartal.commpackets.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Java3AndroidServer {

	public static void main( String[] args )
	{
		
		ServerSocket server;
		System.out.print("Starting server.\n");
		try {
			server = new ServerSocket(5555, 1);
			while (true)
			{
			Socket connection = server.accept();

			ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
			ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
			
			System.out.print("Reading...\n");
			ClientPacket cp = (ClientPacket) input.readObject();
			int num1 = cp.getNum1();
			int num2 = cp.getNum2();
			
			System.out.print(num1 + ", " + num2 + "\n");
			
			System.out.print("Returning sum...\n\n");
			
			ServerPacket sp = new ServerPacket("sp", num1+num2);
			output.writeObject(sp);
			
			input.close();
			output.close();
			
			connection.close();
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
