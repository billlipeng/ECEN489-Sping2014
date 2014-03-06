

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHandler {
	
	private String mDBName;
	private String mFilepath = "jdbc:sqlite:/home/heffay/school/ECEN489-Spring2014/Project2/TeamJCM/";
	private Connection connection;
	private String mTablename= "table1";
	private ResultSet rs; //for handling the query results
	
	DBHandler(String dbname){
		
		mDBName = dbname;
		
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
	ResultSet getDBValues(){
		
		try{
			Statement query = connection.createStatement();
			String sql = "SELECT * from "+mTablename;
			rs = query.executeQuery(sql);
			//System.out.println(rs.getDouble(1));
			while(rs.next()){
			row2imu(rs);
			
			//after returning imu, add to array list;
			}
			query.close();
			return rs;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return rs; //return null if nothing
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
	void row2imu(ResultSet rs){
		try{
			//here is pseudo code
			//imu.setTime(rs.getDouble(1));
			//imu.setLatitude(rs.getDouble(2));
			//imu.setLongitude(rs.getDouble(3));
			//imu.setBearing(rs.getDouble(4));
			//imu.setSpeed(rs.getDouble(5));
			//return imu;
			System.out.println(rs.getDouble(1));
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

}
