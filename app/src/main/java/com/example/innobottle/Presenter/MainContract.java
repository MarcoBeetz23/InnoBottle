package com.example.innobottle.Presenter;

import com.example.innobottle.Entitites.SensorRun;
import com.example.innobottle.Entitites.SensorSeries;

public interface MainContract {

    interface View{
        void onSensorSeriesFound();
        void onSensorRunActive();
        void onSensorRunInactive();
    }

    interface Presenter{
        void retrieveSensorSeries();
        void retrieveSensorData();
        void exportSensorData();
    }

    interface Model{
        void findSensorSeriesInFirebase();
        void findSensorDataInFirebase();
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
