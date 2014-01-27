package com.zpartal.android3;

public class LuxValue {
	private int id;
	private String lux;
	private String time;
	
	public LuxValue() {}
	
	public LuxValue(String lux, String time) {
		this.lux = lux;
		this.time = time;
	}
	public LuxValue(int id, String lux, String time) {
		super();
		this.id = id;
		this.lux = lux;
		this.time = time;
	}
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLux() {
		return lux;
	}
	public void setLux(String lux) {
		this.lux = lux;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}	
	
	@Override
	public String toString() {
		return "Time: " + String.valueOf(time) + " Lux: " + String.valueOf(lux);
	}

}
