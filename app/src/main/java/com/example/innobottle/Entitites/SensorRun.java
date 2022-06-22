package com.example.innobottle.Entitites;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SensorRun {

    /**
     * This class represents a single sensor run
     * The ArrayList holds each measurement of the load cells by timestamp
     * E.g. if the delay in the ESP32 is 200ms, then 5 measurements are generated each second
     * If the duration of the measurement is 30 seconds, the ArrayList's length would be 30 x 5 = 150
     */

    String name;
    short id;
    ArrayList<ArrayList<Float>> dataRows;

    public SensorRun(String name, short id, ArrayList<ArrayList<Float>> dataRows){
        this.name = name;
        this.id = id;
        this.dataRows = dataRows;
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

    public ArrayList<ArrayList<Float>> getDataRows(){
        return this.dataRows;
    }
}
