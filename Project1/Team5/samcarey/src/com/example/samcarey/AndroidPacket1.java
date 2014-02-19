package com.example.samcarey;

import java.io.Serializable;

public class AndroidPacket1 implements Serializable {
	private static final long serialVersionUID = 1L;
	public static String client_id = "TEAM5";
	public String run_id;
	public String timestamp;
	public String date;
	public String attribute;
	public Double latitude;
	public Double longitude;
	public String sensor_id;
	public String sensor_type;
	public Double sensor_value;
	public Double bearing;
	public Double speed;
	public Double altitude;
	
	// constructor
	public  AndroidPacket1( String run_id, 
							String timestamp, 
							String date,
							String attribute,
							Double latitude,
							Double longitude,
							String sensor_id,
							String sensor_type,
							Double sensor_value,
							Double bearing,
							Double speed,
							Double altitude) {
		this.run_id = run_id;
		this.timestamp = timestamp;
		this.date = date;
		this.attribute = attribute;
		this.latitude = latitude;
		this.longitude = longitude;
		this.sensor_id = sensor_id;
		this.sensor_type = sensor_type;
		this.sensor_value = sensor_value;
		this.bearing = bearing;
		this.speed = speed;
		this.altitude = altitude;
	}

	public  AndroidPacket1( String run_id, 
							String timestamp, 
							String date,
							String attribute,
							Double latitude,
							Double longitude,
							String sensor_id,
							String sensor_type,
							Double sensor_value) {
		this.run_id = run_id;
		this.timestamp = timestamp;
		this.date = date;
		this.attribute = attribute;
		this.latitude = latitude;
		this.longitude = longitude;
		this.sensor_id = sensor_id;
		this.sensor_type = sensor_type;
		this.sensor_value = sensor_value;
		this.bearing = null;
		this.speed = null;
		this.altitude = null;
	}
	
	public  AndroidPacket1( String run_id, 
							String timestamp, 
							String date,
							String attribute,
							Double latitude,
							Double longitude) {
		this.run_id = run_id;
		this.timestamp = timestamp;
		this.date = date;
		this.attribute = attribute;
		this.latitude = latitude;
		this.longitude = longitude;
		this.sensor_id = null;
		this.sensor_type = null;
		this.sensor_value = null;
		this.bearing = null;
		this.speed = null;
		this.altitude = null;
	}
}
