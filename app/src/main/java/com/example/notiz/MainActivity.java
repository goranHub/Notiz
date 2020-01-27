package com.example.notiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {


    MainActivityListener mainActivityListener;
    EditText txtNotizEintrag;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAddNote);
        txtNotizEintrag = findViewById(R.id.txtNotizEintrag);

        MainActivityListener mainActivityListener = new MainActivityListener(this);

        btnAdd.setOnClickListener(mainActivityListener);




    }
}
