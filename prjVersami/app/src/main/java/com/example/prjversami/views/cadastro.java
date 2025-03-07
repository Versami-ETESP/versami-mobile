package com.example.prjversami.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.prjversami.R;
import com.example.prjversami.controllers.CadastroController;

public class cadastro extends AppCompatActivity {

    EditText name, email, pass, confirm, birth;
    Button btnLogin, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        name = findViewById(R.id.cadastro_txtNome);
        email = findViewById(R.id.cadastro_txtEmail);
        pass = findViewById(R.id.cadastro_txtPass);
        confirm = findViewById(R.id.cadastro_txtConfirm);
        birth = findViewById(R.id.cadastro_txtNasc);
        btnLogin = findViewById(R.id.cadastro_btnAcessar);
        btnNext = findViewById(R.id.cadastro_btnCriar);

        getSupportActionBar().hide();

        CadastroController cad = new CadastroController(getApplicationContext(), name, email, pass, confirm, birth);
        cad.inputValidate();

    }

}