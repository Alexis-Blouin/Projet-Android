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

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ControlerBrasActivity extends AppCompatActivity {

    // Liste des codes avec leur correspondance avec le robot
    static final char BRAS_OUVRIR_PINCE = 'f';
    static final char BRAS_FERMER_PINCE = 'F';
    static final char BRAS_TOURNER_PINCE_GAUCHE = 'e';
    static final char BRAS_TOURNER_PINCE_DROITE = 'E';
    static final char BRAS_AXE_1_BAISSER = 'D';
    static final char BRAS_AXE_1_LEVER = 'd';
    static final char BRAS_AXE_2_BAISSER = 'c';
    static final char BRAS_AXE_2_LEVER = 'C';
    static final char BRAS_AXE_3_BAISSER = 'B';
    static final char BRAS_AXE_3_LEVER = 'b';
    static final char BRAS_TOURNER_BASE_GAUCHE = 'A';
    static final char BRAS_TOURNER_BASE_DROITE = 'a';
    static final char BRAS_STOPPER = '6';
    static final char BRAS_METTRE_EN_POSITION_PESEE = 'y';
    static final char BRAS_DEMANDER_MASSE = 'z';
    static final char BRAS_RESET = 'x';

    // TAG pour les logs
    String TAG = this.getClass().getSimpleName();

    // Toolbar pour la mise en place de la toolbar custom
    Toolbar m_toolbar;

    // Liste des boutons auxquels sera appliqué le onTouchListener
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

    TextView texteMasse;


    View.OnTouchListener MyOnTouchListener = new View.OnTouchListener() {
        // Appelé à chaque action par rapport au bouton auxquels il est appliqué
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            // Switch sur les différents types d'actions
            switch (event.getAction())
            {
                // Ici, le moment ou le bouton est pressé
                case MotionEvent.ACTION_DOWN:
                    Log.d(TAG, "onTouch: action ACTION_DOWN");
                    // Switch sur les ID des boutons, pour faire différentes actions suivant le bouton
                    switch (v.getId())
                    {
                        // Pour chaque, on envoie le code qui permet de déclencher l'action correspondante du robot
                        case R.id.btnFermerPince:
                            Log.d(TAG, "onTouch: btnFermerPince");
                            socketService.EnvoyerChar(BRAS_FERMER_PINCE);
                            break;
                        case R.id.btnOuvrirPince:
                            Log.d(TAG, "onTouch: btnOuvrirPince");
                            socketService.EnvoyerChar(BRAS_OUVRIR_PINCE);
                            break;
                        case R.id.btnTournerPinceGauche:
                            Log.d(TAG, "onTouch: btnTournerPinceGauche");
                            socketService.EnvoyerChar(BRAS_TOURNER_PINCE_GAUCHE);
                            break;
                        case R.id.btnTournerPinceDroite:
                            Log.d(TAG, "onTouch: btnTournerPinceDroite");
                            socketService.EnvoyerChar(BRAS_TOURNER_PINCE_DROITE);
                            break;
                        case R.id.btnBaisser1:
                            Log.d(TAG, "onTouch: btnBaisser1");
                            socketService.EnvoyerChar(BRAS_AXE_1_BAISSER);
                            break;
                        case R.id.btnLever1:
                            Log.d(TAG, "onTouch: btnLever1");
                            socketService.EnvoyerChar(BRAS_AXE_1_LEVER);
                            break;
                        case R.id.btnBaisser2:
                            Log.d(TAG, "onTouch: btnBaisser2");
                            socketService.EnvoyerChar(BRAS_AXE_2_BAISSER);
                            break;
                        case R.id.btnLever2:
                            Log.d(TAG, "onTouch: btnLever2");
                            socketService.EnvoyerChar(BRAS_AXE_2_LEVER);
                            break;
                        case R.id.btnBaisser3:
                            Log.d(TAG, "onTouch: btnBaisser3");
                            socketService.EnvoyerChar(BRAS_AXE_3_BAISSER);
                            break;
                        case R.id.btnLever3:
                            Log.d(TAG, "onTouch: btnLever3");
                            socketService.EnvoyerChar(BRAS_AXE_3_LEVER);
                            break;
                        case R.id.btnTournerBaseGauche:
                            Log.d(TAG, "onTouch: btnTournerBaseGauche");
                            socketService.EnvoyerChar(BRAS_TOURNER_BASE_GAUCHE);
                            break;
                        case R.id.btnTournerBaseDroit:
                            Log.d(TAG, "onTouch: btnTournerBaseDroit");
                            socketService.EnvoyerChar(BRAS_TOURNER_BASE_DROITE);
                            break;
                    }
                    break;

                // Ici, le moment ou le bouton est relaché
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "onTouch: action ACTION_UP");
                    // On envoie le code demandant l'arrêt du mouvement
                    socketService.EnvoyerChar(BRAS_STOPPER);
                    break;
            }
            return true;
        }
    };

    // Intent vers les service auquel on va se bind.
    Intent intentVersSocketService;

    // Référence vers le service une fois récupéré (bindé)
    SocketService socketService;

    // Permet de savoir si le service est toujours bind
    boolean possedeBinding = false;

    // Interface qui permet de récupérer la référence vers le service quand on le bind.
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SocketService.monBinder binder = (SocketService.monBinder) service;
            socketService = binder.obtenirService();
            possedeBinding = true;

            Log.d(TAG, "onServiceConnected: Binded succesfully");

            boolean hasOpenedSocket = socketService.ouvrirSocket();

            Log.d(TAG, "onServiceConnected: Socket is open ? --> " + hasOpenedSocket);

            // Permet d'indiquer si la connexion a échoué an l'affichant dans un TextView
            if (!hasOpenedSocket)
                ((TextView) findViewById(R.id.erreurIndicateurLabel)).setText(getText(R.string.socket_non_ouvert));
            else
                ((TextView) findViewById(R.id.erreurIndicateurLabel)).setText("");

        }

        // Appelé si le service est tué ou terminé
        @Override
        public void onServiceDisconnected(ComponentName name) {
            possedeBinding = false;
            Log.w(TAG, "onServiceDisconnected: Service was disconnected abruptly");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controler_bras);

        // Police de thread pour utiliser le service bindé
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.d(TAG, "onCreate() from "+TAG);

        //Ajout de m_toolbar
        m_toolbar = (Toolbar) findViewById(R.id.toolbar);
        m_toolbar.setTitle("");
        setSupportActionBar(m_toolbar);
        m_toolbar.setTitle(R.string.titre_activite_controle_bras);
        m_toolbar.setTitleTextColor(Color.WHITE);

        // Associe les boutons par ID
        btnSerrerPince =  (Button) findViewById(R.id.btnFermerPince);
        btnOuvrirPince = (Button) findViewById(R.id.btnOuvrirPince);
        btnTournerPinceGauche = (Button) findViewById(R.id.btnTournerPinceGauche);
        btnTournerPinceDroite = (Button) findViewById(R.id.btnTournerPinceDroite);
        btn3Gauche = (Button) findViewById(R.id.btnBaisser1);
        btn3Droite = (Button) findViewById(R.id.btnLever1);
        btn4Gauche = (Button) findViewById(R.id.btnBaisser2);
        btn4Droite = (Button) findViewById(R.id.btnLever2);
        btn5Gauche = (Button) findViewById(R.id.btnBaisser3);
        btn5Droite = (Button) findViewById(R.id.btnLever3);
        btn6Gauche = (Button) findViewById(R.id.btnTournerBaseGauche);
        btn6Droite = (Button) findViewById(R.id.btnTournerBaseDroit);

        texteMasse = (TextView) findViewById(R.id.TexteMasse);

        // Applique le listener aux boutons
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


        // Déclaration de l'intent vers le service
        intentVersSocketService = new Intent(this, SocketService.class);

        // Bind au service
        Log.d(TAG, "onCreate: Attempting binding call");
        bindService(intentVersSocketService, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "onCreate: Binding successfully called");
    }

    @Override
    protected void onDestroy() {

        // Unbind le service si l'activité est détruite
        if (possedeBinding)
            unbindService(serviceConnection);

        possedeBinding = false;
        socketService = null;

        super.onDestroy();
    }

    // Menu custom pour changer de contrôles ou revenir à la page d'accueil
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

    public void ObtenirMasse(View view) throws InterruptedException {
        socketService.EnvoyerChar(BRAS_DEMANDER_MASSE);

        texteMasse.setText(R.string.chargement);

        final String[] s = new String[1];

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "run: started run");
                try {
                    s[0] = socketService.ObtenirMasse();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                texteMasse.setText(s[0]);
            }
        }, 500);


//        do {
//            s[0] = socketService.ObtenirMasse();
//        } while ( s[0].equals("N/A") && !skip[0]);

//        texteMasse.setText(s[0]);
    }

    public void MettreEnPosition(View view) {
        socketService.EnvoyerChar(BRAS_METTRE_EN_POSITION_PESEE);
    }

    public void Stop(View view) {
        socketService.EnvoyerChar(BRAS_STOPPER);
    }

    public void ResetBras(View view) {
        socketService.EnvoyerChar(BRAS_RESET);
    }
}