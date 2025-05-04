package com.example.prjversami.controllers;

import android.content.Context;
import android.util.Log;

import com.example.prjversami.entities.Livro;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.Conexao;
import com.example.prjversami.util.NavigationUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PerfilController {
    private Conexao con;
    private Context screen;

    public PerfilController(Context screen){
        this.screen = screen;
    }

    public Usuario obtemPerfil(Integer id){
        String sql = "SELECT nome, arroba_usuario, fotoUsuario, fotoCapa, bio_usuario," +
                "(SELECT COUNT(*) FROM tblSeguidores s1 WHERE s1.idSeguido = u.idUsuario) AS seguidores," +
                "(SELECT COUNT(*) FROM tblSeguidores s2 WHERE s2.idSeguidor = u.idUsuario) AS seguindo " +
                "FROM tblUsuario u WHERE u.idUsuario=?";
        Usuario user = null;
        this.con = new Conexao();
        Connection c = this.con.connectDB(this.screen);

        if(c != null){

            try{
                PreparedStatement ps = this.con.connect.prepareStatement(sql);
                ps.setInt(1, id);

                this.con.result = ps.executeQuery();

                if(this.con.result.next()){
                    user = new Usuario();
                    user.setUserName(con.result.getString("nome"));
                    user.setUserLogin(con.result.getString("arroba_usuario"));
                    user.setUserBio(con.result.getString("bio_usuario"));
                    user.setUserImage(con.result.getBytes("fotoUsuario"));
                    user.setUserCover(con.result.getBytes("fotoCapa"));
                    user.setSeguidores(con.result.getInt("seguidores"));
                    user.setSeguindo(con.result.getInt("seguindo"));
                }

                ps.close();

            }catch (SQLException e){
                Log.e("Erro na consulta SQL: ", e.getMessage());
            }

        }else{
            NavigationUtil.activityErro(this.screen);
        }
        return user;
    }

    public List<Livro> obterLivrosFavoritos(int id){
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT l.nomeLivro, l.imgCapa " +
                "FROM tblLivrosFavoritos lf " +
                "JOIN tblLivro l ON l.idLivro = lf.idLivro " +
                "WHERE lf.idUsuario = ?";

        this.con = new Conexao();
        Connection c = this.con.connectDB(screen);

        if(c != null){
            try{
                PreparedStatement ps = this.con.connect.prepareStatement(sql);
                ps.setInt(1, id);

                this.con.result = ps.executeQuery();

                while(this.con.result.next()){
                    Livro livro = new Livro();
                    livro.setTitle(this.con.result.getString("nomeLivro"));
                    livro.setCover(this.con.result.getBytes("imgCapa"));
                    livros.add(livro);
                }
                ps.close();
            }catch (SQLException e){
                Log.e("Erro na consulta", e.getMessage());
            }
        }else{
            NavigationUtil.activityErro(screen);
        }
        return livros;
    }
}
