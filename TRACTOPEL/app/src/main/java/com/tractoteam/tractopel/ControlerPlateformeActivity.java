package com.tractoteam.tractopel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ControlerPlateformeActivity extends AppCompatActivity {

    String TAG = this.getClass().getName();

    Toolbar m_toolbar;

    Button boutonDroite;
    Button boutonGauche;
    Button boutonHaut;
    Button boutonBas;

    /**
     * Donner l'ordre d'avancer à la voiture
     */
    static final int VOITURE_AVANCER = 5;
    /**
     * Donner l'ordre de reculer à la voiture
     */
    static final int VOITURE_RECULER = 2;
    /**
     * Donner l'ordre de tourner à gauche à la voiture
     */
    static final int VOITURE_TOURNER_GAUCHE = 4;
    /**
     * Donner l'ordre de tourner à droite à la voiture
     */
    static final int VOITURE_TOURNER_DROITE = 3;
    /**
     * Donner l'ordre de s'arrêter à la voiture
     */
    static final int VOITURE_STOP = 6;


    Socket socket;
    String ipAddress = "192.168.0.10";
    int port = 1444;

    class MonThread implements Runnable {

        String TAG = this.getClass().getName();

        private char message;
        Socket socket;
        DataOutputStream SenderStream;

        @Override
        public void run() {
            try {
                socket = new Socket(ipAddress, port);
                SenderStream = new DataOutputStream(socket.getOutputStream());

                SenderStream.write(message);

                SenderStream.close();
                SenderStream.flush();
                socket.close();
            }
            catch (IOException e) {
                Log.e(TAG, e.toString());
                throw new RuntimeException(e);
            }
        }

        public void sendMessage(char message){
            Log.d(TAG, "Sent " + message + " to socket");
            this.message = message;
            this.run();
        }
    }
    MonThread TheThread;

    Intent intentToSocketHostService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controler_plateforme);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TheThread = new MonThread();

        Log.d(TAG, "onCreate() from "+TAG);

        boutonDroite = (Button) findViewById(R.id.boutonDroite);
        boutonDroite.setOnTouchListener(DirectionButtonsListener);
        boutonGauche = (Button) findViewById(R.id.boutonGauche);
        boutonGauche.setOnTouchListener(DirectionButtonsListener);
        boutonHaut = (Button) findViewById(R.id.boutonHaut);
        boutonHaut.setOnTouchListener(DirectionButtonsListener);
        boutonBas = (Button) findViewById(R.id.boutonBas);
        boutonBas.setOnTouchListener(DirectionButtonsListener);

        //Ajout de m_toolbar
        m_toolbar = (Toolbar) findViewById(R.id.toolbar);
        m_toolbar.setTitle("");
        setSupportActionBar(m_toolbar);
        m_toolbar.setTitle(R.string.titre_activite_controle_plateforme);
        m_toolbar.setTitleTextColor(Color.WHITE);

        intentToSocketHostService = new Intent(this, SocketHostService.class);
//        startService(intentToSocketHostService);

//        Toast.makeText(this, "OnCreate Activity", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {

//        Toast.makeText(this, "OnDestroy Activity", Toast.LENGTH_SHORT).show();

//        unbindService(mServiceConnection);
//        stopService(intentToSocketHostService);

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.boutonChangerControles :
                startActivity(new Intent(this, ControlerBrasActivity.class));
                break;
            case R.id.boutonRetourAuTest :
                startActivity(new Intent(this, TestConnexionActivity.class));
                break;
        }
        return true;
    }


    // ###########################################
    // ################## DEBUG ##################
    // ###########################################

    SocketHostService socketHostService;
    boolean isConnected = false;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.w(TAG, "dans on service connected");
            SocketHostService.myBinder binder = (SocketHostService.myBinder) service;
            socketHostService = binder.getService();
            isConnected = true;

            Toast.makeText(ControlerPlateformeActivity.this, "", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected = false;
            Log.w(TAG, "onServiceDisconnected: Service was disconnected abruptly");
        }
    };
    Intent intent = new Intent(ControlerPlateformeActivity.this, SocketHostService.class);


    View.OnTouchListener DirectionButtonsListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            String id = v.getResources().getResourceEntryName(v.getId());

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(TAG, "Bouton " + id + " : ACTION_DOWN");

                    // Envoyer à la voiture le signal de direction correspondant au bouton pressé
                    switch (id){
                        case "boutonHaut":
                            Log.w(TAG, "juste avant bind service");
                            bindService(intentToSocketHostService, serviceConnection, Context.BIND_AUTO_CREATE);
                            Toast.makeText(ControlerPlateformeActivity.this, "Binded to service from Voiture", Toast.LENGTH_SHORT).show();
                            break;

                        case "boutonBas":
                            if (!isConnected) {
                                Toast.makeText(ControlerPlateformeActivity.this, "Was not binded from Voiture",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            }                            unbindService(serviceConnection);
                            Toast.makeText(ControlerPlateformeActivity.this, "Unbinded to service from voiture", Toast.LENGTH_SHORT).show();
                            isConnected = false;
                            socketHostService = null;
                            break;

                        case "boutonGauche":
                            socketHostService.debugTestFunc();
                            break;

                        case "boutonDroite":
                            Toast.makeText(ControlerPlateformeActivity.this, "Has service : "+isConnected,
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    // TODO : Envoyer le signal d'arrêt à la voiture
                    break;
            }
            return true;
        }
    };
}