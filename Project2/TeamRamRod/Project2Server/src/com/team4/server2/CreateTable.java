package com.team4.server2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateTable
{
  public static void main( String args[] )
  {
    Connection c = null;
    Statement stmt = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:C:/projtwo.db");
      System.out.println("Opened database successfully");

      stmt = c.createStatement();
      String sql = "CREATE TABLE [ecen489_project2_data] ("
      		+ "[_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
      		+ "[time] REAL, "
      		+ "[latitude] REAL, "
      		+ "[longitude] REAL, "
      		+ "[bearing] REAL, "
      		+ "[speed] REAL, "
      		+ "[accel_X] REAL, "
      		+ "[accel_Y] REAL, "
      		+ "[accel_Z] REAL, "
      		+ "[gyro_X] REAL, "
      		+ "[gyro_Y] REAL, "
      		+ "[gyro_Z] REAL, "
      		+ "[orientation_A] REAL, "
      		+ "[orientation_P] REAL, "
      		+ "[orientation_R] REAL, "
      		+ "[rotVec_X] REAL, "
      		+ "[rotVec_Y] REAL, "
      		+ "[rotVec_Z] REAL, "
      		+ "[rotVec_C] REAL, "
      		+ "[linACC_X] REAL, "
      		+ "[linACC_Y] REAL, "
      		+ "[linACC_Z] REAL, "
      		+ "[gravity_X] REAL, "
      		+ "[gravity_Y] REAL, "
      		+ "[gravity_Z] REAL)"; 
      stmt.executeUpdate(sql);
      stmt.close();
      c.close();
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Table created successfully");
  }
}
