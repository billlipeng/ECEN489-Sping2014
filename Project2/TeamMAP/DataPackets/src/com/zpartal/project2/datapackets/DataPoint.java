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
    private final Integer accelX;
    private final Integer accelY;
    private final Integer accelZ;
    private final Integer orientationA;
    private final Integer orientationP;
    private final Integer orientationR;
    private final Integer rotVecX;
    private final Integer rotVecY;
    private final Integer rotVecZ;
    private final Integer rotVecC;
    private final Integer linAccX;
    private final Integer linAccY;
    private final Integer linAccZ;
    private final Integer gravityX;
    private final Integer gravityY;
    private final Integer gravityZ;
    private final Integer gyroX;
    private final Integer gyroY;
    private final Integer gyroZ;

    public static class Builder {
        private long time;
        private double longitude;
        private double latitude;
        private double bearing;
        private double speed;
        private Integer accelX;
        private Integer accelY;
        private Integer accelZ;
        private Integer orientationA;
        private Integer orientationP;
        private Integer orientationR;
        private Integer rotVecX;
        private Integer rotVecY;
        private Integer rotVecZ;
        private Integer rotVecC;
        private Integer linAccX;
        private Integer linAccY;
        private Integer linAccZ;
        private Integer gravityX;
        private Integer gravityY;
        private Integer gravityZ;
        private Integer gyroX;
        private Integer gyroY;
        private Integer gyroZ;

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
        public Builder accelX(Integer val) {
            accelX = val;
            return this;
        }
        public Builder accelY(Integer val) {
            accelY = val;
            return this;
        }
        public Builder accelZ(Integer val) {
            accelZ = val;
            return this;
        }
        public Builder orientationA(Integer val) {
            orientationA = val;
            return this;
        }
        public Builder orientationP(Integer val) {
            orientationP = val;
            return this;
        }
        public Builder orientationR(Integer val) {
            orientationR = val;
            return this;
        }
        public Builder rotVecX(Integer val) {
            rotVecX = val;
            return this;
        }
        public Builder rotVecY(Integer val) {
            rotVecY = val;
            return this;
        }
        public Builder rotVecZ(Integer val) {
            rotVecZ = val;
            return this;
        }
        public Builder rotVecC(Integer val) {
            rotVecC = val;
            return this;
        }
        public Builder linAccX(Integer val) {
            linAccX = val;
            return this;
        }
        public Builder linAccY(Integer val) {
            linAccY = val;
            return this;
        }
        public Builder linAccZ(Integer val) {
            linAccZ = val;
            return this;
        }
        public Builder gravityX(Integer val) {
            gravityX = val;
            return this;
        }
        public Builder gravityY(Integer val) {
            gravityY = val;
            return this;
        }
        public Builder gravityZ(Integer val) {
            gravityZ = val;
            return this;
        }
        public Builder gyroX(Integer val) {
            gyroX = val;
            return this;
        }
        public Builder gyroY(Integer val) {
            gyroY = val;
            return this;
        }
        public Builder gyroZ(Integer val) {
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

    public Integer getAccelX() {
        return accelX;
    }

    public Integer getAccelY() {
        return accelY;
    }

    public Integer getAccelZ() {
        return accelZ;
    }

    public Integer getOrientationA() {
        return orientationA;
    }

    public Integer getOrientationP() {
        return orientationP;
    }

    public Integer getOrientationR() {
        return orientationR;
    }

    public Integer getRotVecX() {
        return rotVecX;
    }

    public Integer getRotVecY() {
        return rotVecY;
    }

    public Integer getRotVecZ() {
        return rotVecZ;
    }

    public Integer getRotVecC() {
        return rotVecC;
    }

    public Integer getLinAccX() {
        return linAccX;
    }

    public Integer getLinAccY() {
        return linAccY;
    }

    public Integer getLinAccZ() {
        return linAccZ;
    }

    public Integer getGravityX() {
        return gravityX;
    }

    public Integer getGravityY() {
        return gravityY;
    }

    public Integer getGravityZ() {
        return gravityZ;
    }

    public Integer getGyroX() {
        return gyroX;
    }

    public Integer getGyroY() {
        return gyroY;
    }

    public Integer getGyroZ() {
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
        sb.append(accelX+",");
        sb.append(accelX+",");
        sb.append(accelX+",");
        sb.append(accelX+",");
        sb.append(accelX+",");
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
