package com.jdterrell.p2;

import java.util.ArrayList;
import java.lang.Math;

import com.jdterrell.p2.AndroidPacket2;

/* this algorithm calculates the latitude and longitude of an unknown location using speed, bearing, and initial GPS
   location, resets initial GPS location every 10 iterations.
*/


// get data from fusion table
//need some type of loop structure to perform on the table starting from row 1 and resetting every 10 intervals

public class Algorithm {
	
	double t_sample = 10;	//10 seconds between every measurement point
	double R = 6371000; //mean radius of Earth in meters
	
	public Algorithm(ArrayList<AndroidPacket2> data)	{
		try {
	    

//print contents of arraylist
//pick out values we want to work with

//perform the calculation
			
			//all degree values must be converted to radians for trigonometric operations
			double lat1 = deg2rad(data.latitude);
			double lon1 = deg2rad(data.longitude);
			double bearing = deg2rad(data.bearing);	
			
			double speed = data.speed;
			double distance = speed/t_sample;
			
			double lat2 = Math.asin( Math.sin(lat1)*Math.cos(distance/R) + 
			              Math.cos(lat1)*Math.sin(distance/R)*Math.cos(bearing) );
			double lon2 = lon1 + Math.atan2(Math.sin(bearing)*Math.sin(distance/R)*Math.cos(lat1), 
			                     Math.cos(distance/R)-Math.sin(lat1)*Math.sin(lat2));
			
			lat2 = rad2deg(lat2);
			lon2 = rad2deg(lon2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	// function to convert decimal degrees to radians
		private double deg2rad(double deg) {
			return (deg * Math.PI / 180.0);
		}

	// function to convert radians to decimal degrees
		private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
		}	
	
}
