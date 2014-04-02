
public class propogation {

	/* data_points = array of class RSSI_point objects with x, y, interpolated flag, and rssi value
	 * raw_data = array of RSSI_point objects filled with original, raw data
	 * 
	 * 
	 * for each RSSI_point i in data_points
	 * {
	 * 		if (!i.interpolated) //if this point hasn't been assigned a value, we need to perform the max finding
	 * 		{
	 * 			max_value = -10000;
	 * 			for each RSSI_point j in raw_data
	 * 			{
	 * 				compute distance between i.(x,y) and j.(x,y)
	 * 				decayed_value = alpha*j.RSSI_value / distance^2;
	 * 				if (decayed_value > max_value)
	 * 					max_value = decayed_value;
	 * 			}
	 * 			i.rssi_value = max_value; //assign the rssi value as the highest probable value
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
