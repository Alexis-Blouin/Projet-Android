package com.tractoteam.tractopel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

public class TestConnexionActivity extends AppCompatActivity {

    String TAG = TestConnexionActivity.class.getName();

    Toolbar m_toolbar;

    Button lancerTestBouton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_connection_layout);

        Log.d(TAG, "onCreate() from "+TAG);

        m_toolbar = (Toolbar) findViewById(R.id.toolbar);
        lancerTestBouton = (Button) findViewById(R.id.testConnexionBouton);


        //Ajout de m_toolbar
        m_toolbar.setTitle("");
        setSupportActionBar(m_toolbar);
        m_toolbar.setTitle(R.string.test_connexion_titre_activite);
        m_toolbar.setTitleTextColor(Color.WHITE);
    }

    public void LancerTestConnection(View view) {

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