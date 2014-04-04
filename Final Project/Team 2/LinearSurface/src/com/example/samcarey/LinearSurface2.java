package com.example.samcarey;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import delaunay_triangulation.Delaunay_Triangulation;
import delaunay_triangulation.Point_dt;

public class LinearSurface2 {
	
	static String folder = "C:\\Users\\Mark\\Documents\\Eclipse\\Project3Extrapolation\\";
	static int m = 100;
	static int n = 100;
	static double[] origin;
	static double Latitude = 30.6146516;
	static double Longitude = -96.3455358;
	static double field_range = 100; 		 //meters
	static double rssi_range = -100; //dB
	static double earth_r = 6378100; //meters
	static double longitude_unit = 95880.84f;
	static double latitude_unit = 110862.94f;
	static int samples = 50;
	static float alpha = 5.0f;
	
	public static void main(String []args){
		System.out.println(folder);
		origin = new double[2];
		origin[0] = Latitude;
		origin[1] = Longitude;
		double[][] fakeData = generate();
		Point_dt[] grid_data = toGrid(fakeData);
		Point_dt[][] heat_map = interpolate(grid_data);
		extrapolate(heat_map, grid_data);
		toCsv(heat_map, "test");
		//InsertFusionTables uploader = new InsertFusionTables(heat_map, m, n);
		//uploader.fusion_upload();
	}
	
	public static double[][] generate(){
		//System.out.println("generate");
		double[][] data = new double[samples][3];
		Random random = new Random();
		for (int i = 0 ; i < samples ; i++){
			data[i][0] = xToLon(field_range*random.nextFloat());
			data[i][1] = yToLat(field_range*random.nextFloat());
			data[i][2] =  rssi_range*random.nextFloat();
		}
		return data;
	}
	
	public static Point_dt[] toGrid(double[][] coords){
		//System.out.println("toGrid");
		Point_dt[] points = new Point_dt[coords.length];
		for (int i = 0 ; i < points.length ; i++){
			points[i] = new Point_dt(lonToX(coords[i][0]), latToY(coords[i][1]), coords[i][2]);
		}
		return points;
	}
	
	public static Point_dt[][] interpolate(Point_dt[] data){
		Delaunay_Triangulation function = new Delaunay_Triangulation(data);
		Point_dt[][] interpolation = new Point_dt[m][n];
		System.out.println("x: " + function.bb_min().x() + " - " + function.bb_max().x());
		System.out.println("y: " + function.bb_min().y() + " - " + function.bb_max().y());
		
		double x_start = function.bb_min().x();
		double x_end   = function.bb_max().x();
		double y_start = function.bb_min().y();
		double y_end   = function.bb_max().y();;
		double x_width = x_end-x_start;
		double y_width = y_end-y_start;
		double deltaX = x_width/(n-1);
		double deltaY = y_width/(m-1);
		
		for (int i = m-1 ; i >= 0 ; i--){
			for (int j = 0 ; j < n ; j++){
				//System.out.println((x_start+j*deltaX)+"     "+(y_start+i*deltaY));
				if (function.contains((x_start+j*deltaX),(y_start+i*deltaY))){
					interpolation[i][j] = new Point_dt((x_start+j*deltaX), (y_start+i*deltaY), function.z((x_start+j*deltaX), (y_start+i*deltaY)));
					//System.out.println(interpolation[i][j].toString());
				}else{
					interpolation[i][j] = new Point_dt((x_start+j*deltaX), (y_start+i*deltaY), 0);
				}
			}
		}
		return interpolation;
	}
	
	public static void extrapolate(Point_dt[][] heat_map, Point_dt[] grid_data)
	{
		for (int i = m-1 ; i >= 0 ; i--){
			for (int j = 0 ; j < n ; j++){
		  		if (heat_map[i][j].z()==0) //if this point hasn't been assigned a value, we need to perform the max finding
		  		{
		 			double max_value = -10000.0f;
		 			for (int k=0; k<grid_data.length; k++)
		  			{
		  				//compute distance between i.(x,y) and k.(x,y)
		 				double distance = heat_map[i][j].distance(grid_data[k]); //dunno if this works
		  				double decayed_value = grid_data[k].z() - alpha/(distance);//maybe square this
		  				if (decayed_value > max_value)
		  					max_value = decayed_value;
		  			}
		  			heat_map[i][j] = new Point_dt(heat_map[i][j].x(), heat_map[i][j].y(),  max_value); //assign the rssi value as the highest probable value
		  		}
			}
		  }
		return; 
	}
	
