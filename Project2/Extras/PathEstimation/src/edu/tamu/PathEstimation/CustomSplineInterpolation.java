package edu.tamu.PathEstimation;

public class CustomSplineInterpolation {

	private double[] PositionPair = new double[2];
	private double[] LSInputArray = new double[11];
	private double[] InterpolatedPositionArray = new double[11];
	double[] CoefficientArray = new double[8];
	
	private static final double[][] PseudoInverse =
		{ { -0.001585404933120, -0.016561410009754, -0.021437612715514, -0.018619438344246, -0.010614851263027, 0, 0.010614851263027, 0.018619438344246, 0.021437612715514, 0.016561410009754, 0.001585404933120 },
		{ 0.001540681350567, 0.013826059510305, 0.007778644157211, -0.003598622435325, -0.012876406043170, -0.016340465849327, -0.012876406043170, -0.003598622435325, 0.007778644157211, 0.013826059510305, 0.001540681350567 },
		{ -0.001821093508740, -0.008845335005156, 0.003382032497036, 0.012140634289240, 0.010059375565464, 0, -0.010059375565464, -0.012140634289240, -0.003382032497036, 0.008845335005156, 0.001821093508740 },
		{ 0.001434748175291, 0.003608791983714, -0.009633391694305, -0.007316431512205, 0.004434152986848, 0.010642793849866, 0.004434152986848, -0.007316431512205, -0.009633391694305, 0.003608791983714, 0.001434748175291 },
		{ -0.002405149354403, 0.004810303070247, 0.000801719489409, -0.003206863522871, -0.003206861077555, 0, 0.003206861077555, 0.003206863522871, -0.000801719489409, -0.004810303070247, 0.002405149354403 },
		{ 0.001500930070068, -0.003524453297630, 0.003074657570849, 0.003630543470844, -0.000495281481505, -0.002893414084555, -0.000495281481505, 0.003630543470844, 0.003074657570849, -0.003524453297630, 0.001500930070068 } };
	
	private static final double[][] BasisPolynomials =
		{ { 1.00000000000, 0, 0, 0, 0, 0, 0, 0 },
		{ -1.73205080757, 3.46410161514, 0, 0, 0, 0, 0, 0 },
		{ 2.23606797750, -13.41640786500, 13.41640786500, 0, 0, 0, 0, 0 },
		{ -2.64575131106, 31.74901573278, -79.37253933194, 52.91502622129, 0, 0, 0, 0 },
		{ 3.00000000000, -60.00000000000, 270.00000000000, -420.00000000000, 210.00000000000, 0, 0, 0 },
		{ -3.31662479036, 99.49874371066, -696.49120597463, 1857.30988259902, -2089.47361792390, 835.78944716956, 0, 0 },
		{ 3.60555127546, -151.43315356949, 1514.33153569488, -6057.32614277950, 11357.48651771157, -9994.58813558618, 3331.52937852873, 0 },
		{ -3.87298334621, 216.88706738762, -2927.97540973281, 16266.53005407115, -44732.95764869566, 64415.45901412177, -46522.27595464349, 13292.07884418386 } };
	
	private double[] ApproximatePolynomial = new double[8];
	
	public CustomSplineInterpolation (double[] PositionPair, double[] DerivativeArray) {
		this.PositionPair = PositionPair;
		for (int LSInputIndex = 0; LSInputIndex < 11; ++LSInputIndex) {
			LSInputArray[LSInputIndex] = DerivativeArray[LSInputIndex] - (PositionPair[1] - PositionPair[0]);
		}
	}

	public double[] InterpolatePositions() {
		// Computing weights corresponding to eight basis polynomials
		for (int CoefficientIndex = 0; CoefficientIndex <= 7; ++CoefficientIndex) {
			CoefficientArray[CoefficientIndex] = 0.0;
		}
		for (int CoefficientIndex = 2; CoefficientIndex <= 7; ++CoefficientIndex) {
			for (int PseudoIndex = 0; PseudoIndex < 11; ++PseudoIndex) {
				CoefficientArray[CoefficientIndex] = CoefficientArray[CoefficientIndex] + PseudoInverse[CoefficientIndex-2][PseudoIndex] * LSInputArray[PseudoIndex];
			}
		}
		CoefficientArray[0] = 0.5*PositionPair[0] + 0.5*PositionPair[1] - Math.sqrt(5)*CoefficientArray[2] - Math.sqrt(9)*CoefficientArray[4] - Math.sqrt(13)*CoefficientArray[6];
		CoefficientArray[1] = (0.5*PositionPair[1] - 0.5*PositionPair[0] - Math.sqrt(7)*CoefficientArray[3] - Math.sqrt(11)*CoefficientArray[5] - Math.sqrt(15)*CoefficientArray[7])/Math.sqrt(3);

		// Computing coefficients of approximate polynomial resulting from linear combination of basis elements
		for (int PolynomialIndex = 0; PolynomialIndex <= 7; ++PolynomialIndex) {
			ApproximatePolynomial[PolynomialIndex] = 0.0;
		}
		for (int PolynomialIndex = 0; PolynomialIndex <= 7; ++PolynomialIndex) {
			for (int BasisIndex = 0; BasisIndex <= 7; ++BasisIndex) {
				ApproximatePolynomial[PolynomialIndex] = ApproximatePolynomial[PolynomialIndex] + BasisPolynomials[BasisIndex][PolynomialIndex] * CoefficientArray[BasisIndex];
			}
		}
		
		// Evaluating approximate polynomial at the various intended locations {0, 0.1, ..., 1.0}
		double InterpolatedPositionTime = 0.0;
		for (int InterpolatedPositionIndex = 0; InterpolatedPositionIndex < 11; ++InterpolatedPositionIndex) {
			InterpolatedPositionArray[InterpolatedPositionIndex] = 0.0;
		}
		for (int InterpolatedPositionIndex = 0; InterpolatedPositionIndex < 11; ++InterpolatedPositionIndex) {
			for(int CoefficientIndex = 0; CoefficientIndex <= 7; ++CoefficientIndex)
			InterpolatedPositionArray[InterpolatedPositionIndex] += ApproximatePolynomial[CoefficientIndex] * Math.pow(InterpolatedPositionTime, CoefficientIndex);
			InterpolatedPositionTime = InterpolatedPositionTime + 0.1;
		}
		
		// Return interpolated points
		return InterpolatedPositionArray;
	}

	public double[] getBasisWeights() {
		// Expose weights of basis polynomials
		return CoefficientArray;
	}

	public double[] getPolynomialCoefficients() {
		// Expose coefficients of approximate polynomial
		return ApproximatePolynomial;
	}

}
