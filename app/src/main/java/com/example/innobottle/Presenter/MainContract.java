package com.example.innobottle.Presenter;

public interface MainContract {

    interface View{
        void startDataRetrieval();
        void pauseDataRetrieval();
    }

    interface Presenter{
        void handleRawData(String s);
        void setReadyState();
        void setActiveState();
        void setPauseState();
        void startDataTransmissionToFirebase();
    }

    interface Model{
        void setReadyStateInFirebase();
        void setActiveStateInFirebase();
        void setPauseStateInFirebase();
        void startDataTransmissionToFirebase();
        void retrieveSensorInformationInFirebase();
    }

    interface DataListener{
        void onReadyStateInitialized();
        void onActiveStateInitialized();
        void onPauseStateInitialized();
    }
}
