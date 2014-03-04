import static java.lang.Math.*;

public class linearinterpolation {
	public double distance;
	public double longitude1;
	public double latitude1;
	public double longitude2;
	public double latitude2;
	public long radiusearth = 6378;
	linearinterpolation(double longitude12, double latitude12, double longitude22, double latitude22){
		
		this.longitude1= longitude12;
		this.latitude1= latitude12;
		this.longitude2= longitude22;
		this.latitude2 = latitude22;
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
			longitude1R= longitude1R+ atan2(sin(forwardazimuthR)*sin(angulardistanceR)*cos(latitude1R),cos(angulardistanceR)-sin(latitude1R)*sin(latitude2R));
			latitude1R= asin(sin(latitude1R)*cos(angulardistanceR)+ cos(latitude1R)*sin(angulardistanceR)*cos(forwardazimuthR));
			latlongArray[2*(i+1)-2]= toDegrees(longitude1R);
			latlongArray[2*(i+1)-1]= toDegrees(latitude1R);
			forwardazimuthR = acos((sin(latitude2R)-sin(latitude1R)*cos(distance))/(sin(distance)*cos(latitude1R)));
			
		}
		
		return latlongArray;
		
	}
	
}


