package com.mhardiman.gps_retrieval;
import java.io.Serializable;
public class dataRow implements Serializable
{
	public String sensorID;
	public static String teamID = "Team1";
	public int runID;
	public double latitude;
	public double longitude;
	public String date;
	public String time;
	public float sensorValue;
	public String sensorType;
	public String attribute; //start, stop, sensor reading or not
}