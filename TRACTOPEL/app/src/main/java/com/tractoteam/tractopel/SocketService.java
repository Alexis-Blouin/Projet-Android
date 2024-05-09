package com.tractoteam.tractopel;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class SocketService extends Service {

    String TAG = this.getClass().getSimpleName();

    boolean estOuvert = false;

    final String ipAddress = "192.168.0.10";
    final int port = 1444;

    Socket socket;
    DataOutputStream StreamEnvoi;
    DataInputStream StreamRececption;
    BufferedReader bufferedReader;


    /**
     * Interface donnant accès au service
     */
    public class monBinder extends Binder {
        SocketService obtenirService() {
            return SocketService.this;
        }
    }
    monBinder binder = new monBinder();

    /**
     * Ouvre le socket si jamais il n'était pas encore ouvert
     * @return <span style="font-weight:bold;">true</span> si le socket est ouvert, <span style="font-weight:bold;">false</span> sinon
     */
    boolean ouvrirSocket() {
        if (estOuvert)
            return estOuvert;

        try {
            socket = new Socket();

            // Set l'adresse ip et le port du socket, ainsi que le temps de Timeout, et connecte le socket
            socket.connect(new InetSocketAddress(ipAddress, port), 5000);

            // Récupère le flux pour envoyer les données
            StreamEnvoi = new DataOutputStream(socket.getOutputStream());
            StreamRececption = new DataInputStream(socket.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            estOuvert = true;

            EnvoyerChar(ControlerBrasActivity.BRAS_RESET);

            return estOuvert;
        }
        catch (SocketTimeoutException e) {
            Log.e(TAG, "Timeout du socket", e);
            return estOuvert;
        }

        catch (IOException e){
            Log.wtf(TAG, e);
            return estOuvert;
        }
    }

    String ObtenirMasse() throws IOException {

        String donnee = "N/A";

        if (!estOuvert)
            return donnee;
        Log.d(TAG, "ObtenirMasse: Le socket est ouvert");

//        try {
//            Log.d(TAG, "ObtenirMasse: Va tenter la lecture");
//            donnee = StreamRececption.readUTF();
//            Log.d(TAG, "ObtenirMasse: Réussi la lecture");
//        } catch (IOException e) {
//            Log.e(TAG, "ObtenirMasse: ", e);
//        }

//        char[] buffer = new char[64];
//
//        bufferedReader.read(buffer);
//
//        int charsRead = 0;
//
//        while ((charsRead = bufferedReader.read(buffer)) != -1) {
//            donnee += new String(buffer).substring(0, charsRead);
//        }



        try {
            Log.d(TAG, "ObtenirMasse: Try reset");
            bufferedReader.reset();
            Log.d(TAG, "ObtenirMasse: Done reset");
            Log.d(TAG, "ObtenirMasse: bufferedReader.ready()"+bufferedReader.ready());
            Log.d(TAG, "ObtenirMasse: essayer de lire");
            donnee = bufferedReader.readLine();
            Log.d(TAG, "ObtenirMasse: réussi à lire");
        } catch (IOException e) {
            Log.e(TAG, "ObtenirMasse: ", e);
        }

        Log.d(TAG, "ObtenirMasse: "+donnee);
        return donnee;
    }

    /**
     * Ferme le socket
     */
    void fermerSocket() {
        if (!estOuvert)
            return;

        try {
            StreamEnvoi.close();

            socket.close();

            estOuvert = false;
        }
        catch (IOException e) {
            Log.wtf(TAG, e);
        }

    }

    /**
     * Envoie un char
     * @param c char à envoyer
     */
    void EnvoyerChar(char c) {
        if (estOuvert)
            try {
                StreamEnvoi.write(c);

                StreamEnvoi.flush();
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

//        Toast.makeText(this, "Created service", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        fermerSocket();

        super.onDestroy();

//        Toast.makeText(this, "Destroyed service", Toast.LENGTH_SHORT).show();
    }

}