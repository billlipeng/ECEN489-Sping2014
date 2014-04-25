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

    double base_lat = 0.0;
    double base_lon = 0.0;
    double cal_lat = 0.0;
    double cal_lon = 0.0;

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
            ois = new ObjectInputStream(client.getInputStream());
            DataPoint dp = (DataPoint) ois.readObject();

            if (dp.getSsid().equals("base")) {
                base_lat = dp.getLatitude();
                base_lon = dp.getLongitude();
                System.out.println("Base value set");
            }

            dp = (DataPoint) ois.readObject();
            if (dp.getSsid().equals("calibration")) {
                cal_lat = dp.getLatitude();
                cal_lon = dp.getLongitude();
                System.out.println("Calibration value set");
            }

            new Thread(new DatabaseHandler(DBQueue)).start();
            new Thread(new ServoDriver(servoQueue, base_lat,base_lon,cal_lat,cal_lon)).start();

            while(true) {
                dp = (DataPoint) ois.readObject();
                System.out.println(dp.toString());
                if (dp.getSsid().equals(config.END_CODE)) { break; }
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
