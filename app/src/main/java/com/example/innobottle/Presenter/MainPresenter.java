package com.example.innobottle.Presenter;

import com.example.innobottle.Entitites.SensorRun;
import com.example.innobottle.Entitites.SensorSeries;
import com.example.innobottle.Model.MainModel;

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
