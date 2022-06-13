package com.example.innobottle.Entitites;

import java.util.ArrayList;

public class SensorRun {

    String name;
    short id;
    ArrayList<SensorData> SensorDataRecordings;

    public SensorRun(String name, short id, ArrayList<SensorData> SensorDataRecordings){
        this.name = name;
        this.id = id;
        this.SensorDataRecordings = SensorDataRecordings;
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

    public ArrayList<SensorData> getSensorDataRecordings(){
        return this.SensorDataRecordings;
    }
}
