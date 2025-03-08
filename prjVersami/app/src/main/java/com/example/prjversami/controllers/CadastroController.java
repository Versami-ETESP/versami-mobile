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

/**
 * Esta Classe faz o controle dos processos de Cadastro do APP. Os métodos realizarão a validação dos dados e a conexões com o banco de dados.
 */
public class CadastroController {

    private Conexao con;
    private String table = "tblUsuario";
    private Context screen;
    private EditText name, email, pass, confirm, birth, user; // Todos os campos utilizados na view serão espelhados no Controller

    public CadastroController(){

    }

    public CadastroController(EditText name, EditText email, EditText pass, EditText confirm, EditText birth) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.confirm = confirm;
        this.birth = birth;
    }

    public CadastroController(Context screen, EditText user){
        this.screen = screen;
        con = new Conexao();
        con.connectDB(screen);
        this.user = user;
    }

    /**
     * O método 'inputValidate' usa o evento 'addTextChanged' nos campos da view.
     * As informações do input são salvas em uma variavel do tipo String, então é os métodos da classe Validacao são chamados
     * Se o retorno for false, enviará um erro para o input
     */
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

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String usrEmail = email.getText().toString(), msg;
                msg = Validacao.emailValidation(usrEmail) ? null : "Digite um e-mail válido";
                email.setError(msg);
            }
        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String usrPass = pass.getText().toString(), msg;
                msg = Validacao.passValidation(usrPass) ? null : "A senha deve ter no mínimo 8 caracteres.";
                pass.setError(msg);
            }
        });

        birth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String usrbirth = birth.getText().toString(), msg;
                msg = Validacao.birthValidation(usrbirth) ? null : "Você precisa ter pelo menos 13 anos para se registrar";
                birth.setError(msg);
            }
        });

        confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String usrPass = pass.getText().toString(), usrConfirm = confirm.getText().toString(), msg;
                msg = Validacao.passConfirm(usrPass, usrConfirm) ? null : "As senhas não conferem";
                confirm.setError(msg);
            }
        });

    }

    /**
     * O método 'userLoginUnique' simplifica a utilização do método 'userIsUnique' da classe Validacao.
     * É utilizado para verificar no banco de dados se há correspondencia para um arroba de usuario.
     * @param login
     * @return boolean
     */

    public boolean userLoginUnique(String login) {
        return Validacao.userIsUnique(login, this.table, this.con, this.screen);
    }

    /**
     * O método 'formIsEmpty' verifica se um dos campos está vazio. Se sim retorna true, se não retorna false
     * @return boolean
     */

    public boolean formIsEmpty(){

        boolean resposta;
        String
                name = this.name.getText().toString(),
                email = this.email.getText().toString(),
                birth = this.birth.getText().toString(),
                pass = this.pass.getText().toString(),
                confirm = this.confirm.getText().toString();

        if (name.isEmpty() || pass.isEmpty() || confirm.isEmpty() || email.isEmpty() || birth.isEmpty()) {
            resposta = true;
        } else {
            resposta = false;
        }

        return resposta;
    }

    /**
     * O método 'validData' verifica se todos os campos estão com informações válidas. Se sim retorna true, se não retorna false
     * @return boolean
     */

    public boolean validData(){

        String
                name = this.name.getText().toString(),
                email = this.email.getText().toString(),
                birth = this.birth.getText().toString(),
                pass = this.pass.getText().toString(),
                confirm = this.confirm.getText().toString();

        boolean resposta = (!Validacao.nameValidation(name) || !Validacao.birthValidation(birth) || !Validacao.passValidation(pass)
                || !Validacao.passConfirm(pass, confirm) || !Validacao.emailValidation(email));

        return resposta;
    }


    /*
    public String register() {
        String resposta = "";

        String
                name = this.name.getText().toString(),
                email = this.email.getText().toString(),
                birth = this.birth.getText().toString(),
                pass = this.pass.getText().toString(),
                confirm = this.confirm.getText().toString();


        if (!Validacao.nameValidation(name) || !Validacao.birthValidation(birth) || !Validacao.passValidation(pass) || !this.userLoginUnique(user) || !Validacao.passConfirm(pass, confirm)) {
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
    }*/
}
