package com.example.innobottle.Presenter;

import java.util.ArrayList;

public interface LineInformationContract {

    interface View{
        void onSuccess();
    }

    interface Presenter{
        void sendData(String[] data);
    }

    interface Model{
        void sendDataToFirebase(String[] data);
    }

    interface DataListener{
        void onSuccessfullyCreated();
    }
}
