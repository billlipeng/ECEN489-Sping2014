package alg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class algorithm {
	static int size = 25; //size of the matrix
	static int[][] Data = new int[size][size];
	static int[][] Data1 = new int[size][size];
	static int APbuildNumber = 1;
	static int scale = 2; //this is for creating test data
	static int range = 10; //range for AP recreation
	static int depth = 0; // DO NOT CHANGE PLOX
	static int distCheck = 10;
	static double delScale = 1.1;
	static double finScale = .07;
	static String print = " ";
	
	 static Connection c = null;
	 static Statement stmt = null;
	 static String table = "AntennaTwo";
	 
	 static ArrayList<Double> lat = new ArrayList<Double>();
	 static ArrayList<Double> lon = new ArrayList<Double>();
	 static ArrayList<Integer> rssi = new ArrayList<Integer>();
	 
	 static ArrayList<Double> latOut = new ArrayList<Double>();
	 static ArrayList<Double> lonOut = new ArrayList<Double>();
	 static ArrayList<Integer> rssiOut = new ArrayList<Integer>();
	 
	 static double maxLat = 0;
	 static double maxLon=0; 
	 static double minLat = 0;
	 static double minLon=0; 
	 static double scaling=0;
	 
	
	public static void main(String[] args)  {
		//Data1 = buildTestData(APbuildNumber);//insert desired # of ap's here

		 
	     try {
	    	 Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:C:/Users/Miguel/Desktop/Antenna2.sqlite");
			c.setAutoCommit(false);
			String sql=null;
			
			stmt = c.createStatement();
			int maxid=0;
			//System.out.println("Querying for maxid");
			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+table+";" );
			while ( rs.next() ) {
				lat.add(rs.getDouble("Latitude"));	
				lon.add(rs.getDouble("Longitude"));	
				rssi.add(rs.getInt("RSSI"));	
		      }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    maxLat=Collections.max(lat);
	    maxLon=Collections.max(lon);
	    minLat=Collections.min(lat);
	    minLon=Collections.min(lon);
	    
	    if(Math.abs(maxLat-minLat)>Math.abs(maxLon-minLon))
	    	scaling = size/(maxLat-minLat);
	    else
	    	scaling = size/(maxLon-minLon);
	     
	    System.out.println("scaling"+scaling);
	    System.out.println("xsize="+(maxLon-minLon)*scaling+" ysize="+(maxLat-minLat)*scaling);
	    System.out.println("xsize="+(maxLon-minLon)+" ysize="+(maxLat-minLat));
	    for(int i=0; i<lat.size(); i++){
	    	int x=(int) ((lon.get(i)-minLon)*scaling);
	    	int y=(int) ((lat.get(i)-minLat)*scaling);
	    	Data1[x][y]=rssi.get(i);
	    	//System.out.println("lon i ="+lon.get(i)+" min lon="+minLon);
	    	System.out.println(""+x+", "+y+", rssi="+rssi.get(i));
	    }
		//PRINT FUNCS
		for (int i=0;i<size;i++)
		{
			for (int j=0;j<size;j++)
			{
				print = print + " " + Integer.toString(Data1[i][j]);
			}
			System.out.println(print);
			print = " ";
		}
		
		/*
		System.out.println("Stripping and printing");
		//importData();
		Data = stripTestData(Data1);
		
		//PRINT STUFF AGAIN
		
		for (int i=0;i<size;i++)
		{
			for (int j=0;j<size;j++)
			{
				print = print + " " + Integer.toString(Data[i][j]);
			}
			System.out.println(print);
			print = " ";
		}
		
		
		*/
		
		int[][] newData = new int[size][size];
		
		if(APbuildNumber == 1)
		{
			newData = execAlgAP1(Data1);
		}
		if(APbuildNumber == 2)
		{
			newData = execAlgAP2(Data);
		}
		for (int i=0;i<size;i++)
		{
			for (int j=0;j<size;j++)
			{
				print = print + " " + Integer.toString(newData[i][j]);
				lonOut.add(maxLon-((double)i)/scaling);
				latOut.add(maxLat-((double)j)/scaling);
				rssiOut.add(newData[i][j]);
				
			}
			System.out.println(print);
			print = " ";
		}
		
		//ERROR CHECK
		double error = 0;
		for (int i=0;i<size;i++)
		{
			for(int j=0;j<size;j++)
			{
				error = error + Math.abs(Data1[i][j]-newData[i][j]);
			}
		}
		double normError = error/(size*size);
				
		System.out.println("The normalized Error: " + normError);
		
		try{
		for(int i=0; i<latOut.size(); i++){
		stmt = c.createStatement();
		String sql = "INSERT INTO OutputTwo("
				+ "Latitude"
				+ ", Longitude"
				+ ", RSSI"
				+ ") " +
 				"VALUES ("
 						+ ""+latOut.get(i)+""						//id
 						+ ", '" + lonOut.get(i) +"'"				//date
						+ ", '" + rssiOut.get(i) +"'"				//time
						+ ");"; 				
		//System.out.println("Executing statement "+sql+"");
		stmt.executeUpdate(sql);
		//System.out.println("closing Statement ");
        stmt.close();
		}
		c.commit();
	    c.close();
		}catch(SQLException e){
			
		
		}
		
		
	}
	private static int[][] execAlgAP1(int[][] data) {
		// TODO Auto-generated method stub
				int[][] newData = new int[size][size];
				int reductionNumber = 0;
				for(int i=0; i<size;i++)
				{
					for(int j=0; j<size;j++)
					{
						if(data[i][j] !=0)
						{
							reductionNumber = reductionNumber+1;
						}
					}
				}
				
				int[] xPtCoords = new int[reductionNumber];
				int[] yPtCoords = new int[reductionNumber];
				
				//THIS PART GETS X AND Y COORDS FOR ALL POINTS REMAINING IN DATA
				int k = 0;
				for (int i=0;i<size;i++)
				{
					for (int j=0;j<size;j++)
					{
						if (data[i][j] < 0)
						{
							xPtCoords[k] = i;
							yPtCoords[k] = j;
							k = k+1;
						}
					}
				}
				
				
				//THIS PART GOES AND FINDS THE CLOSEST AND SECOND CLOSEST PAIRS OF POINTS
				int[][] pair1 = new int[2][2];
				int[][] pair2 = new int[2][2];
				int[] NearPoint;
				double currentTop = 1000;
				double Distance;
				for (int i =0; i<xPtCoords.length; i++)
				{
					NearPoint = ClosestPoint(xPtCoords[i],yPtCoords[i],xPtCoords,yPtCoords);
					//System.out.println(xPtCoords[i] + " " + yPtCoords[i] + ", " + NearPoint[0] + " " + NearPoint[1] + ", " + Data[xPtCoords[i]][yPtCoords[i]] + " " + Data[NearPoint[0]][NearPoint[1]]);
					Distance = DistBtwnPoints(xPtCoords[i],yPtCoords[i],NearPoint[0],NearPoint[1]);
					if(Distance < currentTop)
					{
						currentTop = Distance;
						if(pair1 != null)
						{
							pair2 = pair1;
						}
						if(data[xPtCoords[i]][yPtCoords[0]] != data[NearPoint[0]][NearPoint[1]]) //the points can't be equal RSSI catch
						{
							pair1[0][0] = xPtCoords[i]; //Xval of first point
							pair1[0][1] = yPtCoords[i]; //Yval of first point
							pair1[1][0] = NearPoint[0]; //Xval of second point
							pair1[1][1] = NearPoint[1]; //Yval of second point
						}
					}
				}
				
				
				
				//NOW PAIR1 IS SHORTEST DIST BTWN AND PAIR2 IS SECOND SHORTEST
				
				//DELX AND DELY CALCULATIONS
				double delx = .15*delScale;
				double dely = .15*delScale;
				/*
				double pair1delx = Math.abs(data[pair1[0][0]][pair1[0][1]]-data[pair1[1][0]][pair1[1][1]])/Math.abs(pair1[0][0]-pair1[1][0]);
				double pair1dely = Math.abs(data[pair1[0][0]][pair1[0][1]]-data[pair1[1][0]][pair1[1][1]])/Math.abs(pair1[0][1]-pair1[1][1]);
				double delx;
				double dely;
				if(pair2 != null) //catch for if closest was found first
				{
					double pair2delx = Math.abs(data[pair2[0][0]][pair2[0][1]]-data[pair2[1][0]][pair2[1][1]])/Math.abs(pair2[0][0]-pair2[1][0]);
					double pair2dely = Math.abs(data[pair2[0][0]][pair2[0][1]]-data[pair2[1][0]][pair2[1][1]])/Math.abs(pair2[0][1]-pair2[1][1]);
					delx = (pair1delx+pair2delx)/2;
					dely = (pair1dely+pair2dely)/2;
				}
				else
				{
					delx = pair1delx;
					dely = pair1dely;
				}
				*/
				//NOW DELX AND DELY ARE DEFINED
				//NOW FIND THE MIN POINT AND ITS CLOSEST NEIGHBOR
				
				int bestVal = -1000;
				int[] minPoint = new int[2];
				for (int i=0;i<size;i++)
				{
					for (int j=0;j<size;j++)
					{
						if(Data[i][j]<0)
						{
							if (Data[i][j] > bestVal)
							{
								bestVal = Data[i][j];
								minPoint[0] = i;
								minPoint[1] = j;
							}
						}
					}
				}
				int[] ClosePoint = ClosestPoint(minPoint[0],minPoint[1],xPtCoords,yPtCoords);
				
				
				//USING THIS STUFF NOW FIND THE AP
				double DistBtwnPts = DistBtwnPoints(minPoint[0],minPoint[1],ClosePoint[0],ClosePoint[1]);
				//NEED AN AV DELX AND AV DELY BASED ON DELX, AND CHANGE IN RSSI ACROSS THESE 2 POINTS
				int xFinal;
				int yFinal;
				//AP X VAL
				if(ClosePoint[0]==minPoint[0])
				{
					xFinal = ClosePoint[0];
				}
				else if(ClosePoint[0]>minPoint[0])
				{
					xFinal = (int) (minPoint[0] - (1/delx));
					if (xFinal >= size)
					{
						xFinal = size - 1;
					}
					if (xFinal < 0)
					{
						xFinal = 0;
					}
				}
				else
				{
					xFinal = (int) (minPoint[0] + (1/delx));
					if (xFinal >= size)
					{
						xFinal = size - 1;
					}
					if (xFinal < 0)
					{
						xFinal = 0;
					}
				}
				
				//NOW FOR THE Y VAL
				if(ClosePoint[1]==minPoint[1])
				{
					yFinal = ClosePoint[1];
				}
				else if(ClosePoint[1]>minPoint[1])
				{
					yFinal = (int) (minPoint[1] - (1/dely));
					if (yFinal >= size)
					{
						yFinal = size - 1;
					}
					if (yFinal < 0)
					{
						yFinal = 0;
					}
				}
				else
				{
					yFinal = (int) (minPoint[1] + (1/dely));
					if (yFinal >= size)
					{
						yFinal = size - 1;
					}
					if (yFinal < 0)
					{
						yFinal = 0;
					}
				}
				
				
				//NOW ITERATE XFINAL AND YFINAL WITHIN RANGE TO FIND BEST APVAL
				int[] APval = new int[2];
//				APval[0] = xFinal;
//				APval[1] = yFinal;
				int[][] APTdata = new int[size][size];
				int[][] AP1data = new int[size][size];
				double minErrorNorm = 1000;
				for(int l=-1*range; l<range;l++)
				{
					for(int m = -1*range;m<range;m++)
					{
						for(int i=0; i<size; i++)
						{
							for(int j=0; j<size; j++)
							{
								if(xFinal+l>=0&&xFinal+l<size&&yFinal+m>=0&&yFinal+m<size)
								{
									APTdata[i][j] = (int) (-1*scale*DistBtwnPoints(i,j,xFinal+l,yFinal+m));
								}
							}
						}
						double errorCalc = 0;
						int[][] Coords = PtCoordsWithinDist(xFinal+l,yFinal+m,xPtCoords,yPtCoords,distCheck);
						for(int i=0;i<xPtCoords.length;i++)
						{
							if(Coords[i][0] != 0 && Coords[i][1] != 0)
							{
								errorCalc = errorCalc + Math.abs(data[xPtCoords[i]][yPtCoords[i]]-APTdata[xPtCoords[i]][yPtCoords[i]]);
							}
						}
						double errorNorm = errorCalc/xPtCoords.length;
						if (errorNorm < minErrorNorm)
						{
							AP1data = APTdata;
							minErrorNorm = errorNorm;
							APval[0] = xFinal+l;
							APval[1] = yFinal+m;
						}
					}
				}
				
				//System.out.println(" " + APval[0] + " ," + APval[1]);
				// NOW THE APVAL 1 HAS BEEN DETERMINED
				/*
				//int[][] AP1data = new int[size][size];
				AP1data[APval[0]][APval[1]] = -1;
				for(int i=0; i<size; i++)
				{
					for(int j=0; j<size; j++)
					{
						AP1data[i][j] = (int) (-1*scale*DistBtwnPoints(i,j,APval[0],APval[1]));
					}
				}
				*/
				newData = AP1data;
				
					//---------------------------------------------------------------------
					//AP2val[0] = xFinal2;
					//AP2val[1] = yFinal2;
						
				//System.out.println(AP2val[0] + ", " + AP2val[1]);
				if(APval[0] < 0)
				{
					APval[0] = 0;
				}
				
				if(APval[1] < 0)
				{
					APval[1] = 0;
				}
				
				if(APval[0] >=size)
				{
					APval[0] = size-1;
				}
				if(APval[1] >= size)
				{
					APval[1] = size-1;
				}
				
				data[APval[0]][APval[1]] = -1*depth;
				
				
				int APxCoord = APval[0];
				int APyCoord = APval[1];
				
				
				
				// NOW NEED TO MESH
				// MESH METHOD 1: JUST BASED ON DISTANCE FROM AP GUESSES
//				int[] WorkPoint = new int[2];
//				int[] nextWorkPt = new int[2];
				double newDataForGrid;
				minErrorNorm = 1000;
				int[][] newData1 = new int[size][size];
				for(int l=1;l<40;l++)
				{
					for(int i=0;i<size;i++)
					{
						for(int j=0;j<size;j++)
						{
							//if (data[i][j]==0)
							//{
//								WorkPoint = ClosestPoint(i,j,APxCoords,APyCoords);
							
							
							newDataForGrid = (-1*DistBtwnPoints(i,j,APxCoord,APyCoord)*l*finScale) - depth;
							newData1[i][j] = (int) newDataForGrid;
								
							
							//}
						}
					}
					double errorCalc=0;
					double errorNorm=0;
					for(int i=0;i<xPtCoords.length;i++)
					{
						errorCalc = errorCalc + Math.abs(data[xPtCoords[i]][yPtCoords[i]]-newData1[xPtCoords[i]][yPtCoords[i]]);
					}
					errorNorm = errorCalc/xPtCoords.length;
					System.out.println("This is errorNorm: " + errorNorm + "for l = " + l);
					if(errorNorm<minErrorNorm)
					{
						newData = newData1;
						minErrorNorm=errorNorm;
					}
				}
				System.out.println("This is minErrorNorm " + minErrorNorm);
				//newData = data;
				//FINAL PRINT TEST
				
				for(int i=0; i<xPtCoords.length;i++)
				{
					System.out.println(Data[xPtCoords[i]][yPtCoords[i]] + " ," + newData[xPtCoords[i]][yPtCoords[i]]);
				}
				
				
				/*//MESH METHOD 2: STD LINEAR INTERPOLATION
				int[] allXvals = new int[xPtCoords.length +2];
				int[] allYvals = new int[yPtCoords.length +2];
				for (int i=0;i<xPtCoords.length;i++)
				{
					allXvals[i] = xPtCoords[i];
					allYvals[i] = yPtCoords[i];
				}
				allXvals[xPtCoords.length] = APval[0];
				allXvals[xPtCoords.length+1] = AP2val[0];
				allYvals[yPtCoords.length] = APval[1];
				allYvals[yPtCoords.length+1] = AP2val[1];
				*/
				
				
				
				return newData;
	}
	private static int[][] execAlgAP2(int[][] data) {
		// TODO Auto-generated method stub
		int[][] newData = new int[size][size];
		int reductionNumber = 0;
		for(int i=0; i<size;i++)
		{
			for(int j=0; j<size;j++)
			{
				if(data[i][j] !=0)
				{
					reductionNumber = reductionNumber+1;
				}
			}
		}
		
		int[] xPtCoords = new int[reductionNumber];
		int[] yPtCoords = new int[reductionNumber];
		
		//THIS PART GETS X AND Y COORDS FOR ALL POINTS REMAINING IN DATA
		int k = 0;
		for (int i=0;i<size;i++)
		{
			for (int j=0;j<size;j++)
			{
				if (data[i][j] < 0)
				{
					xPtCoords[k] = i;
					yPtCoords[k] = j;
					k = k+1;
				}
			}
		}
		
		
		//THIS PART GOES AND FINDS THE CLOSEST AND SECOND CLOSEST PAIRS OF POINTS
		int[][] pair1 = new int[2][2];
		int[][] pair2 = new int[2][2];
		int[] NearPoint;
		double currentTop = 1000;
		double Distance;
		for (int i =0; i<xPtCoords.length; i++)
		{
			NearPoint = ClosestPoint(xPtCoords[i],yPtCoords[i],xPtCoords,yPtCoords);
			//System.out.println(xPtCoords[i] + " " + yPtCoords[i] + ", " + NearPoint[0] + " " + NearPoint[1] + ", " + Data[xPtCoords[i]][yPtCoords[i]] + " " + Data[NearPoint[0]][NearPoint[1]]);
			Distance = DistBtwnPoints(xPtCoords[i],yPtCoords[i],NearPoint[0],NearPoint[1]);
			if(Distance < currentTop)
			{
				currentTop = Distance;
				if(pair1 != null)
				{
					pair2 = pair1;
				}
				if(data[xPtCoords[i]][yPtCoords[0]] != data[NearPoint[0]][NearPoint[1]]) //the points can't be equal RSSI catch
				{
					pair1[0][0] = xPtCoords[i]; //Xval of first point
					pair1[0][1] = yPtCoords[i]; //Yval of first point
					pair1[1][0] = NearPoint[0]; //Xval of second point
					pair1[1][1] = NearPoint[1]; //Yval of second point
				}
			}
		}
		
		
		
		//NOW PAIR1 IS SHORTEST DIST BTWN AND PAIR2 IS SECOND SHORTEST
		
		//DELX AND DELY CALCULATIONS
		double delx = .15*delScale;
		double dely = .15*delScale;
		/*
		double pair1delx = Math.abs(data[pair1[0][0]][pair1[0][1]]-data[pair1[1][0]][pair1[1][1]])/Math.abs(pair1[0][0]-pair1[1][0]);
		double pair1dely = Math.abs(data[pair1[0][0]][pair1[0][1]]-data[pair1[1][0]][pair1[1][1]])/Math.abs(pair1[0][1]-pair1[1][1]);
		double delx;
		double dely;
		if(pair2 != null) //catch for if closest was found first
		{
			double pair2delx = Math.abs(data[pair2[0][0]][pair2[0][1]]-data[pair2[1][0]][pair2[1][1]])/Math.abs(pair2[0][0]-pair2[1][0]);
			double pair2dely = Math.abs(data[pair2[0][0]][pair2[0][1]]-data[pair2[1][0]][pair2[1][1]])/Math.abs(pair2[0][1]-pair2[1][1]);
			delx = (pair1delx+pair2delx)/2;
			dely = (pair1dely+pair2dely)/2;
		}
		else
		{
			delx = pair1delx;
			dely = pair1dely;
		}
		*/
		//NOW DELX AND DELY ARE DEFINED
		//NOW FIND THE MIN POINT AND ITS CLOSEST NEIGHBOR
		
		int bestVal = -1000;
		int[] minPoint = new int[2];
		for (int i=0;i<size;i++)
		{
			for (int j=0;j<size;j++)
			{
				if(Data[i][j]<0)
				{
					if (Data[i][j] > bestVal)
					{
						bestVal = Data[i][j];
						minPoint[0] = i;
						minPoint[1] = j;
					}
				}
			}
		}
		int[] ClosePoint = ClosestPoint(minPoint[0],minPoint[1],xPtCoords,yPtCoords);
		
		
		//USING THIS STUFF NOW FIND THE AP
		double DistBtwnPts = DistBtwnPoints(minPoint[0],minPoint[1],ClosePoint[0],ClosePoint[1]);
		//NEED AN AV DELX AND AV DELY BASED ON DELX, AND CHANGE IN RSSI ACROSS THESE 2 POINTS
		int xFinal;
		int yFinal;
		//AP X VAL
		if(ClosePoint[0]==minPoint[0])
		{
			xFinal = ClosePoint[0];
		}
		else if(ClosePoint[0]>minPoint[0])
		{
			xFinal = (int) (minPoint[0] - (1/delx));
			if (xFinal >= size)
			{
				xFinal = size - 1;
			}
			if (xFinal < 0)
			{
				xFinal = 0;
			}
		}
		else
		{
			xFinal = (int) (minPoint[0] + (1/delx));
			if (xFinal >= size)
			{
				xFinal = size - 1;
			}
			if (xFinal < 0)
			{
				xFinal = 0;
			}
		}
		
		//NOW FOR THE Y VAL
		if(ClosePoint[1]==minPoint[1])
		{
			yFinal = ClosePoint[1];
		}
		else if(ClosePoint[1]>minPoint[1])
		{
			yFinal = (int) (minPoint[1] - (1/dely));
			if (yFinal >= size)
			{
				yFinal = size - 1;
			}
			if (yFinal < 0)
			{
				yFinal = 0;
			}
		}
		else
		{
			yFinal = (int) (minPoint[1] + (1/dely));
			if (yFinal >= size)
			{
				yFinal = size - 1;
			}
			if (yFinal < 0)
			{
				yFinal = 0;
			}
		}
		
		
		//NOW ITERATE XFINAL AND YFINAL WITHIN RANGE TO FIND BEST APVAL
		int[] APval = new int[2];
//		APval[0] = xFinal;
//		APval[1] = yFinal;
		int[][] APTdata = new int[size][size];
		int[][] AP1data = new int[size][size];
		double minErrorNorm = 1000;
		for(int l=-1*range; l<range;l++)
		{
			for(int m = -1*range;m<range;m++)
			{
				for(int i=0; i<size; i++)
				{
					for(int j=0; j<size; j++)
					{
						if(xFinal+l>=0&&xFinal+l<size&&yFinal+m>=0&&yFinal+m<size)
						{
							APTdata[i][j] = (int) (-1*scale*DistBtwnPoints(i,j,xFinal+l,yFinal+m));
						}
					}
				}
				double errorCalc = 0;
				int[][] Coords = PtCoordsWithinDist(xFinal+l,yFinal+m,xPtCoords,yPtCoords,distCheck);
				for(int i=0;i<xPtCoords.length;i++)
				{
					if(Coords[i][0] != 0 && Coords[i][1] != 0)
					{
						errorCalc = errorCalc + Math.abs(data[xPtCoords[i]][yPtCoords[i]]-APTdata[xPtCoords[i]][yPtCoords[i]]);
					}
				}
				double errorNorm = errorCalc/xPtCoords.length;
				if (errorNorm < minErrorNorm)
				{
					AP1data = APTdata;
					minErrorNorm = errorNorm;
					APval[0] = xFinal+l;
					APval[1] = yFinal+m;
				}
			}
		}
		
		//System.out.println(" " + APval[0] + " ," + APval[1]);
		// NOW THE APVAL 1 HAS BEEN DETERMINED
		/*
		//int[][] AP1data = new int[size][size];
		AP1data[APval[0]][APval[1]] = -1;
		for(int i=0; i<size; i++)
		{
			for(int j=0; j<size; j++)
			{
				AP1data[i][j] = (int) (-1*scale*DistBtwnPoints(i,j,APval[0],APval[1]));
			}
		}
		*/
		newData = AP1data;
		int DataDifference = 0;
		int PointOfChoice = 0;
		for(int i=0; i<xPtCoords.length;i++)
		{
			//System.out.println(Data[xPtCoords[i]][yPtCoords[i]] + " ," + AP1data[xPtCoords[i]][yPtCoords[i]]);
			if (Math.abs(Data[xPtCoords[i]][yPtCoords[i]]-AP1data[xPtCoords[i]][yPtCoords[i]])>DataDifference)
			{
				PointOfChoice = i;
				DataDifference = Math.abs(Data[xPtCoords[i]][yPtCoords[i]]-AP1data[xPtCoords[i]][yPtCoords[i]]);
			}
		}
		
		// NOW USING PREV METHOD DETERMINE LOCATION FOR AP2
		int[] newPoint = new int[2];
		newPoint[0] = xPtCoords[PointOfChoice];
		newPoint[1] = yPtCoords[PointOfChoice];
		
		
		int[] AP2ClosePoint = new int[2];
		AP2ClosePoint = ClosestPoint(newPoint[0],newPoint[1],xPtCoords,yPtCoords);
		//----sort right quick
		// so can recycle code
		int[] tempPoint = new int[2];
		if(Data[newPoint[0]][newPoint[1]]>Data[AP2ClosePoint[0]][AP2ClosePoint[1]])
		{
			tempPoint = newPoint;
			newPoint = AP2ClosePoint;
			AP2ClosePoint = newPoint;
		}
		
		
		
		//------------------------------------------------------------
		//AP X VAL
		int xFinal2;
		int yFinal2;
		int[] AP2val = new int[2];
			if(AP2ClosePoint[0]==newPoint[0])
			{
				xFinal2 = AP2ClosePoint[0];
			}
			else if(AP2ClosePoint[0]>newPoint[0])
			{
				xFinal2 = (int) (newPoint[0] - (1/delx));
				if (xFinal2 >= size)
				{
					xFinal2 = size - 1;
				}
				if (xFinal2 < 0)
				{
					xFinal2 = 0;
				}
			}
			else
			{
				xFinal2 = (int) (newPoint[0] + (1/delx));
				if (xFinal2 >= size)
				{
					xFinal2 = size - 1;
				}
				if (xFinal2 < 0)
				{
					xFinal2 = 0;
				}
			}
			
			//NOW FOR THE Y VAL
			if(AP2ClosePoint[1]==newPoint[1])
			{
				yFinal2 = AP2ClosePoint[1];
			}
			else if(AP2ClosePoint[1]>newPoint[1])
			{
				yFinal2 = (int) (newPoint[1] - (1/dely));
				if (yFinal2 >= size)
				{
					yFinal2 = size - 1;
				}
				if (yFinal2 < 0)
				{
					yFinal2 = 0;
				}
			}
			else
			{
				yFinal2 = (int) (newPoint[1] + (1/dely));
				if (yFinal2 >= size)
				{
					yFinal2 = size - 1;
				}
				if (yFinal2 < 0)
				{
					yFinal2 = 0;
				}
			}
			
		//---------------------------------------PASTING STUFF HERE TO EDIT
			int[][] AP2data = new int[size][size];
			minErrorNorm = 1000;
			for(int l=-1*range; l<range;l++)
			{
				for(int m = -1*range;m<range;m++)
				{
					for(int i=0; i<size; i++)
					{
						for(int j=0; j<size; j++)
						{
							if(xFinal2+l>=0&&xFinal2+l<size&&yFinal2+m>=0&&yFinal2+m<size)
							{
								APTdata[i][j] = (int) (-1*scale*DistBtwnPoints(i,j,xFinal2+l,yFinal2+m));
							}
						}
					}
					double errorCalc = 0;
					int[][] Coords = PtCoordsWithinDist(xFinal2+l,yFinal2+m,xPtCoords,yPtCoords,distCheck);
					for(int i=0;i<xPtCoords.length;i++)
					{
						if(Coords[i][0] != 0 && Coords[i][1] != 0)
						{
							errorCalc = errorCalc + Math.abs(data[xPtCoords[i]][yPtCoords[i]]-APTdata[xPtCoords[i]][yPtCoords[i]]);
						}
					}
					double errorNorm = errorCalc/xPtCoords.length;
					if (errorNorm < minErrorNorm)
					{
						AP2data = APTdata;
						minErrorNorm = errorNorm;
						AP2val[0] = xFinal2+l;
						AP2val[1] = yFinal2+m;
					}
				}
			}
			//---------------------------------------------------------------------
			//AP2val[0] = xFinal2;
			//AP2val[1] = yFinal2;
				
		//System.out.println(AP2val[0] + ", " + AP2val[1]);
		if(APval[0] < 0)
		{
			APval[0] = 0;
		}
		if(AP2val[0] <0)
		{
			AP2val[0] = 0;
		}
		if(APval[1] < 0)
		{
			APval[1] = 0;
		}
		if(AP2val[1]<0)
		{
			AP2val[1] = 0;
		}
		if(APval[0] >=size)
		{
			APval[0] = size-1;
		}
		if(APval[1] >= size)
		{
			APval[1] = size-1;
		}
		if(AP2val[0] >= size)
		{
			AP2val[0] = size-1;
		}
		if(AP2val[1] >= size)
		{
			AP2val[1] = size-1;
		}
		data[APval[0]][APval[1]] = -1*depth;
		data[AP2val[0]][AP2val[1]] = -1*depth;
		/*
		xPtCoords[xPtCoords.length] = APval[0];
		xPtCoords[xPtCoords.length] = AP2val[0];
		yPtCoords[yPtCoords.length] = APval[1];
		yPtCoords[yPtCoords.length] = AP2val[1];
		*/
		int[] APxCoords = new int[2];
		int[] APyCoords = new int[2];
		APxCoords[0] = APval[0];
		APxCoords[1] = AP2val[0];
		APyCoords[0] = APval[1];
		APyCoords[1] = AP2val[1];
		
		
		// NOW NEED TO MESH
		// MESH METHOD 1: JUST BASED ON DISTANCE FROM AP GUESSES
//		int[] WorkPoint = new int[2];
//		int[] nextWorkPt = new int[2];
		double newDataForGrid;
		minErrorNorm = 1000;
		int[][] newData1 = new int[size][size];
		for(int l=1;l<40;l++)
		{
			for(int i=0;i<size;i++)
			{
				for(int j=0;j<size;j++)
				{
					//if (data[i][j]==0)
					//{
//						WorkPoint = ClosestPoint(i,j,APxCoords,APyCoords);
					
					
					newDataForGrid = (-1*minDistFromAP(i,j,APxCoords,APyCoords)*l*finScale) - depth;
					newData1[i][j] = (int) newDataForGrid;
						
					
					//}
				}
			}
			double errorCalc=0;
			double errorNorm=0;
			for(int i=0;i<xPtCoords.length;i++)
			{
				errorCalc = errorCalc + Math.abs(data[xPtCoords[i]][yPtCoords[i]]-newData1[xPtCoords[i]][yPtCoords[i]]);
			}
			errorNorm = errorCalc/xPtCoords.length;
			System.out.println("This is errorNorm: " + errorNorm + "for l = " + l);
			if(errorNorm<minErrorNorm)
			{
				newData = newData1;
				minErrorNorm=errorNorm;
			}
		}
		System.out.println("This is minErrorNorm " + minErrorNorm);
		//newData = data;
		//FINAL PRINT TEST
		
		for(int i=0; i<xPtCoords.length;i++)
		{
			System.out.println(Data[xPtCoords[i]][yPtCoords[i]] + " ," + newData[xPtCoords[i]][yPtCoords[i]]);
		}
		
		
		/*//MESH METHOD 2: STD LINEAR INTERPOLATION
		int[] allXvals = new int[xPtCoords.length +2];
		int[] allYvals = new int[yPtCoords.length +2];
		for (int i=0;i<xPtCoords.length;i++)
		{
			allXvals[i] = xPtCoords[i];
			allYvals[i] = yPtCoords[i];
		}
		allXvals[xPtCoords.length] = APval[0];
		allXvals[xPtCoords.length+1] = AP2val[0];
		allYvals[yPtCoords.length] = APval[1];
		allYvals[yPtCoords.length+1] = AP2val[1];
		*/
		
		
		
		return newData;
	}

	private static int[][] stripTestData(int[][] data) {
		// TODO Auto-generated method stub
		int[][] newData = new int[size][size];
		for (int i=0;i<8;i++)
		{
			// picks 8 random points and copies them over stripping the rest.
			int x = randCoordGen();
			int y = randCoordGen();
			newData[x][y] = data[x][y];
		}
		
		
		return newData;
	}

	private static int[][] buildTestData(int APnum) {
		// TODO Auto-generated method stub
		int[][] newData = new int[size][size];
		int AP = APnum;//# of APs
		int[] xCoords = new int[AP];
		int[] yCoords = new int[AP];
		for (int i=0;i<AP;i++)
		{
			int x = randCoordGen();
			int y = randCoordGen();
			newData[x][y] = -1;	
			xCoords[i] = x;
			yCoords[i] = y;
		}
		//PRINT TEST
		String print = " ";
		for (int i=0;i<xCoords.length;i++)
		{
			print = print + " " + xCoords[i];
		}
		System.out.println(print);
		print = " ";
		for (int i=0;i<xCoords.length;i++)
		{
			print = print + " " + yCoords[i];
		}
		System.out.println(print);
		print = " ";
		
		for (int i=0;i<size;i++)
		{
			for (int j=0;j<size;j++)
			{
				if (newData[i][j] != -1)
				{
					newData[i][j] = (int) (-1*scale*minDistFromAP(i,j,xCoords,yCoords));
					if (newData[i][j] == 0)
					{
						System.out.println(minDistFromAP(i,j,xCoords,yCoords));
					}
				}
				else
				{
					newData[i][j] = -1;
				}
			}
		}
		
		
		return newData;
	}
	
	private static int[][] PtCoordsWithinDist(int i, int j, int[] xCoords, int[] yCoords, int Dist){
		int[][] PtCoords = new int[xCoords.length][2];
		for(int k=0;i<xCoords.length;i++)
		{
			if(DistBtwnPoints(i,j,xCoords[k],yCoords[k])<Dist)
			{
				PtCoords[k][0] = xCoords[k];
				PtCoords[k][1] = yCoords[k];
			}
			else
			{
				PtCoords[k][0] = 0;
				PtCoords[k][1] = 0;
			}
		}
		
		return PtCoords;
	}
	
	private static double minDistFromAP(int i, int j, int[] xCoords, int[] yCoords) {
		// TODO Auto-generated method stub
		double[] Dist = new double[xCoords.length];
		for (int k=0;k<xCoords.length;k++)
		{
			int x = Math.abs(i-xCoords[k]);
			int y = Math.abs(j-yCoords[k]);
			Dist[k] = Math.sqrt((x^2)+(y^2));
			if (Dist[k] == 0)
			{
				Dist[k] = 1000;
			}
		}
		double minDist = Dist[0];
		for (int k=1;k<Dist.length;k++)
		{
			if (Dist[k] < minDist)
			{
				minDist = Dist[k];
			}
		}
		return minDist;
	}
	
	private static int[] ClosestPoint(int i, int j, int[] xPtCoords, int[] yPtCoords) {
		// TODO Auto-generated method stub
		int[][] pointCoords = new int[xPtCoords.length][2];
		double[] Dist = new double[xPtCoords.length];
		for (int k=0;k<xPtCoords.length;k++)
		{
			int x = Math.abs(i-xPtCoords[k]);// i = xpt x = 0
			int y = Math.abs(j-yPtCoords[k]);// j = ypt y = 0
			Dist[k] = Math.sqrt((x^2)+(y^2));// dist[id] = 0

			pointCoords[k][0] = xPtCoords[k];// xcoord if dist=0
			pointCoords[k][1] = yPtCoords[k];// ycoord if dist=0


		}
		
		double minDist = 1000;
		
		int[] point = new int[2];
		for (int k=0;k<Dist.length;k++)
		{
			if (Dist[k] < minDist && Dist[k] > 0)
			{
				if(pointCoords[k][0]!=i && pointCoords[k][1]!=j)
				{
					minDist = Dist[k];
					point[0] = pointCoords[k][0];
					point[1] = pointCoords[k][1];
				}
			}
		}
		
		return point;
	}

	private static double DistBtwnPoints(int i, int j, int x, int y) {
		// TODO Auto-generated method stub
		double dist;
		int delX = Math.abs(i-x);
		int delY = Math.abs(j-y);
		dist = Math.sqrt((delX^2)+(delY^2));
		
		return dist;
	}
	
	private static int randCoordGen() {
		Random generator = new Random();
		int rando = generator.nextInt(size);
		return rando;
	}
	
	
	
}