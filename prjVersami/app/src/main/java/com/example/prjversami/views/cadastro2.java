package com.example.prjversami.views;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.prjversami.R;
import com.example.prjversami.controllers.CadastroController;
import com.example.prjversami.models.Usuario;

public class cadastro2 extends AppCompatActivity {

    Usuario user;
    Button btnCadastrar;
    EditText userName;
    ImageView userImage;
    CadastroController cad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro2);

        getSupportActionBar().hide();

        btnCadastrar = findViewById(R.id.cadastro_btnFinalizar);
        userName = findViewById(R.id.cadastro_txtUserName);
        userImage = findViewById(R.id.cadastro_UserImage);

        cad = new CadastroController(getApplicationContext(), userName);
        cad.inputValidate2();

    }

    public void cadastrar(View v){

        if(userName.getText().toString().isEmpty() || !cad.userLoginAvaliable())
            return;

        Bundle dados = getIntent().getExtras();
        if (dados == null){
            Snackbar.make(v, "Erro ao recuperar os dados do cadastro!", Snackbar.LENGTH_LONG).show();
            return;
        }

        user = new Usuario(
                dados.getString("nome"),
                userName.getText().toString(),
                dados.getString("email"),
                dados.getString("nasc"),
                dados.getString("senha"));

        if(cad.register(user))
            Snackbar.make(v, "Usuario cadastrado", Snackbar.LENGTH_LONG).show();
        else
            Snackbar.make(v, "Erro ao cadastrar usuário. Tente novamente mais tarde.", Snackbar.LENGTH_LONG).show();

    }
}