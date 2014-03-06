package com.example.samcarey;

public class SensorData {
	  public String sensor_id;
	  public String sensor_type;
	  public Double sensor_value;
	  public SensorData(String _sensor_id, String _sensor_type, Double _sensor_value) {
		  sensor_id = _sensor_id;
		  sensor_type = _sensor_type;
		  sensor_value = _sensor_value;
	  }
}
