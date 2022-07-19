package com.example.innobottle.Entities;

import java.util.ArrayList;

public class GraphRow {


    public ArrayList<Float> getRingValues() {
        return ringValues;
    }

    public void setRingValues(ArrayList<Float> ringValues) {
        this.ringValues = ringValues;
    }

    public GraphRow(ArrayList<Float> ringValues) {
        this.ringValues = ringValues;
    }

    private ArrayList<Float> ringValues;

    public GraphRow(){}

}
