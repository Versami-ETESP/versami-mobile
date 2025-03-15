package com.example.prjversami.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.prjversami.R;

public class telaBemvindo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_bemvindo);
        getSupportActionBar().hide();
    }
    public void proxTela(View view) {
        // Cria uma nova intenção para abrir a outra ACtivity
        Intent intent = new Intent(telaBemvindo.this, login.class);
        startActivity(intent);
    }
}