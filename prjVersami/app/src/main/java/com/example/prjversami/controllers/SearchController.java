package com.example.prjversami.controllers;

import android.content.Context;
import android.util.Log;

import com.example.prjversami.entities.Livro;
import com.example.prjversami.util.Conexao;
import com.example.prjversami.util.NavigationUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchController {
    private Conexao con;
    private Context screen;

    public SearchController(Context screen){
        this.screen = screen;
    }

    public List<Livro> retornaTopLivros(){
        String sql = "SELECT TOP 10 l.idLivro,l.nomeLivro,l.imgCapa,COUNT(p.idPublicacao) AS totalPublicacoes " +
                "FROM tblLivro l " +
                "LEFT JOIN tblPublicacao p ON l.idLivro = p.idLivro " +
                "GROUP BY l.idLivro, l.nomeLivro, l.imgCapa " +
                "ORDER BY totalPublicacoes DESC, l.nomeLivro";
        List<Livro> livros = new ArrayList<>();
        this.con = new Conexao();
        Connection c = this.con.connectDB(this.screen);

        if(c == null){
            NavigationUtil.activityErro(screen);
            return null;
        }

        try{
            this.con.result = this.con.command.executeQuery(sql);

            while(this.con.result.next()){
                Livro livro = new Livro();
                livro.setBookID(this.con.result.getInt("idLivro"));
                livro.setTitle(this.con.result.getString("nomeLivro"));
                livro.setCover(this.con.result.getBytes("imgCapa"));

                livros.add(livro);
            }
        }catch (SQLException e){
            Log.e("Erro na Consulta", e.getMessage());
        }

        return livros;
    }
}
