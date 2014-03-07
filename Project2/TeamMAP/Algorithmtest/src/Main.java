import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;



public class Main {
	
	 	
	public static void main(String[] args) throws Exception {
		Connection c = null;
	    Statement stmt = null;
	    ArrayList<Double> bearing = new ArrayList<Double>();
	    ArrayList<Double> speed = new ArrayList<Double>();
	    ArrayList<Double> latitude = new ArrayList<Double>();
	    ArrayList<Double> longitude = new ArrayList<Double>();
	    ArrayList<Double> olatitude = new ArrayList<Double>(); //original or complete gps points. used to check percent error
	    ArrayList<Double> olongitude = new ArrayList<Double>();
	    try {
	        Class.forName("org.sqlite.JDBC");
	        c = DriverManager.getConnection("jdbc:sqlite:projtwo.db");
	        c.setAutoCommit(false);
	        System.out.println("Opened database successfully");

	        //get point data
	        stmt = c.createStatement();
	        ResultSet rs = stmt.executeQuery( "SELECT * FROM SampleData2;" );
	        while ( rs.next() ) {
	          bearing.add(rs.getDouble("bearing"));
	          speed.add(rs.getDouble("speed"));
	          latitude.add(rs.getDouble("latitude"));
	          longitude.add(rs.getDouble("longitude"));
	          //System.out.println(rs.getDouble("latitude")+","+rs.getDouble("longitude"));

	        }
	        rs.close();
	        stmt.close();
	        
	        //get complete points
	        stmt = c.createStatement();
	        rs = stmt.executeQuery( "SELECT * FROM SampleData1;" );
	        while ( rs.next() ) {
	          olatitude.add(rs.getDouble("latitude"));
	          olongitude.add(rs.getDouble("longitude"));
	          //System.out.println(rs.getDouble("latitude")+","+rs.getDouble("longitude"));

	        }
	        rs.close();
	        stmt.close();
	        c.close();
	      } catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	      }
		
