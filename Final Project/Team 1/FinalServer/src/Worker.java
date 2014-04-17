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
    private Tester tester = new Tester();
    private double[] cals; //takes base ssid
    private double[] base; 

	public Worker(Socket client) {
		super();
		this.client = client;
	}
	
	@Override
	public void run() {
		cals = new double[2];
		base = new double[2];
		boolean gotcals = false;
		boolean gotbase = false;
		boolean gottrack = false;
		double main_angle = 0.0;
		
		for(int i = 0; i < 2; ++i){
			cals[i] = 0.0;
			base[i] = 0.0;
		}
        dataset = new ArrayList<DataPoint>();
        DBQueue = new ArrayBlockingQueue<DataPoint>(1024);
        servoQueue = new ArrayBlockingQueue<DataPoint>(1024);

        try {
            //new Thread(new DatabaseHandler(DBQueue)).start();
            new Thread(new ServoDriver(servoQueue)).start();
            ois = new ObjectInputStream(client.getInputStream());

            while(true) {
                DataPoint dp = (DataPoint) ois.readObject();
                
                if(dp.getSsid().contains("base")){
                	cals[0] = dp.getLatitude();
                	cals[1] = dp.getLongitude();
                	gotcals = true;
                }else if(dp.getSsid().contains("calibration")){
                	base[0] = dp.getLatitude();
                	base[1] = dp.getLongitude();
                	gotbase = true;
                }else{
                	gottrack = true;
                }
                
                if(gotcals && gotbase && gottrack){
                	System.out.println("Diff "+ (dp.getLatitude()-cals[0]) +","+(dp.getLongitude()-cals[1]));
                Tester.MAIN_ANGLE = Tester.getMotorAngle((float) base[0], (float) base[1], (float) cals[0], (float) cals[1], (float) dp.getLatitude(), (float) dp.getLongitude());
            	System.out.println("Angle "+ Tester.MAIN_ANGLE);
                }
                
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
