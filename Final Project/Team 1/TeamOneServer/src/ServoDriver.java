import java.util.concurrent.BlockingQueue;

import com.zpartal.finalproject.datapackets.DataPoint;

public class ServoDriver implements Runnable {
    protected BlockingQueue<DataPoint> queue = null;

    public ServoDriver(BlockingQueue<DataPoint> _queue) {
        this.queue = _queue;
    }

    @Override
    public void run() {
        SerialHandler sh = new SerialHandler();
        if ( sh.initialize() ) {
            sh.sendData("y");
            try { Thread.sleep(2000); } catch (InterruptedException ie) {}
            sh.sendData("n");
            try { Thread.sleep(2000); } catch (InterruptedException ie) {}
            sh.close();
        }

        // Wait 5 seconds then shutdown
        try { Thread.sleep(2000); } catch (InterruptedException ie) {}
    }
}
