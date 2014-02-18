package project1Team5;

import java.sql.*;
import java.util.ArrayList;

import com.team5.project1.datapackets.*;

//SQLite database is handled in this part of the code

public class DBHand implements Runnable {

	private Config cg = new Config();
	private ArrayList<DataPoint> data = null;
	private Connection connect = null;

	public DBHand(ArrayList<DataPoint> Data) throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		this.data = Data;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			connect = DriverManager.getConnection("jdbc:sqlite:" + Config.path);
			Statement update = connect.createStatement();
			update.setQueryTimeout(60);

			for (DataPoint dp : data) {
				update.executeUpdate(createInsertSQL(dp));
			}
		} catch (SQLException sql) {
			System.out.println(sql);
		} finally {
			// close connection if app disconnects
			try {
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				System.out.println("Connection Closed");
			}
		}

	}

	protected String createInsertSQL(DataPoint dp) {
		StringBuilder s = new StringBuilder();
		s.append("INSERT INTO ");
		s.append(Config.table);
		s.append(" (");
		String space = "";
		for (String sn : Config.column) {
			s.append(space);
			s.append(sn);
			space = ",";
		}
		s.append(") Values (");
		s.append(dp.toString());
		s.append(");");
		return s.toString();
	}

}
