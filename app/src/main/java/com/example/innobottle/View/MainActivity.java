package com.example.innobottle.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.innobottle.Presenter.MainContract;
import com.example.innobottle.Presenter.MainPresenter;
import com.example.innobottle.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    /*
    This activity holds the major screen of the application
    The load cell values are presented, the business logic is manipulated by the UI on this screen
     */

    // Android components
    TextView tvSensorRunName, tvCustomer, tvDate, tvOperator, tvLocation;
    Button btnStartRun, btnPause, btnSave, btnResume, btnDeleteSensorRun, btnSaveSensorRun, btnFinalDelete, btnCancelDelete;
    Dialog dialog, deleteDialog;
    ImageView greenCircle, cancelSaveProcess, cancelDeleteProcess;

    //debug
    ImageView greenBottleImage;


    // Util constants
    private static final String DEFAULT_LINE_INFORMATION = "Customer Line Information";
    private static final String TOAST_ERROR_TEXT = "No Sensor Series has yet been initialized!";
    final Context context = this;
    String currentLineInformation;

    // Architectural
    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setupUIComponents();
        mPresenter = new MainPresenter(this);
        mPresenter.setReadyState();
    }


    @Override
    protected void onPause(){
        super.onPause();
        mPresenter.setPauseState();
    }

    @Override
    protected void onResume(){
        super.onResume();
        // When the activity is shown on screen, the bottle state is set to *ready* by default.
        mPresenter.setReadyState();
        mPresenter.retrieveSensorInformation();
        handleUserClicks();
    }

    private void setupUIComponents(){
        setContentView(R.layout.activity_main);
        tvSensorRunName = findViewById(R.id.tvSensorRunName);
        btnStartRun = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnSave = findViewById(R.id.btn_export);
        btnResume = findViewById(R.id.btn_resume);
        greenCircle = findViewById(R.id.greenCircle);
        tvCustomer = findViewById(R.id.tv_customer);
        tvLocation = findViewById(R.id.tv_location);
        tvDate = findViewById(R.id.tv_date);
        tvOperator = findViewById(R.id.tv_operator);
        //debug
        greenBottleImage = findViewById(R.id.greenBottle);
    }

    private void handleUserClicks(){
        handleStartClick();
        handlePauseClick();
        handleSaveClick();
        handleResumeClick();
        switchScreen();
    }

    private void handleStartClick(){
        btnStartRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sensorRunIsReady()){
                    mPresenter.setActiveState();
                }
                //button change
                btnStartRun.setEnabled(false);
                btnPause.setEnabled(true);
                btnResume.setEnabled(true);
                btnSave.setEnabled(true);
                greenCircle.setVisibility(View.VISIBLE);
            }
        });
    }

    private void handlePauseClick(){
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.setPauseState();
                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.VISIBLE);
                greenCircle.setVisibility(View.GONE);
            }
        });
    }


    private void handleSaveClick(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDialog();
                mPresenter.setPauseState();
                // button change
                btnStartRun.setEnabled(true);
                btnPause.setEnabled(false);
                btnResume.setEnabled(false);
                btnSave.setEnabled(false);
                greenCircle.setVisibility(View.GONE);
            }
        });
    }


    /////////////////////////////////////////////////////////
    // This section represents internal handling of the dialog
    private void handleDialog(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.save_dialog);
        btnDeleteSensorRun = dialog.findViewById(R.id.btn_deleteSensorRun);
        btnSaveSensorRun = dialog.findViewById(R.id.btn_exportSensorRun);
        cancelSaveProcess = dialog.findViewById(R.id.cancel_saveProcess);
        dialog.show();
        handleDialogButtons();
        closeDialog();
    }

    private void closeDialog(){
        cancelSaveProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                // backend stuff
                // what should be done with sensor run when closing the dialog without clicking the buttons? should it be on pause?
            }
        });
    }

    private void handleDialogButtons() {
        btnDeleteSensorRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Delete dialog
                handleDeleteDialog();
            }
        });
    }

    private void handleResumeClick() {
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.setActiveState();
                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                greenCircle.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this,
                        "Your measurement is running again!",
                        Toast.LENGTH_LONG).show(); // muss wahrscheinlich dann in eine andere Funktion rein
            }
        });
    }

    private void handleDeleteDialog(){
        deleteDialog = new Dialog(context);
        deleteDialog.setContentView(R.layout.delete_dialog);
        btnFinalDelete = deleteDialog.findViewById(R.id.btn_finalDelete);
        btnCancelDelete = deleteDialog.findViewById(R.id.btn_cancelDelete);
        cancelDeleteProcess = deleteDialog.findViewById(R.id.cancel_deleteProcess);
        deleteDialog.show();
        handleDeleteDialogButtons();
    }

    private void handleDeleteDialogButtons(){
        btnFinalDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.deleteSensorRun();
                deleteDialog.dismiss();
            }
        });

        btnCancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });

        cancelDeleteProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });
    }
    ////////////////////////////////////////////////////////////////


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


    // switching the screen, may be adapted later, debug mode
    private void switchScreen(){
        greenBottleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LineInformationActivity.class);
                startActivity(i);
            }
        });
    }

    /// finally, the retrieved meta data about sensor information is represented on the screen
    @Override
    public void onInformationRetrieved(ArrayList<String> data) {
        String customerText = data.get(0);
        String operatorText = data.get(4);
        String locationText = data.get(3);
        String dateText = data.get(2);

        tvDate.setText(dateText);
        tvCustomer.setText(customerText);
        tvLocation.setText(locationText);
        tvOperator.setText(operatorText);
    }

    // other callback methods
}