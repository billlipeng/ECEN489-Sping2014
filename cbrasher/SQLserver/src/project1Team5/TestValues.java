package project1Team5;

import java.io.IOException;
import java.util.ArrayList;

import com.team5.project1.datapackets.DataPoint;

public class TestValues {

	public static void main(String[] args) throws ClassNotFoundException,
			IOException {
		// TODO Auto-generated method stub

		Config props = new Config();
		new Loader(props);
		System.out.println(props.toString());

		DataPoint dp = new DataPoint.Builder().time("04:45:00")
				.date("2014-2-17").client_id("destoroyah").run_id("run1")
				.latitude(1234.1234).longitude(4342.2323).bearing(22.78)
				.speed(19.7).altitude(552).sensor_id("S5").sensor_type("temp")
				.sensor_value(112.3).attribute("sensor").build();

		System.out.println(dp.toString());

		ArrayList<DataPoint> dataset = new ArrayList<DataPoint>();
		dataset.add(dp);
		DBHand dbh = new DBHand(dataset);
		new Thread(new DBHand(dataset)).start();
		System.out.println(dbh.createInsertSQL(dp));

	}

}
