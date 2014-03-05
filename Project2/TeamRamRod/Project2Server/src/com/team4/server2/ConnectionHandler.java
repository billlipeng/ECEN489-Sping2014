package com.team4.server2;
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

import org.team4_project2_v1_0.*;



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
    String table = "ecen489_project2_data";
    
    //array declarations
	 Long[] time;
	 Double[] latitude;
	 Double[] longitude;
	 Double[] bearing;
	 Double[] speed;
	
	 Float[] accel_X;
	 Float[] accel_Y;
	 Float[] accel_Z;
	
	 Float[] gyro_X;
	 Float[] gyro_Y;
	 Float[] gyro_Z;
	
	 Float[] orientation_A;
	 Float[] orientation_P;
	 Float[] orientation_R;
	
	 Float[] rotVec_X;
	 Float[] rotVec_Y;
	 Float[] rotVec_Z;
	 Float[] rotVec_C;
	
	 Float[] linACC_X;
	 Float[] linACC_Y;
	 Float[] linACC_Z;	
	
	 Float[] gravity_X;
	 Float[] gravity_Y;
	 Float[] gravity_Z;

	Project2Packet receive;

	boolean connected=false;
	
    public ConnectionHandler(Socket clientSocket) {
    	super();
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {

	            input  = new ObjectInputStream( clientSocket.getInputStream() );
	            Class.forName("org.sqlite.JDBC");
	  	      	c = DriverManager.getConnection("jdbc:sqlite:C:/projtwo.db");
	  	      	System.out.println("database connection successful");
	  	      	c.setAutoCommit(false);
//	  	      	uploadfusion uf = new uploadfusion();
	  	      	String sql=null;

	          //num1 = input.readInt();
				receive = (Project2Packet) input.readObject();
				
				time = receive.getTime();
				latitude = receive.getLatitude();
				longitude = receive.getLongitude();
				bearing = receive.getBearing();
				speed = receive.getSpeed();
				
				accel_X = receive.getAcce_X();
				accel_Y = receive.getAcce_Y();
				accel_Z = receive.getAcce_Z();
				
				gyro_X = receive.getGyro_X();
				gyro_Y = receive.getGyro_Y();
				gyro_Z = receive.getGyro_Z();
				
				orientation_A = receive.getOrientation_A();
				orientation_P = receive.getOrientation_P();
				orientation_R = receive.getOrientation_R();
				
				rotVec_X = receive.getRotVec_X();
				rotVec_Y = receive.getRotVec_Y();
				rotVec_Z = receive.getRotVec_Z();
				rotVec_C = receive.getRotVec_C();
				
				linACC_X = receive.getLinACC_X();
				linACC_Y = receive.getLinACC_Y();
				linACC_Z = receive.getLinACC_Z();
				
				gravity_X = receive.getGravity_X();
				gravity_Y = receive.getGravity_Y();
				gravity_Z = receive.getGravity_Z();
				
				System.out.println(time.length);
				System.out.println(latitude.length);
				System.out.println(longitude.length);
				System.out.println(bearing.length);
				System.out.println(speed.length);
				System.out.println(accel_X.length);
				System.out.println(accel_Y.length);
				System.out.println(accel_Z.length);
				System.out.println(gyro_X.length);
				System.out.println(gyro_Y.length);
				System.out.println(gyro_Z.length);
				System.out.println(orientation_A.length);
				System.out.println(orientation_P.length);
				System.out.println(orientation_R.length);
				System.out.println(rotVec_X.length);
				System.out.println(rotVec_Y.length);
				System.out.println(rotVec_Z.length);
				System.out.println(rotVec_C.length);
				System.out.println(linACC_X.length);
				System.out.println(linACC_Y.length);
				System.out.println(linACC_Z.length);
				System.out.println(gravity_X.length);
				System.out.println(gravity_Y.length);
				System.out.println(gravity_Z.length);

			if(time.length==0)	{
				for(int i=0; i < 10; i++){
					System.out.print("test1");
//					System.out.println("Time: " + time[i] + "  ");
//					System.out.println("Latitude: " + latitude[i] +"  ");
//					System.out.println("Longitude: " + longitude[i] +"  ");
//					System.out.println("Bearing: " + bearing[i] +"  ");
//					System.out.println("Speed: " + speed[i] +"  ");
					System.out.println("Accel_X: " + accel_X[i] +"  ");
					System.out.println("Accel_Y: " + accel_Y[i] +"  ");
					System.out.println("Accel_Z: " + accel_Z[i] +"  ");
					System.out.println("Gyro_X: " + gyro_X[i] +"  ");
					System.out.println("Gyro_Y: " + gyro_Y[i] +"  ");
					System.out.println("Gyro_Z: " + gyro_Z[i] +"  ");
					System.out.println("Orientation_A: " + orientation_A[i] +"  ");
					System.out.println("Orientation_A: " + orientation_P[i] +"  ");
					System.out.println("Orientation_A: " + orientation_R[i] +"  ");
					System.out.println("RotVec_X: " + rotVec_X[i] +"  ");
					System.out.println("RotVec_Y: " + rotVec_Y[i] +"  ");
					System.out.println("RotVec_Z: " + rotVec_Z[i] +"  ");
					System.out.println("RotVec_C: " + rotVec_C[i] +"  ");
					System.out.println("LinAcc_X: " + linACC_X[i] +"  ");
					System.out.println("LinAcc_Y: " + linACC_Y[i] +"  ");
					System.out.println("LinAcc_Z: " + linACC_Z[i] +"  ");
					System.out.println("Gravity_X: " + gravity_X[i] +"  ");
					System.out.println("Gravity_Y: " + gravity_Y[i] +"  ");
					System.out.println("Gravity_Z: " + gravity_Z[i] +"  ");
					
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
//							+ ", time"
//							+ ", latitude"
//							+ ", longitude"
//							+ ", bearing"
//							+ ", speed"
							+ ", accel_X"
							+ ", accel_Y"
							+ ", accel_Z"
							+ ", gyro_X"
							+ ", gyro_Y"
							+ ", gyro_Z"
							+ ", orientation_A"
							+ ", orientation_P"
							+ ", orientation_R"
							+ ", rotVec_X"
							+ ", rotVec_Y"
							+ ", rotVec_Z"
							+ ", rotVec_C"
							+ ", linACC_X"
							+ ", linACC_Y"
							+ ", linACC_Z"
							+ ", gravity_X"
							+ ", gravity_Y"
							+ ", gravity_Z"
							+ ") " +
	    	 				"VALUES ("
	    	 						+ "" + maxid + ""					
//	    	 						+ ", " + time[i] + ""				
//	 								+ ", " + latitude[i] + ""			
//									+ ", " + longitude[i]+ ""					
//									+ ", " + bearing[i] + ""		
//									+ ", " + speed[i] +""		
									+ ", " + accel_X[i] +""		
									+ ", " + accel_Y[i] +""		
									+ ", " + accel_Z[i] +""	
									+ ", " + gyro_X[i] +""		
									+ ", " + gyro_Y[i] +""		
									+ ", " + gyro_Z[i] +""											
									+ ", " + orientation_A[i] +""		
									+ ", " + orientation_P[i] +""		
									+ ", " + orientation_R[i] +""											
									+ ", " + rotVec_X[i] +""		
									+ ", " + rotVec_Y[i] +""		
									+ ", " + rotVec_Z[i] +""			
									+ ", " + rotVec_C[i] +""		
									+ ", " + linACC_X[i] +""	
									+ ", " + linACC_Y[i] +""
									+ ", " + linACC_Z[i] +""			
									+ ", " + gravity_X[i] +""		
									+ ", " + gravity_Y[i] +""	
									+ ", " + gravity_Z[i] +""	
									+ ");"; 				
					//System.out.println("Executing statement "+sql+"");
					stmt.executeUpdate(sql);
					//System.out.println("closing Statement ");
			        stmt.close();
			        System.out.println("i= "+i);
				}
			}
			else if((bearing.length==0) && (time.length > 0)){
				for(int i=0; i < 10; i++){
					System.out.print("test1");
					System.out.println("Time: " + time[i] + "  ");
					System.out.println("Latitude: " + latitude[i] +"  ");
					System.out.println("Longitude: " + longitude[i] +"  ");
//					System.out.println("Bearing: " + bearing[i] +"  ");
//					System.out.println("Speed: " + speed[i] +"  ");
					System.out.println("Accel_X: " + accel_X[i] +"  ");
					System.out.println("Accel_Y: " + accel_Y[i] +"  ");
					System.out.println("Accel_Z: " + accel_Z[i] +"  ");
					System.out.println("Gyro_X: " + gyro_X[i] +"  ");
					System.out.println("Gyro_Y: " + gyro_Y[i] +"  ");
					System.out.println("Gyro_Z: " + gyro_Z[i] +"  ");
					System.out.println("Orientation_A: " + orientation_A[i] +"  ");
					System.out.println("Orientation_A: " + orientation_P[i] +"  ");
					System.out.println("Orientation_A: " + orientation_R[i] +"  ");
					System.out.println("RotVec_X: " + rotVec_X[i] +"  ");
					System.out.println("RotVec_Y: " + rotVec_Y[i] +"  ");
					System.out.println("RotVec_Z: " + rotVec_Z[i] +"  ");
					System.out.println("RotVec_C: " + rotVec_C[i] +"  ");
					System.out.println("LinAcc_X: " + linACC_X[i] +"  ");
					System.out.println("LinAcc_Y: " + linACC_Y[i] +"  ");
					System.out.println("LinAcc_Z: " + linACC_Z[i] +"  ");
					System.out.println("Gravity_X: " + gravity_X[i] +"  ");
					System.out.println("Gravity_Y: " + gravity_Y[i] +"  ");
					System.out.println("Gravity_Z: " + gravity_Z[i] +"  ");
					
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
							+ ", time"
							+ ", latitude"
							+ ", longitude"
//							+ ", bearing"
//							+ ", speed"
							+ ", accel_X"
							+ ", accel_Y"
							+ ", accel_Z"
							+ ", gyro_X"
							+ ", gyro_Y"
							+ ", gyro_Z"
							+ ", orientation_A"
							+ ", orientation_P"
							+ ", orientation_R"
							+ ", rotVec_X"
							+ ", rotVec_Y"
							+ ", rotVec_Z"
							+ ", rotVec_C"
							+ ", linACC_X"
							+ ", linACC_Y"
							+ ", linACC_Z"
							+ ", gravity_X"
							+ ", gravity_Y"
							+ ", gravity_Z"
							+ ") " +
	    	 				"VALUES ("
	    	 						+ "" + maxid + ""					
	    	 						+ ", " + time[i] + ""				
	 								+ ", " + latitude[i] + ""			
									+ ", " + longitude[i]+ ""					
//									+ ", " + bearing[i] + ""		
//									+ ", " + speed[i] +""		
									+ ", " + accel_X[i] +""		
									+ ", " + accel_Y[i] +""		
									+ ", " + accel_Z[i] +""	
									+ ", " + gyro_X[i] +""		
									+ ", " + gyro_Y[i] +""		
									+ ", " + gyro_Z[i] +""											
									+ ", " + orientation_A[i] +""		
									+ ", " + orientation_P[i] +""		
									+ ", " + orientation_R[i] +""											
									+ ", " + rotVec_X[i] +""		
									+ ", " + rotVec_Y[i] +""		
									+ ", " + rotVec_Z[i] +""			
									+ ", " + rotVec_C[i] +""		
									+ ", " + linACC_X[i] +""	
									+ ", " + linACC_Y[i] +""
									+ ", " + linACC_Z[i] +""			
									+ ", " + gravity_X[i] +""		
									+ ", " + gravity_Y[i] +""	
									+ ", " + gravity_Z[i] +""	
									+ ");"; 				
					//System.out.println("Executing statement "+sql+"");
					stmt.executeUpdate(sql);
					//System.out.println("closing Statement ");
			        stmt.close();
			        System.out.println("i= "+i);
				}
			}
			
        	else {
				for(int i=0; i < 10; i++){
					System.out.print("test1");
					System.out.println("Time: " + time[i] + "  ");
					System.out.println("Latitude: " + latitude[i] +"  ");
					System.out.println("Longitude: " + longitude[i] +"  ");
					System.out.println("Bearing: " + bearing[i] +"  ");
					System.out.println("Speed: " + speed[i] +"  ");
					System.out.println("Accel_X: " + accel_X[i] +"  ");
					System.out.println("Accel_Y: " + accel_Y[i] +"  ");
					System.out.println("Accel_Z: " + accel_Z[i] +"  ");
					System.out.println("Gyro_X: " + gyro_X[i] +"  ");
					System.out.println("Gyro_Y: " + gyro_Y[i] +"  ");
					System.out.println("Gyro_Z: " + gyro_Z[i] +"  ");
					System.out.println("Orientation_A: " + orientation_A[i] +"  ");
					System.out.println("Orientation_A: " + orientation_P[i] +"  ");
					System.out.println("Orientation_A: " + orientation_R[i] +"  ");
					System.out.println("RotVec_X: " + rotVec_X[i] +"  ");
					System.out.println("RotVec_Y: " + rotVec_Y[i] +"  ");
					System.out.println("RotVec_Z: " + rotVec_Z[i] +"  ");
					System.out.println("RotVec_C: " + rotVec_C[i] +"  ");
					System.out.println("LinAcc_X: " + linACC_X[i] +"  ");
					System.out.println("LinAcc_Y: " + linACC_Y[i] +"  ");
					System.out.println("LinAcc_Z: " + linACC_Z[i] +"  ");
					System.out.println("Gravity_X: " + gravity_X[i] +"  ");
					System.out.println("Gravity_Y: " + gravity_Y[i] +"  ");
					System.out.println("Gravity_Z: " + gravity_Z[i] +"  ");
					
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
							+ ", time"
							+ ", latitude"
							+ ", longitude"
							+ ", bearing"
							+ ", speed"
							+ ", accel_X"
							+ ", accel_Y"
							+ ", accel_Z"
							+ ", gyro_X"
							+ ", gyro_Y"
							+ ", gyro_Z"
							+ ", orientation_A"
							+ ", orientation_P"
							+ ", orientation_R"
							+ ", rotVec_X"
							+ ", rotVec_Y"
							+ ", rotVec_Z"
							+ ", rotVec_C"
							+ ", linACC_X"
							+ ", linACC_Y"
							+ ", linACC_Z"
							+ ", gravity_X"
							+ ", gravity_Y"
							+ ", gravity_Z"
							+ ") " +
	    	 				"VALUES ("
	    	 						+ "" + maxid + ""					
	    	 						+ ", " + time[i] + ""				
	 								+ ", " + latitude[i] + ""			
									+ ", " + longitude[i]+ ""					
									+ ", " + bearing[i] + ""		
									+ ", " + speed[i] +""		
									+ ", " + accel_X[i] +""		
									+ ", " + accel_Y[i] +""		
									+ ", " + accel_Z[i] +""	
									+ ", " + gyro_X[i] +""		
									+ ", " + gyro_Y[i] +""		
									+ ", " + gyro_Z[i] +""											
									+ ", " + orientation_A[i] +""		
									+ ", " + orientation_P[i] +""		
									+ ", " + orientation_R[i] +""											
									+ ", " + rotVec_X[i] +""		
									+ ", " + rotVec_Y[i] +""		
									+ ", " + rotVec_Z[i] +""			
									+ ", " + rotVec_C[i] +""		
									+ ", " + linACC_X[i] +""	
									+ ", " + linACC_Y[i] +""
									+ ", " + linACC_Z[i] +""			
									+ ", " + gravity_X[i] +""		
									+ ", " + gravity_Y[i] +""	
									+ ", " + gravity_Z[i] +""	
									+ ");"; 				
					//System.out.println("Executing statement "+sql+"");
					stmt.executeUpdate(sql);
					//System.out.println("closing Statement ");
			        stmt.close();
			        System.out.println("i= "+i);
				}
			}
				
				System.out.println("Updating Database...");
				c.commit();
				System.out.println("Database Update Complete!!");
			    c.close();
//fusion tables
//			   uf.update(client_id, run_id, time,date,latitude,longitude,sensor_id,sensor_type,sensor_value, attribute);
				
	         
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