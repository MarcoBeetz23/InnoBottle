package com.example.innobottle.Presenter;

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
        mainModel.activateBottleInFirebase();
        SensorSeries newSensorSeries = buildNewSensorSeries(currentLineInformation);
        mainModel.initValuesInFirebase(newSensorSeries);
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
        currentSensorSeries = mainModel.findSensorSeriesInFirebase(name, counter);
        mainModel.saveCurrentSensorRunFromFirebase();
    }

    @Override
    public void deleteCurrentSensorRun(String name, int counter) {
        currentSensorSeries = mainModel.findSensorSeriesInFirebase(name, counter);
        mainModel.deleteCurrentSensorRunFromFirebase();
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
    public void onSuccess(SensorSeries retrievedSensorSeries) {
        currentSensorSeries = retrievedSensorSeries;
        currentSensorSeriesName = retrievedSensorSeries.getName();
        mainView.onSensorSeriesFound(currentSensorSeriesName, currentSensorSeriesCounter);

    }


    // sensor run
    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(String message) {

    }
}
