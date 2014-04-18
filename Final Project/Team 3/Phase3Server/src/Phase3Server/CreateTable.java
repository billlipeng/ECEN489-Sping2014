package Phase3Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class CreateTable {
	public static void main(String args[]){
		Connection con = null;
		Statement stmt = null;
		String table = "RSSI_Estimated";
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:D:/FinalProjectTeam3.db");
			
			stmt = con.createStatement();
			
			String sql = "Create Table " + table +"("
//					+ "_id ,"
					+ "RSSI REAL, "
		      		+ "latitude REAL, "
		      		+ "longitude REAL, "
		      		+ "weight REAL "
		      		+ ")"; 
			stmt.executeUpdate(sql);
			stmt.close();
			con.close();
					
					
					
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Table "+ table +" Created!");
		
	}
}
