package com.example.innobottle.Presenter;

import android.util.Log;

import com.example.innobottle.Model.LineInformationModel;

import java.util.ArrayList;

public class LineInformationPresenter implements LineInformationContract.Presenter, LineInformationContract.DataListener {

    private LineInformationModel mModel;
    private LineInformationContract.DataListener mListener;
    private LineInformationContract.View viewContract;

    public LineInformationPresenter(LineInformationContract.View viewContract){
        this.viewContract = viewContract;
        mModel = new LineInformationModel(this);
    }

    @Override
    public void sendData(String[] data) {
        mModel.sendDataToFirebase(data);
    }


    @Override
    public void onSuccessfullyCreated() {
        Log.d("test123", "hi");
        viewContract.onSuccess();
    }
}
