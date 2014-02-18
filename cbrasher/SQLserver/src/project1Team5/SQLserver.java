package project1Team5;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class SQLserver {

	static ServerSocket server;
	private static Config cg;
	private static Loader l;

	static Scanner user = new Scanner(System.in);

	public static void main(String[] args) throws IOException {

		cg = new Config();
		l = new Loader(cg);

		System.out.println("Enter port number: \n");
		int port = 0;
		port = user.nextInt();
		System.out.println("Server will be listening on:" + " " + port);

		try {
			server = new ServerSocket(port);
			while (true) {
				Runner run;
				run = new Runner(server.accept());
				Thread r = new Thread(run);
				r.start();

			}

		} catch (IOException e) {
			System.out.print(e);
			System.exit(1);
		}

	}
}