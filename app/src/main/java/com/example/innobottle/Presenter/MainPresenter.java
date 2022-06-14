package com.example.innobottle.Presenter;

import com.example.innobottle.Entitites.SensorRun;
import com.example.innobottle.Entitites.SensorSeries;
import com.example.innobottle.Model.MainModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainPresenter implements MainContract.Presenter, MainContract.onSensorSeriesListener,
        MainContract.onSensorRunListener{


    // Architectural
    private MainModel mainModel;
    private MainContract.View mainView;
    private MainContract.onSensorSeriesListener onSensorSeriesListener;
    private MainContract.onSensorRunListener onSensorRunListener;

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
        mainModel.initValuesInFirebase(currentLineInformation, getCurrentDate());
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

    @Override
    public void onSuccess(SensorSeries retrievedSensorSeries) {

    }

    @Override
    public void onSuccess(SensorRun retrievedSensorRun) {

    }

    @Override
    public void onFailure(String message) {

    }
}
