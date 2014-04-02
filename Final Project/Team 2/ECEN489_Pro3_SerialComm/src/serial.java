import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
 
public class serial implements SerialPortEventListener {
	
	final static double longitude_unit = 95880.84;
	final static double latitude_unit = 110862.94;
	
	
	final static double LONGITUDE = -96.34001913*longitude_unit;
	final static double LATITUDE = 30.62171753*latitude_unit;

	
    static SerialPort serialPort;
        /** The port we're normally going to use. */
    private static final String PORT_NAMES[] = {
            "/dev/tty.usbmodem15501", // Mac OS X
            "/dev/ttyUSB0", // Linux
            "COM3", // Windows
            };
    /** Buffered input stream from the port */
    private static InputStream input;
    /** The output stream to the port */
    private static OutputStream output;
    /** Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 2000;
    /** Default bits per second for COM port. */
    private static final int DATA_RATE = 9600;
 
    public void initialize() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
 
        // iterate through, looking for the port
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
 
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }
 
        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);
 
            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
 
            // open the streams
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
// 
//            String send = "2";
//    		output.write(send);
            
            
            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
 
    /**
     * This should be called when you stop using the port.
     * This will prevent port locking on platforms like Linux.
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }
 
    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent event) {
    	 switch(event.getEventType()) {
         case SerialPortEvent.BI:
         case SerialPortEvent.OE:
         case SerialPortEvent.FE:
         case SerialPortEvent.PE:
         case SerialPortEvent.CD:
         case SerialPortEvent.CTS:
         case SerialPortEvent.DSR:
         case SerialPortEvent.RI:
         case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
             break;
         case SerialPortEvent.DATA_AVAILABLE:
             try {
                 int available = input.available();
                 byte chunk[] = new byte[available];
                 input.read(chunk, 0, available);
  
                 // Displayed results are codepage dependent
                 System.out.print(new String(chunk));
                 break;
             } catch (Exception e) {
                 System.err.println(e.toString());
                 break;
             }
    	 }
    }
 
    public static void main(String[] args) throws Exception {
        try {
        	//setup udp server
        	double[] orientation = new double[3];

			serial main = new serial();
			main.initialize();
			System.out.println("Started");
			
			boolean flag = false; // terminate flag
			byte[] receive = new byte[500];
			String angle = null;
			while(!flag){
				
//				// for user input
//				System.out.println("Input angle:");
//				Scanner reader = new Scanner(System.in);
//			    angle = reader.next();
				// for IMU input
				Thread.sleep(1000);
				orientation = setupUDP(10000);
				//System.out.println("sending"+(orientation-180));
				double angle_1 = calAngle(orientation[1],orientation[0]);
//				angle = Integer.toString(-(orientation-180));
				angle = Double.toString(angle_1);

				// for longitude and latitude input
//		    	angle = calAngle(90, 88) + "";//  calAngle(double longitude, double latitude)

				// sending angle
				output.write(angle.getBytes());
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
    	
    	
    }
    
    // calculate the angle between two point and the base station
    public static double calAngle(double longitude, double latitude){
    	double angle;

    	double[] p0 = {LONGITUDE, LATITUDE};
    	double[] p2 = {longitude*longitude_unit, latitude*latitude_unit};
    	
    	double x1 = 100;
		double y1 = 0;
		double x2 = p2[0] - p0[0];
		double y2 = p2[1] - p0[1];
		
		if(y2>0)
			angle = Math.acos((x1*x2+y1*y2)/(Math.sqrt(Math.pow(x1,2)+Math.pow(y1,2))*Math.sqrt(Math.pow(x2,2)+Math.pow(y2,2))));
		else 
			angle = -Math.acos((x1*x2+y1*y2)/(Math.sqrt(Math.pow(x1,2)+Math.pow(y1,2))*Math.sqrt(Math.pow(x2,2)+Math.pow(y2,2)))); 

		return angle/Math.PI*180;    	
    }
    // setup and receive a UDP packet
    public static double[] setupUDP(int ServerPort) throws SocketException{
    	double[] databack = new double[3];
	   	byte[] receiveData = new byte[1024];
   		DatagramSocket serverSocket = new DatagramSocket(ServerPort);
   		try{
              DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
              serverSocket.receive(receivePacket);
              String sentence = new String( receivePacket.getData());
              String[] data = sentence.split(",");
              databack[0] = (double) Double.parseDouble(data[0]);
              databack[1] = (double) Double.parseDouble(data[1]);
              databack[2] = (double) Double.parseDouble(data[2]);
              System.out.println(databack[0]+"  "+databack[1]);
              serverSocket.close();
              return databack;
	   }
	   catch(IOException e){
		   System.err.println(e);
		   serverSocket.close();
	   }
		return databack;

      }

}