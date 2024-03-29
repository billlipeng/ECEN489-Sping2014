import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Random;

import au.com.bytecode.opencsv.CSVReader;

public class Main {

	static double Lat = 0;
	static double Lon = 0;
	static double speed = 0;
	static double bearing = 0;
	static double speedFactor = 1;
	static double bearingFactor = 5;	
	static double previousBearing = 0;
	static double maxDeltaBearing = 1;	//degrees
	static double curl = 0.05;
	static int minutes = 20;
	static int smallPeriod = 10; //seconds
	static int bigPeriod = 100;
	static int gap = bigPeriod/smallPeriod;
	static String folder = "C:\\Users\\ClayClay\\Desktop\\";
	static ArrayList<DataPoint> generated = new ArrayList<DataPoint>();


	private static long time;
	private static double latitude;
	private static double longitude;
	private static double bearing1;
	private static double speed1;
	private static double accelX;
	private static double accelY;
	private static double accelZ;
	private static double orientationA;
	private static double orientationP;
	private static double orientationR;
	private static double rotVecX;
	private static double rotVecY;
	private static double rotVecZ;
	private static double rotVecC;
	private static double linAccX;
	private static double linAccY;
	private static double linAccZ;
	private static double gravityX;
	private static double gravityY;
	private static double gravityZ;
	private static double gyroX;
	private static double gyroY;
	private static double gyroZ;
	static ArrayList<DataPoint> IMUdata1 = new ArrayList<DataPoint>();
	static ArrayList<DataPoint> IMUdata2 = new ArrayList<DataPoint>();
	
