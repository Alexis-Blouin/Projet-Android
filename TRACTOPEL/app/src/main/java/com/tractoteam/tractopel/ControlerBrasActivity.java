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

public class ControlerBrasActivity extends AppCompatActivity {

    static final char BRAS_OUVRIR_PINCE =           'f';
    static final char BRAS_FERMER_PINCE =           'F';
    static final char BRAS_TOURNER_PINCE_GAUCHE =   'e';
    static final char BRAS_TOURNER_PINCE_DROITE =   'E';
    static final char BRAS_AXE_3_GAUCHE =           'D';
    static final char BRAS_AXE_3_DROITE =           'd';
    static final char BRAS_AXE_4_GAUCHE =           'c';
    static final char BRAS_AXE_4_DROITE =           'C';
    static final char BRAS_AXE_5_GAUCHE =           'B';
    static final char BRAS_AXE_5_DROITE =           'b';
    static final char BRAS_AXE_6_GAUCHE =           'A';
    static final char BRAS_AXE_6_DROITE =           'a';
    static final char BRAS_STOPPER =                '6';
    static final char BRAS_METTRE_EN_POSITION =     '6';
    static final char BRAS_DEMANDER_MASSE =         '6';


    String TAG = this.getClass().getSimpleName();

    Toolbar m_toolbar;

    Button btnSerrerPince;
    Button btnOuvrirPince;
    Button btnTournerPinceGauche;
    Button btnTournerPinceDroite;
    Button btn3Gauche;
    Button btn3Droite;
    Button btn4Gauche;
    Button btn4Droite;
    Button btn5Gauche;
    Button btn5Droite;
    Button btn6Gauche;
    Button btn6Droite;

