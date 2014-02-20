package com.mhardiman.gps_retrieval;
public class sensorData {
		public String sensor_id;
		public String sensor_type;
		public float sensor_value;

	    public sensorData() {
	    }

	    public sensorData(String sensorID, String sensorType, float sensorValue) {
	        this.sensor_id = sensorID;
	        this.sensor_type = sensorType;
	        this.sensor_value = sensorValue;
	    }

		public String getSensorID() {
			return sensor_id;
		}
		public String getSensorType() {
			return sensor_type;
		}
		public float getSensorValue() {
			return sensor_value;
		}
	}