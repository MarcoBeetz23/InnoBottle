package com.example.innobottle.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.innobottle.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LineInformationActivity extends AppCompatActivity {

    /*
    This activity is used for configuring a sensor series or sensor run
     */

    TextInputEditText mTitle, mCustomer, mDate, mLocation, mOperator;
    Button btnStart;

    // constants
    private final static String TITLE = "ViaLink";
    private static final String CUSTOMER = "Krones intern";
    private static final String LOCATION = "Technikum";
    private static final String OPERATOR = "InnoLab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUIComponents();
    }

    private void setupUIComponents(){
        setContentView(R.layout.activity_line_information);
        mTitle = (TextInputEditText) findViewById(R.id.line_information_title);
        mCustomer = (TextInputEditText) findViewById(R.id.line_information_customer);
        mDate = (TextInputEditText) findViewById(R.id.line_information_date);
        mLocation = (TextInputEditText) findViewById(R.id.line_information_location);
        mOperator = (TextInputEditText) findViewById(R.id.line_information_operator);
        btnStart = (Button) findViewById(R.id.btn_startSeries);
    }

    @Override
    protected void onResume(){
        super.onResume();
        fillValues();
    }

    private void fillValues(){
        String currentDate = getCurrentDate();
        mTitle.setText(TITLE);
        mCustomer.setText(CUSTOMER);
        mDate.setText(currentDate);
        mLocation.setText(LOCATION);
        mOperator.setText(OPERATOR);
    }

    private String getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now).toString();
        return date;
    }
}