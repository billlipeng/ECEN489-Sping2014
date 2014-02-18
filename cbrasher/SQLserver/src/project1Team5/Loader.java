package project1Team5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.*;

public class Loader {

	private String f = "config.json";

	public Loader(Config cg) throws IOException {
		String json = new String(Files.readAllBytes(Paths.get(f)));
		handler h = new Gson().fromJson(json, handler.class);
		cg.setPath(h.path);
		cg.setT(h.table);
		cg.setCol(h.column);
	}

	private class handler {
		public String path;
		public String table;
		public ArrayList<String> column;
	}
}
