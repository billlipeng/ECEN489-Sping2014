import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main
{
  public static void main( String args[] )
  {
	  Scanner user_input = new Scanner( System.in );
	  boolean cont=true;
	  String input = null;
	  Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:test.db");
	      c.setAutoCommit(false);
	      System.out.println("Opened database successfully");
	      
	      while(cont){
		      System.out.println("What would you like to do?");
		      System.out.println("Enter Command: Read, Write, Exit");
		      input = user_input.next( );
		      System.out.println(input);
		      //read table
		      if(input.equals("read") || input.equals("Read") ){
		    	  System.out.println("Reading...");
			      stmt = c.createStatement();
			      ResultSet rs = stmt.executeQuery( "SELECT * FROM Table1;" );
			      while ( rs.next() ) {
			         int id = rs.getInt("id");
			         String  entry = rs.getString("Entry");

			         System.out.println( "ID = " + id );
			         System.out.println( "Entry = " + entry );
			         System.out.println();
			      }
			      rs.close();
			      stmt.close();
		    	  
		      }
		      
		    //Write/edit data
		      else if(input.equals("write") || input.equals("Write")){
		    	  System.out.println("Enter an ID, then enter data to store");
		    	  int id = user_input.nextInt( );
		    	  String sql=null;
		    	  boolean exists=false;
			      input = user_input.next( );
		    	  stmt = c.createStatement();
		    	  ResultSet rs = stmt.executeQuery( "SELECT * FROM Table1;" );
			      while ( rs.next() ) {
			         int idout = rs.getInt("id");
			         String  entry = rs.getString("Entry");
			         if(idout==id){
			        	 exists=true;
			        	 break;
			         }
			        	 
			      }
			     if(exists){
		    	  sql = "UPDATE TABLE1 set Entry = '"+input+"' where ID="+id+";";
			     }
			     else{
		          sql = "INSERT INTO Table1 (ID, Entry) " +
		    	 				"VALUES ("+id+", '" + input +"');"; 
			     }
		          stmt.executeUpdate(sql);
		          stmt.close();
		          System.out.println("Data written succesfully!");
		      }
		      
		    //exit 
		      else if(input.equals("exit") || input.equals("Exit")){
		    	  cont=false;
		      }
		      else{
		    	  System.out.println("Invalid command, please try again");
		      }
	      }
	      
	      c.commit();
	      c.close();
	      
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Operation done successfully");
  }
}