package com.example.innobottle.Entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DataRow {

    ArrayList<String> dataRow;
    String millis;

    public DataRow(){}

    public DataRow(ArrayList<String> dataRow, String millis){
        this.dataRow = dataRow;
        this.millis = millis;
    }

    public void setDataRow(ArrayList<String> newDataRow){
        dataRow = newDataRow;
    }

    public void setMillis(String newMillis){
        millis = newMillis;
    }

    public ArrayList<String> getDataRow(){
        return dataRow;
    }

    public String getMillis(){
        return millis;
    }

    // for graph creation, get the bottom, middle and top load cell rows
    // graph needs float

    public ArrayList<Float> bottomRow(){
        ArrayList<Float> values = new ArrayList<>();
        values.add(Float.valueOf(dataRow.get(0)));
        values.add(Float.valueOf(dataRow.get(1)));
        values.add(Float.valueOf(dataRow.get(2)));
        return values;
    }

    public ArrayList<Float> middleRow(){
        ArrayList<Float> values = new ArrayList<>();
        values.add(Float.valueOf(dataRow.get(3)));
        values.add(Float.valueOf(dataRow.get(4)));
        values.add(Float.valueOf(dataRow.get(5)));
        return values;
    }

    public ArrayList<Float> topRow(){
        ArrayList<Float> values = new ArrayList<>();
        values.add(Float.valueOf(dataRow.get(6)));
        values.add(Float.valueOf(dataRow.get(7)));
        values.add(Float.valueOf(dataRow.get(8)));
        return values;
    }

    // pre-pare the data for later usage in GraphRow object
    public Float highestValue(ArrayList<Float> values){
        Float highest = Collections.max(values);
        return highest;
    }

}
