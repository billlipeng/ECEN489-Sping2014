package Phase3Server;

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
import java.util.Collections;
import java.util.Enumeration;
import java.util.Scanner;








import com.FinalPhaseOnePacket.FinalProjectPhaseOnePacket1;

public class PhaseThree implements SerialPortEventListener {
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
	static double rssi_Value;
	
	static String arg;
	
	static String sql1 = null;
	static String sql2 = null;
	
	static private Connection c = null;
	static private Statement stmt = null;
	static private String table1 = "testing_ant_lat_lng";
	static private String table2 = "RSSI_Collected_Values";
	static private String table3 = "RSSI_Estimated";
	static private String table4 = "Tracking_Mode_Values";
	
	static private FinalProjectPhaseOnePacket1 packets;
	static private String table_ID = "1JUw5I0uXdgOiWilp63tepK-QTPqoHlJHNBIruh_E";
	static private double[] attributes;
	
	static private ObjectInputStream is;
	static private ObjectOutputStream os;
	static uploadfusiontable uf1;
	
	Double[] latitude;
	Double[] longitude;
	Double[] RSSI;
	

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
		 uf1 = new uploadfusiontable();
//	     try {
//			table_ID =  uf1.createTable("Data_Stream_mode");
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		System.out.println("Press 1 , Calculate Estimated Values and Upload to Fusion Table");
		System.out.println("Press 2 , Data Stream Mode");
		System.out.println("Press 3 , Data Collection Mode");
		System.out.println("Press 4 , Upload Path to Fusion Table");
		System.out.println("Press 5 , exit");
		
