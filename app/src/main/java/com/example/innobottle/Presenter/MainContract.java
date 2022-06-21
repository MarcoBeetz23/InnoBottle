package com.example.innobottle.Presenter;

import android.hardware.Sensor;

import com.example.innobottle.Entitites.SensorRun;
import com.example.innobottle.Entitites.SensorSeries;
import com.google.firebase.database.DataSnapshot;

public interface MainContract {

    interface View{
        void onSensorSeriesFound(String name, int counter);
        void onSensorRunActive();
        void onSensorRunInactive();
    }

    interface Presenter{
        void connectToBottle();
        void initNewSensorRun(String currentLineInformation);
        void pauseCurrentSensorRun();
        void pauseBottle();
        void saveCurrentSensorRun(String name, int counter);
        void deleteCurrentSensorRun(String name, int counter);
        void retrieveSensorSeries();
        void retrieveSensorData();
        void exportSensorData();
    }

    interface Model{
        void connectBottleInFirebase();
        void activateBottleInFirebase();
        void initValuesInFirebase(SensorSeries sensorSeries);
        void pauseBottleInFirebase();
        void saveCurrentSensorRunFromFirebase(String name, int counter);
        void deleteCurrentSensorRunFromFirebase(String name, int counter);
        SensorSeries findSensorSeriesInFirebase(String name, int counter);
        void findCurrentSensorCounter(String name);
    }

    // 1. Interface
    // wait until sensordata is send to firebase then proceed with setting the mode
    interface InitialDataInteractor {
        void onDataSuccessfullyLoaded(String dataSnapshotValue);
    }

    interface onSensorSeriesListener{
        void onSuccessfullyRetrieved(SensorSeries series);
        void onSuccessfullyCreated();
        void onSuccessfullyUpdated(SensorSeries series);
        void onCounterRetrieved(int counter);
        void onFailure(String message);
    }

    interface onSensorRunListener{
        void onSuccess();
        void onFailure(String message);
    }

}
