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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SQLiteDatabase db = openOrCreateDatabase("guardarDados", Context.MODE_PRIVATE,null);

                try{
                    Cursor c = db.rawQuery("SELECT * FROM usuario",null);
                    if(c.moveToNext()){
                        Intent tela = new Intent(splash.this, teste.class);
                        startActivity(tela);
                        finish();
                    }else{
                        Intent intensao = new Intent(splash.this, telaBemvindo.class);
                        startActivity(intensao);
                        finish();
                    }

                }catch(Exception e){
                    Log.e("SQLite", "Banco de dados indisponível" + e.getMessage());
                    Intent intensao = new Intent(splash.this, telaBemvindo.class);
                    startActivity(intensao);
                    finish();
                }


            }
        },3000);

        getSupportActionBar().hide();
        }
    }
