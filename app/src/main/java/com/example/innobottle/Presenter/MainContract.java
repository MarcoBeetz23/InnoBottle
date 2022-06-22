package com.example.innobottle.Presenter;

import android.hardware.Sensor;

import com.example.innobottle.Entitites.SensorRun;
import com.example.innobottle.Entitites.SensorSeries;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public interface MainContract {

    interface View{
        void onInformationRetrieved(ArrayList<String> data);
    }

    interface Presenter{
        void pauseBottle();
        void connectToBottle();
        void initNewSensorRun();
        void pauseSensorRun();
        void deleteSensorRun();

    }

    interface Model{
        void pauseBottleInFirebase();
        void connectToBottleInFirebase();
        void initNewSensorRunInFirebase();
        void pauseSensorRunInFirebase();
        void deleteSensorRunInFirebase();

        void fetchValuesFromFirebase();
    }


    interface DataListener{
        void onSensorRunInitialized();
    }

}
