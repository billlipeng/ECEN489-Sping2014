package com.example.gps_project;
//Too obvious
public class Book {

	 private int id;
	    private String title;
	    private String author;
	 
	    public Book(){}
	 
	    public Book(String title, String author) {
	        super();
	        this.title = title;
	        this.author = author;
	    }
	    
	    public int getId() {
	        return id;
	      }

	      public void setId(int id) {
	        this.id = id;
	      }

	      public String getAuthor() {
	        return author;
	      }

	      public void setAuthor(String author) {
	        this.author = author;
	      }
	 
	      
	      public String getTitle() {
		        return title;
		      }

	      public void setTitle(String title) {
	    	  	this.title = title;
		      }

	    //getters & setters
	 
	    @Override
	    public String toString() {
	        return "GPS [id=" + id + ", Latitude=" + title + ", Longitude=" + author
	                + "]";
	    }
	
	
}
