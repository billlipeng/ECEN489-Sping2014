import com.zpartal.project1.datapackets.DataPoint;
import java.util.ArrayList;

/*
    Class made soley to test functionality, do not run as main
 */
public class Tester {
    public static void main(String[] args) throws Exception {
//        Config props = new Config();
//        new ConfigLoader(props);
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
//        new Thread(new FusionTableHandler(dataset)).start();

        ArrayList<Integer> listtest = new ArrayList<Integer>();
        listtest.add(1);
        listtest.add(2);
        listtest.add(3);
        listtest.add(4);
        listtest.add(5);
        listtest.add(6);
        listtest.add(7);
        listtest.add(8);
        listtest.add(9);
        listtest.add(10);
        listtest.add(11);
        listtest.add(12);

        int binSize = 3;
        int numBins = (listtest.size() + binSize - 1) / binSize;
//        System.out.println(bins);

    }
}
