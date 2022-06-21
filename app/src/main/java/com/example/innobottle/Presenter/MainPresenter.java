package com.example.innobottle.Presenter;

import android.util.Log;

import com.example.innobottle.Entitites.SensorRun;
import com.example.innobottle.Entitites.SensorSeries;
import com.example.innobottle.Model.MainModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class MainPresenter implements MainContract.Presenter, MainContract.onSensorSeriesListener,
        MainContract.onSensorRunListener{


    // Architectural
    private MainModel mainModel;
    private MainContract.View mainView;
    private MainContract.onSensorSeriesListener onSensorSeriesListener;
    private MainContract.onSensorRunListener onSensorRunListener;

    SensorSeries currentSensorSeries;
    String currentSensorSeriesName;
    int currentSensorSeriesCounter;
    boolean sensorRunIsTheFirst;

    public MainPresenter(MainContract.View mainView){
        this.mainView = mainView;
        mainModel = new MainModel(this, this);
    }

    // results in *init* state
    @Override
    public void connectToBottle() {
        mainModel.connectBottleInFirebase();
    }

    // results in *activate* state
    @Override
    public void initNewSensorRun(String currentLineInformation) {
        Log.d("test123", "step 2...arrived in Presenter");
        if(sensorRunIsTheFirst){
            mainModel.findCurrentSensorCounter(currentLineInformation);
            mainModel.activateBottleInFirebase();
        } else {
            mainModel.activateBottleInFirebase();
        }
    }

    private SensorSeries buildNewSensorSeries(String information){
        int sensorRunCounter = 0;
        String id = UUID.randomUUID().toString().substring(0,4);
        SensorSeries sensorSeries = new SensorSeries(information, getCurrentDate(), id, sensorRunCounter);
        return sensorSeries;
    }

    private String getCurrentDate(){
        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
        return date;
    }

    // results in *pause* state
    @Override
    public void pauseCurrentSensorRun() {
        mainModel.pauseBottleInFirebase();
    }

    @Override
    public void pauseBottle() {
        mainModel.pauseBottleInFirebase();
    }

    @Override
    public void saveCurrentSensorRun(String name, int counter) {
        Log.d("test123", "back in presenter, these values arrived" + "---" + name + String.valueOf(counter));
        currentSensorSeries = mainModel.findSensorSeriesInFirebase(name, counter);
        mainModel.saveCurrentSensorRunFromFirebase(name, counter);
    }

    @Override
    public void deleteCurrentSensorRun(String name, int counter) {
        Log.d("test123", "back in presenter, these values arrived" + "---" + name + String.valueOf(counter));
        currentSensorSeries = mainModel.findSensorSeriesInFirebase(name, counter);
        mainModel.deleteCurrentSensorRunFromFirebase(name, counter);
    }

    // todo
    @Override
    public void retrieveSensorSeries() {

    }

    @Override
    public void retrieveSensorData() {

    }

    @Override
    public void exportSensorData() {

    }


    //// sensor series found

    @Override
    public void onSuccessfullyRetrieved(SensorSeries retrievedSensorSeries) {
        currentSensorSeries = retrievedSensorSeries;
        currentSensorSeriesName = retrievedSensorSeries.getName();
        Log.d("test123", "step 8 - back in presenter, ready for view");
        mainView.onSensorSeriesFound(currentSensorSeriesName, currentSensorSeriesCounter);
    }

    @Override
    public void onSuccessfullyCreated() {
        SensorSeries newSensorSeries = buildNewSensorSeries("hi");
        Log.d("test123", "step 6...we are back and ready to query the firebase");
        mainModel.initValuesInFirebase(newSensorSeries);
    }

    @Override
    public void onSuccessfullyUpdated(SensorSeries updatedSensorSeries){
        Log.d("test123", "after update" + updatedSensorSeries.toString());
    }

    @Override
    public void onCounterRetrieved(int counter) {
        if(counter == 0){
            sensorRunIsTheFirst = true;
        } else {
            sensorRunIsTheFirst = false;
        }
    }


    // sensor run
    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(String message) {

    }
}
