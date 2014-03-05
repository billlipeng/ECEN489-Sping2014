import java.util.ArrayList;
import java.util.List;

public class MapAlgorithm implements InterpolationAlgorithm {
    ArrayList<DataPoint> dataSet;

    @Override
    public ArrayList<DataPoint> analyze(ArrayList<DataPoint> _dataSet) {
        dataSet = _dataSet;
        ArrayList<List<DataPoint>> paritions = paritionData();
        System.out.println("test");




        return dataSet;
    }

    class Coord {
        public double lat;
        public double lng;
        public Coord (double _lat, double _lng) {
            this.lat = _lat;
            this.lng = _lng;
        }
    }
    private Coord genCoord (double degLat1, double degLon1, double distance, double bearing) {
        double R = 6371;
        // convert inputs to radians
        double lat1 = deg2rad(degLat1);
        double lon1 = deg2rad(degLon1);
        double lat2 = Math.asin( Math.sin(lat1)*Math.cos(distance/R) +
                Math.cos(lat1)*Math.sin(distance/R)*Math.cos(bearing) );
        double lon2 = lon1 + Math.atan2(Math.sin(bearing)*Math.sin(distance/R)*Math.cos(lat1),
                Math.cos(distance/R)-Math.sin(lat1)*Math.sin(lat2));
        return new Coord(lat2, lon2);
    }

    private double calcDistance(int t1, int t2, double speed) {
        double time = t2 - t1;
        return speed*time;
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
    private double rad2long(double longRad) {
        double rad = longRad % (Math.PI*2);
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

    private ArrayList<List<DataPoint>> paritionData() {
        ArrayList<List<DataPoint>> partitionData = new ArrayList<List<DataPoint>>();
        int binSize = 11;
        int numBins = (dataSet.size() + binSize -1) / binSize;
        for (int i = 0; i < numBins; i++) {
            int start;
            int end;
            if (i == 0) {
                start = i*binSize;
                end = Math.min(start + binSize, dataSet.size());
            }
            else {
                start = i*binSize-i;
                end = Math.min(start + binSize, dataSet.size());
            }
            partitionData.add(dataSet.subList(start, end));
        }
        return partitionData;
    }
}
