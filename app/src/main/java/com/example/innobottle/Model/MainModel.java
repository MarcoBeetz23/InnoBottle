package com.example.innobottle.Model;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import com.example.innobottle.Entities.DataRow;
import com.example.innobottle.Presenter.MainContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

    private static final int LOAD_CELL_COUNTER = 9;

    public MainModel(MainContract.DataListener listener) {
        this.mListener = listener;
    }

    @Override
    public void setReadyStateInFirebase() {
        refState = database.getReference(STATEPATH);
        refState.setValue(READYSTATE);
        mListener.onReadyStateInitialized();
    }

    @Override
    public void setActiveStateInFirebase() {
        refState = database.getReference(STATEPATH);
        refState.setValue(ACTIVESTATE);
        mListener.onActiveStateInitialized(ACTIVESTATE);
    }

    @Override
    public void setPauseStateInFirebase() {
        refState = database.getReference(STATEPATH);
        refState.setValue(PAUSESTATE);
        mListener.onPauseStateInitialized(PAUSESTATE);
    }

    @Override
    public void startDataTransmissionToFirebase(DataRow row, String time) {
        refData = database.getReference(DATAPATH);
        refData.child(time).child(row.getMillis()).setValue(row);
    }

    @Override
    public void retrieveSensorInformationInFirebase() {

    }
}