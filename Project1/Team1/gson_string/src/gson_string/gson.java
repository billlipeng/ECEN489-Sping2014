package gson_string;

import java.io.IOException;

import com.google.gson.Gson;

public class gson {
	 
	public class sensorData {
		public String sensor_id;
		public String sensor_type;
		public float sensor_value;
	 
	    public sensorData() {
	    }
	 
	    public sensorData(String sensorID, String sensorType, float sensorValue) {
	        this.sensor_id = sensorID;
	        this.sensor_type = sensorType;
	        this.sensor_value = sensorValue;
	    }
	 
		public String getSensorID() {
			return sensor_id;
		}
		public String getSensorType() {
			return sensor_type;
		}
		public float getSensorValue() {
			return sensor_value;
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		Gson gson = new Gson();
		String json = "{ \"sensor_id\":\"Team1\", \"sensor_type\":\"lightsensor\", \"sensor_value\":123.4 }";
		sensorData data = gson.fromJson(json, sensorData.class);
		System.out.println(data.getSensorID());
		System.out.println(data.getSensorType());
		System.out.println(data.getSensorValue());

		
	}
}

