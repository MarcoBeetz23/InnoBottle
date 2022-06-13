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

public class MainModel implements MainContract.Model, MainContract.onSensorSeriesListener,
        MainContract.onSensorRunListener {

    //Firebase
    private FirebaseDatabase database = FirebaseDatabase.getInstance(FIREBASEPATH);
    private DatabaseReference refState;

    //Utils
    private static final String FIREBASEPATH = "https://innolab-66e3b-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final String STATEPATH = "State";
    private static final String DATAPATH = "Data";
    private String activateState = "activate";
    private String stopState = "stop";
    private String idleState = "idle";


    //Architectural
    private MainContract.onSensorSeriesListener onSensorSeriesListener;
    private MainContract.onSensorRunListener onSensorRunListener;

    public MainModel(MainContract.onSensorSeriesListener onSensorSeriesListener,
                     MainContract.onSensorRunListener onSensorRunListener){
        this.onSensorSeriesListener = onSensorSeriesListener;
        this.onSensorRunListener = onSensorRunListener;
    }


    @Override
    public void connectBottleInFirebase() {
        refState = database.getReference(STATEPATH);
        refState.setValue(idleState);

    }

    @Override
    public void activateBottleInFirebase() {
        refState.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                refState.setValue(activateState);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error occured", error.toString());
            }
        });
    }

    @Override
    public void stopBottleInFirebase() {
        refState.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                refState.setValue(stopState);
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

}
