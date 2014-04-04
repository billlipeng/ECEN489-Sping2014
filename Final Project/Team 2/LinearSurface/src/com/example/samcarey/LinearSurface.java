package com.example.samcarey;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import delaunay_triangulation.Delaunay_Triangulation;
import delaunay_triangulation.Point_dt;

public class LinearSurface {
	
	static String folder = "C:\\Users\\Mark\\Documents\\Eclipse\\LinearSurface\\";
	static int m = 100;
	static int n = 100;
	static double[] origin;
	static double field_range = 100; 		 //meters
	static double rssi_range = -100; //dB
	static double earth_r = 6378100; //meters
	static int samples = 50;
	
	public static void main(String []args){
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
		toCsv(interpolate(raw_array), "test");
	}
	
	public static Point_dt[] generate(){
		Point_dt[] data = new Point_dt[samples];
		Random random = new Random();
		for (int i = 0 ; i < samples ; i++){
			data[i] = new Point_dt(field_range*random.nextFloat(),field_range*random.nextFloat(),rssi_range*random.nextFloat());
			//System.out.println(data[i].toString());
		}
		return data;
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
	
	public double latToY(double degrees){
		return toRadians(degrees)*earth_r;
	}
	
	public double yToLat(double meters){
		return toDegrees(meters/earth_r);
	}
	
	public double lonToX(double degrees){
		return toRadians(degrees)*earth_r*Math.cos(origin[0]);
	}
	
	public double xToLon(double meters){
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
