package com.example.prjversami.controllers;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.prjversami.entities.Pergunta;
import com.example.prjversami.util.Conexao;
import com.example.prjversami.util.NavigationUtil;
import com.example.prjversami.util.Validacao;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.views.AvisoDeErro;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Esta Classe faz o controle dos processos de Cadastro do APP. Os métodos realizarão a validação dos dados e a conexões com o banco de dados.
 */
public class CadastroController {

    private Conexao con;
    private String table = "tblUsuario";
    private Context screen;
    private EditText name, email, pass, confirm, birth, user, answer; // Todos os campos utilizados na view serão espelhados no Controller

    public CadastroController() {

    }

    public CadastroController(EditText name, EditText email, EditText pass, EditText confirm, EditText birth,EditText answer, Context screen) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.confirm = confirm;
        this.birth = birth;
        this.screen = screen;
        this.answer = answer;
    }

    public CadastroController(Context screen, EditText user) {
        this.screen = screen;
        this.user = user;
    }

    /**
     * O método 'inputValidate' usa o evento 'addTextChanged' nos campos da view.
     * As informações do input são salvas em uma variavel do tipo String, então é os métodos da classe Validacao são chamados
     * Se o retorno for false, enviará um erro para o input
     */
    public void inputValidate() {
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

    public void inputValidate2() {

        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String msg = userLoginAvaliable() ? null : "Nome de usuário não disponível";
                user.setError(msg);
            }
        });
    }

    /**
     * O método 'userLoginAvaliable' simplifica a utilização do método 'userExist' da classe Validacao.
     * É utilizado para retornar para o usuário se um determinado Nome de Usuário está disponivel para uso.
     *
     * @return boolean
     */

    public boolean userLoginAvaliable() {
        String login = this.user.getText().toString();
        boolean resposta = !Validacao.userExist(login, new Conexao(), this.screen);
        return resposta;
    }

    /**
     * O método 'formIsEmpty' verifica se um dos campos está vazio. Se sim retorna true, se não retorna false
     *
     * @return boolean
     */

    public boolean formIsEmpty() {

        boolean resposta;
        String
                name = this.name.getText().toString(),
                email = this.email.getText().toString(),
                birth = this.birth.getText().toString(),
                pass = this.pass.getText().toString(),
                confirm = this.confirm.getText().toString(),
                anwser = this.answer.getText().toString();

        if (name.isEmpty() || pass.isEmpty() || confirm.isEmpty() || email.isEmpty() || birth.isEmpty()|| anwser.isEmpty()) {
            resposta = true;
        } else {
            resposta = false;
        }

        return resposta;
    }

    /**
     * O método 'validData' verifica se todos os campos estão com informações válidas. Se sim retorna true, se não retorna false
     *
     * @return boolean
     */

    public boolean validData() {

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

    /**
     * O método 'preencheArray' retorna um arraylist do objeto pergunta  para preencher as opções de um spinner na tela de cadastro
     * @return ArrayList<Pergunta>
     */
    public ArrayList<Pergunta> preencheArray(){
        String sql = "SELECT * FROM tblPerguntaSecreta";
        ArrayList<Pergunta> perguntas = new ArrayList<>();
        perguntas.add(new Pergunta(0, "Selecione uma pergunta..."));

        this.con = new Conexao();
        Connection c = this.con.connectDB(this.screen);

        if(c == null){
            NavigationUtil.activityErro(screen);
        }else{
            try {
                con.result = con.command.executeQuery(sql);

                while (con.result.next()){
                    Pergunta p = new Pergunta(con.result.getInt("idPergunta"),con.result.getString("pergunta"));
                    perguntas.add(p);
                }
            }catch (SQLException e){
                Log.e("Erro na Consulta",e.getMessage());
            }
        }
        return perguntas;
    }

    /**
     * O método 'register' recebe um objeto usuario com as informações do usuario presentes na primeira etapa do cadastro.
     * Esse método é chamado na segunda tela, onde ele resgata o input de usuario e com as informaçoes do objeto e do input inserir os dados do usuario no BD
     *
     * @param usuario
     * @return boolean
     */

    public boolean register(Usuario usuario) {
        boolean resposta = false;
        String sql = "INSERT INTO tblUsuario (nome,data_nasc,email,senha,arroba_usuario,fotoUsuario,idPergunta,resposta) VALUES (?,?,?,?,?,?,?,?)";

        this.con = new Conexao();
        Connection c = con.connectDB(screen);

        if(c != null){
            try {
                PreparedStatement ps = con.connect.prepareStatement(sql);
                ps.setString(1, usuario.getUserName());
                ps.setString(2, usuario.getUserBirth());
                ps.setString(3, usuario.getUserEmail());
                ps.setString(4, usuario.getUserPass());
                ps.setString(5, usuario.getUserLogin());
                ps.setInt(7,usuario.getPergunta());
                ps.setString(8,usuario.getResposta());

                byte[] image = usuario.getUserImage();

                if(image != null){
                    ps.setBytes(6, image);
                }else{
                    ps.setNull(6, Types.VARBINARY);
                }

                int res = ps.executeUpdate();

                if (res != 0)
                    resposta = true; //"Usuario cadastrado com sucesso!"


            } catch (SQLException ex) {
                ex.printStackTrace();
                Log.e("SQL erro", "Erro ao executar query: " + ex.getMessage());
                resposta = false; //"Erro ao cadastrar usuário. Tente novamente mais tarde."
            }
        }else{
            NavigationUtil.activityErro(screen);
        }
        return resposta;
    }
}
