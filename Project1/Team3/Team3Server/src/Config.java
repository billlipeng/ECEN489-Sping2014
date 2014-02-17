import java.util.ArrayList;

/*
    Contains all the constants and configuration values. All values come from a config file.
 */

public class Config {
    public static int PORT;
    public static String DB_PATH;
    public static String TABLE_NAME;
    public static ArrayList<String> COLUMN_NAMES;

    public static void setPORT(int PORT) {
        Config.PORT = PORT;
    }

    public static void setDB_PATH(String DB_PATH) {
        Config.DB_PATH = DB_PATH;
    }

    public static void setTABLE_NAME(String TABLE_NAME) {
        Config.TABLE_NAME = TABLE_NAME;
    }

    public static void setCOLUMN_NAMES(ArrayList<String> COLUMN_NAMES) {
        Config.COLUMN_NAMES = COLUMN_NAMES;
    }

    @Override
    public String toString() {
        return DB_PATH;
    }
}
