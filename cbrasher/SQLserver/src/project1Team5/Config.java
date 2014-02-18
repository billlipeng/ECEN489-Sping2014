package project1Team5;

import java.util.ArrayList;

public class Config {

	public static String path;
	public static String table;
	public static ArrayList<String> column;

	public static void setPath(String path) {
		Config.path = path;
	}

	public static void setT(String table) {
		Config.table = table;
	}

	public static void setCol(ArrayList<String> column) {
		Config.column = column;
	}

	public String toString() {
		return path;
	}

}
