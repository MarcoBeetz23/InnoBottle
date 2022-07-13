package com.example.innobottle.Presenter;

import android.util.Log;

import com.example.innobottle.Model.MainModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainPresenter implements MainContract.Presenter, MainContract.DataListener{

    private MainContract.View mView;
    private MainModel mModel;

    private boolean stateIsActive = false;

    public MainPresenter(MainContract.View mainView){
        this.mView = mainView;
        mModel = new MainModel(this);
    }

    @Override
    public void handleRawData(String s) {
        if(stateIsActive){
            Log.d("fetched data ;", s);
        }
    }

    @Override
    public void setReadyState() {
        mModel.setReadyStateInFirebase();
    }

    @Override
    public void setActiveState() {
        mModel.setActiveStateInFirebase();
    }

    @Override
    public void setPauseState() {
        mModel.setPauseStateInFirebase();
    }

    @Override
    public void startDataTransmissionToFirebase() {

    }

    @Override
    public void onReadyStateInitialized() {
        stateIsActive = false;
    }

    @Override
    public void onActiveStateInitialized() {
        stateIsActive = true;
    }

    @Override
    public void onPauseStateInitialized() {
        stateIsActive = false;
    }
}
