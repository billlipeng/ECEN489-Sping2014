package team2;
import java.io.Serializable;


public class DataPacket implements Serializable{

		
		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;
		private float accelX, accelY, accelZ, orientationA, orientationB, orientationC,rotVecX,rotVecY,rotVecZ,linAccX,linAccY,linAccZ,gravityX,gravityY,gravityZ,gyroX,gyroY,gyroZ;
		private long time;
		private double longitude, latitude, bearing, speed;
		
		public DataPacket(long time,double longitude,double latitude,double bearing,double speed,float accelX,float accelY,float accelZ,float orientationA,float orientationB,float orientationC,float rotVecX,float rotVecY,float rotVecZ,float linAccX,float linAccY,float linAccZ,float gravityX,float gravityY,float gravityZ,float gyroX,float gyroY,float gyroZ) {
			super();
			this.time=time;
			this.longitude=longitude;
			this.latitude=latitude;
			this.bearing=bearing;
			this.speed=speed;
			this.accelX = accelX;
			this.accelY=accelY;
			this.accelZ=accelZ;
			this.orientationA=orientationA;
			this.orientationB=orientationB;
			this.orientationC=orientationC;
			this.rotVecX=rotVecX;
			this.rotVecY=rotVecY;
			this.rotVecZ=rotVecZ;
			this.linAccX=linAccX;
			this.linAccY=linAccY;
			this.linAccZ=linAccZ;
			this.gravityX=gravityX;
			this.gravityY=gravityY;
			this.gravityZ=gravityZ;
			this.gyroX=gyroX;
			this.gyroY=gyroY;
			this.gyroZ=gyroZ;
			
		}

		public float getAccelX() {
			return accelX;
		}

		public void setAccelX(float accelX) {
			this.accelX = accelX;
		}

		public float getAccelY() {
			return accelY;
		}

		public void setAccelY(float accelY) {
			this.accelY = accelY;
		}

		public float getAccelZ() {
			return accelZ;
		}

		public void setAccelZ(float accelZ) {
			this.accelZ = accelZ;
		}

		public float getOrientationA() {
			return orientationA;
		}

		public void setOrientationA(float orientationA) {
			this.orientationA = orientationA;
		}

		public float getOrientationB() {
			return orientationB;
		}

		public void setOrientationB(float orientationB) {
			this.orientationB = orientationB;
		}

		public float getOrientationC() {
			return orientationC;
		}

		public void setOrientationC(float orientationC) {
			this.orientationC = orientationC;
		}

		public float getRotVecX() {
			return rotVecX;
		}

		public void setRotVecX(float rotVecX) {
			this.rotVecX = rotVecX;
		}

		public float getRotVecY() {
			return rotVecY;
		}

		public void setRotVecY(float rotVecY) {
			this.rotVecY = rotVecY;
		}

		public float getRotVecZ() {
			return rotVecZ;
		}

		public void setRotVecZ(float rotVecZ) {
			this.rotVecZ = rotVecZ;
		}

		public float getLinAccX() {
			return linAccX;
		}

		public void setLinAccX(float linAccX) {
			this.linAccX = linAccX;
		}

		public float getLinAccY() {
			return linAccY;
		}

		public void setLinAccY(float linAccY) {
			this.linAccY = linAccY;
		}

		public float getLinAccZ() {
			return linAccZ;
		}

		public void setLinAccZ(float linAccZ) {
			this.linAccZ = linAccZ;
		}

		public float getGravityX() {
			return gravityX;
		}

		public void setGravityX(float gravityX) {
			this.gravityX = gravityX;
		}

		public float getGravityY() {
			return gravityY;
		}

		public void setGravityY(float gravityY) {
			this.gravityY = gravityY;
		}

		public float getGravityZ() {
			return gravityZ;
		}

		public void setGravityZ(float gravityZ) {
			this.gravityZ = gravityZ;
		}

		public float getGyroX() {
			return gyroX;
		}

		public void setGyroX(float gyroX) {
			this.gyroX = gyroX;
		}

		public float getGyroY() {
			return gyroY;
		}

		public void setGyroY(float gyroY) {
			this.gyroY = gyroY;
		}

		public float getGyroZ() {
			return gyroZ;
		}

		public void setGyroZ(float gyroZ) {
			this.gyroZ = gyroZ;
		}

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		public double getBearing() {
			return bearing;
		}

		public void setBearing(double bearing) {
			this.bearing = bearing;
		}

		public double getSpeed() {
			return speed;
		}

		public void setSpeed(double speed) {
			this.speed = speed;
		}
		

		



}
