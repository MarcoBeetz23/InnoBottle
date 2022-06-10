package com.example.innobottle.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.innobottle.Presenter.MainContract;
import com.example.innobottle.Presenter.MainPresenter;
import com.example.innobottle.R;

public class MainActivity extends AppCompatActivity implements MainContract.View {


    /*
    This Activity holds the major screen of the application and is used during a sensor run
     */

    // Android components
    TextView tvcustomerLineInformation;
    Button btnStartRun, btnStop, btnExport;

    // Util constants
    private static final String DEFAULT_LINE_INFORMATION = "Customer Line Information";
    private static final String TOAST_ERROR_TEXT = "No Sensor Series has yet been initialized!";

    // Architectural
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUIComponents();
        mainPresenter = new MainPresenter(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        handleStartClick();
    }

    // init UI elements
    private void setupUIComponents(){
        setContentView(R.layout.activity_main);
        tvcustomerLineInformation = findViewById(R.id.tv_customer_line_information);
        btnStartRun = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
        btnExport = findViewById(R.id.btn_export);
    }

    private void handleStartClick(){
        btnStartRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sensorRunIsReady()){

                }
            }
        });
    }

    // if no sensor series is not yet initialized, the text view holds the default string
    // if the default string is presented, the sensor series is not yet ready
    // nothing will happen until user configures a sensor series properly
    private boolean sensorRunIsReady(){
        String currentLineInformation = tvcustomerLineInformation.getText().toString();
        if(currentLineInformation != DEFAULT_LINE_INFORMATION){
            return true;
        } else {
            Toast.makeText(this, TOAST_ERROR_TEXT, Toast.LENGTH_LONG);
            return false;
        }
    }

    // todo

    @Override
    public void onSensorSeriesFound() {

    }

    @Override
    public void onSensorRunActive() {

    }

    @Override
    public void onSensorRunInactive() {

    }
}