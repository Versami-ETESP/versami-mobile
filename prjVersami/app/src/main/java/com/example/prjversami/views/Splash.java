package com.example.prjversami.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.prjversami.R;
import com.example.prjversami.controllers.LoginController;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        SharedPreferences pref = getSharedPreferences("login", Context.MODE_PRIVATE);
        int idUserLogado = pref.getInt("id", 0);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (idUserLogado > 0) {
                    LoginController lc = new LoginController(getApplicationContext());
                    lc.atualizarDadosUsuario(idUserLogado);
                    startActivity(new Intent(Splash.this, MainActivity.class));
                } else {
                    startActivity(new Intent(Splash.this, TelaBemvindo.class));
                }

                finish();
            }
        }, 3000);
    }
}
