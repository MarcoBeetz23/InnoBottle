package com.example.innobottle.Presenter;

import android.util.Log;

import com.example.innobottle.Model.MainModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        ArrayList<String> convertedList = (ArrayList<String>) manipulateList(loadCellValues);
        mView.onLoadCellValuesRetrieved(convertedList);
    }

    private List<String> manipulateList(ArrayList<String> list){
        float c1 = Float.parseFloat(list.get(0));
        float c2 = Float.parseFloat(list.get(1));
        float c3 = Float.parseFloat(list.get(2));
        float c4 = Float.parseFloat(list.get(3));
        float c5 = Float.parseFloat(list.get(4));
        float c6 = Float.parseFloat(list.get(5));
        float c7 = Float.parseFloat(list.get(6));
        float c8 = Float.parseFloat(list.get(7));
        float c9 = Float.parseFloat(list.get(8));

        Random r = new Random();
        float random = (float) (1.0 + r.nextFloat() * (4.0 - 2.0));

        c1 += random;
        c2 += random;
        c3 += random;
        c4 += random;
        c5 += random;
        c6 += random;
        c7 += random;
        c8 += random;
        c9 += random;

        ArrayList<String> convertedList = new ArrayList<>();
        convertedList.add(String.valueOf(c1));
        convertedList.add(String.valueOf(c2));
        convertedList.add(String.valueOf(c3));
        convertedList.add(String.valueOf(c4));
        convertedList.add(String.valueOf(c5));
        convertedList.add(String.valueOf(c6));
        convertedList.add(String.valueOf(c7));
        convertedList.add(String.valueOf(c8));
        convertedList.add(String.valueOf(c9));
        return convertedList;

    }
}
