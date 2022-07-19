package com.example.innobottle.Entities;

public class GraphRow {

    public Float[] getRingValues() {
        return ringValues;
    }

    public void setRingValues(Float[] ringValues) {
        this.ringValues = ringValues;
    }

    private Float[] ringValues;

    public GraphRow(int size){
        ringValues = new Float[size];
    }

    public GraphRow(){}

}
