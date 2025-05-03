package com.example.prjversami.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.prjversami.R;

public class splash extends AppCompatActivity {

    boolean resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        SharedPreferences pref = getSharedPreferences("login", Context.MODE_PRIVATE);

        this.resp = pref.getInt("id", 0) > 0;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (resp) {
                    startActivity(new Intent(splash.this, MainActivity.class));
                } else {
                    startActivity(new Intent(splash.this, telaBemvindo.class));
                }

                finish();
            }
        }, 3000);
    }
}
