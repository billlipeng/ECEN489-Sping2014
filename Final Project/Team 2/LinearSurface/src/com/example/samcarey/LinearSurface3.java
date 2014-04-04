package com.example.samcarey;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import delaunay_triangulation.Delaunay_Triangulation;
import delaunay_triangulation.Point_dt;

public class LinearSurface3 {
	
	static String folder = "/Users/samcarey/Desktop/Spring 2014/489/FinalProject/LinearInterpolation/";
	static int m = 100;
	static int n = 100;
	static int border = 50;
	static double[] origin;
	static double Latitude = 30.6146516;
	static double Longitude = -96.3455358;
	static double field_range = 100; 		 //meters
	static double rssi_range = -100; //dB
	static double earth_r = 6378100; //meters
	static int samples = 50;
	
	public static void main(String []args){
		origin = new double[2];
		origin[0] = Latitude;
		origin[1] = Longitude;
		double[][] fakeData = generate();
		toCsv(interpolate(toGrid(fakeData)), "test");
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
	
	public static Point_dt[][] interpolate(Point_dt[] data){		//linear interpolation
		double[][] grid = new double[m][n];
		for (int i = 0 ; i < m ; i++){
			for (int j = 0 ; j < n ; j++){
				grid[i][j] = 0.0;
			}
		}
		for (int i = 0 ; i < data.length ; i++){
			grid[(int) (data[i].y()/field_range*m)][(int) (data[i].x()/field_range*n)] = data[i].z();
		}
		boolean[][] constant = new boolean[m][n];
		for (int i = 0 ; i < m ; i++){
			for (int j = 0 ; j < n ; j++){
				if ( grid[i][j] != 0 || i==0 || i==m-1 || j==0 || j==n-1   ){
					constant[i][j] = true;
				}else{
					constant[i][j] = false;
				}
			}
		}
		int iterations = 10000;
		for (int loop = 0 ; loop < iterations ; loop++){
			double max_delta = 0;
			for (int i = 0 ; i < m ; i++){
				for (int j = 0 ; j < n ; j++){
					if (!constant[i][j]){
						double omega = 1.25;
						double left = grid[i][j-1];
						double right = grid[i][j+1];
						double above = grid[i-1][j];
		                double below = grid[i+1][j];
		                double old = grid[i][j];
		                
		                //if (left != 0) System.out.println(left);
		                //if (right != 0) System.out.println(right);
		                //if (below != 0) System.out.println(below);
		                //if (above != 0) System.out.println(above);
		                //System.out.println(residual(left,right,above,below,old));
		                
		                grid[i][j] = old + omega*residual(left,right,above,below,old);
		                if (Math.abs(old-grid[i][j]) > max_delta){
		                       max_delta = Math.abs(old-grid[i][j]);
		                }
					}
				}
			}
			System.out.println(max_delta);
		}
		
		Point_dt[][] interpolation = new Point_dt[m][n];
		for (int i = 0 ; i < m ; i++){
			for (int j = 0 ; j < n ; j++){
				interpolation[i][j] = new Point_dt(j/n*field_range,i/m*field_range, grid[i][j]);
			}
		}
		return interpolation;
	}
	private static double residual(double left,double right,double above,double below, double old){
		
		return 0.25*(left+right+above+below) - old;
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
