package com.example.innobottle.Entitites;

import java.util.ArrayList;

public class SensorSeries {

    /**
     * This class represents an entire sensor series, more precisely - multiple sensor runs combined
     * If you measure something 20 times, then 20 sensor runs will be initialized
     * Thus, the length of the ArrayList would be 20
     */

    String name;
    short id;
    ArrayList<SensorRun> SensorRuns;

    public SensorSeries(String name, short id, ArrayList<SensorRun> SensorRuns){
        this.name = name;
        this.id = id;
        this.SensorRuns = SensorRuns;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public void setId(short newId){
        this.id = newId;
    }

    public String getName(){
        return this.name;
    }

    public short getId(){
        return this.id;
    }

    public ArrayList<SensorRun> getSensorRuns(){
        return this.SensorRuns;
    }
}
