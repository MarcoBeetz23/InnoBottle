package com.example.innobottle.Presenter;

import com.example.innobottle.Model.MainModel;

public class MainPresenter implements MainContract.Presenter, MainContract.DataListener{

    private MainContract.View mView;
    private MainModel mModel;

    public MainPresenter(MainContract.View mainView){
        this.mView = mainView;
        mModel = new MainModel(this);
    }

    @Override
    public void connectToBottle() {
        mModel.connectToBottleInFirebase();
    }

    @Override
    public void pauseBottle() {

    }

    @Override
    public void initNewSensorRun() {
        mModel.initNewSensorRunInFirebase();
    }

    @Override
    public void pauseSensorRun() {

    }

    @Override
    public void deleteSensorRun() {

    }

    // After sensor run is initialized, fetching the data (from the ESP) shall be initialized
    @Override
    public void onSensorRunInitialized() {
        mModel.fetchValuesFromFirebase();
    }
}
