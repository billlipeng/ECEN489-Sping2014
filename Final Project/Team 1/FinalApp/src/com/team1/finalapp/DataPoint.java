package com.team1.finalapp;

import java.io.Serializable;

// DataPoint class
/*
    Created using "builde" pattern, http://www.javaspecialists.eu/archive/Issue163.html
  DataPoint dp = new DataPoint.Builder().time(time).longitude(long).latitude(lat).build();
 */

public class DataPoint implements Serializable {
    private final long time;
    private final String device_id;
    private final String ssid;
    private final double rssi;
    private final double longitude;
    private final double latitude;

    public static class Builder {
        private long time;
        private String device_id;
        private String ssid;
        private double rssi;
        private double longitude;
        private double latitude;

        public Builder() {
        }

        public Builder time(long val) {
            time = val;
            return this;
        }
        public Builder device_id(String val) {
            device_id = val;
            return this;
        }
        public Builder ssid(String val) {
            ssid = val;
            return this;
        }
        public Builder rssi(double val) {
            rssi = val;
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

        public DataPoint build() {
            return new DataPoint(this);
        }

    }

    public DataPoint(Builder builder) {
        this.time = builder.time;
        this.device_id = builder.device_id;
        this.ssid = builder.ssid;
        this.rssi = builder.rssi;
        this.longitude = builder.longitude;
        this.latitude = builder.latitude;
    }

    public long getTime() {
        return time;
    }

    public String getDevice_id() {
        return device_id;
    }

    public String getSsid() {
        return ssid;
    }

    public double getRssi() {
        return rssi;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    // Use this to simplify the creation of SQL statments with all fields for inserts
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\'" + time + "\'" +",");
        sb.append("\'" + device_id + "\'" + ",");
        sb.append("\'" + ssid + "\'" + ",");
        sb.append(rssi+",");
        sb.append(longitude+",");
        sb.append(latitude);
        return sb.toString();
    }
}
