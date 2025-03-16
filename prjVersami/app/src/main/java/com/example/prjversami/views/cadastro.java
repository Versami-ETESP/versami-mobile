package com.example.prjversami.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.prjversami.R;
import com.example.prjversami.controllers.CadastroController;
import com.example.prjversami.models.Usuario;

public class cadastro extends AppCompatActivity {

    EditText name, email, pass, confirm, birth;
    Button btnLogin, btnNext;
    CadastroController cad;

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

        cad = new CadastroController(name, email, pass, confirm, birth);
        cad.inputValidate();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tela = new Intent(cadastro.this, login.class);
                startActivity(tela);
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cad.formIsEmpty()) {
                    Snackbar.make(view, "Por favor, preencher todos os campos!", Snackbar.LENGTH_LONG).show();
                } else if (cad.validData()) {
                    Snackbar.make(view, "Por favor, verifique os campos com erro!", Snackbar.LENGTH_LONG).show();
                } else {
                    Intent tela = new Intent(cadastro.this, cadastro2.class);
                    tela.putExtra("nome", name.getText().toString());
                    tela.putExtra("email", email.getText().toString());
                    tela.putExtra("nasc", birth.getText().toString());
                    tela.putExtra("senha", pass.getText().toString());
                    startActivity(tela);
                }
            }
        });
    }


}