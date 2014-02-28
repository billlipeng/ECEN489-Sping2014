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
	
	long[] analyze(){
		
		//implement haversine function
		double h= pow(((latitude2-latitude1)/2),2)+cos(latitude1)*cos(latitude2)*pow(sin((longitude2-longitude1)/2),2);
		distance = (2*radiusearth*asin(sqrt(h)));
		
		
		return null;
		
	}
	
}


