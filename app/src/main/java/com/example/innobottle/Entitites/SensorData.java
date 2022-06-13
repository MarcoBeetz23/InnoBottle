package com.example.innobottle.Entitites;

import java.lang.reflect.Array;

public class SensorData {

    String timestamp;
    float[] loadCellValues;

    public SensorData(String timestamp, float[] loadCellValues){
        this.timestamp = timestamp;
        this.loadCellValues = loadCellValues;
    }

    public void setTimestamp(String newTimestamp){
        this.timestamp = newTimestamp;
    }

    public void setLoadCellValues(float[] newLoadCellValues){
        this.loadCellValues = newLoadCellValues;
    }

    public String getTimestamp(){
        return this.timestamp;
    }

    public float[] getLoadCellValues(){
        return this.loadCellValues;
    }
}
