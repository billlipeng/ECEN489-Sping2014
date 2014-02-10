import java.net.ServerSocket;

public class MultiThreadServer {
	
	static ServerSocket socket;
	
	public static void main(String args[]) throws Exception {
		System.out.println("Waiting for new connections...");
		try {
			socket = new ServerSocket(5678);
			while (true) {
				MultiThreadServerThread mtst = new MultiThreadServerThread(socket.accept());
				Thread t = new Thread(mtst);
				t.start();
				System.out.println("A Client has Connected!");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
