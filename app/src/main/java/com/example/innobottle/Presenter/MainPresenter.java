package com.example.innobottle.Presenter;

import android.util.Log;

import com.example.innobottle.Model.MainModel;

import java.util.ArrayList;

public class MainPresenter implements MainContract.Presenter, MainContract.DataListener{

    private MainContract.View mView;
    private MainModel mModel;

    public MainPresenter(MainContract.View mainView){
        this.mView = mainView;
        mModel = new MainModel(this);
    }


    /////////////////////////////////////////
    // This section handles user input
    @Override
    public void pauseSensorRun() {
    }

    @Override
    public void deleteSensorRun() {
    }

    @Override
    public void resumeSensorRun(){
    }
    //////////////////////////////////////


    ////////////////////////////////////////
    // This section handles state management
    @Override
    public void setReadyState() {
        mModel.setReadyStateInFirebase();
    }

    @Override
    public void setPauseState() {
        mModel.setPauseStateInFirebase();
    }

    @Override
    public void setActiveState() {
        mModel.setActiveStateInFirebase();
    }

    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // This section retrieves meta information about the current Sensor Run
    @Override
    public void retrieveSensorInformation(){
        mModel.retrieveSensorInformationInFirebase();
    }

    @Override
    public void onSensorInformationRetrieved(ArrayList<String> information){
        mView.onInformationRetrieved(information);
    }
    ////////////////////////////////////////////


    /////////////////////////////////////////////
    // This section represents internal processes and is not called initially but later referenced
    // Coming from mModel.setActiveStateInFirebase()
    // When the task is complete, another Model-Method is called that will actually retrieve the values
    @Override
    public void onSensorRunInitialized() {
        mModel.fetchValuesFromFirebase();
    }


    /// The data from the load cells was collected by the model and arrives here again
    @Override
    public void onLoadCellValuesRetrieved(ArrayList<String> loadCellValues) {
        Log.d("test5000", loadCellValues.toString());
    }
}
