
public class propogation {

	/* data_points = array of class RSSI_point objects with x, y, interpolated flag, and rssi value
	 * 
	 * 
	 * 
	 * for each RSSI_point i in data_points
	 * {
	 * 		if (!interpolated) //if this point hasn't been assigned a value, we need to perform the superposition summation
	 * 		{
	 * 			superposition_value = ;
	 * 			for each RSSI_point j in data_points
	 * 			{
	 * 				if (interpolated) //find the contribution from this source and add it to superposition_value
	 * 				{
	 * 					compute distance between i.(x,y) and j.(x,y)
	 * 					contribution from this point = alpha*j.RSSI_value / distance^2;//could also do exponential decay here
	 * 				}
	 * 			}
	 * 			i.RSSI_value = superposition_value;
	 * 		}
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
}
