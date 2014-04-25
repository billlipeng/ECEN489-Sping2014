package com.mhardiman.finalpath;


import java.util.ArrayList;
import java.util.Random;



public class DataHandler{

	//class which helps crunch the data given for a bunch of imu
	
	private ArrayList<DataPoint> mPoints;
	private double alpha = 10.0f;
	private double beta = 2.0f;
	
	final static double longitude_unit = 95880.84;
	final static double latitude_unit = 110862.94;
	final static double LONGITUDE = -96.340525*longitude_unit;
	final static double LATITUDE = 30.620645*latitude_unit;
	
	//constructor
	DataHandler(ArrayList<DataPoint> points){
		mPoints = new ArrayList<DataPoint>(); //initialize
		mPoints = points;
		calculateInterpolation();
	}
	
	
	
	/*void applyModel()
	{	
		DataPoint rows[] = mPoints.toArray(new DataPoint[0]);
		for (int i=0; i<rows.length; i++)
		{
			//if this point needs to be calculated
			double distance = Math.sqrt(Math.pow(rows[i].latitude*latitude_unit - LATITUDE, 2) + Math.pow(rows[i].longitude*longitude_unit - LONGITUDE, 2));
			rows[i].rssi = alpha - beta*distance;
			System.out.println(rows[i].latitude +", " + rows[i].longitude + "; " + rows[i].rssi);
		}
	}*/
	
	
	
	void calculateInterpolation(){

		DataPoint rows[] = mPoints.toArray(new DataPoint[0]);
		System.out.println(rows[0].latitude + ", " + rows[0].longitude + "; " + rows[0].rssi);
		for(int i = 3; i < mPoints.size(); i+=3){

			
			double distance1 = Math.sqrt(Math.pow((rows[i-3].latitude - LATITUDE)*latitude_unit, 2) + Math.pow(rows[i-3].longitude*longitude_unit - LONGITUDE, 2));
			double distance4 = Math.sqrt(Math.pow((rows[i].latitude - LATITUDE)*latitude_unit, 2) + Math.pow(rows[i].longitude*longitude_unit - LONGITUDE, 2));
			double distance2 = Math.sqrt(Math.pow((rows[i-2].latitude - LATITUDE)*latitude_unit, 2) + Math.pow(rows[i-2].longitude*longitude_unit - LONGITUDE, 2));
			double distance3 = Math.sqrt(Math.pow((rows[i-1].latitude - LATITUDE)*latitude_unit, 2) + Math.pow(rows[i-1].longitude*longitude_unit - LONGITUDE, 2));
			
			double dbPerMeter = (rows[i].rssi - rows[i-3].rssi)/(distance4-distance1);
			if (Math.sqrt(Math.pow((rows[i].latitude - rows[i-1].latitude)*latitude_unit, 2) + Math.pow((rows[i].longitude - rows[i-1].longitude)*longitude_unit, 2)) < 18)
			{
				rows[i-2].rssi = Math.round(dbPerMeter*(distance2-distance4)+rows[i].rssi);	
				rows[i-1].rssi = Math.round(dbPerMeter*(distance3-distance4)+rows[i].rssi);			}
			else
			{
				/*distance1 = Math.sqrt(Math.pow((rows[i-4].latitude - LATITUDE)*latitude_unit, 2) + Math.pow(rows[i-4].longitude*longitude_unit - LONGITUDE, 2));
				distance3 = Math.sqrt(Math.pow((rows[i-1].latitude - LATITUDE)*latitude_unit, 2) + Math.pow(rows[i-1].longitude*longitude_unit - LONGITUDE, 2));
				distance4 = Math.sqrt(Math.pow((rows[i-3].latitude - LATITUDE)*latitude_unit, 2) + Math.pow(rows[i-3].longitude*longitude_unit - LONGITUDE, 2));
				distance2 = Math.sqrt(Math.pow((rows[i-2].latitude - LATITUDE)*latitude_unit, 2) + Math.pow(rows[i-2].longitude*longitude_unit - LONGITUDE, 2));
				
				dbPerMeter = (rows[i-1].rssi - rows[i-4].rssi)/(distance4-distance1);
				*/
				rows[i-2].rssi = rows[i-3].rssi;
				rows[i-1].rssi = rows[i-3].rssi;
			}
			
			
			
			System.out.println(rows[i-2].latitude + ", " + rows[i-2].longitude + "; " + rows[i-2].rssi);
			System.out.println(rows[i-1].latitude + ", " + rows[i-1].longitude + "; " + rows[i-1].rssi);
			System.out.println(rows[i].latitude + ", " + rows[i].longitude + "; " + rows[i].rssi);
			

		}
		
		
		System.out.println("Done interpolating.");
	}
	
	

}
