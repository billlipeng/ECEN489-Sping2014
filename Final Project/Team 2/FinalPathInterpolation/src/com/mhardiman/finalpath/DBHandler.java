package com.mhardiman.finalpath;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DBHandler {
	
	private String mDBName;
	private String mFilepath = "jdbc:sqlite:C:/Users/Mark/Documents/Eclipse/FinalPathInterpolation/";
	private Connection connection;
	private String mTablename= "master";
	private ResultSet rs; //for handling the query results
	private ArrayList<DataPoint> points;
	
	DBHandler(String dbname){
		
		mDBName = dbname;
		points = new ArrayList<DataPoint>();
		
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
	
	
	ArrayList<DataPoint> getDBValues(){
			points = new ArrayList<DataPoint>();
			DataPoint temp;
		try{
			Statement query = connection.createStatement();
			String sql = "SELECT * from "+mTablename;
			rs = query.executeQuery(sql);
			//System.out.println(rs.getDouble(1));
			while(rs.next()){
			temp = row2point(rs);
			points.add(temp);			
			}
			query.close();
			return points;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return points; //return null if nothing
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
	DataPoint row2point(ResultSet rs){
		DataPoint temp = new DataPoint();
		try{
			
			temp.latitude =  rs.getDouble(3);
			temp.longitude =  rs.getDouble(4);
			temp.rssi =  rs.getInt(2);
			//System.out.println(temp.latitude +", " + temp.longitude +"; " + temp.rssi);
			return temp;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return temp;
	}
	

}
