package com.example.innobottle.Entities;

import java.util.ArrayList;

public class DataRow {

    ArrayList<Float> dataRow;
    int millis;

    public DataRow(ArrayList<Float> dataRow, int millis){
        this.dataRow = dataRow;
        this.millis = millis;
    }

    public void setDataRow(ArrayList<Float> newDataRow){
        dataRow = newDataRow;
    }

    public void setMillis(int newMillis){
        millis = newMillis;
    }

    public ArrayList<Float> getDataRow(){
        return dataRow;
    }

    public int getMillis(){
        return millis;
    }

}
