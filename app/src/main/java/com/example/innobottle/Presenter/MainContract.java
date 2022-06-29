package com.example.innobottle.Presenter;

import android.hardware.Sensor;

import com.example.innobottle.Entitites.SensorRun;
import com.example.innobottle.Entitites.SensorSeries;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public interface MainContract {

    interface View{
        void onInformationRetrieved(ArrayList<String> data);
        void onLoadCellValuesRetrieved(ArrayList<String> cellValues);
    }

    interface Presenter{
        void pauseSensorRun();
        void deleteSensorRun();
        void resumeSensorRun();

        void setReadyState();
        void setActiveState();
        void setPauseState();

        void retrieveSensorInformation();

    }

    interface Model{
        void setReadyStateInFirebase();
        void setPauseStateInFirebase();
        void setActiveStateInFirebase();

        void resumeSensorRunInFirebase();
        void deleteSensorRunInFirebase();
        void fetchValuesFromFirebase();

        void retrieveSensorInformationInFirebase();
    }


    interface DataListener{
        void onSensorInformationRetrieved(ArrayList<String> information);
        void onSensorRunInitialized();
        void onLoadCellValuesRetrieved(ArrayList<String> loadCellValues);
    }

}
