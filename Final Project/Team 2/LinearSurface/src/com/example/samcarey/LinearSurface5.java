package com.example.samcarey;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import delaunay_triangulation.Delaunay_Triangulation;
import delaunay_triangulation.Point_dt;

public class LinearSurface5 {
	
	static String folder = "/Users/samcarey/Desktop/Spring 2014/489/FinalProject/LinearInterpolation/";
	static int m = 100;
	static int n = 100;
	static int border = 50;
	static double[] origin;
	static double Latitude = 30.6146516;
	static double Longitude = -96.3455358;
	static double field_range = 100; 		 //meters
	static double rssi_range = 100; //dB
	static double earth_r = 6378100; //meters
	static int samples = 50;
	
	public static void main(String []args){
		origin = new double[2];
		origin[0] = Latitude;
		origin[1] = Longitude;
		ArrayList<Point_dt> raw_data = new ArrayList<Point_dt>();
		DBHandler handler = new DBHandler("Antenna1.db");
		raw_data = handler.getDBValues();
		int length = raw_data.size();
		System.out.println("data length = " + Integer.toString(length));
		//Point_dt[] fakeData = generate();
		Point_dt[] raw_array = new Point_dt[length];
		for (int i=0; i<length; i++)
		{
			raw_array[i] = raw_data.get(i);
		}
		toCsv(interpolate(raw_array), "after");
	}
	
	public static Point_dt[] generate(){
		Point_dt[] data = new Point_dt[samples];
		Random random = new Random();
		for (int i = 0 ; i < samples ; i++){
			data[i] = new Point_dt(xToLon(field_range*random.nextFloat()),yToLat(field_range*random.nextFloat()),rssi_range*random.nextFloat());
		}
		return data;
	}
	
	public static Point_dt[] toGrid(Point_dt[] coords){
		Point_dt[] points = new Point_dt[coords.length];
		for (int i = 0 ; i < points.length ; i++){
			points[i] = new Point_dt(lonToX(coords[i].x()), latToY(coords[i].y()), coords[i].z());
		}
		return points;
	}
	
	public static Point_dt[][] toCoord(Point_dt[][] points){
		Point_dt[][] coords = new Point_dt[points.length][points[0].length];
		for (int i = 0 ; i < m ; i++){
			for (int j = 0 ; j < n ; j++){
				coords[i][j] = new Point_dt(xToLon(points[i][j].x()), yToLat(points[i][j].y()), points[i][j].z());
			}
		}
		return coords;
	}
	
	public static Point_dt[][] interpolate(Point_dt[] data){		//linear interpolation
		Delaunay_Triangulation function = new Delaunay_Triangulation(data);
		Point_dt[][] interpolation = new Point_dt[m][n];
		double deltaX = field_range/(n-1);
		double deltaY = field_range/(m-1);
		
		for (int i = m-1 ; i >= 0 ; i--){
			for (int j = 0 ; j < n ; j++){
				if (function.contains((j*deltaX),(i*deltaY))){
					interpolation[i][j] = new Point_dt((j*deltaX), (i*deltaY), function.z((j*deltaX), (i*deltaY)));
				}else{
					interpolation[i][j] = new Point_dt((j*deltaX), (i*deltaY), 0);
				}
			}
		}
		toCsv(interpolation, "before");
		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		
		double left = 0;
		double right = 0;
		double above = 0;
        double below = 0;
        double old = 0;
        
        double goal_delta = 0.01;
		int iterations = 10000;
		for (int loop = 0 ; loop < iterations ; loop++){
			double max_delta = 0;
			for (int i = 0 ; i < m ; i++){
				for (int j = 0 ; j < n ; j++){
					if (!function.contains((j*deltaX),(i*deltaY))){
						if (j==0)   left  = interpolation[i][j].z();
						else 	    left  = interpolation[i][j-1].z();
						if (j==n-1) right = interpolation[i][j].z();
						else		right = interpolation[i][j+1].z();
						if (i==0)   above = interpolation[i][j].z();
						else		above = interpolation[i-1][j].z();
		                if (i==m-1) below = interpolation[i][j].z();
		                else 		below = interpolation[i+1][j].z();
		                
		                old = interpolation[i][j].z();
		                
		                interpolation[i][j] = new Point_dt(interpolation[i][j].x(),interpolation[i][j].y(),0.25*(left+right+above+below));
		                if (Math.abs(old-interpolation[i][j].z()) > max_delta){
		                       max_delta = Math.abs(old-interpolation[i][j].z());
		                }
					}
				}
			}
			if (max_delta < goal_delta) break;
		}
		return interpolation;
	}
	
	private static void toCsv(Point_dt[][] data, String file){	//prints "Lat,Lon\n" for all elements to csvPath
		try {
			FileWriter fw = new FileWriter(folder + file + ".csv");
	        PrintWriter pw = new PrintWriter(fw);
	        for (int i = 0 ; i < data.length ; i++){
	        	for (int j = 0 ; j < data[0].length ; j++){
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
