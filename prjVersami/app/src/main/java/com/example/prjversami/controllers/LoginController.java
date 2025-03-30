package com.example.prjversami.controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.prjversami.util.Conexao;
import com.example.prjversami.util.ImagensUtil;

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

        try{
            this.db = this.screen.openOrCreateDatabase("guardarDados",Context.MODE_PRIVATE,null);

            db.execSQL("CREATE TABLE IF NOT EXISTS usuario" +
                    "(ID integer PRIMARY KEY," +
                    "NOME varchar," +
                    "ARROBA varchar)");

            db.execSQL("INSERT INTO usuario VALUES ("+id+",'"+nome+"','"+arroba+"')");


            if(fotoPerfil != null)
                ImagensUtil.salvarImagem(fotoPerfil,"imagemPerfil.jpeg", this.screen);

        }catch(Exception e){
            Log.e("DB_ERROR", "Erro ao guardar dados: " + e.getMessage());
        }
    }

    /**
     * O método 'login' recebe o arroba e a senha do usuário, faz a validação se existe ou não os dados no BD.
     * Caso sim, usa o método 'guardarDados' para gravar a sessão e retorna true para a view de login.
     * @param nome
     * @param senha
     * @return
     */

    public boolean login(String nome, String senha){

        String sql = "SELECT idUsuario, nome, arroba_usuario, fotoUsuario FROM "+this.table+" WHERE arroba_usuario = ? AND senha = ?";

        try(PreparedStatement ps = con.connect.prepareStatement(sql)){

            ps.setString(1,nome);
            ps.setString(2,senha);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Integer id = rs.getInt("idUsuario");
                    String nomeUser = rs.getString("nome");
                    String arroba = rs.getString("arroba_usuario");
                    byte[] imagem = rs.getBytes("fotoUsuario");
                    guardarDados(id,nomeUser,arroba,imagem);

                    return true;
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
            Log.e("SQL erro", "Erro ao executar query: " + e.getMessage());
        }

        return false;
    }
}
