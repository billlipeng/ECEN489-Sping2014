import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.zpartal.project1.datapackets.*;

public class Worker implements Runnable {
	private Socket client;	
	static private ObjectInputStream ois;
    private Config config = new Config();
    ArrayList<DataPoint> dataset = null;

	public Worker(Socket client) {
		super();
		this.client = client;
	}
	
	@Override
	public void run() {
        try {
            ois = new ObjectInputStream(client.getInputStream());
            dataset = (ArrayList<DataPoint>) ois.readObject();
            ois.close();
            client.close();

            // Local DB
            new Thread(new DatabaseHandler(dataset)).start();

            // Fusion Table
//            new Thread(new FusionTable(dataset)).start();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
	}
}
