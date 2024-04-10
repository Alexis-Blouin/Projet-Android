package com.tractoteam.tractopel;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class SocketHostService extends Service {

    String TAG = this.getClass().getName();

    final String ipAddress = "192.168.0.10";
    final int port = 1444;


    public class myBinder extends Binder {
        SocketHostService getService() {
            return SocketHostService.this;
        }
    }
    myBinder binder = new myBinder();


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


}