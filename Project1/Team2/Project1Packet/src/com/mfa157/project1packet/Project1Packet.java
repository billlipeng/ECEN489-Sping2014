package com.mfa157.project1packet;

import java.io.Serializable;
import java.util.ArrayList;

public class Project1Packet implements Serializable {
	
	private Double sensor_value[];
	private String sensor_id[];
	private String sensor_type[];
	private String latitude[];
	private String longitude[];
	private String time[];
	private String date[];
	private String client_id[];
	private String run_id[];
	private String attribute[];
	
	public Project1Packet(Double sensor_value[], String sensor_id[], String sensor_type[], String latitude[], String longitude[], String time[], String date[], String client_id[], String run_id[], String attribute[]) {
		super();
		this.sensor_value = sensor_value;
		this.sensor_id = sensor_id;
		this.sensor_type = sensor_type;
		this.latitude = latitude;
		this.longitude = longitude;
		this.time = time;
		this.date = date;
		this.client_id = client_id;
		this.run_id = run_id;
		this.attribute = attribute;
	}
	
	public Project1Packet(Double sensor_value[], String sensor_id[], String sensor_type[], String latitude[], String longitude[], String time[], String date[]) {
		super();
		this.sensor_value = sensor_value;
		this.sensor_id = sensor_id;
		this.sensor_type = sensor_type;
		this.latitude = latitude;
		this.longitude = longitude;
		this.time = time;
		this.date = date;

	}

	/**
	 * @return the sensor_value
	 */
	public Double[] getSensor_value() {
		return sensor_value;
	}
	/**
	 * @param sensor_value the sensor_value to set
	 */
	public void setSensor_value(Double[] sensor_value) {
		this.sensor_value = sensor_value;
	}
	/**
	 * @return the sensor_id
	 */
	public String[] getSensor_id() {
		return sensor_id;
	}
	/**
	 * @param sensor_id the sensor_id to set
	 */
	public void setSensor_id(String[] sensor_id) {
		this.sensor_id = sensor_id;
	}
	/**
	 * @return the sensor_type
	 */
	public String[] getSensor_type() {
		return sensor_type;
	}
	/**
	 * @param sensor_type the sensor_type to set
	 */
	public void setSensor_type(String[] sensor_type) {
		this.sensor_type = sensor_type;
	}
	/**
	 * @return the latitude
	 */
	public String[] getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String[] latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public String[] getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String[] longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the time
	 */
	public String[] getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(String[] time) {
		this.time = time;
	}
	/**
	 * @return the date
	 */
	public String[] getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String[] date) {
		this.date = date;
	}

	/**
	 * @return the client_id
	 */
	public String[] getClient_id() {
		return client_id;
	}

	/**
	 * @param client_id the client_id to set
	 */
	public void setClient_id(String[] client_id) {
		this.client_id = client_id;
	}

	/**
	 * @return the run_id
	 */
	public String[] getRun_id() {
		return run_id;
	}

	/**
	 * @param run_id the run_id to set
	 */
	public void setRun_id(String[] run_id) {
		this.run_id = run_id;
	}

	/**
	 * @return the attribute
	 */
	public String[] getAttribute() {
		return attribute;
	}

	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(String[] attribute) {
		this.attribute = attribute;
	}
	


}
