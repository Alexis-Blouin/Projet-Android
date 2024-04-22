package com.tractoteam.tractopel;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class SocketHostService extends Service {

    String TAG = this.getClass().getName();

    boolean isOpen = false;

    final String ipAddress = "192.168.0.10";
    final int port = 1444;

    Socket socket;
    DataOutputStream SenderStream;


    public class myBinder extends Binder {
        SocketHostService getService() {
            return SocketHostService.this;
        }
    }
    myBinder binder = new myBinder();

    boolean openSocket() {

        if (isOpen)
            return isOpen;

        try {
            socket = new Socket();

//            ipAddress, port

            socket.connect(new InetSocketAddress(ipAddress, port), 5000);

            SenderStream = new DataOutputStream(socket.getOutputStream());

            isOpen = true;
            return isOpen;
        }
        catch (SocketTimeoutException e) {
            Log.wtf(TAG, e);
            return isOpen;
        }

        catch (IOException e){
            Log.wtf(TAG, e);
            return isOpen;
        }
    }

    void closeSocket() {
        if (!isOpen)
            return;

        try {
            SenderStream.close();

            socket.close();

            isOpen = false;
        }
        catch (IOException e) {
            Log.wtf(TAG, e);
        }

    }

    void sendChar(char c) {
        if (isOpen)
            try {
                SenderStream.write(c);

                SenderStream.flush();
            }
            catch (IOException e) {
                Log.wtf(TAG, e);
            }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(this, "Created service", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        closeSocket();

        super.onDestroy();

        Toast.makeText(this, "Destroyed service", Toast.LENGTH_SHORT).show();
    }

    public void debugTestFunc() {
        Toast.makeText(this, "Test successful", Toast.LENGTH_SHORT).show();
    }

    void EnvoyerCommandeVoiture(char chr){
        Log.d(TAG, "EnvoyerCommandeVoiture: "+chr);

        // TODO : Faire l'envoi du message dans le socket
    }

    boolean debugFrwd = false;
    void debugMsg() {
        if (!debugFrwd) {
            Log.d(TAG, "debugMsg: "+ControlerPlateformeActivity.VOITURE_AVANCER);

            sendChar(ControlerPlateformeActivity.VOITURE_AVANCER);

            debugFrwd = true;
        }
        else {
            Log.d(TAG, "debugMsg: "+ControlerPlateformeActivity.VOITURE_STOPPER);

            sendChar(ControlerPlateformeActivity.VOITURE_STOPPER);

            debugFrwd = false;
        }
    }


}