package com.example.prjversami.controllers;

import android.content.Context;
import android.util.Log;

import com.example.prjversami.entities.Autor;
import com.example.prjversami.entities.Genero;
import com.example.prjversami.entities.Livro;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.Conexao;
import com.example.prjversami.util.NavigationUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public List<Livro> pesquisarLivros(String termoPesquisa){
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT idLivro, nomeLivro, imgCapa FROM tblLivro WHERE nomeLivro LIKE ?";

        this.con = new Conexao();
        Connection c = this.con.connectDB(screen);

        if(c == null){
            NavigationUtil.activityErro(screen);
            return null;
        }

        try{
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, "%"+termoPesquisa+"%");
            this.con.result = ps.executeQuery();

            while(this.con.result.next()){
                Livro livro = new Livro();
                livro.setBookID(this.con.result.getInt("idLivro"));
                livro.setTitle(this.con.result.getString("nomeLivro"));
                if(this.con.result.getBytes("imgCapa") != null)
                    livro.setCover(this.con.result.getBytes("imgCapa"));

                livros.add(livro);
            }
            ps.close();
            c.close();
        }catch (SQLException e){
            Log.e("Erro na Consulta", e.getMessage());
        }
        return livros;
    }

    public List<Usuario> pesquisarUsuarios(String termoPesquisa){
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT idUsuario, nome, fotoUsuario FROM tblUsuario WHERE arroba_usuario LIKE ?";

        this.con = new Conexao();
        Connection c = this.con.connectDB(screen);

        if(c == null){
            NavigationUtil.activityErro(screen);
            return null;
        }

        try{
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, "%"+termoPesquisa+"%");
            this.con.result = ps.executeQuery();

            while(this.con.result.next()){
                Usuario user = new Usuario();
                user.setUserID(this.con.result.getInt("idUsuario"));
                user.setUserName(this.con.result.getString("nome"));
                if(this.con.result.getBytes("fotoUsuario") != null)
                    user.setUserImage(this.con.result.getBytes("fotoUsuario"));

                usuarios.add(user);
            }
            ps.close();
            c.close();
        }catch (SQLException e){
            Log.e("Erro na Consulta", e.getMessage());
        }
        return usuarios;
    }

    public Livro obterInfoLivro(int idLivro, int idUser){
        String sql = "SELECT l.nomeLivro, l.descLivro, l.imgCapa, a.nomeAutor, g.nomeGenero, " +
                "(SELECT COUNT(*) FROM tblLivrosFavoritos lf WHERE lf.idLivro = l.idLivro AND lf.idUsuario = ?) AS livroFavorito " +
                "FROM tblLivro l " +
                "JOIN tblAutor a ON l.idAutor = a.idAutor " +
                "JOIN tblGenero g ON g.idGenero = l.idGenero " +
                "WHERE l.idLivro = ?";
        Livro livro = new Livro();
        this.con = new Conexao();
        Connection c = this.con.connectDB(screen);

        if(c == null){
            NavigationUtil.activityErro(screen);
            return null;
        }

        try{
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,idUser);
            ps.setInt(2,idLivro);

            this.con.result = ps.executeQuery();

            if(this.con.result.next()){
                Genero genero = new Genero();
                genero.setType(this.con.result.getString("nomeGenero"));
                Autor autor = new Autor();
                autor.setName(this.con.result.getString("nomeAutor"));

                livro.setTitle(this.con.result.getString("nomeLivro"));
                livro.setBookID(idLivro);
                livro.setSummary(this.con.result.getString("descLivro"));
                livro.setFavorite(this.con.result.getInt("livroFavorito") > 0);
                livro.setAutor(autor);
                livro.setGenre(genero);
                if(this.con.result.getBytes("imgCapa") != null) livro.setCover(this.con.result.getBytes("imgCapa"));
            }
            ps.close();
            c.close();

        }catch (SQLException e){
            Log.e("Erro na Consulta", e.getMessage());
        }
        return livro;
    }

    public boolean definirLivroFavorito(int idLivro, int idUser){
        String sql = "INSERT INTO tblLivrosFavoritos (idUsuario, idLivro) VALUES (?,?)";
        boolean resultado = false;

        this.con = new Conexao();
        Connection c = this.con.connectDB(screen);

        if(c == null){
            NavigationUtil.activityErro(screen);
            return false;
        }

        try{
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, idUser);
            ps.setInt(2,idLivro);

            resultado = ps.executeUpdate() > 0;
            ps.close();
            c.close();

        }catch (SQLException e){
            Log.e("Erro no Insert", e.getMessage());
        }

        return resultado;
    }

    public boolean removerLivroFavorito(int idLivro, int idUser){
        String sql = "DELETE FROM tblLivrosFavoritos WHERE idUsuario = ? AND idLivro = ?";
        boolean resultado = false;

        this.con = new Conexao();
        Connection c = this.con.connectDB(screen);

        if(c == null){
            NavigationUtil.activityErro(screen);
            return false;
        }

        try{
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, idUser);
            ps.setInt(2,idLivro);

            resultado = ps.executeUpdate() > 0;
            ps.close();
            c.close();

        }catch (SQLException e){
            Log.e("Erro no Insert", e.getMessage());
        }

        return resultado;
    }
}
