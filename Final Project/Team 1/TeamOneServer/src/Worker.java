import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.zpartal.finalproject.datapackets.DataPoint;

public class Worker implements Runnable {
	private Socket client;	
	static private ObjectInputStream ois;
    private Config config = new Config();
    ArrayList<DataPoint> dataset = null;
    BlockingQueue<DataPoint> DBQueue = null;
    BlockingQueue<DataPoint> servoQueue = null;

	public Worker(Socket client) {
		super();
		this.client = client;
	}
	
	@Override
	public void run() {
        dataset = new ArrayList<DataPoint>();
        DBQueue = new ArrayBlockingQueue<DataPoint>(1024);
        servoQueue = new ArrayBlockingQueue<DataPoint>(1024);

        try {
            new Thread(new DatabaseHandler(DBQueue)).start();
            new Thread(new ServoDriver(servoQueue)).start();
            ois = new ObjectInputStream(client.getInputStream());

            while(true) {
                DataPoint dp = (DataPoint) ois.readObject();
                System.out.println(dp.toString());
                if (dp.getSsid().equals("stop")) { break; }
                dataset.add(dp);
                DBQueue.put(dp);
                servoQueue.put(dp);
            }
            ois.close();
            client.close();

            // Fusion Table
//            new Thread(new FusionTableHandler(dataset)).start();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
