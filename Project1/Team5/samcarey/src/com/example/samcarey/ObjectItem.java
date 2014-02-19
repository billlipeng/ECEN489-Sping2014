package com.example.samcarey;

import java.io.Serializable;

public class ObjectItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int itemId;
	public Double latitude;
	public Double longitude;
	    // constructor
	public ObjectItem(int itemId, Double latitude, Double longitude) {
		this.itemId = itemId;
	    this.latitude = latitude;
	    this.longitude = longitude;
	}
}
