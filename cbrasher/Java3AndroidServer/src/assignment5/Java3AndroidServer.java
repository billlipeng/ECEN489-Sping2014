package assignment5;

import java.net.*;
import java.util.Scanner;
import java.io.*;

import com.zpartal.commpackets.*;

public class Java3AndroidServer {
	
	static ObjectInputStream input;
	static ObjectOutputStream output;
	static ServerSocket server;

	public static void main(String[] args) throws IOException {
		
		System.out.println("Awakening Hive Mind\n");
		
		Scanner user = new Scanner(System.in);
	
		System.out.println("Enter port number: \n");
		int port = user.nextInt();
		try{
			server = new ServerSocket(port);
			System.out.print("The Swarm has found the port\n");
			
		while(true){
	
				Connection connection = new Connection(server.accept());
				Thread threader = new Thread(connection);
				threader.start();
				System.out.println("Connected to the Hive mind\n");
		}
		}
			catch(IOException e){
			System.out.print(e);
			System.exit(1);
			}
		finally{
			server.close();
		}
		
		}
	}
