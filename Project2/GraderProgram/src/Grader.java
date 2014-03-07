import java.util.ArrayList;

/**
 * Created by Zachary on 3/7/14.
 */
public class Grader {

    public static void main(String[] args) throws Exception {
        DatabaseHandler masterDBH = null;
        DatabaseHandler studentDBH = null;
        String masterbdpath = "C:/Users/Zachary/Desktop/master.db";
        String studentdbpath = "C:/Users/Zachary/Desktop/Team5.db";
        String[] tables = {"team1", "team2", "team3", "team4"};

        masterDBH = new DatabaseHandler(masterbdpath);
        studentDBH = new DatabaseHandler(studentdbpath);
        System.out.println(studentdbpath);
        for (String table : tables) {
            ArrayList<Coordinates> masterCoords = masterDBH.readDBData(table);
            ArrayList<Coordinates> studentCoords = studentDBH.readDBData(table);
            Double average = 0.0;
            for (int i = 0; i < masterCoords.size(); i++) {
                average += haversine(masterCoords.get(i), studentCoords.get(i));
            }
            average = average/masterCoords.size();
            System.out.println(table + " " + average);
        }

    }
    static double haversine(Coordinates coordinates0, Coordinates coordinates1) {
        double EarthRadius = 6372.8; // Kilometers
        double deltaLatitude = coordinates1.getLatitude() - coordinates0.getLatitude();
        double deltaLongitude = coordinates1.getLongitude() - coordinates0.getLongitude();

        double a = Math.sin(deltaLatitude * 0.5) * Math.sin(deltaLatitude * 0.5)
                + Math.cos(coordinates0.getLatitude()) * Math.cos(coordinates1.getLatitude())
                * Math.sin(deltaLongitude * 0.5) * Math.sin(deltaLongitude * 0.5);

        return (2 * EarthRadius * Math.asin(Math.sqrt(a)));
    }
}

