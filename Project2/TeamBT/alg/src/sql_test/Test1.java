package sql_test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Test1 {
	public static Vector<Long> time = new Vector<Long>();
	public static Vector<Long> timeCopy = new Vector<Long>();

	public static Vector<Double> longitude = new Vector<Double>(); 
	public static Vector<Double> latitude = new Vector<Double>();
	public static Vector<Double> longitudeCopy = new Vector<Double>(); 
	public static Vector<Double> latitudeCopy = new Vector<Double>();
	
	public static Vector<Double> bearing = new Vector<Double>();
	public static Vector<Double> speed = new Vector<Double>();
	
	public static Vector<Float> accelX = new Vector<Float>();
	public static Vector<Float> accelY = new Vector<Float>();
	public static Vector<Float> accelZ = new Vector<Float>();
	public static Vector<Float> orientationA = new Vector<Float>();
	public static Vector<Float> orientationP = new Vector<Float>();
	public static Vector<Float> orientationR = new Vector<Float>();
	public static Vector<Float> rotVecX = new Vector<Float>();
	public static Vector<Float> rotVecY = new Vector<Float>();
	public static Vector<Float> rotVecZ = new Vector<Float>();
	public static Vector<Float> rotVecC = new Vector<Float>();
	public static Vector<Float> linAccX = new Vector<Float>();
	public static Vector<Float> linAccY = new Vector<Float>();
	public static Vector<Float> linAccZ = new Vector<Float>();
	public static Vector<Float> gravityX = new Vector<Float>();
	public static Vector<Float> gravityY = new Vector<Float>();
	public static Vector<Float> gravityZ = new Vector<Float>();
	public static Vector<Float> gyroX = new Vector<Float>();
	public static Vector<Float> gyroY = new Vector<Float>();
	public static Vector<Float> gyroZ = new Vector<Float>();
	
	
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {

		try{
			
			/*
			double[] p0;
			double[] p1;
			double[] p2;
			p0 = new double[2];
			p1 = new double[2];
			p2 = new double[2];
			
			p0[0] = 0;
			p0[1] = 0;
			p1[0] = 1;
			p1[1] = 0;
			p2[0] = 0;
			p2[1] = 1;
			System.out.println(computeAngle(p0, p1, p2)/Math.PI*180);

*/
			read_data();
			reset_time_unit();
			calculate();
			System.out.println("num\ttime\t\tlongitutde\t\tlatitude");
			for(int i = 0; i<time.size(); i++){
				System.out.print(i+"\t"+time.get(i)+"\t");
				System.out.print(longitude.get(i)+"\t");
				System.out.println(latitude.get(i)+"\t");
			}
			
		}
		finally
		{
			
		}

	}
	// modified from print table
	public static void read_data() throws ClassNotFoundException {

		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = null;
			conn = DriverManager.getConnection("jdbc:sqlite:projtwo_3.db");

			
			Statement statement = conn.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			System.out.println("Loading data...");
			ResultSet rs = statement.executeQuery("select * from ecen489_project2_data");
			while (rs.next()) {
				
				time.addElement(new Long(rs.getLong("time")));
				
				longitude.addElement(new Double(rs.getDouble("longitude")));
				latitude.addElement(new Double(rs.getDouble("latitude")));
				longitudeCopy.addElement(new Double(rs.getDouble("longitude")));
				latitudeCopy.addElement(new Double(rs.getDouble("latitude")));
				bearing.addElement(new Double(rs.getDouble("bearing")));
				speed.addElement(new Double(rs.getDouble("speed")));
			/*
				accelX.addElement(new Float(rs.getFloat("accelX")));
				accelY.addElement(new Float(rs.getFloat("accelY")));
				accelZ.addElement(new Float(rs.getFloat("accelZ")));
				
				orientationA.addElement(new Float(rs.getFloat("orientationA")));
				orientationP.addElement(new Float(rs.getFloat("orientationP")));
				orientationR.addElement(new Float(rs.getFloat("orientationR")));

				rotVecX.addElement(new Float(rs.getFloat("rotVecX")));
				rotVecY.addElement(new Float(rs.getFloat("rotVecY")));
				rotVecZ.addElement(new Float(rs.getFloat("rotVecZ")));
				rotVecC.addElement(new Float(rs.getFloat("rotVecC")));

				linAccX.addElement(new Float(rs.getFloat("linAccX")));
				linAccY.addElement(new Float(rs.getFloat("linAccY")));
				linAccZ.addElement(new Float(rs.getFloat("linAccZ")));
				
				gravityX.addElement(new Float(rs.getFloat("gravityX")));
				gravityY.addElement(new Float(rs.getFloat("gravityY")));
				gravityZ.addElement(new Float(rs.getFloat("gravityZ")));
				
				gyroX.addElement(new Float(rs.getFloat("gyroX")));
				gyroY.addElement(new Float(rs.getFloat("gyroY")));
				gyroZ.addElement(new Float(rs.getFloat("gyroZ"))); */
			}
			if(conn != null)
				conn.close();
			
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		System.out.println("Loading Complete!");

	}
	

	


	public static void insertInTable(Connection conn) throws ClassNotFoundException, IOException {
		String Make = "";
		String Model = "";
		int Year = 0;
		int Amount = 0;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("Enter the Make: ");
			Make = br.readLine();
			System.out.println("Enter the Model: ");
			Model = br.readLine();
			System.out.println("Enter the Year: ");
			Year = Integer.parseInt(br.readLine());
			System.out.println("Enter the Amount: ");
			Amount = Integer.parseInt(br.readLine());
		}
		catch (NumberFormatException nfe) {
			System.err.println("Invalid Number Format!");
		}
		
		try {
			conn.setAutoCommit(false);
			String insert = "INSERT INTO Inventory(Make, Model, Year, Amount) VALUES(?,?,?,?)";
			PreparedStatement insertData = conn.prepareStatement(insert);
			insertData.setString(1,  Make);
			insertData.setString(2, Model);
			insertData.setInt(3, Year);
			insertData.setInt(4, Amount);
			insertData.executeUpdate();
			conn.commit();
			conn.setAutoCommit(true);
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
	}

	public static void calculate() throws ClassNotFoundException, IOException {		
		try{
			System.out.println("Calculating...");

			if(time.size() <= 0){	
				System.err.println("No data");
			}
			else if(time.size()<10){	
				calculate_node(0, time.size()-1, 1, 0);
			}
			else if(time.size()<20){	
				calculate_node(1, 9, -1, 0);
				calculate_node(1, time.size()%10-1, 1, 0);
			}
			else{
				// calculate node list head
				calculate_node(0, 5 , 1, 0);
				calculate_node(1, 5 , -1, 1);
				rotation(5);
				combine_vector(0);
				
				// calculate node list tail
				calculate_node(time.size()/10, time.size()%10-1 , 1, 0);

				// calculate node list middle
				for(int node = 1; node < time.size()/10;node++){
					calculate_node(node, 5,1, 0);
					calculate_node(node+1, 5, -1, 1);
					rotation(node*10+5);
					combine_vector(node);
				}
			}
			System.out.println("Calculating Complete!");

		}
		catch(IOException e){
			System.err.println(e.getMessage());
		}

	}
		
	// calculate nodes by using bearing and velocity
	// input:  	part(int):		part want to calculate
	//			nodeNum(int):	number of nodes to calculate
	//		    direction: 		direction the nodes. 1 forward; -1 backward; 
	//			dataset:		save data in longitude(dataset = 0) or longitudeCopy(dataset =1)
	public static int calculate_node(int node, int nodeNum, int direction, int dataset) throws ClassNotFoundException, IOException {
	
		double radius;
		double radians;
		double v;
		double t;
		// At latitude 30.62
		// Length Of A Degree Of Latitude In Meters: 110862.94	
		// Length Of A Degree Of Longitude In Meters: 95880.84
		double longitude_unit = 95880.84;
		double latitude_unit = 110862.94;

			// forward
			if( direction == 1 ){
				if(node*10+nodeNum>time.size()){
					System.err.print("Error in calculate_node(): ");
					System.err.println("Node numbers bigger than data set.");
					return 1;
				}
					
				for (int i = 0; i < nodeNum; i++) {
					// v = speed[i+1]
					v = (double) speed.get(i+1+node*10);
					// t = time[i+1] - time[i+1]
					t = ((long) timeCopy.get(i+1+node*10)) - ((long) timeCopy.get(i+node*10)) ;
					radius = v * t / 1000.0 ;
					
					//System.out.println("i"+i+"  t = "+t+"  v = "+v+"  radius="+radius);

					radians = (double) bearing.get(i);
					radians = Math.toRadians(radians);
					double deltaX = radius * Math.sin(radians);
					double deltaY = radius * Math.cos(radians);

					if(dataset==0){
						//System.out.println("set(node*10+i+1)="+(longitude.get(node*10+i) + deltaX/longitude_unit));
						longitude.set(node*10+i+1, longitude.get(node*10+i) + deltaX/longitude_unit);
						latitude.set(node*10+i+1, latitude.get(node*10+i) + deltaY/latitude_unit);
					}
					else{
						//System.out.println("set(node*10+i+1)="+(longitudeCopy.get(node*10+i) + deltaX/longitude_unit));
						longitudeCopy.set(node*10+i+1, longitudeCopy.get(node*10+i) + deltaX/longitude_unit);
						latitudeCopy.set(node*10+i+1, latitudeCopy.get(node*10+i) + deltaY/latitude_unit);
					}
				}
				return 0;
			}
			// backward
			else if( direction == -1 )
			{
				if(node == 0 || (node*10-nodeNum < 1)){
					System.err.print("Error in calculate_node(): ");
					System.err.println("0th node cannot do backward calculation.");
					return 1;
				}
					
				for (int i = 0; i < nodeNum; i++) {
					// v = speed[i+1]
					v = (double) speed.get(node*10-i);
					// t = time[i+1] - time[i+1]
					t = ((long) timeCopy.get(node*10-i)) - ((long) timeCopy.get(node*10-i-1)) ;
					radius = v * t /1000.0;
					//System.out.println("ii"+i+"  t = "+t+"  v = "+v+"  radius="+radius);

					radians = (double) bearing.get(node*10-i-1);
					radians = Math.toRadians(radians);
					double deltaX = radius * Math.sin(radians) * (-1.0);
					double deltaY = radius * Math.cos(radians) * (-1.0);

					if(dataset==0){
						//System.out.println("longitude.get(node*10-i)= "+(longitude.get(node*10-i)));
						longitude.set(node*10-i-1, longitude.get(node*10-i) + deltaX/longitude_unit);
						latitude.set(node*10-i-1, latitude.get(node*10-i) + deltaY/latitude_unit);
					}
					else{
						//System.out.println("sset(node*10-i-1)=  "+(longitudeCopy.get(node*10-i) + deltaX/longitude_unit));
						longitudeCopy.set(node*10-i-1, longitudeCopy.get(node*10-i) + deltaX/longitude_unit);
						latitudeCopy.set(node*10-i-1, latitudeCopy.get(node*10-i) + deltaY/latitude_unit);
					}
					
				}
				return 0;
			}
			else{
				System.err.print("Error in calculate_node(): ");
				System.err.println("direction or node not right");
				return 1;
			}
	}
	

	// combine longitude and longitudeCopy at node
	public static void combine_vector(int node) throws IOException {
		double dataX0, dataY0, dataX1, dataY1;
		for(int i = node*10; i<(node*10+10); i++){
			dataX0 = longitude.get(i);
			dataY0 = latitude.get(i);
			dataX1 = longitudeCopy.get(i);
			dataY1 = latitudeCopy.get(i);
			if(dataX1 < -1 && dataX0 > -1)
				longitude.set(i,dataX1);
			if(dataY1 > 1 && dataY0 < 1)
				latitude.set(i,dataY1);
		}
	}
	public static void reset_time_unit() throws IOException {
		for(int i=0; i<time.size(); i++)
		{
			timeCopy.add(i, time.get(i));
			time.set(i, (long) time.get(i)/1000);
		}
	}
	public static void rotation(int point) throws IOException {
		// get middle point
		double midX = (longitude.get(point) + longitudeCopy.get(point))/2.0;
		double midY = (latitude.get(point) + latitudeCopy.get(point))/2.0;
		
		// get the node info head and tail
		int node_head = (point/10)*10;
		int node_tail = (point/10+1)*10;
		int position = point%10;
		double x1, y1; // target position

		double[] p0 = new double[2]; // node
		double[] p1 = new double[2]; // point
		double[] p2 = new double[2]; // middle point
		
		p0[0] = longitude.get(node_head);
		p0[1] = latitude.get(node_head);
		p1[0] = longitude.get(point);
		p1[1] = latitude.get(point);
		p2[0] = midX;
		p2[1] = midY;
		double ratio = Math.sqrt(Math.pow(p2[0]-p0[0],2)+Math.pow(p2[1]-p0[1],2))/Math.sqrt(Math.pow(p1[0]-p0[0], 2)+Math.pow(p1[1]-p0[1], 2));
		/*System.out.println("ratio1="+ratio);
		System.out.println("sqrt="+Math.sqrt(Math.pow(p1[0]-p0[0], 2)+Math.pow(p1[1]-p0[1], 2)));
		System.out.println("point ="+point);
		System.out.println("node_head ="+node_head);
		System.out.println("head="+p1[0]+"   "+p0[0]+"   "+p1[1]+"   "+p0[1]);
*/
		
		// figure out rotate direction
		int direction;
		double bx = p2[0]-p0[0];
		double by = p2[1]-p0[1];
		double cx = p1[0]-p0[0];
		double cy = p1[1]-p0[1];
		if((bx*cy-by*cx)>0)
			direction = 1; // b is on the clock-wise side of c
		else 
			direction = -1;
		for(int i = 1; i< position; i++){
			if(bearing.get(point-i) != 0){
				p1[0] = longitude.get(point-i);
				p1[1] = latitude.get(point-i);
				
				System.out.println("p1[0] = " + p1[0] +"p1[1] = "+ p1[1]);
	
				
				double angle = computeAngle(p0,p1,p2);
				
				System.out.println("angle = " + angle);
	
				double xx = p1[0]-p0[0];
				double yy = p1[1]-p0[1];
				if(direction==1)// clock-wise rotate, change angle to negative
					angle = -angle;
				
				x1 = (xx*Math.cos(angle) - yy*Math.sin(angle)) * ratio;
				y1 = (xx*Math.sin(angle) + yy*Math.cos(angle)) * ratio;			
				longitude.set(point-i, p0[0]+x1);
				System.out.print("point-i ="+(point-i)+" p0[0]+x1  "+( p0[0]+x1));
	
				latitude.set(point-i, p0[1]+y1);
				System.out.println("  p0[0]+y1  "+( p0[1]+y1));
			}
			else{}

		}
		
		
		p0[0] = longitude.get(node_tail);
		p0[1] = latitude.get(node_tail);
		ratio = Math.sqrt(Math.pow(p2[0]-p0[0],2)+Math.pow(p2[1]-p0[1],2))/Math.sqrt(Math.pow(p1[0]-p0[0], 2)+Math.pow(p1[1]-p0[1], 2));
		/*System.out.println("ratio2="+ratio);
		System.out.println("sqrt="+Math.sqrt(Math.pow(p1[0]-p0[0], 2)+Math.pow(p1[1]-p0[1], 2)));
		System.out.println("point ="+point);
		System.out.println("node_tail ="+node_tail);
		System.out.println("tail="+p1[0]+"   "+p0[0]+"   "+p1[1]+"   "+p0[1]);
*/
		
		// figure out rotate direction
		bx = p2[0]-p0[0];
		by = p2[1]-p0[1];
		cx = p1[0]-p0[0];
		cy = p1[1]-p0[1];
		if((bx*cy-by*cx)>0)
			direction = 1; // b is on the clock-wise side of c
		else 
			direction = -1;
		
		for(int i = 1; i< 10-position; i++){
			if(bearing.get(point+i) != 0){
				p1[0] = longitude.get(point+i);
				p1[1] = latitude.get(point+i);
				
				System.out.println("p1[0] = " + p1[0] +"p1[1] = "+ p1[1]);
	
				double angle = computeAngle(p0,p1,p2);
				
				System.out.println("angle = " + angle);
	
				double xx = p1[0]-p0[0];
				double yy = p1[1]-p0[1];
				if(direction==1)// clock-wise rotate, change angle to negative
					angle = -angle;
				// for counter clock-wise rotate
				x1 = (xx*Math.cos(angle) - yy*Math.sin(angle)) * ratio;
				y1 = (xx*Math.sin(angle) + yy*Math.cos(angle)) * ratio;			
				longitude.set(point+i, p0[0]+x1);
				System.out.println("point+i = "+(point+i)+" p0[0]+x1  "+( p0[0]+x1));
	
				latitude.set(point+i, p0[1]+y1);
				System.out.print(" p0[0]+y1  "+( p0[1]+y1));
			}
			else{}

		}
		longitude.set(point, p2[0]);
		latitude.set(point, p2[1]);
		
	}
	public static double computeAngle (double[] p0, double[] p1, double[] p2)
	{
		System.out.print("x1\t y1\t x2\t y2 ");
		double x1 = p1[0] - p0[0]; 		System.out.print(""+x1);
		double y1 = p1[1] - p0[1];    	System.out.print(" "+y1);

		double x2 = p2[0] - p0[0];   	System.out.print(" "+x2);

		double y2 = p2[1] - p0[1];		System.out.println(" "+x2);


		return Math.acos((x1*x2+y1*y2)/(Math.sqrt(Math.pow(x1,2)+Math.pow(y1,2))*Math.sqrt(Math.pow(x2,2)+Math.pow(y2,2))));
	  
	}
}