import java.io.DataInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

import com.zpartal.commpackets.ClientPacket;
import com.zpartal.commpackets.ServerPacket;

/**

 */
public class Connection implements Runnable{

    protected Socket clientSocket = null;
    ObjectInputStream input;
    ObjectOutputStream output;
    int num1=0;
	int num2=0;
	ClientPacket receive;
	ServerPacket send;
	boolean connected=false;
	
    public Connection(Socket clientSocket) {
    	super();
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
	            input  = new ObjectInputStream( clientSocket.getInputStream() );
	            output = new ObjectOutputStream( clientSocket.getOutputStream() );
	            
	            while(true){
	          //num1 = input.readInt();
				receive = (ClientPacket) input.readObject();
				num1 = receive.getNum1();
				num2 = receive.getNum2();
				String clientID = receive.getClientID();
				System.out.println("Client ID: " +clientID);
				System.out.println("Int1: " + num1 + " Int2: " + num2);
				System.out.println("Sending sum...");
				send = new ServerPacket("Miguel", receive.getNum1()+receive.getNum2());
				output.writeObject(send); //send total back to client
				System.out.println("Total = " + (num1 + num2));

	            }
        } catch (IOException | ClassNotFoundException e) {
            //report exception somewhere.
           // e.printStackTrace();
        } 
        
        try {
			output.close();
			input.close();
			System.out.println("Connection Closed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}