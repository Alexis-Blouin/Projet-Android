package com.tractoteam.tractopel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ControlerPlateformeActivity extends AppCompatActivity {

    String TAG = this.getClass().getName();

    Toolbar m_toolbar;

    Button boutonDroite;
    Button boutonGauche;
    Button boutonHaut;
    Button boutonBas;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controler_plateforme);

//        try
//        {
//            //here you must put your computer's IP address.
//            InetAddress serverAddr = InetAddress.getByName("192.168.0.10");
//
//            Log.d("TCP Client", "C: Connecting...");
//
//            //create a socket to make the connection with the server
//            socket = new Socket(serverAddr, 1444);
//        }
//        catch (Exception e) {
//            Log.e("TCP", "C: Error", e);
//        }


//        try {
//            SendToSocket = socket.getOutputStream();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            GetFromSocket = socket.getInputStream();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

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


//    public void buttonRightPressed(View view) {
//        if (view.isPressed())
//        {
//            Log.d(TAG, "Button right is pressed");
//        }
//        if (view.isFocused())
//        {
//            Log.d(TAG, "Button right is focused");
//        }
//        if (view.isHovered())
//        {
//            Log.d(TAG, "Button right is hovered");
//        }
//    }



    View.OnTouchListener DirectionButtonsListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            String id = v.getResources().getResourceEntryName(v.getId());

            //DEBUG
            if (event.getAction() == MotionEvent.ACTION_DOWN && id.equals("boutonHaut")){
                Log.wtf("TEST", "Bouton "+id+" pressé in socket if");
                TheThread.sendMessage('5');
            }
            if (event.getAction() == MotionEvent.ACTION_UP && id.equals("boutonHaut")){
                TheThread.sendMessage('6');
                Log.wtf("TEST", "Bouton "+id+" laché in socket if");
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN && id.equals("boutonBas")){
                TheThread.sendMessage('2');
                Log.wtf("TEST", "Bouton "+id+" laché in socket if");
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN && id.equals("boutonGauche")){
                TheThread.sendMessage('4');
                Log.wtf("TEST", "Bouton "+id+" laché in socket if");
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN && id.equals("boutonDroite")){
                TheThread.sendMessage('3');
                Log.wtf("TEST", "Bouton "+id+" laché in socket if");
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(TAG, "Bouton " + id + " : ACTION_DOWN");
//                    try {
//                        socket.getOutputStream().write('2');
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "Bouton " + id + " : ACTION_UP");
                    TheThread.sendMessage('6');


//                    try {
//                        socket.getOutputStream().write('0');
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
                    break;
//                default:
//                    Log.w(TAG, "WARNING : Unhandled action : Bouton " + id + "; action : "+event.getAction());
//                    break;
            }
            return true;
        }
    };
}