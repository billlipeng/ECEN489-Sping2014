package project1Team5;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.team5.project1.datapackets.*;

public class Runner implements Runnable {

	private Config cg = new Config();
	private ObjectInputStream input;
	private Socket app;
	ArrayList<DataPoint> data = null;

	public Runner(Socket app) {
		super();
		this.app = app;
	}

	@Override
	public void run() {
		try {
			input = new ObjectInputStream(app.getInputStream());
			data = (ArrayList<DataPoint>) input.readObject();
			input.close();
			app.close();

			// Fusion Table handler (code here)

			new Thread(new DBHand(data)).start();

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
