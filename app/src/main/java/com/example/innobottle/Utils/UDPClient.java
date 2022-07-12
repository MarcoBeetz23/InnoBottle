package com.example.innobottle.Utils;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import com.example.innobottle.View.MainActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient extends Thread{

    private static final String IP = "192.168.178.59";
    private static final int PORT = 5700;
    private final MainActivity mainActivity;
    String udpIncomingData;
    private Handler handler;

    public UDPClient(MainActivity context, Handler handler){
        mainActivity = context;
        this.handler = handler;
    }

    public String getUdpData(){
        Log.d("test9", udpIncomingData);
        return udpIncomingData;
    }

    @Override
    public void run() {
        Log.d("hi123", "inside run");
        try {
            while(true){
                byte[] buffer = new byte[150];
                DatagramSocket socket = new DatagramSocket(PORT);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                int i = socket.getLocalPort();
                socket.receive(packet);
                String s = new String(packet.getData());
                udpIncomingData = s;
                Log.d("test8", "udpincoming data is " + udpIncomingData);
                Log.d("test1000", s);
                socket.close();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.d("test123", e.toString());
        } catch (SocketException e) {
            e.printStackTrace();
            Log.d("test123", e.toString());

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("test123", e.toString());
        }
    }
}
