package com.zpartal.project1.datapackets;

import java.io.Serializable;

// DataPoint class
/*
    Created using "builde" pattern, http://www.javaspecialists.eu/archive/Issue163.html
  DataPoint dp = new DataPoint.Builder().client_id("id").run_id("rid").build();
 */

public class DataPoint implements Serializable {
    // Required
    private final String client_id;
    private final String run_id; // teamid_yyyymmdd_hhmmss
    private final String time; // hh:mm:ss
    private final String date; // yyyy-mm-dd
    private final double latitude;
    private final double longitude;
    private final String sensor_id;
    private final String sensor_type;
    private final double sensor_value;
    private final String attribute;
    // Optional
    private final double bearing;
    private final double speed;
    private final double altitude;

    public static class Builder {
        // Required Parameters
        private String client_id;
        private String run_id;
        private String time;
        private String date;
        private double latitude;
        private double longitude;
        private String sensor_id;
        private String sensor_type;
        private double sensor_value;
        private String attribute;
        // Optional Parameters
        private double bearing;
        private double speed;
        private double altitude;

        public Builder() {
        }

        public Builder client_id(String val) {
            client_id = val;
            return this;
        }

        public Builder run_id(String val) {
            run_id = val;
            return this;
        }

        public Builder time(String val) {
            time = val;
            return this;
        }

        public Builder date(String val) {
            date = val;
            return this;
        }

        public Builder latitude(double val) {
            latitude = val;
            return this;
        }

        public Builder longitude(double val) {
            longitude = val;
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

        public Builder altitude(double val) {
            altitude = val;
            return this;
        }

        public Builder sensor_id(String val) {
            sensor_id = val;
            return this;
        }

        public Builder sensor_type(String val) {
            sensor_type = val;
            return this;
        }

        public Builder sensor_value(double val) {
            sensor_value = val;
            return this;
        }

        public Builder attribute(String val) {
            attribute = val;
            return this;
        }

        public DataPoint build() {
            return new DataPoint(this);
        }

    }

    public DataPoint(Builder builder) {
        this.client_id = builder.client_id;
        this.run_id = builder.run_id;
        this.time = builder.time;
        this.date = builder.date;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.bearing = builder.bearing;
        this.speed = builder.speed;
        this.altitude = builder.altitude;
        this.sensor_id = builder.sensor_id;
        this.sensor_type = builder.sensor_type;
        this.sensor_value = builder.sensor_value;
        this.attribute = builder.attribute;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getRun_id() {
        return run_id;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getSensor_id() {
        return sensor_id;
    }

    public String getSensor_type() {
        return sensor_type;
    }

    public double getSensor_value() {
        return sensor_value;
    }

    public String getAttribute() {
        return attribute;
    }

    public double getBearing() {
        return bearing;
    }

    public double getSpeed() {
        return speed;
    }

    public double getAltitude() {
        return altitude;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\'" + date + "\'" +",");
        sb.append("\'" + time + "\'" +",");
        sb.append("\'" + client_id + "\'" +",");
        sb.append("\'" + run_id + "\'" +",");
        sb.append(latitude+",");
        sb.append(longitude+",");
        sb.append(bearing+",");
        sb.append(speed+",");
        sb.append(altitude+",");
        sb.append("\'" + sensor_id + "\'" +",");
        sb.append("\'" + sensor_type + "\'" +",");
        sb.append(sensor_value+",");
        sb.append("\'" + attribute + "\'");
        return sb.toString();
    }
}