import static java.lang.Math.*;

public class linearinterpolation {
	public double distance;
	public long longitude1;
	public long latitude1;
	public long longitude2;
	public long latitude2;
	public long radiusearth = 6378;
	linearinterpolation(long longitude1, long latitude1, long longitude2, long latitude2){
		
		this.longitude1= longitude1;
		this.latitude1= latitude1;
		this.longitude2= longitude2;
		this.latitude2 = latitude2;
	}
	
	double[] analyze(){
		
		double longitude1R = toRadians(longitude1);
		double latitude1R= toRadians(latitude1);
		double longitude2R= toRadians(longitude2);
		double latitude2R= toRadians(latitude2);
		double[] latlongArray= new double[20];
		
		//implement haversine function
		double h= pow(((latitude2R-latitude1R)/2),2)+cos(latitude1R)*cos(latitude2R)*pow(sin((longitude2R-longitude1R)/2),2);
		distance = (2*radiusearth*asin(sqrt(h)));
		
		//calculate distance interval between points
		double interval= distance/10;
		double angulardistanceR= interval/radiusearth;		
		//calculate the forward azimuth(bearing)		
		double forwardazimuthR = acos((sin(latitude2R)-sin(latitude1R)*cos(distance))/(sin(distance)*cos(latitude1R)));
		
		//populate double array that contains [longitude,latitude....] values for each of the 10 points
		for (int i =0; i<10;i++) {
			latlongArray[2*(i+1)-2]= longitude1R+ atan2(sin(forwardazimuthR)*sin(angulardistanceR)*cos(latitude1R),cos(angulardistanceR)-sin(latitude1R)*sin(latitude2R));
			latlongArray[2*(i+1)-1]= asin(sin(latitude1R)*cos(angulardistanceR)+ cos(latitude1R)*sin(angulardistanceR)*cos(forwardazimuthR));
			longitude1R= latlongArray[2*(i+1)-2];
			latitude1R= latlongArray[2*(i+1)-1];
			forwardazimuthR = acos((sin(latitude2R)-sin(latitude1R)*cos(distance))/(sin(distance)*cos(latitude1R)));
		}
		
		return latlongArray;
		
	}
	
}


