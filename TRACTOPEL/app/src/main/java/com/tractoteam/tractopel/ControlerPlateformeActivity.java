package com.tractoteam.tractopel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class ControlerPlateformeActivity extends AppCompatActivity {

    String TAG = this.getClass().getName();

    Toolbar m_toolbar;

    Button boutonDroite;
    Button boutonGauche;
    Button boutonHaut;
    Button boutonBas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controler_plateforme);

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


    public void buttonRightPressed(View view) {
        if (view.isPressed())
        {
            Log.d(TAG, "Button right is pressed");
        }
        if (view.isFocused())
        {
            Log.d(TAG, "Button right is focused");
        }
        if (view.isHovered())
        {
            Log.d(TAG, "Button right is hovered");
        }
    }



    View.OnTouchListener DirectionButtonsListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            String id = v.getResources().getResourceEntryName(v.getId());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(TAG, "Bouton " + id + " : ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "Bouton " + id + " : ACTION_UP");
                    break;
                default:
                    Log.d(TAG, "WARNING : Unhandled action : Bouton " + id);
                    break;
            }
            return true;
        }
    };
}