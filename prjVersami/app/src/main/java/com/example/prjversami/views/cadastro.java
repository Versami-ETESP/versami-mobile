package com.example.prjversami.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.controllers.CadastroController;
import com.example.prjversami.entities.Pergunta;
import com.example.prjversami.util.Criptografia;

import java.util.ArrayList;

public class cadastro extends AppCompatActivity {

    EditText name, email, pass, confirm, birth, answer;
    Button btnLogin, btnNext;
    CadastroController cad;
    Spinner spnPergunta;
    ArrayAdapter<Pergunta> adapter;
    ArrayList<Pergunta> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_cadastro);
        name = findViewById(R.id.cadastro_txtNome);
        email = findViewById(R.id.cadastro_txtEmail);
        pass = findViewById(R.id.cadastro_txtPass);
        confirm = findViewById(R.id.cadastro_txtConfirm);
        birth = findViewById(R.id.cadastro_txtNasc);
        btnLogin = findViewById(R.id.cadastro_btnAcessar);
        btnNext = findViewById(R.id.cadastro_btnCriar);
        answer = findViewById(R.id.cadastro_txtResposta);
        spnPergunta = findViewById(R.id.cadastro_spnPergunta);

        // instanciando classe controller
        cad = new CadastroController(name, email, pass, confirm, birth, answer, getApplicationContext());

        //preenchendo spinner
        questions = cad.preencheArray();
        adapter = new ArrayAdapter<Pergunta>(this, R.layout.spinner_item, questions) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spnPergunta.setAdapter(adapter);

        //chamando validação de campos
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
                } else if (((Pergunta) spnPergunta.getSelectedItem()).getIdPergunta() <= 0) {
                    Snackbar.make(view, "Por favor, selecione uma pergunta!", Snackbar.LENGTH_LONG).show();
                } else {
                    Intent tela = new Intent(cadastro.this, cadastro2.class);
                    tela.putExtra("nome", name.getText().toString());
                    tela.putExtra("email", email.getText().toString());
                    tela.putExtra("nasc", birth.getText().toString());
                    tela.putExtra("senha", Criptografia.criptografaString(pass.getText().toString()));
                    tela.putExtra("resposta", Criptografia.criptografaString(answer.getText().toString()));
                    tela.putExtra("pergunta", ((Pergunta) spnPergunta.getSelectedItem()).getIdPergunta());
                    startActivity(tela);
                }
            }
        });
    }


}