package com.example.innobottle.Entities;

import java.util.ArrayList;

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

}
