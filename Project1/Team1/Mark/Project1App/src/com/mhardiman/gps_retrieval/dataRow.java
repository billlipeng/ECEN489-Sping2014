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
	
	dataRow()
	{
		sensorID = "";
		runID = 0;
		latitude = 0.0f;
		longitude = 0.0f;
		date = "0/0/0";
		time = "0:0:0";
		sensorValue = 0.0f;
		attribute = "";
	}
	
	/*dataRow(dataRow row)
	{
		sensorID = row.sensorID;
		runID = row.runID;
		latitude = row.latitude;
		longitude = row.longitude;
		date = row.date;
		time = row.time;
		sensorValue = row.sensorValue;
		attribute = row.attribute;
	}*/
}