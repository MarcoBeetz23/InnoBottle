package com.example.innobottle.View;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.innobottle.Entities.DataRow;
import com.example.innobottle.Presenter.MainContract;
import com.example.innobottle.Presenter.MainPresenter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import com.example.innobottle.R;


public class MainActivity extends AppCompatActivity implements MainContract.View {

    // Android components
    TextView tvSensorRunName, tvCustomer, tvDate, tvOperator, tvLocation, tvUnit1, tvUnit2, tvUnit3, tvUnit4, tvUnit5, tvUnit6, tvUnit7, tvUnit8, tvUnit9, tvUnit1red, tvUnit2red, tvUnit3red, tvUnit4red, tvUnit5red, tvUnit6red, tvUnit7red, tvUnit8red, tvUnit9red;
    Button btnNewRun, btnPause, btnSave, btnStart, btnDeleteSensorRun, btnSaveSensorRun, btnFinalDelete, btnCancelDelete;
    Dialog dialog, deleteDialog;
    ImageView greenCircle, cancelSaveProcess, cancelDeleteProcess;
    // Text views for load cell values
    TextView tvValue1, tvValue2, tvValue3, tvValue4, tvValue5, tvValue6, tvValue7, tvValue8, tvValue9;

    /// refactor the TextView variables to Arrays for reducing duplicate code
    TextView[] loadCellValues = new TextView[9];


    //debug
    ImageView greenBottleImage;
    final Context context = this;

    String messageString;
    ArrayList<String> dataRowsForTextViews = new ArrayList<>();
    private MainPresenter mPresenter;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            messageString = msg.toString();
            mPresenter.handleRawData(messageString);
            Log.d("message as string", messageString);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUIComponents();
        mPresenter = new MainPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.setReadyState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleUDP();
        handleUserClicks();
    }

    private void setupUIComponents() {
        setContentView(R.layout.activity_main);
        tvSensorRunName = findViewById(R.id.tvSensorRunName);
        btnNewRun = findViewById(R.id.btn_new);
        btnPause = findViewById(R.id.btn_pause);
        btnSave = findViewById(R.id.btn_export);
        btnStart = findViewById(R.id.btn_start);
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
        // converted into array
        loadCellValues[0] = findViewById(R.id.cellValue1);
        loadCellValues[1] = findViewById(R.id.cellValue2);
        loadCellValues[2] = findViewById(R.id.cellValue3);
        loadCellValues[3] = findViewById(R.id.cellValue4);
        loadCellValues[4] = findViewById(R.id.cellValue5);
        loadCellValues[5] = findViewById(R.id.cellValue6);
        loadCellValues[6] = findViewById(R.id.cellValue7);
        loadCellValues[7] = findViewById(R.id.cellValue8);
        loadCellValues[8] = findViewById(R.id.cellValue9);


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

    private void handleUserClicks() {
        handleStartClick();
        handleNewRunClick();
        handleSaveClick();
        handlePauseClick();
        switchScreen();
    }

    private void handleNewRunClick() {
        btnNewRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNewRun.setEnabled(false);
                btnPause.setEnabled(true);
                btnStart.setEnabled(true);
                btnSave.setEnabled(true);
                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                greenCircle.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this,
                        "Your measurement is running!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleStartClick() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo - firebase get current state
                mPresenter.setActiveState();
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


    private void handleSaveClick() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    private void handleDialog() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.save_dialog);
        btnDeleteSensorRun = dialog.findViewById(R.id.btn_deleteSensorRun);
        btnSaveSensorRun = dialog.findViewById(R.id.btn_exportSensorRun);
        cancelSaveProcess = dialog.findViewById(R.id.cancel_saveProcess);
        dialog.show();
        handleDialogButtons();
        closeDialog();
    }

    private void closeDialog() {
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
                mPresenter.setPauseState();
                btnStart.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
                greenCircle.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,
                        "Your measurement is paused.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleDeleteDialogButtons() {
        btnFinalDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


    private void handleDeleteDialog() {
        deleteDialog = new Dialog(context);
        deleteDialog.setContentView(R.layout.delete_dialog);
        btnFinalDelete = deleteDialog.findViewById(R.id.btn_finalDelete);
        btnCancelDelete = deleteDialog.findViewById(R.id.btn_cancelDelete);
        cancelDeleteProcess = deleteDialog.findViewById(R.id.cancel_deleteProcess);
        deleteDialog.show();
        handleDeleteDialogButtons();
    }

    private void switchScreen() {
        greenBottleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LineInformationActivity.class);
                startActivity(i);
            }
        });
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

    public void handleUDP() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[70];
                DatagramSocket socket = null;
                try {
                    while(true){
                        socket = new DatagramSocket(5700);
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        String s = new String(packet.getData());
                        socket.close();
                        handler.obtainMessage(0, s).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    @Override
    public void startDataRetrieval(DataRow row) {
        dataRowsForTextViews = row.getDataRow();
        showDataOnScreen();
    }

    private void showDataOnScreen(){
        for(int i = 0 ; i < dataRowsForTextViews.size() ; i++){
            loadCellValues[i].setText(dataRowsForTextViews.get(i));
        }
    }

    @Override
    public void pauseDataRetrieval() {

    }
}