package com.example.innobottle.View;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

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
    TextView tvSensorRunName, tvCustomer, tvDate, tvOperator, tvLocation, tvUnit1, tvUnit2, tvUnit3, tvUnit4, tvUnit5, tvUnit6, tvUnit7, tvUnit8, tvUnit9, tvUnit1red, tvUnit2red, tvUnit3red, tvUnit4red, tvUnit5red, tvUnit6red, tvUnit7red, tvUnit8red, tvUnit9red;
    Button btnNewRun, btnPause, btnSave, btnStart, btnDeleteSensorRun, btnSaveSensorRun, btnFinalDelete, btnCancelDelete;
    Dialog dialog, deleteDialog;
    ImageView greenCircle, cancelSaveProcess, cancelDeleteProcess;
    // Text views for load cell values
    TextView tvValue1, tvValue2, tvValue3, tvValue4, tvValue5, tvValue6, tvValue7, tvValue8, tvValue9;

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
        btnNewRun = findViewById(R.id.btn_new);
        btnPause = findViewById(R.id.btn_pause);
        btnSave = findViewById(R.id.btn_export);
        btnStart= findViewById(R.id.btn_start);
        greenCircle = findViewById(R.id.greenCircle);
        tvCustomer = findViewById(R.id.tv_customer);
        tvLocation = findViewById(R.id.tv_location);
        tvDate = findViewById(R.id.tv_date);
        tvOperator = findViewById(R.id.tv_operator);
        //units for load cell values
        tvUnit1 = findViewById(R.id.newton1);
        tvUnit2 = findViewById(R.id.newton2);
        tvUnit3 = findViewById(R.id.newton3);
        tvUnit4 = findViewById(R.id.newton4);
        tvUnit5 = findViewById(R.id.newton5);
        tvUnit6 = findViewById(R.id.newton6);
        tvUnit7 = findViewById(R.id.newton7);
        tvUnit8 = findViewById(R.id.newton8);
        tvUnit9 = findViewById(R.id.newton9);
        //red if warning
        tvUnit1red = findViewById(R.id.newton1red);
        tvUnit2red = findViewById(R.id.newton1red);
        tvUnit3red = findViewById(R.id.newton1red);
        tvUnit4red = findViewById(R.id.newton1red);
        tvUnit5red = findViewById(R.id.newton1red);
        tvUnit6red = findViewById(R.id.newton1red);
        tvUnit7red = findViewById(R.id.newton1red);
        tvUnit8red = findViewById(R.id.newton1red);
        tvUnit9red = findViewById(R.id.newton1red);
        //debug
        greenBottleImage = findViewById(R.id.greenBottle);
        //load Cell values text view
        tvValue1 = findViewById(R.id.cellValue1);
        tvValue2 = findViewById(R.id.cellValue2);
        tvValue3 = findViewById(R.id.cellValue3);
        tvValue4 = findViewById(R.id.cellValue4);
        tvValue5 = findViewById(R.id.cellValue5);
        tvValue6 = findViewById(R.id.cellValue6);
        tvValue7 = findViewById(R.id.cellValue7);
        tvValue8 = findViewById(R.id.cellValue8);
        tvValue9 = findViewById(R.id.cellValue9);
    }

    private void handleUserClicks(){
        handleStartClick();
        handleNewRunClick();
        handleSaveClick();
        handlePauseClick();
        switchScreen();
    }

    private void handleNewRunClick(){
        btnNewRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNewRun.setEnabled(false);
                btnPause.setEnabled(true);
                btnStart.setEnabled(true);
                btnSave.setEnabled(true);
                tvSensorRunName.setText("Sensor Run 1");
                Toast.makeText(MainActivity.this,
                        "A new sensor run has been created. Let's start!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleStartClick(){
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sensorRunIsReady()){
                    mPresenter.setActiveState();
                }
                //button change
                mPresenter.setPauseState();
                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                greenCircle.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this,
                        "Your measurement is running!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    private void handleSaveClick(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handleDialog();
                mPresenter.setReadyState();
                showDefault();
                removeUnits();
                // button change
                btnNewRun.setEnabled(true);
                btnStart.setEnabled(false);
                btnStart.setVisibility(View.VISIBLE);
                btnPause.setEnabled(false);
                btnPause.setVisibility(View.GONE);
                btnSave.setEnabled(false);
                greenCircle.setVisibility(View.GONE);
                tvSensorRunName.setText("Force Measurement");
                Toast.makeText(MainActivity.this,
                        "The sensor run has been terminated. Your data is saved automatically.",
                        Toast.LENGTH_LONG).show();
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

    private void handlePauseClick() {
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.setActiveState();
                btnStart.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
                greenCircle.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,
                        "Your measurement is paused.",
                        Toast.LENGTH_LONG).show();
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



    // The model and finally the presenter fetched data from Firebase and give it back to the Ui
    @Override
    public void onLoadCellValuesRetrieved(ArrayList<String> cellValues) {
        showLoadCellValues(cellValues);
        //showWarning(cellValues);
    }

    private void showLoadCellValues(ArrayList<String> list) {
        tvValue1.setText(list.get(0));
        tvValue2.setText(list.get(1));
        tvValue3.setText(list.get(2));
        tvValue4.setText(list.get(3));
        tvValue5.setText(list.get(4));
        tvValue6.setText(list.get(5));
        tvValue7.setText(list.get(6));
        tvValue8.setText(list.get(7));
        tvValue9.setText(list.get(8));

        // UI changes
        showUnits();
    }

    private void showUnits() {
        tvUnit1.setVisibility(View.VISIBLE);
        tvUnit2.setVisibility(View.VISIBLE);
        tvUnit3.setVisibility(View.VISIBLE);
        tvUnit4.setVisibility(View.VISIBLE);
        tvUnit5.setVisibility(View.VISIBLE);
        tvUnit6.setVisibility(View.VISIBLE);
        tvUnit7.setVisibility(View.VISIBLE);
        tvUnit8.setVisibility(View.VISIBLE);
        tvUnit9.setVisibility(View.VISIBLE);

        //warnings change color
        tvUnit1red.setVisibility(View.VISIBLE);
        tvUnit2red.setVisibility(View.VISIBLE);
        tvUnit3red.setVisibility(View.VISIBLE);
        tvUnit4red.setVisibility(View.VISIBLE);
        tvUnit5red.setVisibility(View.VISIBLE);
        tvUnit6red.setVisibility(View.VISIBLE);
        tvUnit7red.setVisibility(View.VISIBLE);
        tvUnit8red.setVisibility(View.VISIBLE);
        tvUnit9red.setVisibility(View.VISIBLE);
    }

    private void showDefault() {
        tvValue1.setText("-");
        tvValue2.setText("-");
        tvValue3.setText("-");
        tvValue4.setText("-");
        tvValue5.setText("-");
        tvValue6.setText("-");
        tvValue7.setText("-");
        tvValue8.setText("-");
        tvValue9.setText("-");
    }

    private void removeUnits() {
        tvUnit1.setVisibility(View.GONE);
        tvUnit2.setVisibility(View.GONE);
        tvUnit3.setVisibility(View.GONE);
        tvUnit4.setVisibility(View.GONE);
        tvUnit5.setVisibility(View.GONE);
        tvUnit6.setVisibility(View.GONE);
        tvUnit7.setVisibility(View.GONE);
        tvUnit8.setVisibility(View.GONE);
        tvUnit9.setVisibility(View.GONE);

        //warnings change color
        tvUnit1red.setVisibility(View.GONE);
        tvUnit2red.setVisibility(View.GONE);
        tvUnit3red.setVisibility(View.GONE);
        tvUnit4red.setVisibility(View.GONE);
        tvUnit5red.setVisibility(View.GONE);
        tvUnit6red.setVisibility(View.GONE);
        tvUnit7red.setVisibility(View.GONE);
        tvUnit8red.setVisibility(View.GONE);
        tvUnit9red.setVisibility(View.GONE);
    }

    //private void showWarning(ArrayList<String> list) {
        //if (new Integer(list.get(0)) > 0) {
            //tvValue1.setTextColor(Color.parseColor("#CF1D0E"));
        //}
    //}

    // other callback methods
}