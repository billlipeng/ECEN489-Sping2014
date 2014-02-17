

	import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

	public class MultiThreadedServer implements Runnable{

	    protected int          serverPort   = 3333;
	    protected ServerSocket serverSocket = null;
	    protected boolean      isStopped    = false;
	    protected Thread       runningThread= null;
	    private Connection c;
	    private static String filepath = "/home/heffay/school/ECEN489-Spring2014/jeffsanwa/Java2/";
	    private Statement query;

	    public MultiThreadedServer(int port){
	        this.serverPort = port;
	    }

	    public void run(){
	        synchronized(this){
	            this.runningThread = Thread.currentThread();
	        }
	        openServerSocket();
	        while(!isStopped()){
	            Socket clientSocket = null;
	            try {
	            	System.out.println("Waiting for connection: ");
	                clientSocket = this.serverSocket.accept();
	            } catch (IOException e) {
	                if(isStopped()) {
	                    System.out.println("Server Stopped.") ;
	                    return;
	                }
	                throw new RuntimeException(
	                    "Error accepting client connection", e);
	            }
	            new Thread(
	                new WorkerRunnable(
	                    clientSocket, "Multithreaded Server")
	            ).start();
	        }
	            
	        
	    }


	    private synchronized boolean isStopped() {
	        return this.isStopped;
	    }

	    public synchronized void stop(){
	        this.isStopped = true;
	        try {
	            this.serverSocket.close();
	        } catch (IOException e) {
	            throw new RuntimeException("Error closing server", e);
	        }
	    }

	    private void openServerSocket() {
	        try {
	            this.serverSocket = new ServerSocket(this.serverPort);
	        } catch (IOException e) {
	            throw new RuntimeException("Cannot open port "+serverPort, e);
	        }
	    }


}
