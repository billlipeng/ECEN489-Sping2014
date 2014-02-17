import com.google.gson.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Properties {
    public static String CONFIG_FILE = "config.ini";
    public static int PORT = 5555;
    public static String DB_PATH = "C:/projone.db";
    public static String TABLE_NAME = "ecen489_project1_data";
    public ArrayList<String> COLUMN_NAMES = new ArrayList(Arrays.asList("date", "time",
            "client_id", "run_id", "latitude", "longitude", "bearing", "speed", "altitude",
            "sensor_id", "sensor_type", "sensor_value", "attribute"));

    public Properties() {

    }
}
