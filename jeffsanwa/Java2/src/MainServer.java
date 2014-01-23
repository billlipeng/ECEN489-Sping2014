import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;


public class MainServer {
	
		public static int port = 3333; // choose random port
		public static String fromServer = "";
		public static ServerSocket serverSocket;
		public static Statement query;
		private static String filepath = "/home/heffay/";
		
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Connection c = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:"+filepath+"test.db");
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Opened database successfully");

		try {
			//let's wait for the client connection
			System.out.println("Waiting for connection: ");
			
			     serverSocket = new ServerSocket(port); //new socket port 3333, if bind successful should work
			    Socket clientSocket = serverSocket.accept();
			    
			    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			    
			    out.println("Hello\n");
			    
			    while(true){
			    	System.out.println("waiting for something");
			    	String temp = in.readLine();
			    	out.println("Got Something");
			    	System.out.println(temp);
			    	
			    	if(temp.contains("%")){
			    		try {
			    			temp = temp.substring(1);
			    			int tempNum = (int) Math.random();
			    			query = c.createStatement();
			    			String sql = "INSERT INTO test(id,name)VALUES("+tempNum+",'"+temp+"')";
			    		      query.executeUpdate(sql);
			    		      query.close();
			    		      c.close();
			    		} catch (SQLException e1) {
			    			// TODO Auto-generated catch block
			    			e1.printStackTrace();
			    		}
			    	}
			    	if(temp.contains("Bye")){
			    		out.println("Bye");
			    		break;
			    	}
			    }
			    
		}catch (IOException e){
				e.printStackTrace();
			}finally{
				try {
					serverSocket.close();
					System.exit(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

	}
}
