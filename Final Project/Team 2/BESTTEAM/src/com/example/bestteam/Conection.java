package com.example.bestteam;

import java.io.Serializable;

public class Conection implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double Latitude;
	private double Longitude;
	int RIIS;
	
	public Conection (double Latitude, double Longitude, int RSSI){
		super();
		this.Latitude=Latitude;
		this.Longitude=Longitude;
		this.RIIS=RSSI;
	}
	
	
	public double getLatitude() {
		return Latitude;
	}
	public void setLatitude(double latitude) {
		Latitude = latitude;
	}
	public double getLongitude() {
		return Longitude;
	}
	public void setLongitude(double longitude) {
		Longitude = longitude;
	}
	public int getRIIS() {
		return RIIS;
	}
	public void setRIIS(int rIIS) {
		RIIS = rIIS;
	}
	
}