		while(InputCheck){
		
		Scanner s = new Scanner(System.in);
		arg = s.next();
		if(arg.equals("4")){
			double rssi,lati,longi,wei;
		     
		     ArrayList<Double> Rssi = new ArrayList <Double>();
		     ArrayList<Double> Lati = new ArrayList <Double>();
		     ArrayList<Double> Longi = new ArrayList <Double>();
		     ArrayList<Double> Weight = new ArrayList <Double>();
			try {
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:D:/FinalProjectTeam3.db");
			     c.setAutoCommit(false);
			     System.out.println("Opened database successfully");
			     uploadfusiontable uf = new uploadfusiontable();
			     
			     stmt = c.createStatement();
			     ResultSet rs = stmt.executeQuery("SELECT * FROM "+table4);
			     
			     while(rs.next()){
			    	  rssi = rs.getDouble("RSSI");
			    	  lati = rs.getDouble("latitude");
			    	  longi = rs.getDouble("longitude");
//			    	  wei =rs.getDouble("weight");
			    	  Rssi.add(rssi);
			    	  Lati.add(lati);
			    	  Longi.add(longi);
//			    	  Weight.add(wei);
			    	  
			     }
			     
			     double [] Rssi_array = new double[Rssi.size()];
			     double [] Lati_array = new double[Lati.size()];
			     double [] Longi_array = new double[Longi.size()];
			     double [] Weight_array = new double[Rssi.size()];
			     double W = Math.abs(Collections.min(Rssi));
			   
//			     System.out.println(W);
			    		 
			    		 
			    for (int n = 0;n<Rssi_array.length;n++){
			    		 Rssi_array[n] = Rssi.get(n);
			    		 Weight_array[n] = (Rssi.get(n)+W)/(W+20);
			    }
			    for (int n = 0;n<Lati_array.length;n++){
		    		 Lati_array[n] = Lati.get(n);
			    }
			    for (int n = 0;n<Longi_array.length;n++){
		    		 Longi_array[n] = Longi.get(n);
			    }
			     
			     
			     
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
							tableID = uf.createTable1(tableName);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("Fusion Table "+ tableName+" created successfully!");
						int length = Rssi_array.length;
						if (length<400){
							uf.update1(tableID,Lati_array, Longi_array, Rssi_array, Weight_array);
							
							
						}
						else{
							
							int totalnum = Lati_array.length;
						      int iteration = totalnum/400;
						      int reminder = totalnum%400;
						      System.out.println(totalnum);
						      System.out.println(iteration);
						      System.out.println(reminder);
						      if (reminder!=0){   
						      for (int m= 0;m<iteration;m++){
						    	  double[] tempLat = new double[400];
						    	  double[] tempLng = new double[400];
						    	  double[] tempRSSI = new double[400];
						    	  double[] tempWeight = new double[400];
						    	  for(int n = 0;n<400;n++){
						    		  tempLat[n]= Lati_array[m*400+n];
						    		  tempLng[n]= Longi_array[m*400+n];
						    		  tempRSSI[n]= Rssi_array[m*400+n];
						    		  tempWeight[n]=Weight_array[m*400+n];
						    	  }
						    	  uf.update1(tableID,tempLat,tempLng,tempRSSI,tempWeight);
						      }
						      
						      double[] tempLat = new double[reminder];
					    	  double[] tempLng = new double[reminder];
					    	  double[] tempRSSI = new double[reminder];
					    	  double[] tempWeight = new double[reminder];
						      
						      for (int n = 0;n<reminder;n++){
						    	  tempLat[n] = Lati_array[iteration*400+n];
						    	  tempLng[n] = Longi_array[iteration*400+n];
						    	  tempRSSI[n] = Rssi_array[iteration*400+n];
						    	  tempWeight[n]=Weight_array[iteration*400+n];
						      }
						      uf.update1(tableID,tempLat,tempLng,tempRSSI,tempWeight);
							}
						      
						      else{
						    	  for (int m= 0;m<iteration;m++){
							    	  double[] tempLat = new double[400];
							    	  double[] tempLng = new double[400];
							    	  double[] tempRSSI = new double[400];
							    	  double[] tempWeight = new double[400];
							    	  for(int n = 0;n<400;n++){
							    		  tempLat[n]= Lati_array[m*400+n];
							    		  tempLng[n]= Longi_array[m*400+n];
							    		  tempRSSI[n]= Rssi_array[m*400+n];
							    		  tempWeight[n]=Weight_array[m*400+n];
							    	  }
							    	  uf.update1(tableID,tempLat,tempLng,tempRSSI,tempWeight);
							      }
						      }
							
							
						      
						}
						
						
						System.out.println("upload data successfully!");
						check = true;
						System.out.println("1.Create and insert into a new Fusion Table: press 1");
						System.out.println("2.Insert into an existing Fusion Table: press 2");
						System.out.println("Exit press 3");
					}
					else if (Choice.equals("2")){
						System.out.println("Please type the TableID for the fusion table:");
						String tableID = scanner.next();
//						String tableID = uf.createTable(tableName);
//						System.out.println("Fusion Table "+ tableName+" created successfully!");
						uf.update(tableID,Lati_array, Longi_array, Rssi_array);
						System.out.println("upload data successfully!");
						check = true;
						System.out.println("1.Create and insert into a new Fusion Table: press 1");
						System.out.println("2.Insert into an existing Fusion Table: press 2");
						System.out.println("Exit press 3");
					}
					else if(Choice.equals("3")){
						System.out.println("exit");
						check = false;
					} 
					else{
						System.out.println("invalid input, please input number from 1 to 3:");
						check = true;
						System.out.println("1.Create and insert into a new Fusion Table: press 1");
						System.out.println("2.Insert into an existing Fusion Table: press 2");
						System.out.println("Exit press 3");
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
		
		
		
		
		
		else if(arg.equals("5")){
			System.out.println("Exit");
			System.exit(0);
		}
		else if(arg.equals("1")){
			System.out.println("Calculating and Uploading......");
			
			
			
			
			
			
			
			
			
			algorithm alg = new algorithm();
			
			
			
			double rssi,lati,longi,wei;
		     
		     ArrayList<Double> Rssi = new ArrayList <Double>();
		     ArrayList<Double> Lati = new ArrayList <Double>();
		     ArrayList<Double> Longi = new ArrayList <Double>();
		     ArrayList<Double> Weight = new ArrayList <Double>();
			try {
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:D:/FinalProjectTeam3.db");
			     c.setAutoCommit(false);
			     System.out.println("Opened database successfully");
			     uploadfusiontable uf = new uploadfusiontable();
			     
			     stmt = c.createStatement();
			     ResultSet rs = stmt.executeQuery("SELECT * FROM "+table3);
			     
			     while(rs.next()){
			    	  rssi = rs.getDouble("RSSI");
			    	  lati = rs.getDouble("latitude");
			    	  longi = rs.getDouble("longitude");
			    	  wei =rs.getDouble("weight");
			    	  Rssi.add(rssi);
			    	  Lati.add(lati);
			    	  Longi.add(longi);
			    	  Weight.add(wei);
			    	  
			     }
			     
			     double [] Rssi_array = new double[Rssi.size()];
			     double [] Lati_array = new double[Lati.size()];
			     double [] Longi_array = new double[Longi.size()];
			     double [] Weight_array = new double[Weight.size()];
			     
			   
			    for (int n = 0;n<Rssi_array.length;n++){
			    		 Rssi_array[n] = Rssi.get(n);
			    }
			    for (int n = 0;n<Lati_array.length;n++){
		    		 Lati_array[n] = Lati.get(n);
			    }
			    for (int n = 0;n<Longi_array.length;n++){
		    		 Longi_array[n] = Longi.get(n);
			    }
			    for (int n = 0;n<Weight_array.length;n++){
		    		 Weight_array[n] = Weight.get(n);
			    }
			     
			     
			     
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
							tableID = uf.createTable1(tableName);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("Fusion Table "+ tableName+" created successfully!");
						int length = Rssi_array.length;
						if (length<400){
							uf.update1(tableID,Lati_array, Longi_array, Rssi_array, Weight_array);
							
							
						}
						else{
							
							int totalnum = Lati_array.length;
						      int iteration = totalnum/400;
						      int reminder = totalnum%400;
						      System.out.println(totalnum);
						      System.out.println(iteration);
						      System.out.println(reminder);
						      if (reminder!=0){   
						      for (int m= 0;m<iteration;m++){
						    	  double[] tempLat = new double[400];
						    	  double[] tempLng = new double[400];
						    	  double[] tempRSSI = new double[400];
						    	  double[] tempWeight = new double[400];
						    	  for(int n = 0;n<400;n++){
						    		  tempLat[n]= Lati_array[m*400+n];
						    		  tempLng[n]= Longi_array[m*400+n];
						    		  tempRSSI[n]= Rssi_array[m*400+n];
						    		  tempWeight[n]=Weight_array[m*400+n];
						    	  }
						    	  uf.update1(tableID,tempLat,tempLng,tempRSSI,tempWeight);
						      }
						      
						      double[] tempLat = new double[reminder];
					    	  double[] tempLng = new double[reminder];
					    	  double[] tempRSSI = new double[reminder];
					    	  double[] tempWeight = new double[reminder];
						      
						      for (int n = 0;n<reminder;n++){
						    	  tempLat[n] = Lati_array[iteration*400+n];
						    	  tempLng[n] = Longi_array[iteration*400+n];
						    	  tempRSSI[n] = Rssi_array[iteration*400+n];
						    	  tempWeight[n]=Weight_array[iteration*400+n];
						      }
						      uf.update1(tableID,tempLat,tempLng,tempRSSI,tempWeight);
							}
						      
						      else{
						    	  for (int m= 0;m<iteration;m++){
							    	  double[] tempLat = new double[400];
							    	  double[] tempLng = new double[400];
							    	  double[] tempRSSI = new double[400];
							    	  double[] tempWeight = new double[400];
							    	  for(int n = 0;n<400;n++){
							    		  tempLat[n]= Lati_array[m*400+n];
							    		  tempLng[n]= Longi_array[m*400+n];
							    		  tempRSSI[n]= Rssi_array[m*400+n];
							    		  tempWeight[n]=Weight_array[m*400+n];
							    	  }
							    	  uf.update1(tableID,tempLat,tempLng,tempRSSI,tempWeight);
							      }
						      }
							
							
						      
						}
						
						
						System.out.println("upload data successfully!");
						check = true;
						System.out.println("1.Create and insert into a new Fusion Table: press 1");
						System.out.println("2.Insert into an existing Fusion Table: press 2");
						System.out.println("Exit press 3");
					}
					else if (Choice.equals("2")){
						System.out.println("Please type the TableID for the fusion table:");
						String tableID = scanner.next();
//						String tableID = uf.createTable(tableName);
//						System.out.println("Fusion Table "+ tableName+" created successfully!");
						uf.update(tableID,Lati_array, Longi_array, Rssi_array);
						System.out.println("upload data successfully!");
						check = true;
						System.out.println("1.Create and insert into a new Fusion Table: press 1");
						System.out.println("2.Insert into an existing Fusion Table: press 2");
						System.out.println("Exit press 3");
					}
					else if(Choice.equals("3")){
						System.out.println("exit");
						check = false;
					} 
					else{
						System.out.println("invalid input, please input number from 1 to 3:");
						check = true;
						System.out.println("1.Create and insert into a new Fusion Table: press 1");
						System.out.println("2.Insert into an existing Fusion Table: press 2");
						System.out.println("Exit press 3");
					}
					
				}
			} catch (ClassNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			InputCheck = true;
			System.out.println("Press 1 , Calculate Estimated Values and Upload to Fusion Table");
			System.out.println("Press 2 , Data Stream Mode");
			System.out.println("Press 3 , Data Collection Mode");
			System.out.println("Press 4 , Upload Path to Fusion Table");
			System.out.println("Press 5 , exit");
		}
		else if (arg.equals("2")){
			InputCheck = false;
		}
		else if (arg.equals("3")){
			InputCheck = false;
		}
		else{
			System.out.println("Press 1 , Calculate Estimated Values and Upload to Fusion Table");
			System.out.println("Press 2 , Data Stream Mode");
			System.out.println("Press 3 , Data Collection Mode");
			System.out.println("Press 4 , Upload Path to Fusion Table");
			System.out.println("Press 5 , exit");
		}
		}
		PhaseThree main = new PhaseThree();
		main.initialize();
		new PhaseThree().start();
		
	}

