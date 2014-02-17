import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.zpartal.project1.datapackets.*;

public class Worker implements Runnable {
	private Socket client;	
	static private ObjectInputStream ois;
    private Properties properties = new Properties();
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
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Local DB
        try {
            Thread t = new Thread(new DatabaseHandler(dataset));
            t.start();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        // Fusion Table

	}
}
