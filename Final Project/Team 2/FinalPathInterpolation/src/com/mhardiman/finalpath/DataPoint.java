package com.mhardiman.finalpath;

public class DataPoint {

	public double latitude;
	public double longitude;
	public double rssi;
	
	public DataPoint(double latitude, double longitude, double rssi)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.rssi = rssi;
	}
	
	public DataPoint()
	{
		latitude = 0f;
		longitude = 0f;
		rssi = 0f;
	}
}
