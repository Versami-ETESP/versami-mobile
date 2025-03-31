package com.example.prjversami.views;

import android.content.Context;
import android.content.Intent;
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

        try (SQLiteDatabase db = this.openOrCreateDatabase("guardarDados", Context.MODE_PRIVATE, null);
             Cursor c = db.rawQuery("SELECT * FROM usuario", null);) {

            if (c.moveToNext()) {
                this.resp = true;

            } else {
                this.resp = false;
            }

        } catch (Exception e) {
            Log.e("SQLite", "Banco de dados indisponível" + e.getMessage());
            this.resp = false;
        }

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
