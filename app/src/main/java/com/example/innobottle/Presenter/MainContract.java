package com.example.innobottle.Presenter;

import com.example.innobottle.Entitites.SensorRun;
import com.example.innobottle.Entitites.SensorSeries;
import com.google.firebase.database.DataSnapshot;

public interface MainContract {

    interface View{
        void onSensorSeriesFound();
        void onSensorRunActive();
        void onSensorRunInactive();
    }

    interface Presenter{
        void connectToBottle();
        void initNewSensorRun(String currentLineInformation);
        void pauseCurrentSensorRun();
        void pauseBottle();
        void retrieveSensorSeries();
        void retrieveSensorData();
        void exportSensorData();
    }

    interface Model{
        void connectBottleInFirebase();
        void activateBottleInFirebase();
        void initValuesInFirebase(SensorSeries sensorSeries);
        void pauseBottleInFirebase();
        void findSensorSeriesInFirebase();
        void findSensorDataInFirebase();
    }

    // 1. Interface
    // wait until sensordata is send to firebase then proceed with setting the mode
    interface InitialDataInteractor {
        void onDataSuccessfullyLoaded(String dataSnapshotValue);
    }

    interface onSensorSeriesListener{
        void onSuccess(SensorSeries retrievedSensorSeries);
        void onFailure(String message);
    }

    interface onSensorRunListener{
        void onSuccess(SensorRun retrievedSensorRun);
        void onFailure(String message);
    }

}
