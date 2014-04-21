package com.example.bestteam;

public class Book {

	 private int id;
	    private String latitude;
	    private String longitude;
	    private String rssi;
	 
	    public Book(){}
	 
	    public Book(String rssi,String latitude, String longitude) {
		//  public Book(String latitude, String longitude) {

	        super();
	        this.latitude = latitude;
	        this.longitude = longitude;
	        this.rssi=rssi;
	    }
	    
	    public int getId() {
	        return id;
	      }

	      public void setId(int id) {
	        this.id = id;
	      }
		  public String getrssi() {
		        return rssi;
		     }

		      public void setrssi(String rssi) {
		        this.rssi = rssi;
		      }

	      public String getlongitude() {
	        return longitude;
	      }

	      public void setlongitude(String longitude) {
	        this.longitude = longitude;
	      }
	 
	      
	      public String getlatitude() {
		        return latitude;
		      }

	      public void setlatitude(String latitude) {
	    	  	this.latitude = latitude;
		      }

	    //getters & setters
	 
	    @Override
	    public String toString() {
	        return "GPS [id=" + id + ", rssi=" + rssi + ", Latitude=" + latitude + ", Longitude=" + longitude
	                + "]";
	    }
	
	
}
