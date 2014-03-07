package com.google.api.services.samples.fusiontables.cmdline;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Vector;

public class Test1 {
	//vector for calculation
	public static Vector<Integer> flag = new Vector<Integer>(); //flag for display
	public static Vector<Integer> crazy_angle = new Vector<Integer>(); // angle with mistake
	public static Vector<Integer> crazy_distance = new Vector<Integer>(); // angle with mistake

	public static Vector<Long> time = new Vector<Long>();
	public static Vector<Long> timeCopy = new Vector<Long>();

	public static Vector<Double> longitude = new Vector<Double>(); 
	public static Vector<Double> latitude = new Vector<Double>();
	public static Vector<Double> longitudeCopy = new Vector<Double>(); 
	public static Vector<Double> latitudeCopy = new Vector<Double>();
	public static Vector<Double> longitudeOriginal = new Vector<Double>(); 
	public static Vector<Double> latitudeOriginal = new Vector<Double>();
	
	
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
	
	final static double longitude_unit = 95880.84;
	final static double latitude_unit = 110862.94;
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {

		try{

			read_data();
			//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			// avg speed
			double before;
			double sum=0;
			for(int i = 1; i< time.size();i++){
				before = Math.sqrt(Math.pow(latitudeOriginal.get(i)*latitude_unit - latitudeOriginal.get(i-1)*latitude_unit, 2)
						+Math.pow(longitudeOriginal.get(i)*longitude_unit - longitudeOriginal.get(i-1)*longitude_unit,2));
				sum += before;
			}
			System.out.println("avg:"+sum/(time.size()-1));
			//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			for(int i = 0; i < time.size(); i++){
				if(i%10 == 0)
					flag.add(i, 2);
				else if(i%5 == 0)
					flag.add(i,1);
				else
					flag.add(i,0);
			}
			new InsertFusionTables(longitudeOriginal,latitudeOriginal,"Original",flag).start();
			reset_time_unit();
			calculate();
			System.out.println("num\ttime\t\tlatitude\t\tlongitutde");
			for(int i = 0; i<time.size(); i++){
				System.out.print(i+"\t"+time.get(i)+"\t");
				System.out.print(latitude.get(i)+"\t");
				System.out.println(longitude.get(i)+"\t");

			}
			Thread.sleep(6000);
			Thread.sleep(1);			
			
			new InsertFusionTables(longitude,latitude,"Calculate",flag).start();


		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			conn = DriverManager.getConnection("jdbc:sqlite:DataSampleMiguel.sqlite");

			
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
			//--------------------------------------------------------		
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:DataSampleMiguel.sqlite");

			
			statement = conn.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			System.out.println("Loading data...");
			rs = statement.executeQuery("select * from ecen489_project2_data_Complete");
			while (rs.next()) {
				longitudeOriginal.addElement(new Double(rs.getDouble("longitude")));
				latitudeOriginal.addElement(new Double(rs.getDouble("latitude")));
			}
			if(conn != null)
				conn.close();
			//--------------------------------------------------------		
			
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
				calculate_node(0, time.size()-1, 1);
			}
			else{
				// calculate_node(node, nodeNum, direction, dataset)
				// calculate node list
				for(int node = 0; node < (time.size()-1)/10;node++){
					calculate_node(node, 5, 1);
					calculate_node(node+1, 5, -1);
					rotation(node*10+5);
					//combine_vector(node);
				}
				
				// calculate node list tail
				calculate_node((time.size()-1)/10, (time.size()-1)%10 , 1);
			}
			detect_angle();
			detect_distance();
			repair();
			System.out.println("ii\tangle\tdistance");

			for(int ii=0;ii<crazy_angle.size();ii++)
			{
				System.out.println(ii+"\t"+crazy_angle.get(ii)+"\t"+crazy_distance.get(ii));
			}

			
			