	public static void main(String []args){

		try {
			Reader reader = new FileReader("C:\\Users\\ClayClay\\Desktop\\SampleData2Proj2.csv");
			CSVReader csvReader = new CSVReader(new FileReader("C:\\Users\\ClayClay\\Desktop\\SampleData2Proj2.csv"));
			String[] row = csvReader.readNext();
			while ((row = csvReader.readNext()) != null) {
				time = Long.parseLong(row[0].toString());
				if (row[1].toString().isEmpty())
					latitude = 0;
				else 
					latitude = Double.parseDouble(row[1].toString());
				if (row[2].toString().isEmpty())
					longitude = 0;
				else
					longitude = Double.parseDouble(row[2].toString());
				bearing1 = Double.parseDouble(row[3].toString());
				speed1 = Double.parseDouble(row[4].toString());
				accelX = Double.parseDouble(row[5].toString());
				accelY = Double.parseDouble(row[6].toString());
				accelZ = Double.parseDouble(row[7].toString());
				orientationA = Double.parseDouble(row[8].toString());
				orientationP = Double.parseDouble(row[9].toString());
				orientationR = Double.parseDouble(row[10].toString());
				rotVecX = Double.parseDouble(row[11].toString());
				rotVecY = Double.parseDouble(row[12].toString());
				rotVecZ = Double.parseDouble(row[13].toString());
				rotVecC = Double.parseDouble(row[14].toString());
				linAccX = Double.parseDouble(row[15].toString());
				linAccY = Double.parseDouble(row[16].toString());
				linAccZ = Double.parseDouble(row[17].toString());
				gravityX = Double.parseDouble(row[18].toString());
				gravityY = Double.parseDouble(row[19].toString());
				gravityZ = Double.parseDouble(row[20].toString());
				gyroX = Double.parseDouble(row[21].toString());
				if (row[22].toString().isEmpty())
					gyroY = 0.0;
				else 
					gyroY = Double.parseDouble(row[22].toString());
				if (row[23].toString().isEmpty())
					gyroZ = 0.0;
				else
					gyroZ = Double.parseDouble(row[23].toString());


				DataPoint dp = new DataPoint.Builder().time(time)
						.latitude(latitude)
						.longitude(longitude)
						.bearing(bearing1)
						.speed(speed1)
						.accelX(accelX)
						.accelY(accelY)
						.accelZ(accelZ)
						.orientationA(orientationA)
						.orientationP(orientationP)
						.orientationR(orientationR)
						.rotVecX(rotVecX)
						.rotVecY(rotVecY)
						.rotVecZ(rotVecZ)
						.rotVecC(rotVecC)
						.linAccX(linAccX)
						.linAccY(linAccY)
						.linAccZ(linAccZ)
						.gravityX(gravityX)
						.gravityY(gravityY)
						.gravityZ(gravityZ)
						.gyroX(gyroX)
						.gyroY(gyroY)
						.gyroZ(gyroZ).build();
				IMUdata1.add(dp);
				IMUdata2.add(dp);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//generated = genFake();
		//toCsv(trim(generated),"generated");

		/////////////////////// Choose One interpolation method for the path reconstruction
		ArrayList<DataPoint> guess1 = linear(IMUdata1);
		ArrayList<DataPoint> guess2 = vector(IMUdata2);

		toCsv(guess1,"guess1");
		toCsv(guess2,"guess2");
		System.out.println("done");
	}

	private static ArrayList<DataPoint> vector(ArrayList<DataPoint> dps){	//sums vectors,then scales to end point
		ArrayList<ArrayList<DataPoint>> segments = segment(dps);
		ArrayList<ArrayList<DataPoint>> guesses = new ArrayList<ArrayList<DataPoint>>();
		for (int i = 0 ; i < segments.size() ; i++){
			ArrayList<DataPoint> segment = segments.get(i);

			ArrayList<DataPoint> guess = new ArrayList<DataPoint>();
			///////Interpolation begins here
			Double xRelative = 0.0;
			Double yRelative = 0.0;
			Double[][] extensions = new Double[segment.size()-1][2];
			for (int j = 0 ; j < segment.size()-1 ; j++){
				double newBearing = segment.get(j).getBearing();
				//Cap Bearing change
				if (newBearing > previousBearing+maxDeltaBearing) newBearing = previousBearing+maxDeltaBearing;
				if (newBearing < previousBearing-maxDeltaBearing) newBearing = previousBearing-maxDeltaBearing;
				//
				double newSpeed = segment.get(j).getSpeed()*smallPeriod;
				xRelative += newSpeed*Math.cos(toRadians(newBearing));
				yRelative += newSpeed*Math.sin(toRadians(newBearing));
				previousBearing = newBearing;
				extensions[j][0] = xRelative;
				extensions[j][1] = yRelative;
			}
			Double correctDeltaX = segment.get(segment.size()-1).getLatitude() - segment.get(0).getLatitude();
			Double correctDeltaY = segment.get(segment.size()-1).getLongitude() - segment.get(0).getLongitude();
			Double xFactor = correctDeltaX/xRelative;
			Double yFactor = correctDeltaY/yRelative;
			Double[][] coords = extensions;
			for(int j = 0 ; j < segment.size()-1 ; j++){
				coords[j][0] = segment.get(0).getLatitude()  + extensions[j][0]*xFactor;
				coords[j][1] = segment.get(0).getLongitude() + extensions[j][1]*yFactor;
			}
			guess.add(new DataPoint.Builder().time(segment.get(0).getTime()).latitude(segment.get(0).getLatitude()).longitude(segment.get(0).getLongitude()).bearing(segment.get(0).getBearing()).speed(segment.get(0).getSpeed()).accelX(segment.get(0).getAccelX()).accelY(segment.get(0).getAccelY()).accelZ(segment.get(0).getAccelZ()).orientationA(segment.get(0).getOrientationA()).orientationP(segment.get(0).getOrientationP()).orientationR(segment.get(0).getOrientationR()).rotVecX(segment.get(0).getRotVecX()).rotVecY(segment.get(0).getRotVecY()).rotVecZ(segment.get(0).getRotVecZ()).rotVecC(segment.get(0).getRotVecC()).linAccX(segment.get(0).getLinAccX()).linAccY(segment.get(0).getLinAccY()).linAccZ(segment.get(0).getLinAccZ()).gravityX(segment.get(0).getGravityX()).gravityY(segment.get(0).getGravityY()).gravityZ(segment.get(0).getGravityZ()).gyroX(segment.get(0).getGyroX()).gyroY(segment.get(0).getGyroY()).gyroZ(segment.get(0).getGyroZ()).build());
			for(int j = 0 ; j < segment.size()-1 ; j++){
				guess.add(new DataPoint.Builder().time(segment.get(j).getTime()).latitude(coords[j][0]).longitude(coords[j][1]).bearing(segment.get(j).getBearing()).speed(segment.get(j).getSpeed()).accelX(segment.get(j).getAccelX()).accelY(segment.get(j).getAccelY()).accelZ(segment.get(j).getAccelZ()).orientationA(segment.get(j).getOrientationA()).orientationP(segment.get(j).getOrientationP()).orientationR(segment.get(j).getOrientationR()).rotVecX(segment.get(j).getRotVecX()).rotVecY(segment.get(j).getRotVecY()).rotVecZ(segment.get(j).getRotVecZ()).rotVecC(segment.get(j).getRotVecC()).linAccX(segment.get(j).getLinAccX()).linAccY(segment.get(j).getLinAccY()).linAccZ(segment.get(j).getLinAccZ()).gravityX(segment.get(j).getGravityX()).gravityY(segment.get(j).getGravityY()).gravityZ(segment.get(j).getGravityZ()).gyroX(segment.get(j).getGyroX()).gyroY(segment.get(j).getGyroY()).gyroZ(segment.get(j).getGyroZ()).build());
			}
			///////End interpolation
			guesses.add(guess);
		}
		return unSegment(guesses);
	}
	private static ArrayList<DataPoint> linear(ArrayList<DataPoint> dps){	//linearly, evenly interpolate		
		ArrayList<ArrayList<DataPoint>> segments = segment(dps);
		ArrayList<ArrayList<DataPoint>> guesses = new ArrayList<ArrayList<DataPoint>>();
		for (int i = 0 ; i < segments.size() ; i++){
			ArrayList<DataPoint> segment = segments.get(i);
			ArrayList<DataPoint> guess = new ArrayList<DataPoint>();
			///////Interpolation begins here
			Double deltaX = segment.get(segment.size()-1).getLatitude() - segment.get(0).getLatitude();
			Double deltaY = segment.get(segment.size()-1).getLongitude() - segment.get(0).getLongitude();
			Double xIncrement = deltaX/(segment.size()-1);
			Double yIncrement = deltaY/(segment.size()-1);
			for (int j = 0 ; j < segment.size() ; j++){
				guess.add(new DataPoint.Builder().time(segment.get(j).getTime()).latitude(segment.get(0).getLatitude()+j*xIncrement).longitude(segment.get(0).getLongitude()+j*yIncrement).bearing(segment.get(j).getBearing()).speed(segment.get(j).getSpeed()).accelX(segment.get(j).getAccelX()).accelY(segment.get(j).getAccelY()).accelZ(segment.get(j).getAccelZ()).orientationA(segment.get(j).getOrientationA()).orientationP(segment.get(j).getOrientationP()).orientationR(segment.get(j).getOrientationR()).rotVecX(segment.get(j).getRotVecX()).rotVecY(segment.get(j).getRotVecY()).rotVecZ(segment.get(j).getRotVecZ()).rotVecC(segment.get(j).getRotVecC()).linAccX(segment.get(j).getLinAccX()).linAccY(segment.get(j).getLinAccY()).linAccZ(segment.get(j).getLinAccZ()).gravityX(segment.get(j).getGravityX()).gravityY(segment.get(j).getGravityY()).gravityZ(segment.get(j).getGravityZ()).gyroX(segment.get(j).getGyroX()).gyroY(segment.get(j).getGyroY()).gyroZ(segment.get(j).getGyroZ()).build());
			}
			///////End interpolation
			guesses.add(guess);
		}
		return unSegment(guesses);
	}
	private static ArrayList<ArrayList<DataPoint>> segment(ArrayList<DataPoint> dps){ //Break the array into segments
		dps = trim(dps);
		ArrayList<ArrayList<DataPoint>> segments = new ArrayList<ArrayList<DataPoint>>();
		for(int i = 0 ; i < dps.size()-gap ; i--){
			ArrayList<DataPoint> segment = new ArrayList<DataPoint>();
			for(int j = 0 ; j < gap+1 ; j++){
				segment.add(dps.get(i));
				i++;
			}
			segments.add(segment);
		}
		System.out.println("Number of Segments: " + segments.size());
		return segments;
	}
	private static ArrayList<DataPoint> unSegment(ArrayList<ArrayList<DataPoint>> segments){ //inverse of segment()
		ArrayList<DataPoint> dps = new ArrayList<DataPoint>();
		dps.add(segments.get(0).get(0));
		for (int i = 0 ; i < segments.size(); i++){
			for(int j = 1 ; j <= gap ; j++){
				dps.add(segments.get(i).get(j));
			}
		}
		return dps;
	}
	private static ArrayList<DataPoint> genFake(){	// generate random smooth path of length minutes
		ArrayList<DataPoint> dps = new ArrayList<DataPoint>();
		Integer seconds = (minutes*60);
		Random rand = new Random();
		for (int i = 0 ; i <= seconds ; i++){
			Lat += speed * Math.cos(toRadians(bearing));
			Lon += speed * Math.sin(toRadians(bearing));
			if ((i % smallPeriod) == 0){
				dps.add(new DataPoint.Builder().time(i).latitude(Lat).longitude(Lon).bearing(bearing).speed(speed).accelX(0).accelY(0).accelZ(0).orientationA(0).orientationP(0).orientationR(0).rotVecX(0).rotVecY(0).rotVecZ(0).rotVecC(0).linAccX(0).linAccY(0).linAccZ(0).gravityX(0).gravityY(0).gravityZ(0).gyroX(0).gyroY(0).gyroZ(0).build());
			}
			speed += (rand.nextDouble() - 0.5) * speedFactor; //adjust speed;
			if (speed > 3) speed = 3;
			if (speed < 0) speed = 0;
			bearing += (rand.nextDouble() - 0.5 + curl) * bearingFactor; 		 //adjust bearing;
		}
		return dps;
	}
	private static ArrayList<DataPoint> strip(ArrayList<DataPoint> dps){	// ensures sparse location data
		ArrayList<DataPoint> stripped = new ArrayList<DataPoint>();
		for(int i = 0 ; i < dps.size(); i++){
			if((i % gap)>0){
				stripped.add(new DataPoint.Builder().time(dps.get(i).getTime()).latitude(0).longitude(0).bearing(dps.get(i).getBearing()).speed(dps.get(i).getSpeed()).accelX(dps.get(i).getAccelX()).accelY(dps.get(i).getAccelY()).accelZ(dps.get(i).getAccelZ()).orientationA(dps.get(i).getOrientationA()).orientationP(dps.get(i).getOrientationP()).orientationR(dps.get(i).getOrientationR()).rotVecX(dps.get(i).getRotVecX()).rotVecY(dps.get(i).getRotVecY()).rotVecZ(dps.get(i).getRotVecZ()).rotVecC(dps.get(i).getRotVecC()).linAccX(dps.get(i).getLinAccX()).linAccY(dps.get(i).getLinAccY()).linAccZ(dps.get(i).getLinAccZ()).gravityX(dps.get(i).getGravityX()).gravityY(dps.get(i).getGravityY()).gravityZ(dps.get(i).getGravityZ()).gyroX(dps.get(i).getGyroX()).gyroY(dps.get(i).getGyroY()).gyroZ(dps.get(i).getGyroZ()).build());
			}else{
				stripped.add(new DataPoint.Builder().time(dps.get(i).getTime()).latitude(dps.get(i).getLatitude()).longitude(dps.get(i).getLongitude()).bearing(dps.get(i).getBearing()).speed(dps.get(i).getSpeed()).accelX(dps.get(i).getAccelX()).accelY(dps.get(i).getAccelY()).accelZ(dps.get(i).getAccelZ()).orientationA(dps.get(i).getOrientationA()).orientationP(dps.get(i).getOrientationP()).orientationR(dps.get(i).getOrientationR()).rotVecX(dps.get(i).getRotVecX()).rotVecY(dps.get(i).getRotVecY()).rotVecZ(dps.get(i).getRotVecZ()).rotVecC(dps.get(i).getRotVecC()).linAccX(dps.get(i).getLinAccX()).linAccY(dps.get(i).getLinAccY()).linAccZ(dps.get(i).getLinAccZ()).gravityX(dps.get(i).getGravityX()).gravityY(dps.get(i).getGravityY()).gravityZ(dps.get(i).getGravityZ()).gyroX(dps.get(i).getGyroX()).gyroY(dps.get(i).getGyroY()).gyroZ(dps.get(i).getGyroZ()).build());
			}
		}
		return stripped;
	}
	private static ArrayList<DataPoint> trim(ArrayList<DataPoint> dps){	// trims data after final bigPeriod
		Integer roundedSize = (int) (Math.round(dps.size()/((double) gap) - 0.5)*gap)+1;
		while(dps.size()-roundedSize > 0){
			dps.remove(dps.size()-1);
		}
		return dps;
	}
	private static double toRadians(double degrees){
		return degrees * Math.PI / 180;
	}
	private static double toDegrees(double radians){
		return radians * 180 / Math.PI;
	}
	private static void toCsv(ArrayList<DataPoint> dps, String file){	//prints "Lat,Lon\n" for all elements to csvPath
		try {
			FileWriter fw = new FileWriter(folder + file + ".csv");
			PrintWriter pw = new PrintWriter(fw);
			for (int i = 0 ; i < dps.size() ; i++){
				pw.print(dps.get(i).getLatitude());
				pw.print(",");
				pw.println(dps.get(i).getLongitude());
			}
			pw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}






