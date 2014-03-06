package com.jdterrell.p2;

import java.io.Serializable;

public class AndroidPacket2 implements Serializable {
	private static final long serialVersionUID = 1L;
//	public static String client_id = "TEAM4";
	public Long time;
	public Double latitude;
	public Double longitude;
	public Double bearing;
	public Double speed;
	
	public Double accel_x;
	public Double accel_y;
	public Double accel_z;
	
	public Double gyro_x;
	public Double gyro_y;
	public Double gyro_z;
	
	public Double orientation_A;
	public Double orientation_P;
	public Double orientation_R;
	
	public Double rotVec_X;
	public Double rotVec_Y;
	public Double rotVec_Z;
	public Double rotVec_C;
	
	public Double linACC_X;
	public Double linACC_Y;
	public Double linACC_Z;	
	
	public Double gravity_X;
	public Double gravity_Y;
	public Double gravity_Z;	

	
	// constructor
	public  AndroidPacket2( 	
				Long time,
				Double latitude,
				Double longitude,
				Double bearing,
				Double speed,
				
				Double accel_x,
				Double accel_y,
				Double accel_z,
				
				Double gyro_x,
				Double gyro_y,
				Double gyro_z,
				
				Double orientation_A,
				Double orientation_P,
				Double orientation_R,
				
				Double rotVec_X,
				Double rotVec_Y,
				Double rotVec_Z,
				Double rotVec_C,
				
				Double linACC_X,
				Double linACC_Y,
				Double linACC_Z,
				
				Double gravity_X,
				Double gravity_Y,
				Double gravity_Z) {
					this.time = time;
					this.latitude = latitude;
					this.longitude = longitude;
					this.bearing = bearing;
					this.latitude = latitude;
					this.longitude = longitude;
					this.speed = speed;
					
					this.accel_x = accel_x;
					this.accel_y = accel_y;
					this.accel_z = accel_z;
					
					this.gyro_x = gyro_x;
					this.gyro_y = gyro_y;
					this.gyro_z = gyro_z;
					
					this.orientation_A = orientation_A;
					this.orientation_P = orientation_P;
					this.orientation_R = orientation_R;
					
					this.rotVec_X = rotVec_X;
					this.rotVec_Y = rotVec_Y;
					this.rotVec_Z = rotVec_Z;
					this.rotVec_C = rotVec_C;
					
					this.linACC_X = linACC_X;
					this.linACC_Y = linACC_Y;
					this.linACC_Z = linACC_Z;
					
					this.gravity_X = gravity_X;
					this.gravity_Y = gravity_Y;
					this.gravity_Z = gravity_Z;
	}
	
	//handle case where latitude and longitude are null?
}
