package edu.tamu.PathEstimation;

public class PathEstimation {
	static public double EarthRadius = 6372.8; // Kilometers

	static double haversine(Coordinates coordinates0, Coordinates coordinates1) {
		double deltaLatitude = coordinates1.getLatitude() - coordinates0.getLatitude();
		double deltaLongitude = coordinates1.getLongitude() - coordinates0.getLongitude();

		double a = Math.sin(deltaLatitude * 0.5) * Math.sin(deltaLatitude * 0.5)
				+ Math.cos(coordinates0.getLatitude()) * Math.cos(coordinates1.getLatitude())
				* Math.sin(deltaLongitude * 0.5) * Math.sin(deltaLongitude * 0.5);
		
		return (2 * EarthRadius * Math.asin(Math.sqrt(a)));		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final Coordinates NewYork = new Coordinates(0.710599509,-1.291647896);
		final Coordinates Chicago =  new Coordinates(0.73091096,-1.5294285);

		System.out.printf("Origin - New York: ");
		System.out.printf("Lat: %f, ", NewYork.getLatitude()*180/Math.PI);
		System.out.printf("Long: %f\n", NewYork.getLongitude()*180/Math.PI);

		System.out.printf("Destination - Chicago: ");
		System.out.printf("Lat: %f, ", Chicago.getLatitude()*180/Math.PI);
		System.out.printf("Long: %f\n", Chicago.getLongitude()*180/Math.PI);

		System.out.printf("\n");

		LineInterpolation linearLocations = new LineInterpolation(NewYork,Chicago);
		for(double indexTime = 0; indexTime <= 1; indexTime = indexTime + 0.1) {
			linearLocations.setWeightedCombination(indexTime);
			System.out.printf("Time: %f, ", indexTime);
			System.out.printf("Lat: %f, ", linearLocations.getWeightedPoint().getLatitude()*180/Math.PI);
			System.out.printf("Lon: %f, ", linearLocations.getWeightedPoint().getLongitude()*180/Math.PI);
			System.out.printf("From: %f km, ", haversine(NewYork,linearLocations.getWeightedPoint()));
			System.out.printf("To: %f km\n", haversine(linearLocations.getWeightedPoint(),Chicago));
		}
		System.out.printf("Total Distance: %f km\n\n", haversine(NewYork, Chicago));
		
		double[] XYZcoordinatesOrigin = NewYork.getXYZ();
		double[] XYZcoordinatesDestination = Chicago.getXYZ();
		
		double[] XPositionPair = new double[2];
		double[] XDerivativeArray;
		XDerivativeArray = new double[11];
		for (int index = 0; index < 11; ++index) {
			XDerivativeArray[index] = 0.1 * (XPositionPair[1] - XPositionPair[0]);
		}
		XPositionPair[0] = XYZcoordinatesOrigin[0];
		XPositionPair[1] = XYZcoordinatesDestination[0];
		CustomSplineInterpolation XInterpolation = new CustomSplineInterpolation(XPositionPair, XDerivativeArray); 
		double[] XInterpolatedLocations = XInterpolation.InterpolatePositions();
		
		double[] YPositionPair = new double[2];
		double[] YDerivativeArray;
		YDerivativeArray = new double[11];
		for (int index = 0; index < 11; ++index) {
			YDerivativeArray[index] = 0.1 * (YPositionPair[1] - YPositionPair[0]);
		}
		YPositionPair[0] = XYZcoordinatesOrigin[1];
		YPositionPair[1] = XYZcoordinatesDestination[1];
		CustomSplineInterpolation YInterpolation = new CustomSplineInterpolation(YPositionPair, YDerivativeArray); 
		double[] YInterpolatedLocations = YInterpolation.InterpolatePositions();

		double[] ZPositionPair = new double[2];
		double[] ZDerivativeArray;
		ZDerivativeArray = new double[11];
		for (int index = 0; index < 11; ++index) {
			ZDerivativeArray[index] = 0.1 * (ZPositionPair[1] - ZPositionPair[0]);
		}
		ZPositionPair[0] = XYZcoordinatesOrigin[2];
		ZPositionPair[1] = XYZcoordinatesDestination[2];
		CustomSplineInterpolation ZInterpolation = new CustomSplineInterpolation(ZPositionPair, ZDerivativeArray); 
		double[] ZInterpolatedLocations = ZInterpolation.InterpolatePositions();

		Coordinates splineLocation = new Coordinates();
		double[] splineXYZ;
		splineXYZ = new double[3];
		for(int stepIndex = 0; stepIndex < 11; ++stepIndex) {
			splineXYZ[0] = XInterpolatedLocations[stepIndex];
			splineXYZ[1] = YInterpolatedLocations[stepIndex];
			splineXYZ[2] = ZInterpolatedLocations[stepIndex];
			splineLocation.setXYZ(splineXYZ);
			System.out.printf("Step: %d, ", stepIndex);
			System.out.printf("Lat: %f, ", splineLocation.getLatitude()*180/Math.PI);
			System.out.printf("Lon: %f, ", splineLocation.getLongitude()*180/Math.PI);
			System.out.printf("From: %f km, ", haversine(NewYork,splineLocation));
			System.out.printf("To: %f km\n", haversine(splineLocation,Chicago));
		}

		System.out.printf("\n");
		System.out.printf("\n End \n");
	}

}
