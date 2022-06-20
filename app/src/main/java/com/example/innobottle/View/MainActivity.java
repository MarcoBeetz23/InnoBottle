package com.example.innobottle.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.innobottle.Presenter.MainContract;
import com.example.innobottle.Presenter.MainPresenter;
import com.example.innobottle.R;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    /*
    This activity holds the major screen of the application
    The load cell values are presented, the business logic is manipulated by the UI on this screen
     */

    // Android components
    TextView tvSensorRunName;
    Button btnStartRun, btnPause, btnSave, btnDeleteSensorRun, btnSaveSensorRun;
    Dialog dialog;

    // Util constants
    private static final String DEFAULT_LINE_INFORMATION = "Customer Line Information";
    private static final String TOAST_ERROR_TEXT = "No Sensor Series has yet been initialized!";
    final Context context = this;
    String currentLineInformation;

    // MVP values
    String currentName;
    int currentCounter;

    // Architectural
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUIComponents();
        mainPresenter = new MainPresenter(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mainPresenter.pauseBottle();
    }

    @Override
    protected void onResume(){
        super.onResume();
        // When the activity is shown on screen, the bottle state is set to *init* by default.
        mainPresenter.connectToBottle();
        handleStartClick();
        handlePauseClick();
    }

    // init UI elements
    private void setupUIComponents(){
        setContentView(R.layout.activity_main);
        tvSensorRunName = findViewById(R.id.tvSensorRunName);
        btnStartRun = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnSave = findViewById(R.id.btn_export);
        btnDeleteSensorRun = findViewById(R.id.btn_deleteSensorRun);
        btnSaveSensorRun = findViewById(R.id.btn_exportSensorRun);
    }

    private void handleStartClick(){
        btnStartRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sensorRunIsReady()){
                    mainPresenter.initNewSensorRun(currentLineInformation);
                    Log.d("test123", "clicked button in activity");
                }
            }
        });
    }

    private void handlePauseClick(){
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.pauseCurrentSensorRun();
            }
        });
    }

    private void handleSaveClick(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDialog();
                mainPresenter.pauseCurrentSensorRun();
            }
        });
    }

    private void handleDialog(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.save_dialog);
        dialog.show();
        handleDialogButtons();
    }

    private void handleDialogButtons(){
        btnDeleteSensorRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.deleteCurrentSensorRun(currentName, currentCounter);
                dialog.dismiss();
            }
        });

        btnSaveSensorRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.saveCurrentSensorRun(currentName, currentCounter);
                dialog.dismiss();
            }
        });
    }

    // if no sensor series is initialized yet, the text view holds the default string
    // if the default string is presented, the sensor series is not yet ready
    // nothing will happen until user configures a sensor series properly
    private boolean sensorRunIsReady(){
        currentLineInformation = tvSensorRunName.getText().toString();
        if(currentLineInformation != DEFAULT_LINE_INFORMATION){
            return true;
        } else {
            Toast.makeText(this, TOAST_ERROR_TEXT, Toast.LENGTH_LONG);
            return false;
        }
    }

    // todo
    @Override
    public void onSensorSeriesFound(String name, int counter) {
        currentName = name;
        currentCounter = counter;
        handleSaveClick();
    }

    @Override
    public void onSensorRunActive() {

    }

    @Override
    public void onSensorRunInactive() {

    }
}