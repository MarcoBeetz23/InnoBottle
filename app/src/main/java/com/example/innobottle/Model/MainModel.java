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
import com.google.firebase.database.ValueEventListener;

public class MainModel implements MainContract.Model {

    private MainContract.DataListener mListener;

    private FirebaseDatabase database = FirebaseDatabase.getInstance(FIREBASEPATH);
    private DatabaseReference refData, refState;

    private static final String FIREBASEPATH = "https://innolab-66e3b-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final String DATAPATH = "SensorData";
    private static final String STATEPATH = "State";
    private String readyState = "ready";
    private String activeState = "active";
    private String pauseState = "pause";

    public MainModel(MainContract.DataListener listener){
        this.mListener = listener;
    }


    @Override
    public void pauseBottleInFirebase() {

    }

    @Override
    public void connectToBottleInFirebase() {
        refState = database.getReference(STATEPATH);
        refState.setValue(readyState);
    }

    @Override
    public void initNewSensorRunInFirebase() {
        refState.setValue(activeState).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    mListener.onSensorRunInitialized();
                }
            }
        });
    }

    @Override
    public void pauseSensorRunInFirebase() {
        refState.setValue(readyState);
    }

    @Override
    public void deleteSensorRunInFirebase() {

    }

    @Override
    public void fetchValuesFromFirebase() {
        refData = database.getReference(DATAPATH);
        Log.d("test123", "before value event listener" + "---" + refData.toString());
        refData.addValueEventListener(new ValueEventListener() {
<<<<<<< Updated upstream
=======
        Log.d("test123", "hi");
        refData.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
>>>>>>> Stashed changes
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    Log.d("test300", String.valueOf(snapshot.getChildrenCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error occured", error.toString());
            }
        });


    }
}
