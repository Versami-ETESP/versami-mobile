package com.example.prjversami.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.prjversami.R;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }

}