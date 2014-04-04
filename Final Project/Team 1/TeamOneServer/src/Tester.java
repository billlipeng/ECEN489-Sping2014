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
        double lat1 = 30.61869655;
        double lon1 = -96.34153996;
        double testlat = 30.6180784;
        double testlon = -96.3412879;
        int bearing = (int) calculateBearing(lat1, testlat, lon1, testlon);
        System.out.println(bearing);

        SerialHandler sh = new SerialHandler();
        if ( sh.initialize() ) {
            while(true) {
                Random ran = new Random();
                int x = ran.nextInt(360)-180;
                sh.sendData(String.valueOf(x));
                try { Thread.sleep(4000); } catch (InterruptedException ie) {}
//                System.out.println(String.valueOf(bearing)+'\n');
            }
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
