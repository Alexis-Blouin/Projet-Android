package com.tractoteam.tractopel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestConnexionActivity extends AppCompatActivity {

    String TAG = this.getClass().getName();

    Toolbar m_toolbar;

    TextView resultatTestConnectionTextview;

    Button lancerTestBouton;
    Button boutonControlerPlateforme;
    Button boutonControlerBras;

    Intent intentControlerBrasActivity;
    Intent intentControlerPlateformeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_connection_layout);

        Log.d(TAG, "onCreate() from "+TAG);


        resultatTestConnectionTextview = (TextView) findViewById(R.id.resultatTestConnectionTextview);

        lancerTestBouton = (Button) findViewById(R.id.testConnexionBouton);
        boutonControlerPlateforme = (Button) findViewById(R.id.controlerPlateforme);
        boutonControlerBras = (Button) findViewById(R.id.controlerBras);


        // Initialisation des intents
        intentControlerPlateformeActivity = new Intent(this, ControlerPlateformeActivity.class);
        intentControlerBrasActivity = new Intent(this, ControlerBrasActivity.class);


        //Ajout de m_toolbar
        m_toolbar = (Toolbar) findViewById(R.id.toolbar);
        m_toolbar.setTitle("");
        setSupportActionBar(m_toolbar);
        m_toolbar.setTitle(R.string.titre_activite_test_connexion);
        m_toolbar.setTitleTextColor(Color.WHITE);
    }

    public void LancerTestConnection(View view) {
        boolean resultatTest = false;


        if (resultatTest){
            resultatTestConnectionTextview.setText(R.string.test_connection_reussi);
            resultatTestConnectionTextview.setTextColor(Color.DKGRAY);
        }
        else {
            resultatTestConnectionTextview.setText(R.string.test_connexion_echoue);
            resultatTestConnectionTextview.setTextColor(Color.RED);
        }

        boutonControlerBras.setEnabled(true);
        boutonControlerPlateforme.setEnabled(true);
    }

    public void LancerControlePlateforme(View view) {
        startActivity(intentControlerPlateformeActivity);
    }

    public void LancerControleBras(View view) {
        startActivity(intentControlerBrasActivity);
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }
*/
}