package com.example.prjversami.views;

import android.content.Intent;
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
                Intent intensao = new Intent(MainActivity.this, teste.class);
                startActivity(intensao);
                finish();
            }
        },3000);

        getSupportActionBar().hide();
        }
    }
