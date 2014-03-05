import java.util.ArrayList;
import java.util.List;

public class MapAlgorithm implements InterpolationAlgorithm {
    ArrayList<DataPoint> dataSet;

    @Override
    public ArrayList<DataPoint> analyze(ArrayList<DataPoint> _dataSet) {
        dataSet = _dataSet;
        ArrayList<List<DataPoint>> partitions = paritionData();
        for (List<DataPoint> partition : partitions) {
            for (int i = 0; i < partition.size(); i++) {
                if (i == 0 || i == partition.size()) continue;
                else {
                    DataPoint dp_prev = partition.get(i-1);
                    DataPoint dp_curr = partition.get(i);
                    double distance = calcDistance(dp_prev.getTime(), dp_curr.getTime(), dp_curr.getSpeed());
                    Coord coords = genCoord(dp_prev.getLatitude(), dp_prev.getLongitude(), distance, dp_prev.getBearing());
                    dp_curr.setLatitude(coords.lat);
                    dp_curr.setLongitude(coords.lon);
                }
            }
//            // When its not a complete partition
//            if (partition.size() != 11) {
//                continue;
//            }
//            // Complete Partition
//            else {
//                for (int i = 0; i < partition.size(); i++) {
//                    if (i == 0 || i == partition.size()) continue;
//                    else {
//                        DataPoint dp_prev = partition.get(i-1);
//                        DataPoint dp_curr = partition.get(i);
//                        double distance = calcDistance(dp_prev.getTime(), dp_curr.getTime(), dp_curr.getSpeed());
//                        Coord coords = genCoord(dp_prev.getLatitude(), dp_prev.getLongitude(), distance, dp_prev.getBearing());
//                        dp_curr.setLatitude(coords.lat);
//                        dp_curr.setLongitude(coords.lon);
//                    }
//                }
//            }
        }
        return mergePartitions(partitions);
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

    private ArrayList<DataPoint> mergePartitions(ArrayList<List<DataPoint>> partitions) {
        ArrayList<DataPoint> mergedData = new ArrayList<DataPoint>();
        for (List<DataPoint> partition : partitions) {
            // incomplete partitions
            if (partition.size() != 11) {
                for (int i =0; i < partition.size(); i++) {
                    mergedData.add(partition.get(i));
                }
            }
            // complete partitions
            else {
                for (int i = 0; i < partition.size()-1; i++) {
                    mergedData.add(partition.get(i));
                }
            }
        }
        return mergedData;
    }
}
