package com.mhardiman.project2;

import jama.Matrix;
import java.util.ArrayList;
import java.util.Random;
import jkalman.JKalman;

import com.mhardiman.project2app.imu;


public class DataHandler{

	//class which helps crunch the data given for a bunch of imu
	
	private ArrayList<imu> mImus;
	private ArrayList<ArrayList<Double>> distWithBearings,estimatedCoords;
	private double R = 6371*1000; //meters
	
	//constructor
	DataHandler(ArrayList<imu> imus){
		mImus = new ArrayList<imu>(); //initialize
		distWithBearings = new ArrayList<ArrayList<Double>>();
		estimatedCoords = new ArrayList<ArrayList<Double>>();
		mImus = imus;
	}
	
	//getters
	
	//returns avg in m/s
	double getAverageSpeed(){
		double avg = 0;
		for(int i = 0; i<mImus.size(); ++i){
			avg += mImus.get(i).speed;
		}
		
		avg /= mImus.size();
		return avg;
	}
	void calculatePathSimple(){
		System.out.println("Using the SIMPLE path:");
		double coordinates[][] = new double[2][mImus.size()];//lat,long
		for(int i = 0; i < mImus.size()-10; i+=10){
			imu row;
			imu row2;
			row = mImus.get(i);
			row2 = mImus.get(i+10);
			double a = row.latitude;
			double b = row.longitude;
			double a1 = row2.latitude;
			double b1 = row2.longitude;
			double deltx = b - b1;
			double delty = a - a1;
			for(int j = 0; j<10;j++){
				coordinates[0][i+j] = b + deltx*j/10;
				coordinates[1][i+j] = a + delty*j/10;
				System.out.println(coordinates[1][i+j] + ", " + coordinates[0][i+j]);
			}
		}
		System.out.println("Done using the SIMPLE path.");
	}
	
	void calculatePath(){
		System.out.println("Using the default path:");
		double lonHolder,latHolder,distance = 0.0;
		imu lastImu = new imu();
		imu currentImu = new imu();
		double lat2,lon2,oldlat = 0,oldlon = 0.0;
		
		for( int i = 0; i<mImus.size(); ++ i){
			ArrayList<Double> tempList = new ArrayList<Double>();
			ArrayList<Double> tempcoords = new ArrayList<Double>();
			
			
			currentImu = mImus.get(i); // only want to call get once
			if(i == 0){
				lastImu = currentImu;
				oldlat = lastImu.latitude;
				oldlon = lastImu.longitude;
			}else{
				if((lastImu.latitude != currentImu.latitude) && (lastImu.longitude != currentImu.longitude)){
					
					double tempdist = ((currentImu.time - lastImu.time)/1000)*currentImu.speed;
					tempList.add(tempdist);
					tempList.add(currentImu.bearing);
					distWithBearings.add(tempList);
					
					double dist = tempdist / R;
					double brng = Math.toRadians(currentImu.bearing); //conversion for haversine
					double lat1 = Math.toRadians(oldlat); //conversion for haversine
					double lon1 = Math.toRadians(oldlon); //conversion for haversine

					//haversine formulas commonly found online for this conversion
					 lat2 = Math.asin( Math.sin(lat1)*Math.cos(dist) + Math.cos(lat1)*Math.sin(dist)*Math.cos(brng) );
					double a = Math.atan2(Math.sin(brng)*Math.sin(dist)*Math.cos(lat1), Math.cos(dist)-Math.sin(lat1)*Math.sin(lat2));
					 lon2 = lon1 + a;

					lon2 = (lon2+ 3*Math.PI) % (2*Math.PI) - Math.PI;
//debug
					System.out.println(Math.toDegrees(lat2)+","+Math.toDegrees(lon2));
					lat2 = Math.toDegrees(lat2);
					lon2 = Math.toDegrees(lon2);
				     
				     tempcoords.add(lat2);//lat first
				     tempcoords.add(lon2);
				     estimatedCoords.add(tempcoords);
				     
				     oldlat = lat2;
				     oldlon = lon2;
				     
				     
				     
					distance +=  tempdist; //expecting milliseconds
				}
				lastImu = currentImu;
			}
		}
		
		System.out.println("Done using the default path.");
	}
	
