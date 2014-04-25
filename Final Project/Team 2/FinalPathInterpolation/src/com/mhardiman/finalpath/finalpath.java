package com.mhardiman.finalpath;

import java.util.ArrayList;

public class finalpath {
	
	public static void main(String[] args)
	{
		String dbName = "TestDB.db";
		DBHandler dbhandler = new DBHandler(dbName);
		ArrayList<DataPoint> points = dbhandler.getDBValues();
		DataHandler datahandler = new DataHandler(points);
	}
	
}