	private class Task implements Runnable {
		
		private Socket socket;
		

		// private Double[] latitude;
		// private Double[] longitude;

		public Task(Socket socket) {
			super();
			this.socket = socket;
		}

		@Override
		public void run() {
			
			
			
			
			
			
			if (arg.equals("2")){
				System.out.println("Data Stream Mode");
				try {
					is = new ObjectInputStream(socket.getInputStream());
					attributes = (double[]) is.readObject();
					anteLat = attributes[0];
					anteLng = attributes[1];
					CurrLat = attributes[2];
					CurrLng = attributes[3];
					rssi_Value = attributes[4];
					
					
//					EndLng = attributes[5];
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				
				try {
					Class.forName("org.sqlite.JDBC");
					c = DriverManager.getConnection("jdbc:sqlite:D:/FinalProjectTeam3.db");
				     c.setAutoCommit(false);
				     System.out.println("Opened database successfully");
				     
				     stmt = c.createStatement();
//				     ResultSet rs = stmt.executeQuery("SELECT * FROM "+table2);
				     double [] Latitude_temp = new double[1];
				     double [] Longitude_temp = new double[1];
				     double [] RSSI_temp = new double[1];
				     
				     
				     sql1 = "insert into "+ table4
				    		 +"("+"RSSI"
				    		 +","+"latitude"
				    		 +","+"longitude"+")"
				    		 +" values("
				    		 +rssi_Value
				    		 +","+CurrLat
				    		 +","+CurrLng
				    		 +");";
				     System.out.println(sql1);
				     System.out.println(rssi_Value);
				     stmt.executeUpdate(sql1);
				     stmt.close();
				     c.commit();
				     c.close();
				     
//				     Latitude_temp[0] = CurrLat;
//				     Longitude_temp[0] = CurrLng;
//				     RSSI_temp[0] = rssi_Value;
//				     uf1.update(table_ID, Latitude_temp, Longitude_temp, RSSI_temp);
//				     System.out.println("Uploaded to Fusion Table!");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
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
				
			}
			
			if (arg.equals("3")){
				System.out.println("Data Collection Mode");
				try {
					is = new ObjectInputStream(socket.getInputStream());
					packets = (FinalProjectPhaseOnePacket1) is.readObject();
					latitude = packets.getLatitude();
					longitude = packets.getLongitude();
					RSSI =packets.getRSSI();
					Class.forName("org.sqlite.JDBC");
					c = DriverManager.getConnection("jdbc:sqlite:D:/FinalProjectTeam3.db");
				     c.setAutoCommit(false);
				     System.out.println("Opened database successfully");
				     
				     
//				     ResultSet rs = stmt.executeQuery("SELECT * FROM "+table2);
				     
				     for (int i = 0;i<RSSI.length;i++){
				    	 stmt = c.createStatement();
				     sql2 = "insert into "+ table2 
				    		 +"("+"RSSI"
				    		 +","+"latitude"
				    		 +","+"longitude"+")"
				    		 +" values("
				    		 +RSSI[i]
				    		 +","+latitude[i]
				    		 +","+longitude[i]
				    		 +");";
				     System.out.println(sql2);
				     stmt.executeUpdate(sql2);
				     stmt.close();
				    }
				     
				     
				     System.out.println("Committing Data.......");
				     c.commit();
				     c.close();
				     System.out.println("Data Collected");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
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

		else if ((latB >= latA) && (lngB >= lngA)) {
			result = 180 - rad2deg(basic);
			System.out.println("SecondYY");
		}

		else if ((latB <= latA) && (lngB >= lngA)) {
			result = -180 + rad2deg(basic);
			System.out.println("ThirdZZ");
		}

		else{//if ((latB <= latA) && (lngB <= lngA)) {
			result = -rad2deg(basic);
			System.out.println("FourthWW");
		}

		if (result >=-180 && result <= 90)
			result = result + 90;
		else if (result > 90)
			result = result -270;
			
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