			System.out.println("Calculating Complete!");

		}
		catch(IOException e){
			System.err.println(e.getMessage());
		}

	}
	private static void detect_angle() {
		double[] mid = new double[2];
		double[] before = new double[2];
		double[] after = new double[2];
		double angle;
		int flag = 0;
		crazy_angle.add(0);// set zero to 0
		for(int i = 1; i< (time.size()-1);i++){
			while(latitude.get(i-1)==latitude.get(i)&&longitude.get(i-1)==longitude.get(i)){
				i++;
				crazy_angle.add(0);
			}
			mid[0] = latitude.get(i)*latitude_unit;
			mid[1] = longitude.get(i)*longitude_unit;
			before[0] = latitude.get(i-1)*latitude_unit;
			before[1] = longitude.get(i-1)*longitude_unit;			
			after[0] = latitude.get(i+1)*latitude_unit;
			after[1] = longitude.get(i+1)*longitude_unit;
			angle = computeAngle(mid,before,after);
			
			if(angle < Math.PI/180*105){
				flag++;
				if(flag>=2)
				{
					crazy_angle.set(i-1, 1);
					crazy_angle.add(1);
				}
				else
					crazy_angle.add(0);
			}
			else{
				if(flag>=2){
					crazy_angle.add(1);
					// reset flag
					flag = 0;
				}
				else
					crazy_angle.add(0);
			}
						
		}
		// set last element to 0 
		crazy_angle.add(0);
			
	}
	private static void detect_distance() {
		double before;
		double after;
		double ratio;
		crazy_distance.add(0);// set zero to 0
		for(int i = 1; i< (time.size()-1);i++){
			while(latitude.get(i-1)==latitude.get(i)&&longitude.get(i-1)==longitude.get(i)){
				i++;
				crazy_distance.add(0);
			}
			before = Math.sqrt(Math.pow(latitude.get(i)*latitude_unit - latitude.get(i-1)*latitude_unit, 2)
							+Math.pow(longitude.get(i)*longitude_unit - longitude.get(i-1)*longitude_unit,2));
			after = Math.sqrt(Math.pow(latitude.get(i+1)*latitude_unit - latitude.get(i)*latitude_unit, 2)
					+Math.pow(longitude.get(i+1)*longitude_unit - longitude.get(i)*longitude_unit,2));	
			ratio = Math.max(before, after)/Math.min(before, after);
			if(ratio>2&&Math.max(before, after)>25){
				crazy_distance.add(1);
			}
			else{
				crazy_distance.add(0);
			}			
		}
		// set last element to 0 
		crazy_distance.add(0);
			
	}
	
	private static void repair() throws ClassNotFoundException, IOException{
		Vector<Double> latitude_temp = new Vector<Double>();
		Vector<Double> longitude_temp = new Vector<Double>();
		int cnt = 0;
		for(int i=0; i<crazy_angle.size(); i++){
			if(crazy_angle.get(i)==1){
				cnt++;		
				
			}
			else {
				if(cnt>=4){
					for(int j=(i-cnt); j<i; j++){
						if(j%10==0){
							calculate_node(j/10, i-j, 1);
							calculate_node(j/10, j-(i-cnt),-1);
							for(int k = j-1; k>(i-cnt-1); k--){
								longitude.set(k,longitudeCopy.get(k));
								latitude.set(k,latitudeCopy.get(k));
							}
							cnt = 0;
						}	
					}
					for(int j=(i-cnt); j<i; j++){
						if(j%5==0){
							cnt = 0;
						}	
					}
					if(i%10==0){
						calculate_node(i/10, cnt, -1);
						for(int k = i-1 ; k>(i-cnt-1); k--){
							longitude.set(k,longitudeCopy.get(k));
							latitude.set(k,latitudeCopy.get(k));
							System.out.println("I am here");
						}
						cnt = 0;
					}
					else if((i-cnt-1)%10==0){
						calculate_node(i, cnt, 1);
						cnt = 0;
					}
					else
						cnt = 0;
				}
				else if(cnt==3){
					cnt = 0;
				}
				cnt = 0;
			}
		}
	}
	
	
	/** calculate points by using bearing and velocity
	 	input:  part(int):		part want to calculate
				nodeNum(int):	number of nodes to calculate
			    direction: 		direction the nodes. 1 forward; -1 backward; 
				dataset:		save data in longitude(dataset = 0) or longitudeCopy(dataset =1)
	**/
	public static int calculate_node(int node, int nodeNum, int direction) throws ClassNotFoundException, IOException {
	
		double radius;
		double radians;
		double v;
		double t;
		
		int point;
		
		double deltaX;
		double deltaY;
		// At latitude 30.62
		// Length Of A Degree Of Latitude In Meters: 110862.94	
		// Length Of A Degree Of Longitude In Meters: 95880.84
		


		if((direction == 1 && node*10+nodeNum>time.size())||(direction == -1 && node*10 > time.size())){
			System.err.print("Error in calculate_node(): ");
			System.err.println("Node numbers bigger than data set.");
			return 1;
		}
		for (int i = 0; i < nodeNum; i++) {
			if(direction == 1)
				point = node*10 + i + 1;						
			else
				point = node*10 - i;
			
			// v = speed[i+1]
			v = (double) speed.get(point);
			// t = time[i+1] - time[i+1]
			t = ((long) timeCopy.get(point)) - ((long) timeCopy.get(point -1)) ;
			radius = v * t / 1000.0 ;
			
			//System.out.println("i"+i+"  t = "+t+"  v = "+v+"  radius="+radius);

			radians = (double) bearing.get(point);
			radians = Math.toRadians(radians);
			deltaX = radius * Math.sin(radians);
			deltaY = radius * Math.cos(radians);

			if(direction == 1){
				//System.out.println("set(node*10+i+1)="+(longitude.get(node*10+i) + deltaX/longitude_unit));
				longitude.set(point, longitude.get(point-1) + deltaX/longitude_unit);
				latitude.set(point, latitude.get(point-1) + deltaY/latitude_unit);
			}
			else{
				deltaX = -deltaX;
				deltaY = -deltaY;
				//System.out.println("set(node*10+i+1)="+(longitudeCopy.get(node*10+i) + deltaX/longitude_unit));
				longitudeCopy.set(point-1, longitudeCopy.get(point) + deltaX/longitude_unit);
				latitudeCopy.set(point-1, latitudeCopy.get(point) + deltaY/latitude_unit);
			}
		}
		return 0;
	}
	

	// combine longitude and longitudeCopy at node
	public static void combine_vector(int node) throws IOException {
		double dataX, dataY;
		for(int i = node*10 + 6; i<(node*10+10); i++){
			dataX = longitudeCopy.get(i);
			dataY = latitudeCopy.get(i);
			longitude.set(i,dataX);
			latitude.set(i,dataY);
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
		// rotate angle
		double angle = computeAngle(p0,p1,p2);
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
		if(direction==1)// clock-wise rotate, change angle to negative
			angle = -angle;
		for(int i = 1; i< position; i++){
			if(bearing.get(point-i) != 0){
				p1[0] = longitude.get(point-i);
				p1[1] = latitude.get(point-i);
				x1 = 0;
				y1 = 0;
				System.out.println("p1[0] = " + p1[0] +"  p1[1] = "+ p1[1]);
		
				double xx = p1[0]-p0[0];
				double yy = p1[1]-p0[1];

				if(Math.abs(angle) < Math.PI/180*45)
				{
					x1 = (xx*Math.cos(angle) - yy*Math.sin(angle)) * ratio;
					y1 = (xx*Math.sin(angle) + yy*Math.cos(angle)) * ratio;
				}
				longitude.set(point-i, p0[0]+x1);
				System.out.println("point-i ="+(point-i)+" p0[0]+x1  "+( p0[0]+x1));
	
				latitude.set(point-i, p0[1]+y1);
				System.out.println("  p0[0]+y1  "+( p0[1]+y1));
			}
			else{}

		}
		
		
		p0[0] = longitude.get(node_tail);
		p0[1] = latitude.get(node_tail);
		p1[0] = longitudeCopy.get(point);
		p1[1] = latitudeCopy.get(point);
		ratio = Math.sqrt(Math.pow(p2[0]-p0[0],2)+Math.pow(p2[1]-p0[1],2))/Math.sqrt(Math.pow(p1[0]-p0[0], 2)+Math.pow(p1[1]-p0[1], 2));
		// rotate angle
		angle = computeAngle(p0,p1,p2);
		// figure out rotate direction
		bx = p2[0]-p0[0];
		by = p2[1]-p0[1];
		cx = p1[0]-p0[0];
		cy = p1[1]-p0[1];
		if((bx*cy-by*cx)>0)
			direction = 1; // b is on the clock-wise side of c
		else 
			direction = -1;
		if(direction==1)// clock-wise rotate, change angle to negative
			angle = -angle;
		for(int i = 1; i< 10-position; i++){
			if(bearing.get(point+i) != 0 ){
				p1[0] = longitudeCopy.get(point+i);
				p1[1] = latitudeCopy.get(point+i);
				x1 = 0;
				y1 = 0;
				System.out.println("p1[0] = " + p1[0] +"   p1[1] = "+ p1[1]);
	
				double xx = p1[0]-p0[0];
				double yy = p1[1]-p0[1];

				// for counter clock-wise rotate
				if(Math.abs(angle) < Math.PI/180*45)
				{
					x1 = (xx*Math.cos(angle) - yy*Math.sin(angle)) * ratio;
					y1 = (xx*Math.sin(angle) + yy*Math.cos(angle)) * ratio;	
				}
				longitude.set(point+i, p0[0]+x1);
				longitudeCopy.set(point+i, p0[0]+x1);

				System.out.println("point+i = "+(point+i)+" p0[0]+x1  "+( p0[0]+x1));
	
				latitude.set(point+i, p0[1]+y1);
				latitudeCopy.set(point+i, p0[1]+y1);

				System.out.println(" p0[0]+y1  "+( p0[1]+y1));
			}
			else{}

		}
		longitude.set(point, p2[0]);
		latitude.set(point, p2[1]);
		
	}

	public static double computeAngle (double[] p0, double[] p1, double[] p2)
	{
		double x1 = p1[0] - p0[0];
		double y1 = p1[1] - p0[1];
		double x2 = p2[0] - p0[0];
		double y2 = p2[1] - p0[1];
		double angle = Math.acos((x1*x2+y1*y2)/(Math.sqrt(Math.pow(x1,2)+Math.pow(y1,2))*Math.sqrt(Math.pow(x2,2)+Math.pow(y2,2))));
		System.out.println("=======================");
		System.out.println("angle = " + angle/Math.PI*180 + "\t\t"+angle+"pi");
		System.out.println("=======================");
		System.out.println("x1\t\t\t y1\t\t\t x2\t\t\t y2 ");
		System.out.println( x1+"\t"+y1+"\t"+ x2+"\t"+y2);	
		return angle;
	}
}