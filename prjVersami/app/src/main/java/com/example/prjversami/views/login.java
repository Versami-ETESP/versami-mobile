package com.example.prjversami.views;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.prjversami.R;
import com.example.prjversami.controllers.LoginController;

public class login extends AppCompatActivity {

    Button btnAcessar, btnCadastrar;
    EditText txtUsuario, txtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnAcessar = findViewById(R.id.login_btnAcessar);
        btnCadastrar = findViewById(R.id.login_btnCriar);
        txtUsuario = findViewById(R.id.login_txtUsuario);
        txtSenha = findViewById(R.id.login_txtPass);

        getSupportActionBar().hide();

        LoginController log = new LoginController(this.getApplicationContext());

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tela = new Intent(login.this, cadastro.class);
                startActivity(tela);
                finish();
            }
        });

        btnAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!txtSenha.getText().toString().isEmpty() && !txtUsuario.getText().toString().isEmpty()) {
                    if (log.login(txtUsuario.getText().toString(), txtSenha.getText().toString())) {
                        Intent tela = new Intent(login.this, teste.class);
                        startActivity(tela);
                        finish();
                    } else {
                        Snackbar.make(view, "Usuário ou senha inválido!", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(view, "Por favor, preencher os campos!", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

}