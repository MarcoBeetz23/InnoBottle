package com.example.innobottle.Entitites;

import android.hardware.Sensor;

import java.util.ArrayList;

public class SensorSeries {

    /**
     * This class represents an entire sensor series, more precisely - multiple sensor runs combined
     * If you measure something 20 times, then 20 sensor runs will be initialized
     * Thus, the length of the ArrayList would be 20
     */

    String name, time, id;
    int sensorRunCounter;
    ArrayList<SensorRun> SensorRuns;

    // empty default constructor for firebase
    public SensorSeries(){}

    // constructor to hold the actual data
    public SensorSeries(String name, String time, String id, ArrayList<SensorRun> SensorRuns){
        this.name = name;
        this.time = time;
        this.id = id;
        this.SensorRuns = SensorRuns;
    }

    // constructor to hold meta data for ESP32 retrieval and state observation
    public SensorSeries(String name, String time, String id, int sensorRunCounter){
        this.name = name;
        this.time = time;
        this.id = id;
        this.sensorRunCounter = sensorRunCounter;
    }

    public void setSensorRunCounter(int newSensorRunCounter){
        this.sensorRunCounter = newSensorRunCounter;
    }

    public int getSensorRunCounter(){
        return sensorRunCounter;
    }


    public void setName(String newName){
        this.name = newName;
    }

    public void setTime(String newTime){
        this.time = newTime;
    }

    public void setId(String newId){
        this.id = newId;
    }

    public String getName(){
        return this.name;
    }

    public String getTime(){
        return this.time;
    }

    public String getId(){
        return this.id;
    }

    public ArrayList<SensorRun> getSensorRuns(){
        return this.SensorRuns;
    }
}
