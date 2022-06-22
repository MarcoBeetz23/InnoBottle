package com.example.innobottle.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.innobottle.Entitites.SensorRun;
import com.example.innobottle.Entitites.SensorSeries;
import com.example.innobottle.Presenter.MainContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Handler;

public class MainModel implements MainContract.Model, MainContract.InitialDataInteractor {

    //Firebase
    private FirebaseDatabase database = FirebaseDatabase.getInstance(FIREBASEPATH);
    private DatabaseReference refState, refName, refData;

    //Utils
    private static final String FIREBASEPATH = "https://innolab-66e3b-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final String STATEPATH = "State";
    private static final String SENSORVALUES = "SensorInformation";
    private static final String DATAPATH = "SensorSeries";
    private static final int DELAY_TIME = 3000;
    private String activeState = "active";
    private String pauseState = "pause";
    private String initState = "init";


    //Architectural
    private MainContract.DataListener dataListener;

    public MainModel(MainContract.DataListener dataListener){
        this.dataListener = dataListener;
    }


    // initializing the *pause* state
    // Starting from *pause*, a new sensor run will be initialized when proceeding
    @Override
    public void connectBottleInFirebase() {
        refState = database.getReference(STATEPATH);
        refName = database.getReference(SENSORVALUES);
        refState.setValue(pauseState);
        refName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // setting the *init* state
    // If activated -> ESP32 will send measurement data to Firebase
    @Override
    public void activateBottleInFirebase() {
        Log.d("test123", "step 3...arrived in Model");
        refState.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                refState.setValue(initState);
                Log.d("test123", "step 4....manipulating firebase");
                setActiveStateAfterDelay();
                Log.d("test123", "step 5...delay is over!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error occured", error.toString());
            }
        });
    }

    @Override
    public void initValuesInFirebase(SensorSeries sensorSeries) {
        Log.d("test123", "ok...lets go!!!");
        refName.setValue(sensorSeries).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    dataListener.onSuccessfullyRetrieved(sensorSeries);
                    Log.d("test123", "step 7...we are done");
                }
            }
        });
    }

    private void setActiveStateAfterDelay(){
        final Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refState.setValue(activeState).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SensorSeries test = new SensorSeries();
                        dataListener.onSuccessfullyCreated(test);
                    }
                });
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
    public void saveCurrentSensorRunFromFirebase(String name, int counter) {
        Log.d("test123", "arrived in model with" + "---" + name + "xxx" + String.valueOf(counter));
        refName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id = snapshot.getValue(SensorSeries.class).getId();
                String time = snapshot.getValue(SensorSeries.class).getTime();
                SensorSeries updatedSensorSeries = new SensorSeries(name, time, id, counter+1);
                refName.setValue(updatedSensorSeries).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            dataListener.onSuccessfullyUpdated(updatedSensorSeries);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void deleteCurrentSensorRunFromFirebase(String name, int counter) {

    }

    @Override
    public SensorSeries findSensorSeriesInFirebase(String name, int counter) {
        // todo
        SensorSeries series= new SensorSeries();
        return series;
    }

    @Override
    public void findCurrentSensorCounter(String name) {
        refName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter = snapshot.getValue(SensorSeries.class).getSensorRunCounter();
                dataListener.onCounterRetrieved(counter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onDataSuccessfullyLoaded(String dataSnapshotValue) {

    }
}
