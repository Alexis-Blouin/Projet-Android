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

public class ControlerPlateformeActivity extends AppCompatActivity {

    String TAG = this.getClass().getSimpleName();

    Toolbar m_toolbar;

    Button boutonDroite;
    Button boutonGauche;
    Button boutonHaut;
    Button boutonBas;

    /**
     * Donner l'ordre d'avancer à la voiture
     */
    static final char VOITURE_AVANCER = '2';

    /**
     * Donner l'ordre de reculer à la voiture
     */
    static final char VOITURE_RECULER = '5';

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
    static final char VOITURE_STOPPER = '6';

    /**
     * Adresse IP du robot (fixe, sert à lui envoyer les instructions)
     */
    String ipAddress = "192.168.0.10";
    /**
     * Port sur lequel communiquer
     */
    int port = 1444;


    // Intent vers les service auquel on va se bind.
    Intent intentToSocketHostService;

    // Référence vers le service une fois récupéré (bindé)
    SocketService socketService;

    // Permet de savoir si le service est toujours bind
    boolean hasBinding = false;

    // Interface qui permet de récupérer la référence vers le service quand on le bind.
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SocketService.monBinder binder = (SocketService.monBinder) service;
            socketService = binder.obtenirService();
            hasBinding = true;

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
            hasBinding = false;
            Log.w(TAG, "onServiceDisconnected: Service was disconnected abruptly");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controler_plateforme);

        // Police de thread pour utiliser le service bindé
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.d(TAG, "onCreate() from "+TAG);

        // Associe les boutons par ID
        boutonDroite = (Button) findViewById(R.id.boutonDroite);
        boutonGauche = (Button) findViewById(R.id.boutonGauche);
        boutonHaut = (Button) findViewById(R.id.boutonHaut);
        boutonBas = (Button) findViewById(R.id.boutonBas);

        // Applique le listener aux boutons
        boutonBas.setOnTouchListener(DirectionButtonsListener);
        boutonDroite.setOnTouchListener(DirectionButtonsListener);
        boutonGauche.setOnTouchListener(DirectionButtonsListener);
        boutonHaut.setOnTouchListener(DirectionButtonsListener);

        //Ajout de m_toolbar
        m_toolbar = (Toolbar) findViewById(R.id.toolbar);
        m_toolbar.setTitle("");
        setSupportActionBar(m_toolbar);
        m_toolbar.setTitle(R.string.titre_activite_controle_plateforme);
        m_toolbar.setTitleTextColor(Color.WHITE);

        // Déclaration de l'intent vers le service
        intentToSocketHostService = new Intent(this, SocketService.class);

        // Bind au service
        Log.d(TAG, "onCreate: Attempting binding call");
        bindService(intentToSocketHostService, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "onCreate: Binding successfully called");
    }

    @Override
    protected void onDestroy() {

        // Unbind le service si l'activité est détruite
        if (hasBinding)
            unbindService(serviceConnection);

        hasBinding = false;
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

    View.OnTouchListener DirectionButtonsListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            String id = v.getResources().getResourceEntryName(v.getId());

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    // Envoie à la voiture le signal de direction correspondant au bouton pressé
                    switch (id){
                        case "boutonHaut":
                            socketService.EnvoyerChar(VOITURE_AVANCER);
                            break;

                        case "boutonBas":
                            socketService.EnvoyerChar(VOITURE_RECULER);
                            break;

                        case "boutonGauche":
                            socketService.EnvoyerChar(VOITURE_TOURNER_GAUCHE);
                            break;

                        case "boutonDroite":
                            socketService.EnvoyerChar(VOITURE_TOURNER_DROITE);
                            break;
                    }

                    break;

                // Ici, le moment ou le bouton est relaché
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "onTouch: action ACTION_UP");
                    // On envoie le code demandant l'arrêt du mouvement
                    socketService.EnvoyerChar(VOITURE_STOPPER);
                    break;
            }
            return true;
        }
    };
}