package com.zpartal.project1.datapackets;

import java.util.ArrayList;

public class DataSet {
    private ArrayList<DataPoint> dpList;

    void add(DataPoint dp) {
        dpList.add(dp);
    }

    ArrayList<DataPoint> getlist() {
        return dpList;
    }
}
