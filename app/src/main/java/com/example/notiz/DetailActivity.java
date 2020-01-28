package com.example.notiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_QUOTE_AUTHOR = "EXTRA_QUOTE_AUTHOR";
    public static final String EXTRA_QUOTE_TEXT = "EXTRA_QUOTE_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        //Initialisieren der Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail_activity);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Füllen des View-Elements mit den empfangenen Daten des Intent-Objektes
        Intent receivedIntent = getIntent();
        if (receivedIntent != null && receivedIntent.hasExtra(EXTRA_QUOTE_TEXT)) {
            String quoteAuthor = receivedIntent.getStringExtra(EXTRA_QUOTE_AUTHOR);
            String quoteText = receivedIntent.getStringExtra(EXTRA_QUOTE_TEXT);

            actionBar.setTitle(quoteAuthor);

            TextView txtText = (TextView) findViewById(R.id.txtDetailActivity);
            txtText.setText("\"" + quoteText + "\"");

        }
    }
}
