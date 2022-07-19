package com.example.innobottle.Presenter;

import com.example.innobottle.Entities.DataRow;

import java.util.ArrayList;

public interface MainContract {

    interface View{
        void startDataRetrieval(DataRow row);
        void pauseDataRetrieval();
        void startGraphData(ArrayList<Float> values);
        void onInformationRetrieved(ArrayList<String> data);
    }

    interface Presenter{
        void handleRawData(String s);
        void setReadyState();
        void setActiveState();
        void setPauseState();
        void retrieveSensorInformation();
    }

    interface Model{
        void setReadyStateInFirebase();
        void setActiveStateInFirebase();
        void setPauseStateInFirebase();
        void startDataTransmissionToFirebase(DataRow row, String time);
        void retrieveSensorInformationInFirebase();
    }

    interface DataListener{
        void onReadyStateInitialized();
        void onActiveStateInitialized(String state);
        void onPauseStateInitialized(String state);
        void onSensorInformationRetrieved(ArrayList<String> information);
    }
}
