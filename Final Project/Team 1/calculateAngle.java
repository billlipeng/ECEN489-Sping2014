import com.google.android.maps.GeoPoint;
    
public double calculateAngle(GeoPoint startPoint, GeoPoint endPoint) {
    
	double lat1 = startPoint.getLatitudeE6() / 1E6;
	double lat2 = endPoint.getLatitudeE6() / 1E6;
	double long2 = startPoint.getLongitudeE6() / 1E6;
	double long1 = endPoint.getLongitudeE6() / 1E6;
	double dy = lat2 - lat1;
	double dx = Math.cos(Math.PI / 180 * lat1) * (long2 - long1);
	double angle = Math.atan2(dy, dx);

	//logic to keep angle within -180 <= theta <= +180 boundary
	
	if (angle >= 180) {
		angle = angle - 180;
		return angle;
	} else if (angle <= -180) {
		angle = angle + 180;
		return angle;
	} else 
		return angle;
}