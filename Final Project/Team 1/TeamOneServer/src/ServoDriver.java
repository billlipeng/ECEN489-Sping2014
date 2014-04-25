import java.util.concurrent.BlockingQueue;

import com.zpartal.finalproject.datapackets.DataPoint;

public class ServoDriver implements Runnable {
    private Config config = new Config();
    protected BlockingQueue<DataPoint> queue = null;
    SerialHandler sh = null;
    double base_lat = 0.0;
    double base_lon = 0.0;
    double cal_lat = 0.0;
    double cal_lon = 0.0;

    public ServoDriver(BlockingQueue<DataPoint> _queue,double _base_lat, double _base_lon, double _cal_lat, double _cal_lon) {
        this.queue = _queue;
        this.base_lat = _base_lat;
        this.base_lon = _base_lon;
        this.cal_lat = _cal_lat;
        this.cal_lon = _cal_lon;
    }

    public ServoDriver() {
        this.queue = null;
    }

    public void setPoints(double _base_lat, double _base_lon, double _cal_lat, double _cal_lon) {
        this.base_lat = _base_lat;
        this.base_lon = _base_lon;
        this.cal_lat = _cal_lat;
        this.cal_lon = _cal_lon;
    }

    @Override
    public void run() {
        int count = 0;
        sh = new SerialHandler();

        try {
            if ( sh.initialize() ) {
                while(true) {
                    DataPoint dp = queue.take();
                    if (dp.getSsid().equals(config.END_CODE)) { break; }
                    int motorAngle = (int) getMotorAngle(base_lat,  base_lon, cal_lat, cal_lon, dp.getLatitude(), dp.getLongitude());
//                    if (count % 5 == 0) sh.sendData(String.valueOf(motorAngle));
                    sh.sendData(String.valueOf(motorAngle));
                    System.out.println(String.valueOf(motorAngle)+'\n');
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sh.close();

        // Wait 5 seconds then shutdown
        try { Thread.sleep(2000); } catch (InterruptedException ie) {}
    }

    public static double getMotorAngle(double base_lat, double base_lon, double cal_lat, double cal_lon, double trc_lat, double trc_lon) {
        int calquad = getQuadrant(cal_lat,cal_lon,base_lat,base_lon);
        int trcquad = getQuadrant(cal_lat,cal_lon,trc_lat,trc_lon);

        System.out.println("calquad "+ calquad + " trcquad " + trcquad);



        double alpha = getAlphaBeta(base_lat,base_lon,cal_lat,cal_lon);
        double beta = getAlphaBeta(trc_lat,trc_lon,cal_lat,cal_lon);

        System.out.println("alpha "+ alpha + " beta " + beta);

        if(calquad == trcquad){
            if(alpha > beta){
                return -1.0*getAngle(base_lat,base_lon,cal_lat,cal_lon,trc_lat,trc_lon);
            }else{
                return getAngle(base_lat,base_lon,cal_lat,cal_lon,trc_lat,trc_lon);
            }
        }else if((int)Math.abs((double)(calquad-trcquad))%4 == 2){
            if(alpha > beta){
                return (180.0 + getAngle(base_lat,base_lon,cal_lat,cal_lon,trc_lat,trc_lon));
            }else{
                return -1.0*(180.0 - getAngle(base_lat,base_lon,cal_lat,cal_lon,trc_lat,trc_lon));
            }
        }else if(((trcquad == 1 && calquad == 4)) || ((trcquad > calquad) && (trcquad != 4))){
            if(beta > alpha){
                return (beta - alpha) + 90.0;
                //return (180.0 + getAngle(base_lat,base_lon,cal_lat,cal_lon,trc_lat,trc_lon));
            }else{
                return getAngle(base_lat,base_lon,cal_lat,cal_lon,trc_lat,trc_lon);
            }
        }else{
            if(beta > alpha){
                return -1.0*getAngle(base_lat,base_lon,cal_lat,cal_lon,trc_lat,trc_lon);
            }else{
                return -1.0*(180.0 - getAngle(base_lat,base_lon,cal_lat,cal_lon,trc_lat,trc_lon));
            }
        }
    }

    public static double getAlphaBeta(double base_lat, double base_lon, double cal_lat, double cal_lon) {
        int calquad = getQuadrant(cal_lat,cal_lon,base_lat,base_lon);

        double alpha = 0.0;

        switch(calquad){
            case 1:
                alpha = getAngle(base_lat,base_lon, cal_lat,cal_lon,cal_lat,base_lon);
                break;
            case 2:
                alpha = getAngle(base_lat,base_lon, cal_lat,cal_lon,base_lat,cal_lon);
                break;
            case 3:
                alpha = getAngle(base_lat,base_lon, cal_lat,cal_lon,cal_lat,base_lon);
                break;
            case 4:
                alpha = getAngle(base_lat,base_lon, cal_lat,cal_lon,base_lat,cal_lon);
                break;
            default:
                alpha = 0.0;
        }
        return alpha;
    }

    public static double getAngle(double base_lat, double base_lon, double cal_lat, double cal_lon, double trc_lat, double trc_lon) {
        double cal_base_lat = Math.toRadians(base_lat) - Math.toRadians(cal_lat);
        double cal_base_lon = Math.toRadians(base_lon) - Math.toRadians(cal_lon);



        double cal_base_mag = Math.sqrt(cal_base_lat*cal_base_lat+cal_base_lon*cal_base_lon);

        double cal_trc_lat = Math.toRadians(trc_lat) - Math.toRadians(cal_lat);
        double cal_trc_lon = Math.toRadians(trc_lon) - Math.toRadians(cal_lon);
        double cal_trc_mag = Math.sqrt(cal_trc_lat*cal_trc_lat+cal_trc_lon*cal_trc_lon);

        double return_ang = Math.asin((cal_base_lat*cal_trc_lon - cal_base_lon*cal_trc_lat)/(cal_base_mag*cal_trc_mag));
        // System.out.println("angle "+return_ang);
        return Math.toDegrees(return_ang);
    }

    public static int getQuadrant(double cal_lat, double cal_lon, double trc_lat, double trc_lon){
        double cal_base_lat = Math.toRadians(trc_lat) - Math.toRadians(cal_lat);
        double cal_base_lon = Math.toRadians(trc_lon) - Math.toRadians(cal_lon);

//System.out.println("lat "+cal_base_lat+" lon "+cal_base_lon);
        if((cal_base_lat >= 0) && (cal_base_lon >= 0)){
            return 1;
        }else if((cal_base_lat >= 0) && (cal_base_lon < 0)){
            return 2;
        }else if((cal_base_lat < 0) && (cal_base_lon < 0)){
            return 3;
        }else{
            return 4;
        }
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
