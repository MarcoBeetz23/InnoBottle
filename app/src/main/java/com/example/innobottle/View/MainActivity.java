package com.example.innobottle.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.example.innobottle.Utils.UDPClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.example.innobottle.R;


public class MainActivity extends AppCompatActivity {

    String messageString;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            messageString = msg.toString();
            Log.d("message as string", messageString);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleUDP();
    }


    public void handleUDP() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[150];
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

}