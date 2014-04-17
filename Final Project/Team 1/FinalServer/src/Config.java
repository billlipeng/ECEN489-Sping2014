import java.util.ArrayList;

/*
    Contains all the constants and configuration values. All values come from a config file.
 */

public class Config {
    public static int PORT;
    public static String DB_PATH;
    public static String TABLE_NAME;
    public static String FUSION_TABLE_NAME;
    public static ArrayList<String> COLUMN_NAMES;
    public static double BASE_LAT;
    public static double BASE_LON;
    public static String END_CODE;
    public static String COM_PORT;

    public static void setPORT(int PORT) {
        Config.PORT = PORT;
    }

    public static void setDB_PATH(String DB_PATH) {
        Config.DB_PATH = DB_PATH;
    }

    public static void setTABLE_NAME(String TABLE_NAME) {
        Config.TABLE_NAME = TABLE_NAME;
    }

    public static void setFUSION_TABLE_NAME(String FUSION_TABLE_NAME) {
        Config.FUSION_TABLE_NAME = FUSION_TABLE_NAME;
    }

    public static void setCOLUMN_NAMES(ArrayList<String> COLUMN_NAMES) {
        Config.COLUMN_NAMES = COLUMN_NAMES;
    }

    public static void setBASE_LAT(double BASE_LAT) {
        Config.BASE_LAT = BASE_LAT;
    }

    public static void setBASE_LON(double BASE_LON) {
        Config.BASE_LON = BASE_LON;
    }

    public static void setEND_CODE(String END_CODE) {
        Config.END_CODE = END_CODE;
    }

    public static void setCOM_PORT(String COM_PORT) {
        Config.COM_PORT = COM_PORT;
    }

    @Override
    public String toString() {
        return DB_PATH;
    }
}
