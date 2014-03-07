package com.mhardiman.project2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mhardiman.project2app.imu;

public class DBHandler {
	
	private String mDBName;
	private String mFilepath = "jdbc:sqlite:C:/Users/Mark/Documents/Eclipse/Project2ServerFinal/";
	private Connection connection;
	private String mTablename= "DataTable1";
	private ResultSet rs; //for handling the query results
	private ArrayList<imu> imus;
	
	DBHandler(String dbname){
		
		mDBName = dbname;
		imus = new ArrayList<imu>();
		
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
	
	//instead of ResultSet, return ArrayList<imu>
	ArrayList<imu> getDBValues(){
			imus.clear(); //re-initalize arraylist
			imu temp;
		try{
			Statement query = connection.createStatement();
			String sql = "SELECT * from "+mTablename;
			rs = query.executeQuery(sql);
			//System.out.println(rs.getDouble(1));
			while(rs.next()){
			temp = row2imu(rs);
			imus.add(temp);
			//after returning imu, add to array list;
			
			}
			query.close();
			return imus;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return imus; //return null if nothing
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
	imu row2imu(ResultSet rs){
		imu temp = new imu();
		try{
			
			temp.time =  rs.getLong(1);
			temp.longitude =  rs.getDouble(2);
			temp.latitude =  rs.getDouble(3);
			temp.bearing =  rs.getDouble(4);
			temp.speed =  rs.getDouble(5);
			return temp;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return temp;
	}
	

}
