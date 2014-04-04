import java.util.concurrent.BlockingQueue;

import com.zpartal.finalproject.datapackets.DataPoint;

public class ServoDriver implements Runnable {
    private Config config = new Config();
    protected BlockingQueue<DataPoint> queue = null;
    SerialHandler sh = null;

    public ServoDriver(BlockingQueue<DataPoint> _queue) {
        this.queue = _queue;
    }

    public ServoDriver() {
        this.queue = null;
    }

    @Override
    public void run() {
        sh = new SerialHandler();
        try {
            if ( sh.initialize() ) {
                while(true) {
                    DataPoint dp = queue.take();
                    if (dp.getSsid().equals(config.END_CODE)) { break; }
                    double bearing = calculateBearing(config.BASE_LAT, dp.getLatitude(), config.BASE_LON, dp.getLongitude());
                    sh.sendData(String.valueOf(bearing));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sh.close();

        // Wait 5 seconds then shutdown
        try { Thread.sleep(2000); } catch (InterruptedException ie) {}
    }

    public static double calculateBearing(double lat1, double lat2, double long1, double long2) {

        lat1 = deg2rad(lat1);
        lat2 = deg2rad(lat2);
        long1 = deg2rad(long1);
        long2 = deg2rad(long2);

        double dLong = deg2rad(long2) - deg2rad(long1);
        double y = Math.sin(dLong) * Math.cos(lat2);
        double x = Math.cos(lat1)*Math.sin(lat2) - Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLong);

        return rad2deg(Math.atan2(y, x));
    }

    //function to convert from degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    //function to convert from radians to degrees
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


}
