package com.example.innobottle.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.innobottle.R;

public class MainActivity extends AppCompatActivity {


    /*
    This Activity holds the major screen of the application and is used during a sensor run
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}