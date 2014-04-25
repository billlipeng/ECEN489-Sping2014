import java.util.ArrayList;
import java.lang.Math;

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
        ArrayList<Double> averages = new ArrayList<Double>();
        for (String table : tables) {
            ArrayList<Tuple> masterData = masterDBH.readDBData(table);
            ArrayList<Tuple> studentCoords = studentDBH.readDBData(table);

            Double average = 0.0;
            for (int i = 0; i < masterData.size(); i++) {
                average += Math.pow((masterData.get(i).y - studentCoords.get(i).y),2.0);
            }
            average = average/masterData.size();
            averages.add(average);
            System.out.println(table + " " + average*1000 + "m");
        }
        Double totalaverage = 0.0;
        for (Double average:averages) {
            totalaverage += average;
        }
        System.out.println("Total Average: " + totalaverage/averages.size()*1000 + "m");
    }

}

