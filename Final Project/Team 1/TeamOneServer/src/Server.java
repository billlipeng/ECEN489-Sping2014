
import java.io.*;
import java.net.*;

public class Server {
	private static ServerSocket server;
    private static int port = 5555;

	private static Config config;
    private static ConfigLoader pl;

	public static void main(String[] args) throws Exception {
        // Load config
        config = new Config();
        pl = new ConfigLoader(config);

        System.out.println("IP: " + Inet4Address.getLocalHost().getHostAddress());
        System.out.println("Port: " + Integer.toString(config.PORT));

		try {
			server = new ServerSocket(config.PORT);
			// Worker threads are launched upon acceptance of client connection
			while(true) {
				Worker worker;
				worker = new Worker(server.accept());
				System.out.println("New Connection...");
				Thread t = new Thread(worker);
				t.start();				
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
