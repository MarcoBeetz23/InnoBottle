package com.example.innobottle.Entitites;

import java.util.ArrayList;

public class SensorSeries {

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
