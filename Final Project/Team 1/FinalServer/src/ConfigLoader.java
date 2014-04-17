import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ConfigLoader {
    private String configFile = "config.json";

    public ConfigLoader(Config _config) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(configFile)));
        ConfigHolder propHold = new Gson().fromJson(json, ConfigHolder.class);
        _config.setPORT(propHold.PORT);
        _config.setDB_PATH(propHold.DB_PATH);
        _config.setTABLE_NAME(propHold.TABLE_NAME);
        _config.setFUSION_TABLE_NAME(propHold.FUSION_TABLE_NAME);
        _config.setCOLUMN_NAMES(propHold.COLUMN_NAMES);
        _config.setBASE_LAT(propHold.BASE_LAT);
        _config.setBASE_LON(propHold.BASE_LON);
        _config.setEND_CODE(propHold.END_CODE);
        _config.setCOM_PORT(propHold.COM_PORT);
    }

    class ConfigHolder {
        public int PORT;
        public String DB_PATH;
        public String TABLE_NAME;
        public String FUSION_TABLE_NAME;
        public ArrayList<String> COLUMN_NAMES;
        double BASE_LAT;
        double BASE_LON;
        String END_CODE;
        String COM_PORT;
    }
}
