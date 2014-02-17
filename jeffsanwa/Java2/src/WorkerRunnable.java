
	import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
	/**

	 */
	public class WorkerRunnable implements Runnable{

	    protected Socket clientSocket = null;
	    protected String serverText   = null;
	    private Connection c;
	    private static String filepath = "/home/heffay/school/ECEN489-Spring2014/jeffsanwa/Java2/";
	    private Statement query;
	    private String temp;
	    private int tempNum;
	    private String sql;

	    public WorkerRunnable(Socket clientSocket, String serverText) {
	        this.clientSocket = clientSocket;
	        this.serverText   = serverText;
	        temp = "";
	        tempNum = 0;
	    }

	    public void run() {
	    	try{
	            Class.forName("org.sqlite.JDBC");
			      c = DriverManager.getConnection("jdbc:sqlite:"+filepath+"test.db");
			    
			    System.out.println("Opened database successfully");
			    
			    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			    
			    out.println("Hello\n");
			    
			    while(true){
			    	System.out.println("waiting for something");
			    	temp = in.readLine();
			    	out.println("Got Something");
			    	
			    	
			    	if(temp.contains("%")){
			    		try {
			    			temp = temp.substring(1);
			    			 tempNum = (int) Math.random();
			    			query = c.createStatement();
			    			 sql = "INSERT INTO test(id,name)VALUES("+tempNum+",'"+temp+"')";
			    		      query.executeUpdate(sql);
			    		      query.close();
			    		      
			    		} catch (SQLException e1) {
			    			// TODO Auto-generated catch block
			    			e1.printStackTrace();
			    		}
			    	}
			    	if(temp.contains("Bye")){
			    		out.println("Bye");
			    		c.close();
			    		break;
			    	}
			    }
			    } catch ( Exception e ) {
				      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				      System.exit(0);
				    }
			    
	            finally{
				try {
					clientSocket.close();
					System.out.println("Server Stopped.") ;
					//System.exit(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	        }

}
