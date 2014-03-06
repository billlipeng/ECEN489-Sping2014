import java.sql.ResultSet;
import java.sql.SQLException;



public class DBTest {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		DBHandler handler = new DBHandler("proj2testdata.db");
		handler.getDBValues();

		
	}

}
