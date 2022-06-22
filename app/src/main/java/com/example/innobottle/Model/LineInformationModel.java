package com.example.innobottle.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.innobottle.Presenter.LineInformationContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class LineInformationModel implements LineInformationContract.Model {

    private static final String FIREBASEPATH = "https://innolab-66e3b-default-rtdb.europe-west1.firebasedatabase.app/";

    // Firebase
    private FirebaseDatabase database = FirebaseDatabase.getInstance(FIREBASEPATH);
    private DatabaseReference refInformation = database.getReference("SensorInformation");
    private LineInformationContract.DataListener mListener;


    public LineInformationModel(LineInformationContract.DataListener dataListener){
        this.mListener = dataListener;
    }

    @Override
    public void sendDataToFirebase(String[] data) {
        refInformation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<String> arrList = new ArrayList<>(Arrays.asList(data));
                refInformation.setValue(arrList).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(mListener == null){
                                Log.d("test123", "listener ist null");
                            } else {
                                Log.d("test123", mListener.toString());
                            }
                            mListener.onSuccessfullyCreated();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

}