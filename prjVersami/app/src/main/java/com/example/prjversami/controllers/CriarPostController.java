package com.example.prjversami.controllers;

import android.content.Context;
import android.util.Log;

import com.example.prjversami.entities.Livro;
import com.example.prjversami.entities.Publicacao;
import com.example.prjversami.util.Conexao;
import com.example.prjversami.util.NavigationUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CriarPostController {
    private Conexao con;
    private Context screen;

    public CriarPostController(Context screen){
        this.screen = screen;
    }

    public List<Livro> buscarLivros(String nomeLivro){
        String sql = "SELECT idLivro, nomeLivro, imgCapa FROM tblLivro WHERE nomeLivro LIKE ? ORDER BY nomeLivro";
        List<Livro> livros = new ArrayList<>();

        con = new Conexao();
        Connection c = con.connectDB(screen);

        if(c != null){
            try(PreparedStatement ps = con.connect.prepareStatement(sql)){
                ps.setString(1,"%"+nomeLivro+"%");

                try(ResultSet rs = ps.executeQuery()){

                    while (rs.next()){
                        Livro livro = new Livro();
                        livro.setBookID(rs.getInt("idLivro"));
                        livro.setTitle(rs.getString("nomeLivro"));
                        livro.setCover(rs.getBytes("imgCapa"));

                        livros.add(livro);
                    }
                }
            }catch(SQLException e){
                Log.e("Erro na consulta", e.getMessage());
            }
        }else{
            NavigationUtil.activityErro(screen);
        }
        return livros;
    }

    public boolean postarPublicação(Publicacao publicacao){
        String sql = "INSERT INTO tblPublicacao (conteudo,dataPublic,idUsuario,idLivro) VALUES (?,?,?,?);";
        boolean resposta = false;

        con = new Conexao();
        Connection c = con.connectDB(screen);

        if(c != null){
            try(PreparedStatement ps = con.connect.prepareStatement(sql)){
                ps.setString(1,publicacao.getContent());
                ps.setString(2,publicacao.getPostDate());
                ps.setInt(3, publicacao.getUsuario().getUserID());

                if(publicacao.getLivro().getBookID() != null)
                    ps.setInt(4, publicacao.getLivro().getBookID());
                else
                    ps.setNull(4, Types.INTEGER);

                int retorno = ps.executeUpdate();
                resposta = retorno > 0;

            }catch(SQLException e){
                Log.e("Erro no Insert", e.getMessage());
            }
        }else{
            NavigationUtil.activityErro(screen);
        }
        return resposta;
    }
}
