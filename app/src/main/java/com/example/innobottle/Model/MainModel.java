package com.example.innobottle.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.innobottle.Presenter.MainContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MainModel implements MainContract.Model {

    private MainContract.DataListener mListener;
    // Firebase Setup
    private FirebaseDatabase database = FirebaseDatabase.getInstance(FIREBASEPATH);
    private DatabaseReference refData, refState, refInformation;

    // Firebase Utils
    private static final String FIREBASEPATH = "https://innolab-66e3b-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final String DATAPATH = "SensorData";
    private static final String STATEPATH = "State";
    private static final String INFORMATIONPATH = "SensorInformation";

    // State constants
    private static final String READYSTATE = "ready";
    private static final String ACTIVESTATE = "active";
    private static final String PAUSESTATE = "pause";

    public MainModel(MainContract.DataListener listener){
        this.mListener = listener;
    }


    ////////////////////////////////////////////////////
    // This section deals with state management
    // When the screen appears for the first time or is present, the state is set to ready
    // Thus, getting the database reference is only required here since the other two states
    // can only be continued after the first
    @Override
    public void setReadyStateInFirebase() {
        refState = database.getReference(STATEPATH);
        refState.setValue(READYSTATE);
    }


    // When the sensor run is initialized but not started yet
    // When the sensor run has been interrupted
    @Override
    public void setPauseStateInFirebase() {
        refState.setValue(PAUSESTATE);
    }

    // When the sensor run is active, the ESP32 will retrieve data from load cells and send it
    @Override
    public void setActiveStateInFirebase() {
        refState.setValue(ACTIVESTATE).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    // wait until task is complete to make sure there will be no null reference
                    mListener.onSensorRunInitialized();
                }
            }
        });
    }

    // Coming from user interaction inside the dialog
    @Override
    public void resumeSensorRunInFirebase() {

    }

    @Override
    public void deleteSensorRunInFirebase() {

    }


    // Here, we retrieve the actual sensor data from Firebase and send it back to the Activity later on
    @Override
    public void fetchValuesFromFirebase() {
        refData = database.getReference(DATAPATH);
        Query lastChild = refData.orderByKey().limitToLast(1);
        lastChild.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    String s = String.valueOf(snap.getValue());
                    String key = snap.getKey();
                    HashMap<String, String> map = new HashMap<String, String>();
                    Collection<String> values = map.values();
                    ArrayList<String> listOfValues = new ArrayList<String>(values);
                    Log.d("test123", listOfValues.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("test123", "before value event listener" + "---" + refData.toString());
        //tbd
    }

    // Retrieving meta information about the current sensor run etc.
    @Override
    public void retrieveSensorInformationInFirebase() {
        refInformation = database.getReference(INFORMATIONPATH);
        refInformation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ArrayList<String> retrievedInformation = (ArrayList<String>) snapshot.getValue();
                    mListener.onSensorInformationRetrieved(retrievedInformation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
