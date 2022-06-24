package com.example.innobottle.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.innobottle.Presenter.LineInformationContract;
import com.example.innobottle.Presenter.LineInformationPresenter;
import com.example.innobottle.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LineInformationActivity extends AppCompatActivity implements LineInformationContract.View {

    /*
    This activity is used for configuring a sensor series or sensor run
     */

    TextInputEditText mTitle, mCustomer, mDate, mLocation, mOperator;
    Button btnStart;

    private LineInformationPresenter mPresenter;

    // constants
    private final static String TITLE = "ViaLink";
    private static final String CUSTOMER = "Krones intern";
    private static final String LOCATION = "Technikum";
    private static final String OPERATOR = "InnoLab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUIComponents();
        mPresenter = new LineInformationPresenter(this);
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
        handleButtonClick();
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

    private String[] getCurrentData(){
        String title = mTitle.getText().toString();
        String customer = mCustomer.getText().toString();
        String date = mDate.getText().toString();
        String location = mLocation.getText().toString();
        String operator = mOperator.getText().toString();
        String[] data = {title, customer, date, location, operator};
        return data;
    }

    private void handleButtonClick(){
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] currentData = getCurrentData();
                Log.d("test123", "button first fired!");
                mPresenter.sendData(currentData);
                onSuccess();
            }
        });
    }

    @Override
    public void onSuccess() {
        Intent i = new Intent(LineInformationActivity.this, MainActivity.class);
        startActivity(i);
        Log.d("test123", "finally, intent done!");
    }
}