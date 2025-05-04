package com.example.prjversami.views;


import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.controllers.LoginController;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.Criptografia;
import com.example.prjversami.util.Validacao;

public class TrocaSenha extends AppCompatActivity {

    EditText nomeUser, resposta, senha, confirma;
    Button btnPesquisar, btnAlterar;
    TextView pergunta;
    Usuario user = new Usuario();

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
            String[] dados = lc.resgatarPergunta(nomeUser.getText().toString());

            if (dados != null && !dados[0].isEmpty()){
                this.user.setResposta(dados[1]);
                this.user.setUserLogin(nomeUser.getText().toString());

                nomeUser.setEnabled(false);
                pergunta.setText(dados[0]);
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
        confirma = this.confirma.getText().toString();

        if(!Validacao.passValidation(senha)){
            this.senha.setError("A senha deve ter 8 ou mais caracteres");
            return;
        }

        if(!Validacao.passConfirm(senha, confirma)){
            this.confirma.setError("As senhas não coincidem");
            return;
        }

        if(resposta.isEmpty()){
            this.resposta.setError("Necessário responder a pergunta para alterar a senha");
            return;
        }

        LoginController lc = new LoginController(getApplicationContext());
        this.user.setUserPass(Criptografia.criptografaString(senha));

        if(lc.alteraSenha(this.user, resposta)){
            Snackbar.make(v, "Senha Alterada", Snackbar.LENGTH_LONG).show();
            startActivity(new Intent(TrocaSenha.this, login.class));
            finish();
        }else{
            Snackbar.make(v, "Erro ao alterar a senha. Tente Novamente!", Snackbar.LENGTH_LONG).show();
        }
    }
}