package com.example.prjversami;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.prjversami.controllers.LoginController;
import com.example.prjversami.util.Validacao;

public class TrocaSenha extends AppCompatActivity {

    EditText nomeUser, resposta, senha, confirma;
    Button btnPesquisar, btnAlterar;
    TextView pergunta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca_senha);

        getSupportActionBar().hide();

        nomeUser = findViewById(R.id.senha_txtUser);
        pergunta = findViewById(R.id.senha_txtPergunta);
        resposta = findViewById(R.id.senha_txtResposta);
        senha = findViewById(R.id.senha_txtSenha);
        confirma = findViewById(R.id.senha_txtConfirma);
        btnPesquisar = findViewById(R.id.senha_btnPesquisar);
        btnAlterar = findViewById(R.id.senha_btnAlterar);
    }

    public void pesquisar(View v){

        if(!nomeUser.getText().toString().isEmpty()){
            LoginController lc = new LoginController(getApplicationContext());
            String perg = lc.resgatarPergunta(nomeUser.getText().toString());

            if (!perg.isEmpty()){
                nomeUser.setEnabled(false);
                pergunta.setText(perg);
                pergunta.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                pergunta.setSelected(true);
            }else{
                nomeUser.setError("Usuário não localizado.");
            }
        }else{
            nomeUser.setError("Preencher Usuário");
        }
    }

    public void alterarSenhar(View v){
        String resposta, senha, confirma;
        resposta = this.resposta.getText().toString();
        senha = this.senha.getText().toString();
        confirma = this.confirma.toString();

        if(!Validacao.passConfirm(senha, confirma)){
            this.confirma.setError("As senhas não coincidem");
            return;
        }

        if(resposta.isEmpty()){
            this.resposta.setError("Necessário responder a pergunta para alterar a senha");
            return;
        }

     // continuar daqui... criar o metodo para alterar a senha

    }
}