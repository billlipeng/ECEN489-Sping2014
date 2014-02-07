package com.example.assignment3;


public class Entry {



    //private variables
    int _id;
    String _Entry;
    
    // Empty constructor
    public Entry(){

    }
    // constructor
    public Entry(int id, String entry){
        this._id = id;
        this._Entry = entry;
       }

    // constructor
    public Entry(String entry){
        this._Entry = entry;
       }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getEntry(){
        return this._Entry;
    }

    // setting name
    public void setEntry(String entry){
        this._Entry = entry;
    }
  

}