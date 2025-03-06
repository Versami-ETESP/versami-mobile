package com.example.prjversami.controllers;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.prjversami.core.Conexao;
import com.example.prjversami.core.Validacao;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class CadastroController {

    private Conexao con;
    private String table = "tblUsuario";
    private Context screen;
    EditText name, email, pass, confirm, birth;

    public CadastroController(Context screen, EditText name, EditText email, EditText pass, EditText confirm, EditText birth) {
        this.screen = screen;
        con = new Conexao();
        con.connectDB(screen);
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.confirm = confirm;
        this.birth = birth;
    }

    public void inputValidate(){
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String usrNome = name.getText().toString(), msg;
                msg = Validacao.nameValidation(usrNome) ? null : "Nome precisa ter no mínimo 3 caracteres";
                name.setError(msg);
            }
        });
    }

    public boolean userLoginUnique(String login) {
        return Validacao.userIsUnique(login, this.table, this.con, this.screen);
    }

    public String register(String name, String user, String pass, String confirm, String email, String birth) {

        LocalDate birthDate = LocalDate.parse(birth);
        String resposta = "";

        if (name.isEmpty() || user.isEmpty() || pass.isEmpty() || confirm.isEmpty() || email.isEmpty() || birth.isEmpty()) {
            resposta = "Necessário preencher todos os campos para o cadastro";
            return resposta;
        }

        if (!Validacao.nameValidation(name) || !Validacao.birthValidation(birthDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) || !Validacao.passValidation(pass) || !this.userLoginUnique(user) || !Validacao.passConfirm(pass, confirm)) {
            resposta = "Preencha todos os campos corretamente";
            return resposta;
        }

        try {
            int res = con.command.executeUpdate("insert into "+this.table+"(nome,data_nasc,email,senha,arroba_usuario) values ('"+name+"','"+birth+"','"+email+"','"+pass+"','"+user+"')");

            if(res!=0)
                resposta = "Usuario cadastrado com sucesso!";

        } catch (SQLException ex) {
            resposta = "Erro ao cadastrar usuário. Tente novamente mais tarde.";
        }

        return resposta;
    }
}
