package com.example.prjversami.views;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.prjversami.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SQLiteDatabase db = openOrCreateDatabase("guardarDados", Context.MODE_PRIVATE,null);
                Cursor c = db.rawQuery("SELECT * FROM usuario",null);

                if(c.moveToNext()){
                    Intent tela = new Intent(MainActivity.this, teste.class);
                    startActivity(tela);
                    finish();
                }else{
                    Intent intensao = new Intent(MainActivity.this, telaBemvindo.class);
                    startActivity(intensao);
                    finish();
                }

            }
        },3000);

        getSupportActionBar().hide();
        }
    }
