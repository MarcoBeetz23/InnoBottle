package com.example.innobottle.Presenter;

import android.util.Log;

import com.example.innobottle.Entities.DataRow;
import com.example.innobottle.Entities.GraphRow;
import com.example.innobottle.Model.MainModel;

import java.lang.reflect.Array;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainPresenter implements MainContract.Presenter, MainContract.DataListener{

    private MainContract.View mView;
    private MainModel mModel;

    private boolean stateIsActive = false;
    private String currentState;
    private static final String READYSTATE = "ready";
    private static final String ACTIVESTATE = "active";
    String time;

    private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public MainPresenter(MainContract.View mainView){
        this.mView = mainView;
        mModel = new MainModel(this);
    }

    // algorithm to extract raw data of load cells coming from message string
    // parameter is s, the incoming message string
    private ArrayList<String> normalizeData(String s){

        // declare a string
        String removedHeadAndTail;

        // remove the beginning of the message by splitting it at "obj="
        String[] dropHead = s.split("obj=");

        // remove the overflow buffer strings that are not recognized
        // the buffer string will look like this: "�"
        // but you cannot find a regex to look for "�"
        // look for a character that is neither a digit, character, a comma, point, or minus
        String[] dropTail = dropHead[1].split("[^a-z, 0-9, .,-]");

        // removeHeadandTail now holds the desired values, but it is still a simple string
        removedHeadAndTail = dropTail[0];

        // split the string again at the "," and wrap it into a string array
        String[] splittedString = removedHeadAndTail.split(",");

        // convert the string array to a list and finally an array list
        List<String> list = Arrays.asList(splittedString);

        ArrayList<String> values = new ArrayList<String>(list);
        // return the ArrayList with the desired values. Index 0-9 holds the load cell values

        return values;
    }

    @Override
    public void handleRawData(String s) {
        if(stateIsActive){
            ArrayList<String> stringData = normalizeData(s);
            String millis = stringData.get(stringData.size()-1);
            stringData.remove(stringData.size()-1);
            DataRow dataRow = new DataRow(stringData, millis);
            GraphRow graphRow = convertDataForGraph(dataRow);
            Log.d("test123", graphRow.toString());
            Log.d("test124", graphRow.getRingValues().toString());
            mView.startDataRetrieval(dataRow);
            mModel.startDataTransmissionToFirebase(dataRow, time);
        }
    }

    private GraphRow convertDataForGraph(DataRow row){
        ArrayList<Float> bottomRow = row.bottomRow();
        Float highestValue = row.highestValue(bottomRow);
        GraphRow graphRow = new GraphRow(1);
        Float[] highestValues = new Float[1];
        highestValues[0] = highestValue;
        graphRow.setRingValues(highestValues);
        return graphRow;
    }

    private String getCurrentTime(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf3.format(timestamp);
    }

    @Override
    public void setReadyState() {
        mModel.setReadyStateInFirebase();
    }

    @Override
    public void setActiveState() {
        mModel.setActiveStateInFirebase();
    }

    @Override
    public void setPauseState() {
        mModel.setPauseStateInFirebase();
    }

    @Override
    public void onReadyStateInitialized() {
        stateIsActive = false;
    }

    @Override
    public void onActiveStateInitialized(String state) {
        currentState = state;
        stateIsActive = true;
        time = getCurrentTime();
    }

    @Override
    public void onPauseStateInitialized(String state) {
        currentState = state;
        stateIsActive = false;
    }
}
