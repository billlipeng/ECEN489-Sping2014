import java.util.ArrayList;
import java.util.List;

public class LinearInterpolationAlgorithm implements InterpolationAlgorithm {
	ArrayList<DataPoint> dataSet;

	    @Override
	    public ArrayList<DataPoint> analyze(ArrayList<DataPoint> _dataSet) {
	    	ArrayList<List<DataPoint>> paritions = paritionData();
	    	dataSet = _dataSet;
	        
	        for (int i = 0; i<paritions.size();i++){
	        	List<DataPoint> list = paritions.get(i);
	        	if (list.size()!=11){
	        		
	        	}
	        	else{
	        		
	        		double longitude1= list.get(0).getLongitude();
	        		double latitude1= list.get(0).getLatitude();
	        		double longitude2= list.get(0).getLongitude();
	        		double latitude2= list.get(0).getLatitude();
	        		linearinterpolation lp = new linearinterpolation(longitude1,latitude1,longitude2,latitude2);
	        		double[] dub = lp.analyze();
	        		
	        		for(int p=0;p<dub.length;p++){
	        			dataSet.get(i*10+p).setLatitude(dub[2*(p+1)-2]);
	        			dataSet.get(i*10+p).setLongitude(dub[2*(p+1)-1]);
	        			
	        			
	        		}
	        		
	        	}
	        	
	        	
	        }



	        return dataSet;
	    }
	
	
	    private ArrayList<List<DataPoint>> paritionData() {
	        ArrayList<List<DataPoint>> partitionData = new ArrayList<List<DataPoint>>();
	        int binSize = 11;
	        int numBins = (dataSet.size() + binSize -1) / binSize;
	        for (int i = 0; i < numBins; i++) {
	            int start;
	            int end;
	            if (i == 0) {
	                start = i*binSize;
	                end = Math.min(start + binSize, dataSet.size());
	            }
	            else {
	                start = i*binSize-i;
	                end = Math.min(start + binSize, dataSet.size());
	            }
	            partitionData.add(dataSet.subList(start, end));
	        }
	        return partitionData;
	    }
	    
}
