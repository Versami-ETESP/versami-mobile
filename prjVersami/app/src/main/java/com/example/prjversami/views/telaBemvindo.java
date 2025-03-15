package com.example.prjversami.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.prjversami.R;

public class telaBemvindo extends AppCompatActivity {

    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_bemvindo);
        btnNext = findViewById(R.id.btnIniciar);
        getSupportActionBar().hide();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(telaBemvindo.this, login.class);
                startActivity(intent);
            }
        });

    }

}