import java.sql.SQLException;
import java.util.ArrayList;

import com.mhardiman.project2app.imu;



public class DBTest {

	private static ArrayList<imu> imus;
	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		imus = new ArrayList<imu>();
		
		DBHandler handler = new DBHandler("projtwo.db");
		
		imus = handler.getDBValues();
		
		DataHandler dhandler = new DataHandler(imus);
		dhandler.calculatePathKalman();
		//System.out.println(dhandler.getTotalDistance());
		
	}

}
