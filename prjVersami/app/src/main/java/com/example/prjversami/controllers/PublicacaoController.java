package com.example.prjversami.controllers;

import android.content.Context;
import android.util.Log;

import com.example.prjversami.entities.Livro;
import com.example.prjversami.entities.Publicacao;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PublicacaoController {
    private Conexao con;
    private String table = "tblPublicacao";
    private Context screen;

    public PublicacaoController(Context screen){
        this.screen = screen;
        this.con = new Conexao();
        con.connectDB(this.screen);
    }

    public List<Publicacao> pegarPublicacoes(int usuarioId){
        List<Publicacao> listaPublicaoes = new ArrayList<>();
        String sql = "SELECT * FROM "+this.table+" WHERE idUsuario = ?";

        try(PreparedStatement ps = con.connect.prepareStatement(sql)){
            ps.setInt(1, usuarioId);

            try(ResultSet rs = ps.executeQuery()){

                while (rs.next()){
                    Publicacao publicacao = new Publicacao();
                    publicacao.setUser(rs.getInt("idUsuario"));
                    publicacao.setBook(rs.getInt("idLivro"));
                    publicacao.setContent(rs.getString("conteudo"));
                    publicacao.setPostDate(rs.getDate("dataPublic").toString());

                    listaPublicaoes.add(publicacao);
                }
            }
        }catch (SQLException e){
            Log.e("Erro na Consulta: ", e.getMessage());
            return null;
        }

        return listaPublicaoes;
    }

    public List<Publicacao> pegarPublicacoes(){
        List<Publicacao> listaPublicaoes = new ArrayList<>();
        String sql = "SELECT * FROM "+this.table+ " ORDER BY dataPublic DESC";

        try{
            con.result = con.command.executeQuery(sql);

            while(con.result.next()){
                Publicacao publicacao = new Publicacao();
                publicacao.setUser(con.result.getInt("idUsuario"));
                publicacao.setBook(con.result.getInt("idLivro"));
                publicacao.setContent(con.result.getString("conteudo"));
                publicacao.setPostDate(con.result.getDate("dataPublic").toString());

                listaPublicaoes.add(publicacao);
            }
        }catch (SQLException e){
            Log.e("Erro na consulta SQL: ", e.getMessage());
        }
        return listaPublicaoes;
    }

    public Usuario pegarUsuario(Integer idUsuario){
        String sql = "SELECT nome, arroba_usuario, fotoUsuario FROM tblUsuario WHERE idUsuario="+idUsuario;
        Usuario user = null;
        try{
            con.result = con.command.executeQuery(sql);

            if(con.result.next()){
                user = new Usuario();
                user.setUserName(con.result.getString("nome"));
                user.setUserLogin(con.result.getString("arroba_usuario"));
                user.setUserImage(con.result.getBytes("fotoUsuario"));
            }
        }catch(SQLException e){
            Log.e("Erro na consulta SQL: ", e.getMessage());
        }

        return user;
    }

    public Livro pegarLivro(Integer idLivro){
        String sql = "SELECT nomeLivro, imgCapa FROM tblLivro WHERE idLivro="+idLivro;
        Livro livro = null;
        try{
            con.result = con.command.executeQuery(sql);

            if(con.result.next()){
                livro = new Livro();
                livro.setTitle(con.result.getString("nomeLivro"));
                livro.setCover(con.result.getBytes("imgCapa"));
            }
        }catch(SQLException e){
            Log.e("Erro na consulta SQL: ", e.getMessage());
        }
        return livro;
    }
}
