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
import android.widget.TextView;
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
    static final char VOITURE_AVANCER = '5';

    /**
     * Donner l'ordre de reculer à la voiture
     */
    static final char VOITURE_RECULER = '2';

    /**
     * Donner l'ordre de tourner à gauche à la voiture
     */
    static final char VOITURE_TOURNER_GAUCHE = '4';

    /**
     * Donner l'ordre de tourner à droite à la voiture
     */
    static final char VOITURE_TOURNER_DROITE = '3';

    /**
     * Donner l'ordre de s'arrêter à la voiture
     */
    static final char VOITURE_STOP = '6';

    /**
     * Adresse IP du robot (fixe, sert à lui envoyer les instructions)
     */
    String ipAddress = "192.168.0.10";
    /**
     * Port sur lequel communiquer
     */
    int port = 1444;


//    class MonThread implements Runnable {
//
//        String TAG = this.getClass().getName();
//
//        private char message;
//        Socket socket;
//        DataOutputStream SenderStream;
//
//        @Override
//        public void run() {
//            try {
//                socket = new Socket(ipAddress, port);
//                SenderStream = new DataOutputStream(socket.getOutputStream());
//
//                SenderStream.write(message);
//
//                SenderStream.close();
//                SenderStream.flush();
//                socket.close();
//            }
//            catch (IOException e) {
//                Log.e(TAG, e.toString());
//                throw new RuntimeException(e);
//            }
//        }
//
//        public void sendMessage(char message){
//            Log.d(TAG, "Sent " + message + " to socket");
//            this.message = message;
//            this.run();
//        }
//    }
    //MonThread TheThread;

    Intent intentToSocketHostService;

    // SocketHostService
    /**
     * Réf. au SocketHostService
     */
    SocketHostService socketHostService;
    /**
     * Indique l'état de la connexion au service
     */
    boolean hasBinding = false;
    /**
     * Interface permettant le binding
     */
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SocketHostService.myBinder binder = (SocketHostService.myBinder) service;
            socketHostService = binder.getService();
            hasBinding = true;

            Log.d(TAG, "onServiceConnected: Binded succesfully");

            boolean hasOpenedSocket = socketHostService.openSocket();

            Log.d(TAG, "onServiceConnected: Socket is open ? --> " + hasOpenedSocket);

            if (!hasOpenedSocket)
                ((TextView) findViewById(R.id.errorIndicatorLabel)).setText(getText(R.string.socket_non_ouvert));

            else
                ((TextView) findViewById(R.id.errorIndicatorLabel)).setText("");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            hasBinding = false;
            Log.w(TAG, "onServiceDisconnected: Service was disconnected abruptly");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controler_plateforme);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //TheThread = new MonThread();

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

        // Déclaration de l'intent vers le service
        intentToSocketHostService = new Intent(this, SocketHostService.class);

        // Binding to service
        Log.d(TAG, "onCreate: Attempting binding to Service");
        bindService(intentToSocketHostService, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "onCreate: Binding successfully called");

    }

    @Override
    protected void onDestroy() {

        if (hasBinding)
            unbindService(serviceConnection);

        hasBinding = false;
        socketHostService = null;

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


    public void ObtenirMasse(View view) {

        // TODO: 15/04/2024 implement function to obtain the mass

//        socketHostService.debugMsg();
    }

    View.OnTouchListener DirectionButtonsListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            String id = v.getResources().getResourceEntryName(v.getId());

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(TAG, "Bouton " + id + " : ACTION_DOWN");

                    // Envoie à la voiture le signal de direction correspondant au bouton pressé
                    switch (id){
                        case "boutonHaut":
                            socketHostService.sendChar(VOITURE_AVANCER);
                            break;

                        case "boutonBas":
                            socketHostService.sendChar(VOITURE_RECULER);
                            break;

                        case "boutonGauche":
                            socketHostService.sendChar(VOITURE_TOURNER_GAUCHE);
                            break;

                        case "boutonDroite":
                            socketHostService.sendChar(VOITURE_TOURNER_DROITE);
                            break;
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    socketHostService.sendChar(VOITURE_STOP);
                    break;
            }
            return true;
        }
    };
}