    View.OnTouchListener MyOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    switch (v.getId())
                    {
                        case R.id.btnFermerPince:
                            Log.d(TAG, "onTouch: btnSerrerPince");
                            socketHostService.sendChar(BRAS_FERMER_PINCE);
                            break;
                        case R.id.btnOuvrirPince:
                            Log.d(TAG, "onTouch: btnOuvrirPince");
                            socketHostService.sendChar(BRAS_OUVRIR_PINCE);
                            break;
                        case R.id.btnTournerPinceGauche:
                            Log.d(TAG, "onTouch: btnTournerPinceGauche");
                            socketHostService.sendChar(BRAS_TOURNER_PINCE_GAUCHE);
                            break;
                        case R.id.btnTournerPinceDroite:
                            Log.d(TAG, "onTouch: btnTournerPinceDroite");
                            socketHostService.sendChar(BRAS_TOURNER_PINCE_DROITE);
                            break;
                        case R.id.Axe3Gauche:
                            Log.d(TAG, "onTouch: Axe3Gauche");
                            socketHostService.sendChar(BRAS_AXE_3_GAUCHE);
                            break;
                        case R.id.Axe3Droite:
                            Log.d(TAG, "onTouch: Axe3Droite");
                            socketHostService.sendChar(BRAS_AXE_3_DROITE);
                            break;
                        case R.id.Axe4Gauche:
                            Log.d(TAG, "onTouch: Axe4Gauche");
                            socketHostService.sendChar(BRAS_AXE_4_GAUCHE);
                            break;
                        case R.id.Axe4Droite:
                            Log.d(TAG, "onTouch: Axe4Droite");
                            socketHostService.sendChar(BRAS_AXE_4_DROITE);
                            break;
                        case R.id.Axe5Gauche:
                            Log.d(TAG, "onTouch: Axe5Gauche");
                            socketHostService.sendChar(BRAS_AXE_5_GAUCHE);
                            break;
                        case R.id.Axe5Droite:
                            Log.d(TAG, "onTouch: Axe5Droite");
                            socketHostService.sendChar(BRAS_AXE_5_DROITE);
                            break;
                        case R.id.Axe6Gauche:
                            Log.d(TAG, "onTouch: Axe6Gauche");
                            socketHostService.sendChar(BRAS_AXE_6_GAUCHE);
                            break;
                        case R.id.Axe6Droite:
                            Log.d(TAG, "onTouch: Axe6Droite");
                            socketHostService.sendChar(BRAS_AXE_6_DROITE);
                            break;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "onTouch: action UP");
                    socketHostService.sendChar(BRAS_STOPPER);
                    break;

            }
            return true;
        }
    };

    Intent intentToSocketHostService;
    SocketHostService socketHostService;
    boolean hasBinding = false;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.w(TAG, "onServiceConnected: 105");
            SocketHostService.myBinder binder = (SocketHostService.myBinder) service;
            Log.w(TAG, "onServiceConnected: 107");
            socketHostService = binder.getService();
            Log.w(TAG, "onServiceConnected: 109");
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
        setContentView(R.layout.activity_controler_bras);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Log.d(TAG, "onCreate() from "+TAG);

        //Ajout de m_toolbar
        m_toolbar = (Toolbar) findViewById(R.id.toolbar);
        m_toolbar.setTitle("");
        setSupportActionBar(m_toolbar);
        m_toolbar.setTitle(R.string.titre_activite_controle_bras);
        m_toolbar.setTitleTextColor(Color.WHITE);

        btnSerrerPince =  (Button) findViewById(R.id.btnFermerPince);
        btnOuvrirPince = (Button) findViewById(R.id.btnOuvrirPince);
        btnTournerPinceGauche = (Button) findViewById(R.id.btnTournerPinceGauche);
        btnTournerPinceDroite = (Button) findViewById(R.id.btnTournerPinceDroite);
        btn3Gauche = (Button) findViewById(R.id.Axe3Gauche);
        btn3Droite = (Button) findViewById(R.id.Axe3Droite);
        btn4Gauche = (Button) findViewById(R.id.Axe4Gauche);
        btn4Droite = (Button) findViewById(R.id.Axe4Droite);
        btn5Gauche = (Button) findViewById(R.id.Axe5Gauche);
        btn5Droite = (Button) findViewById(R.id.Axe5Droite);
        btn6Gauche = (Button) findViewById(R.id.Axe6Gauche);
        btn6Droite = (Button) findViewById(R.id.Axe6Droite);

        btnSerrerPince.setOnTouchListener(MyOnTouchListener);
        btnOuvrirPince.setOnTouchListener(MyOnTouchListener);
        btnTournerPinceGauche.setOnTouchListener(MyOnTouchListener);
        btnTournerPinceDroite.setOnTouchListener(MyOnTouchListener);
        btn3Gauche.setOnTouchListener(MyOnTouchListener);
        btn3Droite.setOnTouchListener(MyOnTouchListener);
        btn4Gauche.setOnTouchListener(MyOnTouchListener);
        btn4Droite.setOnTouchListener(MyOnTouchListener);
        btn5Gauche.setOnTouchListener(MyOnTouchListener);
        btn5Droite.setOnTouchListener(MyOnTouchListener);
        btn6Gauche.setOnTouchListener(MyOnTouchListener);
        btn6Droite.setOnTouchListener(MyOnTouchListener);


        // Setup intent
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
        switch (item.getItemId()){
            case R.id.boutonChangerControles :
                startActivity(new Intent(this, ControlerPlateformeActivity.class));
                break;
            case R.id.boutonRetourAuTest :
                startActivity(new Intent(this, TestConnexionActivity.class));
                break;
        }
        return true;
    }

    public void ObtenirMasse(View view) {
        socketHostService.sendChar(BRAS_DEMANDER_MASSE);
    }

    public void MettreEnPosition(View view) {
        socketHostService.sendChar(BRAS_METTRE_EN_POSITION);
    }

    public void Stop(View view) {
        socketHostService.sendChar(BRAS_STOPPER);
    }
}