	    //forward(bearing, speed, latitude, longitude, olatitude, olongitude);
		//avg_forward(bearing, speed, latitude, longitude, olatitude, olongitude);
		//half_half(bearing, speed, latitude, longitude, olatitude, olongitude);
		forward_back_scaled(bearing, speed, latitude, longitude, olatitude, olongitude);
	   
		
		
	}
	
	public static void forward(ArrayList<Double> bearing, ArrayList<Double> speed, ArrayList<Double> latitude, ArrayList<Double> longitude,  ArrayList<Double> olatitude, ArrayList<Double> olongitude){
		double lat1= latitude.get(0)*Math.PI/180;
		double lon1=longitude.get(0)*Math.PI/180;
		double lat2, lon2;
		double brng = 136*Math.PI/180;
		double d=.013;
		final  double R= 6371; //Earth radius in km
		//avg bearing and speed
		
		for(int i=0; i<bearing.size()-1; i++){
			if((i)%10==0){
				//System.out.println("refreshing values");
				lat1= latitude.get(i)*Math.PI/180;
				lon1=longitude.get(i)*Math.PI/180;
				
			}
			else{
			d=speed.get(i)*10/1000;
			brng=bearing.get(i)*Math.PI/180;
			lat2 = Math.asin( Math.sin(lat1)*Math.cos(d/R) + Math.cos(lat1)*Math.sin(d/R)*Math.cos(brng) );
			lon2= lon1 + Math.atan2(Math.sin(brng)*Math.sin(d/R)*Math.cos(lat1), Math.cos(d/R)-Math.sin(lat1)*Math.sin(lat2));
			lat1=lat2;
			lon1=lon2;
			}
			latitude.set(i, lat1*180/Math.PI);
			longitude.set(i, lon1*180/Math.PI);
			System.out.println(lat1*180/Math.PI+","+lon1*180/Math.PI);
		}
		d=speed.get(latitude.size()-1)*10/1000;
		brng=bearing.get(latitude.size()-1)*Math.PI/180;
		lat2 = Math.asin( Math.sin(lat1)*Math.cos(d/R) + Math.cos(lat1)*Math.sin(d/R)*Math.cos(brng) );
		lon2= lon1 + Math.atan2(Math.sin(brng)*Math.sin(d/R)*Math.cos(lat1), Math.cos(d/R)-Math.sin(lat1)*Math.sin(lat2));
		lat1=lat2;
		lon1=lon2;
		latitude.set(latitude.size()-1, lat1*180/Math.PI);
		longitude.set(latitude.size()-1, lon1*180/Math.PI);
		System.out.println(lat1*180/Math.PI+","+lon1*180/Math.PI);
		 pError(olatitude, olongitude, latitude, longitude);
	}
	
	public static void avg_forward(ArrayList<Double> bearing, ArrayList<Double> speed,ArrayList<Double> latitude, ArrayList<Double> longitude, ArrayList<Double> olatitude, ArrayList<Double> olongitude){
		double lat1= latitude.get(0)*Math.PI/180;
		double lon1=longitude.get(0)*Math.PI/180;
		double lat2, lon2;
		double brng = 136*Math.PI/180;
		double d=.013;
		final  double R= 6371; //Earth radius in km
		//avg bearing and speed
		
		for(int i=0; i<bearing.size()-1; i++){
			if((i)%10==0){
				lat1= latitude.get(i)*Math.PI/180;
				lon1=longitude.get(i)*Math.PI/180;
				
			}
			else{
			d=(speed.get(i)+speed.get(i+1))/2*10/1000;
			brng=(bearing.get(i)+bearing.get(i+1))/2*Math.PI/180;
			lat2 = Math.asin( Math.sin(lat1)*Math.cos(d/R) + Math.cos(lat1)*Math.sin(d/R)*Math.cos(brng) );
			lon2= lon1 + Math.atan2(Math.sin(brng)*Math.sin(d/R)*Math.cos(lat1), Math.cos(d/R)-Math.sin(lat1)*Math.sin(lat2));
			lat1=lat2;
			lon1=lon2;
			}
			latitude.set(i, lat1*180/Math.PI);
			longitude.set(i, lon1*180/Math.PI);
			System.out.println(lat1*180/Math.PI+","+lon1*180/Math.PI);
			
		}
		d=speed.get(latitude.size()-1)*10/1000;
		brng=bearing.get(latitude.size()-1)*Math.PI/180;
		lat2 = Math.asin( Math.sin(lat1)*Math.cos(d/R) + Math.cos(lat1)*Math.sin(d/R)*Math.cos(brng) );
		lon2= lon1 + Math.atan2(Math.sin(brng)*Math.sin(d/R)*Math.cos(lat1), Math.cos(d/R)-Math.sin(lat1)*Math.sin(lat2));
		lat1=lat2;
		lon1=lon2;
		latitude.set(latitude.size()-1, lat1*180/Math.PI);
		longitude.set(latitude.size()-1, lon1*180/Math.PI);
		System.out.println(lat1*180/Math.PI+","+lon1*180/Math.PI);
		 pError(olatitude, olongitude, latitude, longitude);
	}
	
	public static void half_half(ArrayList<Double> bearing, ArrayList<Double> speed, ArrayList<Double> latitude, ArrayList<Double> longitude,ArrayList<Double> olatitude, ArrayList<Double> olongitude){
		double lat1= latitude.get(0)*Math.PI/180;
		double lon1=longitude.get(0)*Math.PI/180;
		double lat2, lon2;
		double brng = 136*Math.PI/180;
		double d=.013;
		final  double R= 6371; //Earth radius in km
		
		for(int i=0; i<bearing.size()-1; i++){
			if((i)%10==0){
				lat1= latitude.get(i)*Math.PI/180;
				lon1=longitude.get(i)*Math.PI/180;
				
			}
			else{
			d=speed.get(i+1)*10/1000;
			brng=bearing.get(i)*Math.PI/180;
			lat2 = Math.asin( Math.sin(lat1)*Math.cos(d/R) + Math.cos(lat1)*Math.sin(d/R)*Math.cos(brng) );
			lon2= lon1 + Math.atan2(Math.sin(brng)*Math.sin(d/R)*Math.cos(lat1), Math.cos(d/R)-Math.sin(lat1)*Math.sin(lat2));
			lat1=lat2;
			lon1=lon2;
			latitude.set(i, lat1*180/Math.PI);
			longitude.set(i, lon1*180/Math.PI);
			}
			System.out.println(lat1*180/Math.PI+","+lon1*180/Math.PI);
			
			if(i%5==0 && i%10!=0 && i!=0 && i+5<bearing.size()){
				//System.out.println("reversing");
				lat1= latitude.get(i+5)*Math.PI/180;
				lon1=longitude.get(i+5)*Math.PI/180;
				for(int j=i+5; j>i; j--){
					d=(speed.get(j-1))*10/1000;
					brng=(bearing.get(j-1)+180)*Math.PI/180;
					lat2 = Math.asin( Math.sin(lat1)*Math.cos(d/R) + Math.cos(lat1)*Math.sin(d/R)*Math.cos(brng) );
					lon2= lon1 + Math.atan2(Math.sin(brng)*Math.sin(d/R)*Math.cos(lat1), Math.cos(d/R)-Math.sin(lat1)*Math.sin(lat2));
					lat1=lat2;
					lon1=lon2;
					latitude.set(j, lat1*180/Math.PI);
					longitude.set(j, lon1*180/Math.PI);
					System.out.println(lat1*180/Math.PI+","+lon1*180/Math.PI);
				}
				i=i+5;
				//System.out.println("reversing done");
			}
		}
		d=speed.get(latitude.size()-1)*10/1000;
		brng=bearing.get(latitude.size()-1)*Math.PI/180;
		lat2 = Math.asin( Math.sin(lat1)*Math.cos(d/R) + Math.cos(lat1)*Math.sin(d/R)*Math.cos(brng) );
		lon2= lon1 + Math.atan2(Math.sin(brng)*Math.sin(d/R)*Math.cos(lat1), Math.cos(d/R)-Math.sin(lat1)*Math.sin(lat2));
		lat1=lat2;
		lon1=lon2;
		latitude.set(latitude.size()-1, lat1*180/Math.PI);
		longitude.set(latitude.size()-1, lon1*180/Math.PI);
		System.out.println(lat1*180/Math.PI+","+lon1*180/Math.PI);
		 pError(olatitude, olongitude, latitude, longitude);
	}
	
	public static void forward_back_scaled(ArrayList<Double> bearing, ArrayList<Double> speed, ArrayList<Double> latitude, ArrayList<Double> longitude,ArrayList<Double> olatitude, ArrayList<Double> olongitude){
		double lat1= latitude.get(0)*Math.PI/180;
		double lon1=longitude.get(0)*Math.PI/180;
		double lat2, lon2;
		double brng = 136*Math.PI/180;
		double d=.013;
		final  double R= 6371; //Earth radius in km
		
		ArrayList<Double> longfor = new ArrayList<Double>();
		ArrayList<Double> latfor = new ArrayList<Double>();
		ArrayList<Double> longback = new ArrayList<Double>();
		ArrayList<Double> latback = new ArrayList<Double>();
		
		int lastpoint=0;
		//forward
		for(int i=0; i<bearing.size()-1; i++){
			if((i)%10==0){
				//System.out.println("refreshing values");
				lat1= latitude.get(i)*Math.PI/180;
				lon1=longitude.get(i)*Math.PI/180;
				lastpoint=i;
			}
			else{
			d=speed.get(i+1)*10/1000;
			brng=bearing.get(i+1)*Math.PI/180;
			lat2 = Math.asin( Math.sin(lat1)*Math.cos(d/R) + Math.cos(lat1)*Math.sin(d/R)*Math.cos(brng) );
			lon2= lon1 + Math.atan2(Math.sin(brng)*Math.sin(d/R)*Math.cos(lat1), Math.cos(d/R)-Math.sin(lat1)*Math.sin(lat2));
			lat1=lat2;
			lon1=lon2;
			}
			latfor.add(lat1*180/Math.PI);
			longfor.add(lon1*180/Math.PI);
			//System.out.println(lat1*180/Math.PI+","+lon1*180/Math.PI);
		}
		d=speed.get(latitude.size()-1)*10/1000;
		brng=bearing.get(latitude.size()-1)*Math.PI/180;
		lat2 = Math.asin( Math.sin(lat1)*Math.cos(d/R) + Math.cos(lat1)*Math.sin(d/R)*Math.cos(brng) );
		lon2= lon1 + Math.atan2(Math.sin(brng)*Math.sin(d/R)*Math.cos(lat1), Math.cos(d/R)-Math.sin(lat1)*Math.sin(lat2));
		lat1=lat2;
		lon1=lon2;
		latfor.add(lat1*180/Math.PI);
		longfor.add(lon1*180/Math.PI);
		//end forward
		
		//backward
		lat1=0;
		lon1=0;
		for(int i=lastpoint; i>=0; i--){
			if((i)%10==0){
				//System.out.println("refreshing values");
				lat1= latitude.get(i)*Math.PI/180;
				lon1=longitude.get(i)*Math.PI/180;
				
			}
			else{
			d=speed.get(i)*10/1000;
			brng=(bearing.get(i)+180)*Math.PI/180;
			lat2 = Math.asin( Math.sin(lat1)*Math.cos(d/R) + Math.cos(lat1)*Math.sin(d/R)*Math.cos(brng) );
			lon2= lon1 + Math.atan2(Math.sin(brng)*Math.sin(d/R)*Math.cos(lat1), Math.cos(d/R)-Math.sin(lat1)*Math.sin(lat2));
			lat1=lat2;
			lon1=lon2;
			}
			latback.add(lat1*180/Math.PI);
			longback.add(lon1*180/Math.PI);
			//System.out.println(lat1*180/Math.PI+","+lon1*180/Math.PI);
		}
		/*
		d=speed.get(latitude.size()-1)*10/1000;
		brng=(bearing.get(latitude.size()-1)+180)*Math.PI/180;
		lat2 = Math.asin( Math.sin(lat1)*Math.cos(d/R) + Math.cos(lat1)*Math.sin(d/R)*Math.cos(brng) );
		lon2= lon1 + Math.atan2(Math.sin(brng)*Math.sin(d/R)*Math.cos(lat1), Math.cos(d/R)-Math.sin(lat1)*Math.sin(lat2));
		lat1=lat2;
		lon1=lon2;
		latback.add(lat1*180/Math.PI);
		longback.add(lon1*180/Math.PI);
		*/
		//endbackward
		
		Collections.reverse(latback);
		Collections.reverse(longback);
		
		
		System.out.println("Generating points");

		for(int i=0;i<longitude.size(); i++ ){
			double weight=(i%10)*.1;
			if(i>lastpoint){
				longitude.set(i, longfor.get(i));
				latitude.set(i, latfor.get(i));
			}
			else{
				//all point average
				//longitude.set(i, (longfor.get(i)+longback.get(i))/2);
				//latitude.set(i, (latfor.get(i)+latback.get(i))/2);
				
				//weighted avg
				longitude.set(i, (longfor.get(i)*(1-weight)+longback.get(i)*(weight)));
				latitude.set(i, (latfor.get(i)*(1-weight)+latback.get(i)*(weight)));
				
				//half_half
				/*
				if(i%10>=6){
					longitude.set(i, longfor.get(i));
					latitude.set(i, latfor.get(i));
				}
				else{
					longitude.set(i, longback.get(i));
					latitude.set(i, latback.get(i));		
				}
					*/
				//longitude.set(i, longback.get(i));
				//latitude.set(i, latback.get(i));
			}
			System.out.println(latitude.get(i)+","+longitude.get(i));
			//System.out.println(weight);
			
			
		}
		 pError(olatitude, olongitude, latitude, longitude);
	}
	
	public static void pError(ArrayList<Double> olatitude, ArrayList<Double> olongitude, ArrayList<Double> latitude, ArrayList<Double> longitude){
		double laterror=0;
		double lonerror=0;
		for(int i=0; i<olatitude.size(); i++){
			laterror = laterror+Math.abs(100*(olatitude.get(i)-latitude.get(i))/olatitude.get(i));
			lonerror = lonerror+Math.abs(100*(olongitude.get(i)-longitude.get(i))/olongitude.get(i));
			//System.out.println("laterror: "+laterror+" lonerror: "+lonerror);
			//System.out.println(latitude.get(i)+","+olatitude.get(i));
		}
		
		System.out.println("laterror: "+laterror+" lonerror: "+lonerror);
	}
	
}