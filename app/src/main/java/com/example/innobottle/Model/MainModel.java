package com.example.innobottle.Model;

import com.example.innobottle.Entitites.SensorRun;
import com.example.innobottle.Entitites.SensorSeries;
import com.example.innobottle.Presenter.MainContract;

public class MainModel implements MainContract.Model, MainContract.onSensorSeriesListener,
        MainContract.onSensorRunListener {

    //Architectural
    private MainContract.onSensorSeriesListener onSensorSeriesListener;
    private MainContract.onSensorRunListener onSensorRunListener;

    public MainModel(MainContract.onSensorSeriesListener onSensorSeriesListener,
                     MainContract.onSensorRunListener onSensorRunListener){
        this.onSensorSeriesListener = onSensorSeriesListener;
        this.onSensorRunListener = onSensorRunListener;
    }

    // todo

    @Override
    public void findSensorSeriesInFirebase() {

    }

    @Override
    public void findSensorDataInFirebase() {

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
