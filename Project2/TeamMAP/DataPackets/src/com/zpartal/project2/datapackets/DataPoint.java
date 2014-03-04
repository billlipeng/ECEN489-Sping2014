package com.zpartal.project2.datapackets;

import java.io.Serializable;

// DataPoint class
/*
    Created using "builde" pattern, http://www.javaspecialists.eu/archive/Issue163.html
  DataPoint dp = new DataPoint.Builder().time(time).longitude(long).latitude(lat).build();
 */

public class DataPoint implements Serializable {
    private final long time;
    private final double longitude;
    private final double latitude;
    private final double bearing;
    private final double speed;
    private final double accelX;
    private final double accelY;
    private final double accelZ;
    private final double orientationA;
    private final double orientationP;
    private final double orientationR;
    private final double rotVecX;
    private final double rotVecY;
    private final double rotVecZ;
    private final double rotVecC;
    private final double linAccX;
    private final double linAccY;
    private final double linAccZ;
    private final double gravityX;
    private final double gravityY;
    private final double gravityZ;
    private final double gyroX;
    private final double gyroY;
    private final double gyroZ;

    public static class Builder {
        private long time;
        private double longitude;
        private double latitude;
        private double bearing;
        private double speed;
        private double accelX;
        private double accelY;
        private double accelZ;
        private double orientationA;
        private double orientationP;
        private double orientationR;
        private double rotVecX;
        private double rotVecY;
        private double rotVecZ;
        private double rotVecC;
        private double linAccX;
        private double linAccY;
        private double linAccZ;
        private double gravityX;
        private double gravityY;
        private double gravityZ;
        private double gyroX;
        private double gyroY;
        private double gyroZ;

        public Builder() {
        }

        public Builder time(long val) {
            time = val;
            return this;
        }
        public Builder longitude(double val) {
            longitude = val;
            return this;
        }
        public Builder latitude(double val) {
            latitude = val;
            return this;
        }
        public Builder bearing(double val) {
            bearing = val;
            return this;
        }
        public Builder speed(double val) {
            speed = val;
            return this;
        }
        public Builder accelX(double val) {
            accelX = val;
            return this;
        }
        public Builder accelY(double val) {
            accelY = val;
            return this;
        }
        public Builder accelZ(double val) {
            accelZ = val;
            return this;
        }
        public Builder orientationA(double val) {
            orientationA = val;
            return this;
        }
        public Builder orientationP(double val) {
            orientationP = val;
            return this;
        }
        public Builder orientationR(double val) {
            orientationR = val;
            return this;
        }
        public Builder rotVecX(double val) {
            rotVecX = val;
            return this;
        }
        public Builder rotVecY(double val) {
            rotVecY = val;
            return this;
        }
        public Builder rotVecZ(double val) {
            rotVecZ = val;
            return this;
        }
        public Builder rotVecC(double val) {
            rotVecC = val;
            return this;
        }
        public Builder linAccX(double val) {
            linAccX = val;
            return this;
        }
        public Builder linAccY(double val) {
            linAccY = val;
            return this;
        }
        public Builder linAccZ(double val) {
            linAccZ = val;
            return this;
        }
        public Builder gravityX(double val) {
            gravityX = val;
            return this;
        }
        public Builder gravityY(double val) {
            gravityY = val;
            return this;
        }
        public Builder gravityZ(double val) {
            gravityZ = val;
            return this;
        }
        public Builder gyroX(double val) {
            gyroX = val;
            return this;
        }
        public Builder gyroY(double val) {
            gyroY = val;
            return this;
        }
        public Builder gyroZ(double val) {
            gyroZ = val;
            return this;
        }

        public DataPoint build() {
            return new DataPoint(this);
        }

    }

    public DataPoint(Builder builder) {
        this.time = builder.time;
        this.longitude = builder.longitude;
        this.latitude = builder.latitude;
        this.bearing = builder.bearing;
        this.speed = builder.speed;
        this.accelX = builder.accelX;
        this.accelY = builder.accelY;
        this.accelZ = builder.accelZ;
        this.orientationA = builder.orientationA;
        this.orientationP = builder.orientationP;
        this.orientationR = builder.orientationR;
        this.rotVecX = builder.rotVecX;
        this.rotVecY = builder.rotVecY;
        this.rotVecZ = builder.rotVecZ;
        this.rotVecC = builder.rotVecC;
        this.linAccX = builder.linAccX;
        this.linAccY = builder.linAccY;
        this.linAccZ = builder.linAccZ;
        this.gravityX = builder.gravityX;
        this.gravityY = builder.gravityY;
        this.gravityZ = builder.gravityZ;
        this.gyroX = builder.gyroX;
        this.gyroY = builder.gyroY;
        this.gyroZ = builder.gyroZ;
    }

    public long getTime() {
        return time;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getBearing() {
        return bearing;
    }

    public double getSpeed() {
        return speed;
    }

    public double getAccelX() {
        return accelX;
    }

    public double getAccelY() {
        return accelY;
    }

    public double getAccelZ() {
        return accelZ;
    }

    public double getOrientationA() {
        return orientationA;
    }

    public double getOrientationP() {
        return orientationP;
    }

    public double getOrientationR() {
        return orientationR;
    }

    public double getRotVecX() {
        return rotVecX;
    }

    public double getRotVecY() {
        return rotVecY;
    }

    public double getRotVecZ() {
        return rotVecZ;
    }

    public double getRotVecC() {
        return rotVecC;
    }

    public double getLinAccX() {
        return linAccX;
    }

    public double getLinAccY() {
        return linAccY;
    }

    public double getLinAccZ() {
        return linAccZ;
    }

    public double getGravityX() {
        return gravityX;
    }

    public double getGravityY() {
        return gravityY;
    }

    public double getGravityZ() {
        return gravityZ;
    }

    public double getGyroX() {
        return gyroX;
    }

    public double getGyroY() {
        return gyroY;
    }

    public double getGyroZ() {
        return gyroZ;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\'" + time + "\'" +",");
        sb.append(longitude+",");
        sb.append(latitude+",");
        sb.append(bearing+",");
        sb.append(speed+",");
        sb.append(accelX+",");
        sb.append(accelY+",");
        sb.append(accelZ+",");
        sb.append(orientationA+",");
        sb.append(orientationP+",");
        sb.append(orientationR+",");
        sb.append(rotVecX+",");
        sb.append(rotVecY+",");
        sb.append(rotVecZ+",");
        sb.append(rotVecC+",");
        sb.append(linAccX+",");
        sb.append(linAccY+",");
        sb.append(linAccZ+",");
        sb.append(gravityX+",");
        sb.append(gravityY+",");
        sb.append(gravityZ+",");
        sb.append(gyroX+",");
        sb.append(gyroY+",");
        sb.append(gyroZ);
        return sb.toString();
    }
//    Class clazz = TestClass.class;
//    Field[] fields = clazz.getDeclaredFields();
//    for (Field field : fields)
//    {
//        System.out.println("fieldName: "+field.getName()+", fieldType: "+field.getType());
//    }
}
