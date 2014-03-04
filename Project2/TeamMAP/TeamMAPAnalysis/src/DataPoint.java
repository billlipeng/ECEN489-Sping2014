
public class DataPoint {
    private long time;
    private double longitude;
    private double latitude;
    private double bearing;
    private double speed;

    public DataPoint(long time, double longitude, double latitude, double bearing, double speed) {
        this.time = time;
        this.longitude = longitude;
        this.latitude = latitude;
        this.bearing = bearing;
        this.speed = speed;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getTime() {
        return time;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getBearing() {
        return bearing;
    }

    public double getSpeed() {
        return speed;
    }

    public void toSql(String tableName) {

    }
}