	private static Point_dt[] guessCorners(Point_dt[] points){
		int extra = 4;
		Point_dt[] newPoints = new Point_dt[points.length+extra];
		System.arraycopy(points, 0, newPoints, 0, points.length);
		
		Delaunay_Triangulation function = new Delaunay_Triangulation(points);
		double x_start = function.bb_min().x();
		double x_end   = function.bb_max().x();
		double y_start = function.bb_min().y();
		double y_end   = function.bb_max().y();
		System.out.println("x: " + x_start + " - " + x_end);
		System.out.println("y: " + y_start + " - " + y_end);
		
		newPoints[points.length + 0] = guessPoint(points, new Point_dt(x_start,y_start));
		newPoints[points.length + 1] = guessPoint(points, new Point_dt(x_start,y_end));
		newPoints[points.length + 2] = guessPoint(points, new Point_dt(x_end,y_start));
		newPoints[points.length + 3] = guessPoint(points, new Point_dt(x_end,y_end));

		return newPoints;
	}
	
	private static Point_dt guessPoint(Point_dt[] map, Point_dt point){
		
		point = new Point_dt(point.x(),point.y(),0);
		return point;
	}
	
	private static void toCsv(Point_dt[][] data, String file){	//prints "Lat,Lon\n" for all elements to csvPath
		try {
			FileWriter fw = new FileWriter(folder + file + ".csv");
	        PrintWriter pw = new PrintWriter(fw);
	        //System.out.println(data[0].length + "    " + data.length);
	        for (int i = 0 ; i < data.length ; i++){
	        	for (int j = 0 ; j < data[0].length ; j++){
	        		//System.out.println(data[i][j].z());
		        	pw.print(data[i][j].z());
		        	if (j < (data[0].length-1)) pw.print(",");
		        	else pw.println("");
	        	}
	        }
	        
	        pw.flush();
	        pw.close();
	        fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*private static void db_upload(Point_dt[][] heat_map)
	{
		
		System.out.println("Attempting connection to SQL server.");

		//SQL DATA ENTRY
		try { 
			Class.forName("org.sqlite.JDBC");
		  	Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/Mark/Documents/Eclipse/proj3.db");

		  	String insert;
		  	for (int i = m-1 ; i >= 0 ; i--)
		  	{
				for (int j = 0 ; j < n ; j++)
				{
					connection.setAutoCommit(false);
					insert += "INSERT INTO proj3_heatmap(latitude, longitude, RSSI) VALUES(?,?,?); ";
					
					insertData.setDouble(1, heat_map[i][j].);
					insertData.setDouble(2, row.time);
					insertData.setDouble(3, row.teamID);
					//format run_id: teamid_yyyymmdd_hhmmss
					
				}
		  	}
		  	PreparedStatement insertData = connection.prepareStatement(insert);
		  	insertData.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);
			try
			{							
				System.out.println("Socket terminated.");
				connection.close();				
			}					
			catch(SQLException e)
			{
				System.out.println("SQL Exception failed trying to close!");
				System.err.println(e.getMessage());
			}

		}
		catch (SQLException e) 
		{
			System.err.println(e.getMessage());
		}

		
	}*/
	
	public double[] toYX(double[] coord){
		double[] yx = new double[2];
		yx[0] = latToY(coord[0]-origin[0]);
		yx[1] = lonToX(coord[1]-origin[1]);
		return yx;
	}
	
	public double[] toCoord(double[] yx){
		double[] coord = new double[2];
		coord[0] = yToLat(yx[0])+origin[0];
		coord[1] = xToLon(yx[0])+origin[1];
		return coord;
	}
	
	public static double latToY(double degrees){
		return toRadians(degrees)*earth_r;
	}
	
	public static double yToLat(double meters){
		return toDegrees(meters/earth_r);
	}
	
	public static double lonToX(double degrees){
		return toRadians(degrees)*earth_r*Math.cos(origin[0]);
	}
	
	public static double xToLon(double meters){
		return toDegrees(meters/(earth_r*Math.cos(origin[0])));
	}
	
	public double distance(double[] first, double[] second){
		return Math.sqrt((latToY(first[0]-second[0])*latToY(first[0]-second[0]))+(lonToX(first[1]-second[1])*lonToX(first[1]-second[1])));
	}
	private static double toRadians(double degrees){
		return degrees * Math.PI / 180;
	}
	private static double toDegrees(double radians){
		return radians * 180 / Math.PI;
	}
}
