package com.example.prjversami.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.Conexao;
import com.example.prjversami.util.Criptografia;
import com.example.prjversami.util.ImagensUtil;
import com.example.prjversami.util.NavigationUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Esta classe faz o controle do processo de login do APP. Os metodos farão a consulta no banco de dados para certificar que o usuário foi cadastrado.
 */
public class LoginController {

    private Conexao con;
    private String table = "tblUsuario";
    private Context screen;
    private SQLiteDatabase db;


    public LoginController(Context screen){
        this.screen = screen;
        this.con = new Conexao();
        con.connectDB(this.screen);
    }

    /**
     *  O método 'guardarDados' recebe as informações do usuario e guarda no banco SQLite os dados
     *  de id e arroba, além de salvar a foto de perfil no armazenamento do APP.
     * @param id
     * @param nome
     * @param arroba
     * @param fotoPerfil
     */
    public void guardarDados(int id, String nome, String arroba, byte[] fotoPerfil){
        SharedPreferences pref = screen.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("id", id);
        editor.putString("nome", nome);
        editor.putString("arroba", arroba);

        editor.apply();

        if(fotoPerfil != null)
            ImagensUtil.salvarImagem(fotoPerfil,"imagemPerfil.jpeg", this.screen);

    }

    public String[] resgatarPergunta(String user){
        String sql = "SELECT p.pergunta, u.resposta " +
                "FROM tblUsuario u " +
                "JOIN tblPerguntaSecreta p on u.idPergunta = p.idPergunta " +
                "WHERE arroba_usuario=?";
        String[] pergunta = new String[2];

        con = new Conexao();
        Connection c = con.connectDB(screen);
        if(c != null){
            try{
                PreparedStatement ps = con.connect.prepareStatement(sql);
                ps.setString(1,user);
                con.result = ps.executeQuery();

                if(con.result.next()){
                    pergunta[0] = con.result.getString("pergunta");
                    pergunta[1] = con.result.getString("resposta");
                }
            }catch (SQLException e){
                Log.e("Erro na Consulta", e.getMessage());
            }
        }else{
            NavigationUtil.activityErro(screen);
        }
        return pergunta;
    }

    public boolean alteraSenha(Usuario user, String inputResp){
        boolean retorno = false;
        this.con = new Conexao();
        Connection c = con.connectDB(screen);

        if(c != null){
            if(user.getResposta().equals(Criptografia.criptografaString(inputResp))){
                try{
                    String sql = "UPDATE tblUsuario SET senha=? WHERE arroba_usuario=?";
                    PreparedStatement ps = con.connect.prepareStatement(sql);
                    ps.setString(1,user.getUserPass());
                    ps.setString(2,user.getUserLogin());

                    retorno = ps.executeUpdate() > 0;
                    ps.close();
                }catch (SQLException e){
                    Log.e("Erro Troca Senha",e.getMessage());
                }
            }
        }else{
            NavigationUtil.activityErro(screen);
        }
        return retorno;
   }

    /**
     * O método 'login' recebe o arroba e a senha do usuário, faz a validação se existe ou não os dados no BD.
     * Caso sim, usa o método 'guardarDados' para gravar a sessão e retorna true para a view de login.
     * @param nome
     * @param senha
     * @return
     */

    public boolean login(String nome, String senha){

        boolean retorno = false;
        String inputHash = Criptografia.criptografaString(senha);
        String sql = "SELECT idUsuario, nome, arroba_usuario, fotoUsuario,senha FROM tblUsuario WHERE arroba_usuario = ?";
        this.con = new Conexao();
        Connection c = con.connectDB(screen);

        if(c != null){
            try{
                PreparedStatement ps = con.connect.prepareStatement(sql);
                ps.setString(1, nome);

                con.result = ps.executeQuery();

                if(con.result.next() && con.result.getString("senha").equals(inputHash)){
                    Integer id = con.result.getInt("idUsuario");
                    String nomeUser = con.result.getString("nome");
                    String arroba = con.result.getString("arroba_usuario");
                    byte[] imagem = con.result.getBytes("fotoUsuario");
                    guardarDados(id,nomeUser,arroba,imagem);
                    retorno = true;
                }
            }catch (Exception e){
                Log.e("SQL erro", "Erro ao executar query: " + e.getMessage());
            }
        }else{
            NavigationUtil.activityErro(screen);
        }
        return retorno;
    }
}
