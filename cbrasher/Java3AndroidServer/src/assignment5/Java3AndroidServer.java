package assignment5;

import java.net.*;
import java.nio.*;
import java.util.Scanner;
import java.io.*;

public class Java3AndroidServer {
	
	static ObjectInputStream input;
	static ObjectOutputStream output;

	public static void main(String[] args) throws IOException {
		
		System.out.println("Awakening Hive Mind\n");
		
		Scanner user = new Scanner(System.in);
		ServerSocket server = null;
		Socket clientSocket = null;
	
		System.out.println("Enter port number: \n");
		int port = user.nextInt();
		try{
			server = new ServerSocket(port);
			System.out.print("The Swarm has found the port\n");
			}
			catch (IOException e){
				System.out.println(e);
				System.exit(1);
			}
		while(true){
			try{
				clientSocket = server.accept();
				System.out.println("Connected to the Hive mind\n");

				}
			catch(IOException e){
				server.close();
			System.out.print(e);
			System.exit(2);
			}
			new Threader(clientSocket).start();
		}
	}
	}

