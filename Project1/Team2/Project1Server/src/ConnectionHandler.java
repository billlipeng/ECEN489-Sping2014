import java.io.DataInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mfa157.project1packet.Project1Packet;



/**

 */
public class ConnectionHandler implements Runnable{

	//connection declarations
    protected Socket clientSocket = null;
    ObjectInputStream input;
    ObjectOutputStream output;
    
    //sqlite declarations
    Connection c = null;
    Statement stmt = null;
    String table = "ecen489_project1_data";
    
    //array declarations
    Double sensor_value[];
    String sensor_id[];
    String sensor_type[];
    String latitude[];
    String longitude[];
    String time[];
    String date[];
    String client_id[];
    String run_id[];
    String attribute[];

	Project1Packet receive;

	boolean connected=false;
	
    public ConnectionHandler(Socket clientSocket) {
    	super();
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {

	            input  = new ObjectInputStream( clientSocket.getInputStream() );
	            Class.forName("org.sqlite.JDBC");
	  	      	c = DriverManager.getConnection("jdbc:sqlite:C:/projone.db");
	  	      	c.setAutoCommit(false);
	  	      	uploadfusion uf = new uploadfusion();
	  	      	String sql=null;

	          //num1 = input.readInt();
				receive = (Project1Packet) input.readObject();
				sensor_value = receive.getSensor_value();
				sensor_id = receive.getSensor_id();
				sensor_type = receive.getSensor_type();
				latitude = receive.getLatitude();
				longitude = receive.getLongitude();
				time = receive.getTime();
				date = receive.getDate();
				client_id = receive.getClient_id();
				run_id = receive.getRun_id();
				attribute = receive.getAttribute();
				
				System.out.println(sensor_id.length);
				System.out.println(latitude.length);
				System.out.println(longitude.length);
				System.out.println(sensor_type.length);
				System.out.println(time.length);
				for(int i=0; i<sensor_id.length; i++){
					System.out.print("ClientID: " + client_id[i] +"  ");
					System.out.print("RunID: " + run_id[i] +"  ");
					System.out.print("Latitude: " + latitude[i] +"  ");
					System.out.print("Longitude: " + longitude[i] +"  ");
					System.out.print("SensorID: " + sensor_id[i] +"  ");
					System.out.print("SensorType: " + sensor_type[i] +"  ");
					System.out.print("SensorValue: " + sensor_value[i] +"  ");
					System.out.print("Time: " + time[i] +"  ");
					System.out.print("Date: " + date[i] +"  ");
					System.out.println("Attribute: " + attribute[i] +"  ");
					
					//System.out.println("Creating Statement ");
					stmt = c.createStatement();
					int maxid=0;
					//System.out.println("Querying for maxid");
					ResultSet rs = stmt.executeQuery( "SELECT _id FROM "+table+";" );
					while ( rs.next() ) {
						maxid = rs.getInt("_id")+1;				        	 
				      }
					
					//System.out.println("Creating insert statement, maxid= "+maxid+" ");
					sql = "INSERT INTO "+table+"("
							+ "_id"
							+ ", date"
							+ ", time"
							+ ", client_id"
							+ ", run_id"
							+ ", latitude"
							+ ", longitude"
						//	+ ", bearing"
						//	+ ", speed"
						//	+ ", altitude"
							+ ", sensor_id"
							+ ", sensor_type"
							+ ", sensor_value"
							+ ", attribute"
							+ ") " +
	    	 				"VALUES ("
	    	 						+ ""+maxid+""						//id
	    	 						+ ", '" + date[i] +"'"				//date
	 								+ ", '" + time[i] +"'"				//time
									+ ", '"+client_id[i]+"'"					//client_id
									+ ", '"+run_id[i]+"'"						//run_id
									+ ", " + latitude[i] +""			//latitude
									+ ", " + longitude[i] +""			//longitude
						//			+ ", bearing"						//bearing
						//			+ ", speed"							//speed
						//			+ ", altitude"						//altitude
									+ ", '" + sensor_id[i] +"'"			//sensor_id
									+ ", '" + sensor_type[i] +"'"		//sensor_type
									+ ", " + sensor_value[i] +" "		//sensor value
									+ ", '"+attribute[i]+"' "					//attribute
									+ ");"; 				
					//System.out.println("Executing statement "+sql+"");
					stmt.executeUpdate(sql);
					//System.out.println("closing Statement ");
			        stmt.close();
			        System.out.println("i= "+i);
				}
				
				System.out.println("committing data ");
				c.commit();
			    c.close();
			   uf.update(client_id, run_id, time,date,latitude,longitude,sensor_id,sensor_type,sensor_value, attribute);
				
	         
        } catch (IOException | ClassNotFoundException | SQLException e) {
            //report exception somewhere.
            e.printStackTrace();
        } 
        
        try {
			input.close();
			System.out.println("Connection Closed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}