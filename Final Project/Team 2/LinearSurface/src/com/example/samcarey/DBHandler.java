package com.example.samcarey;
import delaunay_triangulation.Point_dt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBHandler {
	
	private String mDBName;
	private String mFilepath = "jdbc:sqlite:C:/Users/Mark/Documents/Eclipse/LinearSurface/";
	private Connection connection;
	private String mTablename= "AntennaOne";
	private ResultSet rs; //for handling the query results
	private ArrayList<Point_dt> raw_data;

	
	
	DBHandler(String dbname){
		
		mDBName = dbname;
		raw_data = new ArrayList<Point_dt>();
		
		
		try{
			Class.forName("org.sqlite.JDBC");
			
			try{
				connection = DriverManager.getConnection(mFilepath+dbname);
			}catch(SQLException e1){
				e1.printStackTrace();
			}
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		
	}
	
	//Getters
	String getDBName(){
		return mDBName;
	}
	
	String getmFilepath(){
		return mFilepath;
	}
	
	//instead of ResultSet, return ArrayList<Point_dt>
	ArrayList<Point_dt> getDBValues(){
			raw_data.clear(); //re-initalize arraylist
			Point_dt temp;
		try{
			Statement query = connection.createStatement();
			String sql = "SELECT * from "+mTablename;
			rs = query.executeQuery(sql);
			//System.out.println(rs.getDouble(1));
			
			while(rs.next()){
			temp = row2point(rs);
			raw_data.add(temp);
			
			
			}
			query.close();
			return raw_data;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return raw_data; //return null if nothing
	}
	
	
	//setters
	
	void changeDBName(String newName){
		mDBName = newName;
	}
	
	//Handles
	void quit(){
		try{
			connection.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	//Information swap.. we need to return type imu here
	Point_dt row2point(ResultSet rs){
		Point_dt temp = new Point_dt();
		try{
			
			double z = rs.getDouble(1);
			double y =  rs.getDouble(2);
			double x =  rs.getDouble(3);
			temp = new Point_dt(x, y, z);
			return temp;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return temp;
	}
	

}
