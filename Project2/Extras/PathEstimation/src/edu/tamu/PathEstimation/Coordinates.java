package edu.tamu.PathEstimation;

public class Coordinates {
		
	private double latitude;
	private double longitude;
	private double altitude;

	public Coordinates (double latitude, double longitude, double altitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
	}
	
	public Coordinates (double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = 0.0;
	}

	public Coordinates () {
		this.latitude = 0.0;
		this.longitude = 0.0;
		this.altitude = 0.0;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public double getAltitude() {
		return altitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}
	
	public double[] getXYZ() {
		double[] XYZcoordinates;
		XYZcoordinates = new double[3];
		
		// Transform latitude and longitude into Cartesian coordinates
		XYZcoordinates[0] = Math.cos(latitude)*Math.cos(longitude);
		XYZcoordinates[1] = Math.cos(latitude)*Math.sin(longitude);
		XYZcoordinates[2] = Math.sin(latitude);
		
		return XYZcoordinates;
	}
	
	public void setXYZ(double[] XYZcoordinates) {
		double weightedHypotenuse = Math.sqrt(XYZcoordinates[0]*XYZcoordinates[0] + XYZcoordinates[1]*XYZcoordinates[1]);
		latitude = Math.atan2(XYZcoordinates[2], weightedHypotenuse);
		longitude = Math.atan2(XYZcoordinates[1], XYZcoordinates[0]);
		altitude = 0.0;
	}

}