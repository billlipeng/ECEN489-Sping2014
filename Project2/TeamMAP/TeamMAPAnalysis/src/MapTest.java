
public class MapTest {
    DataPoint dp1 = new DataPoint(1393963655007L, -96.33979889, 30.62160107, 8.399999618530273, 0.55901700258255);
    DataPoint dp2 = new DataPoint(1393963665000L, 0, 0, 136.0, 1.5811388492584229);

    public static void main(String[] args) throws Exception {
        new MapTest();
    }

    public MapTest() {
        double distance = calcDistance(dp1.getTime(), dp2.getTime(), dp2.getSpeed());
        Coord coords = genCoord(dp1.getLatitude(), dp1.getLongitude(), distance, dp1.getBearing());
        System.out.println("Lon: "+  coords.lon);
        System.out.println("Lat: "+ coords.lat);
    }

    class Coord {
        public double lat;
        public double lon;
        public Coord (double _lat, double _lng) {
            this.lat = _lat;
            this.lon = _lng;
        }
    }
    private Coord genCoord (double degLat1, double degLon1, double distance, double bearing) {
        double R = 6371;
        // convert inputs to radians
        double lat1 = deg2rad(degLat1);
        double lon1 = deg2rad(degLon1);
        // Calculate new lat and lon
        double lat2 = Math.asin( Math.sin(lat1)*Math.cos(distance/R) +
                Math.cos(lat1)*Math.sin(distance/R)*Math.cos(bearing) );
        double lon2 = lon1 + Math.atan2(Math.sin(bearing)*Math.sin(distance/R)*Math.cos(lat1),
                Math.cos(distance/R)-Math.sin(lat1)*Math.sin(lat2));
        // return calculated lat and lon
        return new Coord(rad2lat(lat2), rad2lon(lon2));
    }

    private double calcDistance(long t1, long t2, double speed) {
        double time = (t2 - t1)/1000.0;
        return speed*time/1000;
    }

    private double deg2rad(double degrees) {
        return degrees*(Math.PI/180);
    }

    private double rad2lat(double latRad) {
        double rad = latRad % (Math.PI*2);
        if (rad < 0)
            rad = 2*Math.PI + rad;
        double rad180 = rad % (Math.PI);
        if (rad180 > Math.PI/2)
            rad180 = Math.PI - rad180;

        if (rad > Math.PI)
            rad = -rad180;
        else
            rad = rad180;

        return(rad/Math.PI*180);
    }
    private double rad2lon(double lonRad) {
        double rad = lonRad % (Math.PI*2);
        if (rad < 0)
            rad = 2*Math.PI + rad;
        double rad360 = rad % (Math.PI*2);
        if (rad360 > Math.PI)
            rad360 = Math.PI*2 - rad360;

        if (rad > Math.PI)
            rad = -rad360;
        else
            rad = rad360;

        return(rad/Math.PI*180);
    }
}

