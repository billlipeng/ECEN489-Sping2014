package edu.tamu.PathEstimation;

public class LineInterpolation {

	private Coordinates coordinates0;
	private Coordinates coordinates1;
	private Coordinates coordinatesW;
	
	public LineInterpolation (Coordinates startCoordinates, Coordinates endCoordinates) {
		this.coordinates0 = startCoordinates;
		this.coordinates1 = endCoordinates;
		this.coordinatesW = new Coordinates();
	}
	
	public void setWeightedCombination (double weightFactor) {		
		// Transform latitude and longitude into Cartesian coordinates
		double[] XYZcoordinates0 = coordinates0.getXYZ();
		double[] XYZcoordinates1 = coordinates1.getXYZ();
		double[] XYZcoordinatesW = coordinatesW.getXYZ();
		
		// Compute weighted Cartesian coordinate
		XYZcoordinatesW[0] = (1.0 - weightFactor)*XYZcoordinates0[0] + weightFactor*XYZcoordinates1[0];
		XYZcoordinatesW[1] = (1.0 - weightFactor)*XYZcoordinates0[1] + weightFactor*XYZcoordinates1[1];
		XYZcoordinatesW[2] = (1.0 - weightFactor)*XYZcoordinates0[2] + weightFactor*XYZcoordinates1[2];

		coordinatesW.setXYZ(XYZcoordinatesW);
	}
	
	public void setWeightedCombination () {
		this.setWeightedCombination(0.5);
	}
	
	public Coordinates getWeightedPoint() {
		return coordinatesW;
	}
}
