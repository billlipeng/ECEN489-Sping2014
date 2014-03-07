package com.mhardiman.project2;

import java.sql.SQLException;
import java.util.ArrayList;

import com.mhardiman.project2app.imu;


public class DBTest {

	private static ArrayList<imu> imus;
	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		imus = new ArrayList<imu>();
		
		DBHandler handler = new DBHandler("projtwo.db");
		
		imus = handler.getDBValues();
		
		DataHandler datahandler = new DataHandler(imus);
		datahandler.calculatePathSimple();
		datahandler.calculateInterpolation();
		datahandler.calculateDefault();
		//datahandler.calculatePath();
		//datahandler.calculatePathKalman();
		//System.out.println(dhandler.getTotalDistance());
		
	}

}