	void calculatePathKalman(){
		System.out.println("Using the KALMAN path.");
		
		 try {
	            JKalman kalman = new JKalman(4, 2);

	           // Random rand = new Random(System.currentTimeMillis() % 2011);
	            double x = 0;
	            double y = 0;
	            // constant velocity
	            double dx = 0;
	            double dy = 0;
	            
	            // init
	            Matrix s = new Matrix(4, 1); // state [x, y, dx, dy, dxy]        
	            Matrix c = new Matrix(4, 1); // corrected state [x, y, dx, dy, dxy]                
	            
	            Matrix m = new Matrix(2, 1); // measurement [x]
	            

	            // transitions for x, y, dx, dy
	            double[][] tr = { {1, 0, 1, 0}, 
	                              {0, 1, 0, 1}, 
	                              {0, 0, 0.9, 0}, 
	                              {0, 0, 0, 0.9} };
	            kalman.setTransition_matrix(new Matrix(tr));
	            
	            // 1s somewhere?
	            kalman.setError_cov_post(kalman.getError_cov_post().identity());

	            // init first assumption similar to first observation (cheat :)
	            // kalman.setState_post(kalman.getState_post());

	            // report what happend first :)

	    		double lonHolder,latHolder,distance = 0.0;
	    		imu lastImu = new imu();
	    		imu currentImu = new imu();
	    		double lat2,lon2,oldlat = 0,oldlon = 0.0;
	            
	            
	            // For debug only
	            for (int i = 0; i < mImus.size(); ++i) {
	            	ArrayList<Double> tempList = new ArrayList<Double>();
	    			ArrayList<Double> tempcoords = new ArrayList<Double>();
	    			
	    			
	    			currentImu = mImus.get(i); // only want to call get once
	    			if(i == 0){
	    				lastImu = currentImu;
	    				oldlat = lastImu.latitude;
	    				oldlon = lastImu.longitude;
	    				x = oldlat;
	    				y = oldlon;
	    				m.set(0, 0, x);
	    	            m.set(1, 0, y);
	    			}else{
	    				if((lastImu.latitude != currentImu.latitude) && (lastImu.longitude != currentImu.longitude)){
	    					double[][] tr1 = { {1, 0, 1, 0}, 
		                              {0, 1, 0, 1}, 
		                              {0, 0, 1.0, 0}, 
		                              {0, 0, 0, 1.0} };
		            kalman.setTransition_matrix(new Matrix(tr1));
		            
		            // 1s somewhere?
		            kalman.setError_cov_post(kalman.getError_cov_post().identity());
	    					s = kalman.Predict();
	    	                x = currentImu.latitude;
	    	                y = currentImu.longitude;
	    	            
	    					double tempdist = ((currentImu.time - lastImu.time)/1000)*currentImu.speed;
	    					tempList.add(tempdist);
	    					tempList.add(currentImu.bearing);
	    					distWithBearings.add(tempList);
	    					
	    					double dist = tempdist / R;
	    					double brng = Math.toRadians(currentImu.bearing); //conversion for haversine
	    					double lat1 = Math.toRadians(oldlat); //conversion for haversine
	    					double lon1 = Math.toRadians(oldlon); //conversion for haversine
	    					
	    					

	    					//haversine formulas commonly found online for this conversion
	    					 lat2 = Math.asin( Math.sin(lat1)*Math.cos(dist) + Math.cos(lat1)*Math.sin(dist)*Math.cos(brng) );
	    					double a = Math.atan2(Math.sin(brng)*Math.sin(dist)*Math.cos(lat1), Math.cos(dist)-Math.sin(lat1)*Math.sin(lat2));
	    					 lon2 = lon1 + a;

	    					lon2 = (lon2+ 3*Math.PI) % (2*Math.PI) - Math.PI;
	    					lat2 = Math.toDegrees(lat2);
	    					lon2 = Math.toDegrees(lon2);
	    					
	    					 m.set(0, 0, m.get(0, 0) + (lat2-oldlat));
		    	                m.set(1, 0, m.get(1, 0) + (lon2-oldlon));                
		    	                    c = kalman.Correct(m);
		    	                
		    	                    System.out.println(c.get(0, 0) + "," + c.get(1, 0));
	    //debug
	    					//System.out.println(Math.toDegrees(lat2)+","+Math.toDegrees(lon2));
	    					
	    				     
	    				     tempcoords.add(lat2);//lat first
	    				     tempcoords.add(lon2);
	    				     estimatedCoords.add(tempcoords);
	    				     
	    				     oldlat = lat2;
	    				     oldlon = lon2;
	    				     
	    				     
	    				     
	    					distance +=  tempdist; //expecting milliseconds
	    				lastImu = currentImu;
	                           
	                // check state before
	                
	    				}
	    			}
	            }

	            }catch (Exception ex) {
	            System.out.println(ex.getMessage());
	        }
		 System.out.println("Done using the KALMAN path. :)");
	}
	
	//getters
	
	//returns arral list with lat,lon ArrayLists
	ArrayList<ArrayList<Double>> getEstimatedCoords(){
		return estimatedCoords;
	}
	
	ArrayList<ArrayList<Double>> getDistances(){
		return distWithBearings;
	}
	
	

}
