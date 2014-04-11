package phase2Server;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;








import java.util.Scanner;

import com.FinalPhaseOnePacket.FinalProjectPhaseOnePacket1;

public class PhaseTwo implements SerialPortEventListener {
	static double currlat;
	static double currlng;
	static double lat2;
	static double lng2;
	static double AntennaLat;
	static double AntennaLng;

	static double anteLat;
	static double anteLng;
	static double CurrLat;
	static double CurrLng;
	static double EndLat;
	static double EndLng;
	
	static String arg;
	
	

	static double LatMeterPerDegree = 110862.59735714174;
	static double LngMeterPerDegree = 95900.54550307001;

	SerialPort serialPort;
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { "/dev/tty.usbserial-A9007UX1", // Mac
																				// OS
																				// X
			"/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM3", // Windows
	};
	/**
	 * A BufferedReader which will be fed by a InputStreamReader converting the
	 * bytes into characters making the displayed results codepage independent
	 */
	private BufferedReader input;
	/** The output stream to the port */
	private static OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public void initialize() {
		// the next line is for Raspberry Pi and
		// gets us into the while loop and was suggested here was suggested
		// http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
		// System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
					.nextElement();
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
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(
					serialPort.getInputStream()));
			output = serialPort.getOutputStream();
			
//			System.out.println("got output");
//			output.write("90".getBytes());

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
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
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = input.readLine();
				System.out.println(inputLine);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other
		// ones.
	}

	public static synchronized void writeData(String data) {
		System.out.println("Sent: " + data);
		try {
//			output.flush();
			output.write(data.getBytes());
		} catch (Exception e) {
			System.out.println("could not write to port");
			e.printStackTrace();
		}
	}

	public void start() {
		int port = 4444;
		ServerSocket serversocket = null;
		System.out.println("Waiting for New Connection..........");
		try {
			serversocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				Socket socket = serversocket.accept();
				Task task = new Task(socket);
				Thread thread = new Thread(task);
				thread.start();
				System.out.println("New Connection");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public static void main(String[] args) {
		boolean InputCheck = true;
		System.out.println("Press 1 , upload to fusion table");
		System.out.println("Press 2 , Recevie data from APP");
		System.out.println("Press 3 , exit");
		while(InputCheck){
		
		Scanner s = new Scanner(System.in);
		arg = s.next();
		if(arg.equals("3")){
			System.out.println("Exit");
			System.exit(0);
		}
		else if(arg.equals("1")){
			InputCheck = false;
		}
		else if (arg.equals("2")){
			InputCheck = false;
		}
		else{
			System.out.println("Invalid Input, Please type again");
			System.out.println("Press 1 , upload to fusion table");
			System.out.println("Press 2 , Recevie data from APP");
			System.out.println("Press 3 , exit");
		}
		}
		PhaseTwo main = new PhaseTwo();
		main.initialize();
		new PhaseTwo().start();
		
	}

	private class Task implements Runnable {
		private FinalProjectPhaseOnePacket1 packets;
		private double[] attributes;
		private Socket socket;
		private ObjectInputStream is;
		private ObjectOutputStream os;
		private Connection c = null;
		private Statement stmt = null;
		private String table1 = "Team3RSSI";
		private String table2 = "Team3RSSI_Estimated";

		// private Double[] latitude;
		// private Double[] longitude;

		public Task(Socket socket) {
			super();
			this.socket = socket;
		}

		@Override
		public void run() {
			
			
			
			if (arg.equals("1")){
				System.out.println("Upload fusion table");
				double rssi,lati,longi;
			     
			     ArrayList<Double> Rssi = new ArrayList <Double>();
			     ArrayList<Double> Lati = new ArrayList <Double>();
			     ArrayList<Double> Longi = new ArrayList <Double>();
				try {
					Class.forName("org.sqlite.JDBC");
					c = DriverManager.getConnection("jdbc:sqlite:E:/FinalProjectPhaseTwo.db");
				     c.setAutoCommit(false);
				     System.out.println("Opened database successfully");
				     uploadfusiontable uf = new uploadfusiontable();
				     
				     stmt = c.createStatement();
				     ResultSet rs = stmt.executeQuery("SELECT * FROM "+table2);
				     
				     while(rs.next()){
				    	  rssi = rs.getLong("RSSI");
				    	  lati = rs.getDouble("latitude");
				    	  longi = rs.getDouble("longitude");
				    	  
				    	  Rssi.add(rssi);
				    	  Lati.add(lati);
				    	  Longi.add(longi);
				     }
				     
				     double [] Rssi_array = new double[Rssi.size()];
				     double [] Lati_array = new double[Lati.size()];
				     double [] Longi_array = new double[Longi.size()];
				     
				     
				     Scanner scanner = new Scanner(System.in);
				     System.out.println("1.Create and insert into a new Fusion Table: press 1");
					System.out.println("2.Insert into an existing Fusion Table: press 2");
					System.out.println("Exit press 3");
					
					boolean check = true;
					while(check){
						String Choice = scanner.next();
						if (Choice.equals("1")){
							
							System.out.println("Please type the Table Name you want to create for Fusion Table");
							String tableName = scanner.next();
							String tableID = null;
							try {
								tableID = uf.createTable(tableName);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println("Fusion Table "+ tableName+" created successfully!");
							uf.update(tableID,Lati_array, Longi_array, Rssi_array);
							System.out.println("upload data successfully!");
							check = false;
						}
						else if (Choice.equals("2")){
							System.out.println("Please type the TableID for the fusion table:");
							String tableID = scanner.next();
//							String tableID = uf.createTable(tableName);
//							System.out.println("Fusion Table "+ tableName+" created successfully!");
							uf.update(tableID,Lati_array, Longi_array, Rssi_array);
							System.out.println("upload data successfully!");
							check = false;
						}
						else if(Choice.equals("3")){
							check = false;
						} 
						else{
							System.out.println("invalid input, please input number from 1 to 3:");
							check = true;
						}
						
					}
				} catch (ClassNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
			if (arg.equals("2")){
				System.out.println("receive data mode");
				try {
					is = new ObjectInputStream(socket.getInputStream());
					attributes = (double[]) is.readObject();
					anteLat = attributes[0];
					anteLng = attributes[1];
					CurrLat = attributes[2];
					CurrLng = attributes[3];
//					EndLat = attributes[4];
//					EndLng = attributes[5];
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}

				AntennaLat = deg2rad(attributes[0]);
				AntennaLng = deg2rad(attributes[1]);
				currlat = deg2rad(attributes[2]);
				currlng = deg2rad(attributes[3]);
//				lat2 = deg2rad(attributes[4]);
//				lng2 = deg2rad(attributes[5]);


				
				double Current_Point = CalcuAngle(AntennaLat,AntennaLng,currlat,currlng);
				String toCurrPoint = Double.toString(Current_Point);
				writeData(toCurrPoint);
				System.out.println("Degree to Current Point:"+ Current_Point);
				
				
				
//				if (EndLat == 0 && EndLng == 0) {
//					double toStartPoint = CalcuAngle(AntennaLat, AntennaLng, lat1,
//							lng1);
//					double toEndPoint = CalcuAngle(AntennaLat, AntennaLng, lat2,
//							lng2);
//
//					String StartPointDegree = Double.toString(toStartPoint);
//
//					
//
//					writeData(StartPointDegree);
//					System.out.println("Degree to Start:" + toStartPoint);
//
//				}
//
//				else {
//
//					double toEndPoint = CalcuAngle(AntennaLat, AntennaLng, lat2,
//							lng2);
//
//					String EndPointDegree = Double.toString(toEndPoint);
//
//					try {
//						Thread.sleep(2000);
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					System.out.println("Degree to End:" + toEndPoint);
//					writeData(EndPointDegree);
//
//				}
				
				
			}
			

		}

	}

	private static double CalcuAngle(double Lat1, double Lng1, double Lat2,
			double Lng2) {
		double latA = deg2rad(LatMeterPerDegree * Lat1);
		double latB = deg2rad(LatMeterPerDegree * Lat2);
		double lngA = deg2rad(LngMeterPerDegree * Lng1);
		double lngB = deg2rad(LngMeterPerDegree * Lng2);
		double result = 0;


		// WEST
		double nume = Math.sqrt(Math.pow((latB - latA), 2));
		double deno = Math.sqrt(Math.pow((lngB - lngA), 2)
				+ Math.pow((latB - latA), 2));
		double basic = Math.asin(nume / deno);

		if ((latB >= latA) && (lngB <= lngA)) {
			result = rad2deg(basic);
			System.out.println("FirstXX");
		}

		if ((latB >= latA) && (lngB >= lngA)) {
			result = 180 - rad2deg(basic);
			System.out.println("SecondYY");
		}

		if ((latB <= latA) && (lngB >= lngA)) {
			result = -180 + rad2deg(basic);
			System.out.println("ThirdZZ");
		}

		if ((latB <= latA) && (lngB <= lngA)) {
			result = -rad2deg(basic);
			System.out.println("FourthWW");
		}


		return result;


	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	// function to convert radians to decimal degrees
	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

}
