package com.example.project1;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.Callable;

import android.os.Handler;

public class Timer {
	//TIMER Variables
	public long strtTime = 0;
	Handler timerHandler = new Handler();
	public int interv = -1;
	Callable<Integer> func;
	
	//Timer, executes the function whenever a multiple of the interval is reached
	Runnable timerRunnable = new Runnable() {
	@Override
	  public void run() {
	      long millis = System.currentTimeMillis() - strtTime;
	      int seconds = (int) (millis / 1000);
	      //int minutes = seconds / 60;
	      seconds = seconds % 60;
	
	      //.setText(String.format("%d:%02d", minutes, seconds));
	          if(seconds%interv == 0)
	          {
	        	  try{
	        	  func.call();
	        	  }
	        	  catch(Exception e)
	        	  {        		  
	        	  }
	          }
	          timerHandler.postDelayed(this, 500);
	      }
	  };
	  
	// Constructor, need to receive a function to be executed
	public Timer(int interv, Callable<Integer> func) {
		super();
		this.interv = interv;
		this.func = func;
	  	}
	
	public void start()
	{
		strtTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
	}
	
	public void stop(){
		timerHandler.removeCallbacks(timerRunnable);
	}
	  
	public int getInterv() {
		return interv;
	}

	public void setInterv(int interv) {
		this.interv = interv;
	}

	public java.sql.Date getSQLDate()
	  {
		  Calendar cal = new GregorianCalendar();
		  java.util.Date callTime = cal.getTime();
		  java.sql.Date sqlDate = new java.sql.Date(callTime.getTime());
		  return sqlDate; 
	  }
}
