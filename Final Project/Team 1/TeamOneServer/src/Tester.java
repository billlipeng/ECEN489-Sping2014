import com.zpartal.finalproject.datapackets.DataPoint;
import java.util.ArrayList;
import java.util.Random;

/*
    Class made soley to test functionality, do not run as main
 */
public class Tester {
    public static void main(String[] args) throws Exception {
        /*Config props = new Config();
        new ConfigLoader(props);
//        System.out.println(props.toString());
//
//        DataPoint dp = new DataPoint.Builder()
//                .time("12:45:00").date("2014-2-16").client_id("zpartal")
//                .run_id("run1").latitude(1234.1234).longitude(4342.2323)
//                .bearing(23.42).speed(23.2).altitude(554).sensor_id("S1")
//                .sensor_type("temp").sensor_value(112.3).attribute("sensor")
//                .build();
//
//        System.out.println(dp.toString());
//
//        ArrayList<DataPoint> dataset = new ArrayList<DataPoint>();
//        dataset.add(dp);
//        dataset.add(dp);
//        dataset.add(dp);
//        DatabaseHandler dbh = new DatabaseHandler(dataset);
//        new Thread(new DatabaseHandler(dataset)).start();
//        System.out.println(dbh.createInsertSQL(dp));
//        FusionTableHandler fth = new FusionTableHandler(dataset);
//        System.out.println(fth.createMultipleInsertSQL(dataset));
        ArrayList<DataPoint> dataset = new ArrayList<DataPoint>();
        int num = 1100;
        for (int i = 0; i < num; i++) {
//            dataset.add(new DataPoint.Builder()
//                .time("12:45:00").date("2014-2-16").client_id("zpartal")
//                .run_id("run1").latitude(1234.1234).longitude(4342.2323)
//                .bearing(23.42).speed(23.2).altitude(554).sensor_id("S1")
//                .sensor_type("temp").sensor_value(112.3).attribute("sensor")
//                .build());
        }

//        FusionTableHandler fth = new FusionTableHandler(dataset);
//        System.out.println(fth.createMultipleInsertSQL(dataset));

        new Thread(new FusionTableHandler(dataset)).start();*/

//        new Thread(new ServoDriver()).start();
        double base_lat = 30.623399;
        double base_lon = -96.335571;
        double cal_lat = 30.623698;
        double cal_lon = -96.334913;
        double trc_lat = 30.623566;
        double trc_lon = -96.334748;

        int bearing = (int) getAngle(base_lat, base_lon, cal_lat, cal_lon, trc_lat, trc_lon);
        System.out.println(bearing);

//        SerialHandler sh = new SerialHandler();
//        if ( sh.initialize() ) {
//            while(true) {
//                Random ran = new Random();
//                int x = ran.nextInt(360)-180;
//                sh.sendData(String.valueOf(x));
//                try { Thread.sleep(4000); } catch (InterruptedException ie) {}
////                System.out.println(String.valueOf(bearing)+'\n');
//            }
//        }



    }

    public static double getAngle(double base_lat, double base_lon, double cal_lat, double cal_lon, double trc_lat, double trc_lon) {
        double cal_base_lat = Math.toRadians(base_lat) - Math.toRadians(cal_lat);
        double cal_base_lon = Math.toRadians(base_lon) - Math.toRadians(cal_lon);
        double cal_base_mag = Math.sqrt(cal_base_lat*cal_base_lat+cal_base_lon*cal_base_lon);

        double cal_trc_lat = Math.toRadians(trc_lat) - Math.toRadians(cal_lat);
        double cal_trc_lon = Math.toRadians(trc_lon) - Math.toRadians(cal_lon);
        double cal_trc_mag = Math.sqrt(cal_trc_lat*cal_trc_lat+cal_trc_lon*cal_trc_lon);

        double return_ang = Math.asin((cal_base_lat*cal_trc_lon - cal_base_lon*cal_trc_lat)/(cal_base_mag*cal_trc_mag));
        return Math.toDegrees(return_ang);
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
