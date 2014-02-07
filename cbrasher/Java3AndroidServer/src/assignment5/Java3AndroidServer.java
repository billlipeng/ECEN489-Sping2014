package assignment5;

import java.net.*;
import java.nio.*;
import java.util.Scanner;
import java.io.*;

import com.zpartal.commpackets.*;

public class Java3AndroidServer {

	public static void main(String[] args) {
		
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
				System.out.println("Connected to the Hive mind/n");
				if (user.nextLine() == "quit"){
				server.close();
				}
			}
			catch(IOException e){
			System.out.print(e);
			System.exit(2);
			}
			new Threader(clientSocket).start();
		}
	}
	}

