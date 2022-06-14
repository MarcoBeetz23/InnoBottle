package com.example.innobottle.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.innobottle.Entitites.SensorRun;
import com.example.innobottle.Entitites.SensorSeries;
import com.example.innobottle.Presenter.MainContract;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Handler;

public class MainModel implements MainContract.Model, MainContract.onSensorSeriesListener,
        MainContract.onSensorRunListener, MainContract.InitialDataInteractor {

    //Firebase
    private FirebaseDatabase database = FirebaseDatabase.getInstance(FIREBASEPATH);
    private DatabaseReference refState, refName, refData;

    //Utils
    private static final String FIREBASEPATH = "https://innolab-66e3b-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final String STATEPATH = "State";
    private static final String SENSORVALUES = "SensorSeriesValues";
    private static final String DATAPATH = "Data";
    private static final int DELAY_TIME = 3000;
    private String activeState = "active";
    private String pauseState = "pause";
    private String initState = "init";


    //Architectural
    private MainContract.onSensorSeriesListener onSensorSeriesListener;
    private MainContract.onSensorRunListener onSensorRunListener;

    public MainModel(MainContract.onSensorSeriesListener onSensorSeriesListener,
                     MainContract.onSensorRunListener onSensorRunListener){
        this.onSensorSeriesListener = onSensorSeriesListener;
        this.onSensorRunListener = onSensorRunListener;
    }


    // initializing the *pause* state
    // Starting from *pause*, a new sensor run will be initialized when proceeding
    @Override
    public void connectBottleInFirebase() {
        refState = database.getReference(STATEPATH);
        refName = database.getReference(SENSORVALUES);
        refState.setValue(pauseState);
    }


    // setting the *init* state
    // If activated -> ESP32 will send measurement data to Firebase
    @Override
    public void activateBottleInFirebase() {
        refState.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                refState.setValue(initState);
                setActiveStateAfterDelay();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error occured", error.toString());
            }
        });
    }

    @Override
    public void initValuesInFirebase(SensorSeries sensorSeries) {
        Log.d("test123", "data passed inside Model");
        String name = sensorSeries.getName();
        String time = sensorSeries.getTime();
        String id = sensorSeries.getId();
        int sensorRunCounter = sensorSeries.getSensorRuns().size();
        // even though it would be easier to just store the entire object in the rd like
        // refName.setValue(sensorSeries),
        // this does not work since the ArrayList of SensorRuns is still empty (size = 0)
        // and would just be ignored inside firebase
        refName.child("Name").setValue(name);
        refName.child("Date").setValue(time);
        refName.child("Id").setValue(id);
        refName.child("ChildrenCount").setValue(sensorRunCounter);
    }

    private void setActiveStateAfterDelay(){
        final Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refState.setValue(activeState);
            }
        }, DELAY_TIME);
    }

    // setting the *stop* state
    // If stopped -> ESP32 will stop sending data to Firebase
    @Override
    public void pauseBottleInFirebase() {
        refState.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                refState.setValue(pauseState);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void findSensorSeriesInFirebase() {

    }

    @Override
    public void findSensorDataInFirebase() {

    }

    @Override
    public void onSuccess(SensorSeries retrievedSensorSeries) {

    }

    @Override
    public void onSuccess(SensorRun retrievedSensorRun) {

    }

    @Override
    public void onFailure(String message) {

    }

    /// 1.

    @Override
    public void onDataSuccessfullyLoaded(String dataSnapshotValue) {

    }
}
