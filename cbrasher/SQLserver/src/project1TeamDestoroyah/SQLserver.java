package project1TeamDestoroyah;

import java.io.*;
import java.sql.*;
import java.util.*;

public class SQLserver {
	
	public void openConnection(){
		String DATABASE_URL = "jdbc:odbc:embedded_sql_app"; // establish connection to database
	    try {
			Connection connection = DriverManager.getConnection( DATABASE_URL,"sa","123" );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    java.sql.Statement stmt = connect.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT a, b, c FROM Table1");
	    while (rs.next()) { // retrieve and print the values for the current row
	          int i = rs.getInt("a");
	          String s = rs.getString("b");
	          float f = rs.getFloat("c");
	          System.out.println("ROW = " + i + " " + s + " " + f);
	}

}
}
