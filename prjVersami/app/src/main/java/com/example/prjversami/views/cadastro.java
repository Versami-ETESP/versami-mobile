package com.example.prjversami.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.prjversami.R;
import com.example.prjversami.controllers.CadastroController;
import com.example.prjversami.core.Validacao;

public class cadastro extends AppCompatActivity {

    EditText name, email, pass, confirm, birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        name = findViewById(R.id.cadastro_txtNome);
        email = findViewById(R.id.cadastro_txtEmail);
        pass = findViewById(R.id.cadastro_txtPass);
        confirm = findViewById(R.id.cadastro_txtConfirm);
        birth = findViewById(R.id.cadastro_txtNasc);
        getSupportActionBar().hide();

        CadastroController cad = new CadastroController(getApplicationContext(), name, email, pass, confirm, birth);
        cad.inputValidate();
    }


}