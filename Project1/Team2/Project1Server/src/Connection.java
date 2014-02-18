import java.io.DataInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

import com.mfa157.project1packet.Project1Packet;



/**

 */
public class Connection implements Runnable{

    protected Socket clientSocket = null;
    ObjectInputStream input;
    ObjectOutputStream output;
    
    double sensor_value[];
    String sensor_id[];
    String sensor_type[];
    String latitude[];
    String longitude[];
    String time[];
    String date[];

	Project1Packet receive;

	boolean connected=false;
	
    public Connection(Socket clientSocket) {
    	super();
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
	            input  = new ObjectInputStream( clientSocket.getInputStream() );
	           // output = new ObjectOutputStream( clientSocket.getOutputStream() );
	            
	            while(true){
	          //num1 = input.readInt();
				receive = (Project1Packet) input.readObject();
				sensor_value = receive.getSensor_value();
				sensor_id = receive.getSensor_id();
				sensor_type = receive.getSensor_type();
				latitude = receive.getLatitude();
				longitude = receive.getLongitude();
				time = receive.getTime();
				date = receive.getDate();
				
				for(int i=0; i<sensor_id.length+1; i++){
					System.out.print("Latitude: " + latitude +"  ");
					System.out.print("Longitude: " + longitude +"  ");
					System.out.print("SensorID: " + sensor_id +"  ");
					System.out.print("SensorType: " + sensor_type +"  ");
					System.out.print("SensorValue: " + sensor_value +"  ");
					System.out.print("Time: " + time +"  ");
					System.out.println("Date: " + date +"  ");
				}
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