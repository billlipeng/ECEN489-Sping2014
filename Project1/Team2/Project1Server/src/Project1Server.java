import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;




public class Project1Server {
	static ObjectInputStream input;
	static ObjectOutputStream output;
	static ServerSocket server;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		System.out.println("Waiting for connections...");
		
		try{
			
			server = new ServerSocket(4445);
			
			while(true){
				ConnectionHandler connection = new ConnectionHandler(server.accept());
				Thread t = new Thread(connection);
				t.start();	
				System.out.println("New Connection");
			}
			
			
			
		}
		catch(Exception e){
			System.out.println("Disconnected");
			
		}
		
		
		
		
		
	}
}