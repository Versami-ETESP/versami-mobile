package com.example.prjversami.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.prjversami.entities.Livro;
import com.example.prjversami.entities.Publicacao;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta classe faz os procedimentos de conexão ao banco de dados, servindo de controlador das publicações dos usuarios.
 */
public class PublicacaoController {
    private Conexao con;
    private String table = "tblPublicacao";
    private Context screen;

    public PublicacaoController(Context screen){
        this.screen = screen;
    }

    /**
     * Este método resgata todas as publicações do usuario logado no sistema. O resultado podera ser usuado no recycler view
     * @param usuarioId
     * @return
     */
    public List<Publicacao> pegarPublicacoes(int usuarioId){
        List<Publicacao> listaPublicaoes = new ArrayList<>();
        String sql = "SELECT * FROM "+this.table+" WHERE idUsuario = ?";

        this.con = new Conexao();
        con.connectDB(this.screen);

        try(PreparedStatement ps = con.connect.prepareStatement(sql)){
            ps.setInt(1, usuarioId);

            try(ResultSet rs = ps.executeQuery()){

                while (rs.next()){
                    Publicacao publicacao = new Publicacao();
                    publicacao.setUser(rs.getInt("idUsuario"));
                    publicacao.setBook(rs.getInt("idLivro"));
                    publicacao.setContent(rs.getString("conteudo"));
                    publicacao.setPostDate(rs.getDate("dataPublic").toString());
                    publicacao.setIdPublicacao(rs.getInt("idPublicacao"));

                    con.result = con.command.executeQuery("SELECT COUNT(*) AS 'liked' FROM tblLikesPorPost WHERE idUsuario="+usuarioId+" AND idPublicacao="+publicacao.getIdPublicacao());

                    if(con.result.next() && con.result.getInt("liked") != 0){
                        publicacao.addLike(true);
                    }else{
                        publicacao.removeLike(false);
                    }

                    listaPublicaoes.add(publicacao);
                }
            }
        }catch (SQLException e){
            Log.e("Erro na Consulta: ", e.getMessage());
            return null;
        }

        return listaPublicaoes;
    }

    /**
     * Este método resgata todas as publicações dos usuarios do aplicativo. O resultado podera ser usuado no recycler view
     * @return
     */
    public List<Publicacao> pegarPublicacoes(){
        List<Publicacao> listaPublicaoes = new ArrayList<>();
        String sql = "SELECT * FROM "+this.table+ " ORDER BY dataPublic DESC";

        this.con = new Conexao();
        con.connectDB(this.screen);

        try{
            con.result = con.command.executeQuery(sql);
            SharedPreferences pref = screen.getSharedPreferences("login", Context.MODE_PRIVATE);
            int userAtual= pref.getInt("id", 0);
            Statement stmt = con.connect.createStatement();

            while(con.result.next()){
                Publicacao publicacao = new Publicacao();
                publicacao.setUser(con.result.getInt("idUsuario"));
                publicacao.setBook(con.result.getInt("idLivro"));
                publicacao.setContent(con.result.getString("conteudo"));
                publicacao.setPostDate(con.result.getDate("dataPublic").toString());
                publicacao.setIdPublicacao(con.result.getInt("idPublicacao"));


                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS 'liked' FROM tblLikesPorPost WHERE idUsuario="+userAtual+" AND idPublicacao="+publicacao.getIdPublicacao());
                if(rs.next() && rs.getInt("liked") != 0){
                    publicacao.addLike(true);
                }else{
                    publicacao.removeLike(false);
                }
                rs.close();
                listaPublicaoes.add(publicacao);
            }
            stmt.close();
        }catch (SQLException e){
            Log.e("Erro na consulta SQL: ", e.getMessage());
        }
        return listaPublicaoes;
    }

    /**
     * Este método resgata as informações do usuario para serem utilizadas na publicação. Preenchendo assim o nome, o arroba e outros dados do usuario
     * @param idUsuario
     * @return
     */
    public Usuario pegarUsuario(Integer idUsuario){
        String sql = "SELECT nome, arroba_usuario, fotoUsuario FROM tblUsuario WHERE idUsuario="+idUsuario;
        Usuario user = null;

        this.con = new Conexao();
        con.connectDB(this.screen);

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

    /**
     * Este método regata as informações do titulo e capa do livro, caso a publicação possua um livro vinculado.
     * @param idLivro
     * @return
     */
    public Livro pegarLivro(Integer idLivro){
        String sql = "SELECT nomeLivro, imgCapa FROM tblLivro WHERE idLivro="+idLivro;
        Livro livro = null;

        this.con = new Conexao();
        con.connectDB(this.screen);

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

    /**
     * Este método consulta no BD e retorna o total de curtidas de uma publicação.
     * @param id
     * @return
     */
    public String getCurtidas(Integer id){
        String sql = "SELECT COUNT(*) AS 'totCurtidas' FROM tblLikesPorPost WHERE idPublicacao="+id;
        String resultado = "";

        this.con = new Conexao();
        con.connectDB(this.screen);

        try{
            con.result = con.command.executeQuery(sql);

            if(con.result.next()){
                resultado = Integer.toString(con.result.getInt("totCurtidas"));
            }
        }catch(SQLException e){
            Log.e("Erro na consulta SQL: ", e.getMessage());
        }
        return resultado;
    }

    /**
     * Este método insere uma curtida no post. Essa curtida é gravada no banco de dados
     * @param idPost int
     */
    public void setCurtidas(Integer idPost){
        SharedPreferences pref = screen.getSharedPreferences("login", Context.MODE_PRIVATE);
        Integer idUsuario = pref.getInt("id", 0);
        String sql = "INSERT INTO tblLikesPorPost VALUES("+idUsuario+","+idPost+")";

        this.con = new Conexao();
        con.connectDB(this.screen);

        try{
            int res = con.command.executeUpdate(sql);

            if(res == 0)
                Log.e("Erro", "Valor nao inserido no Banco");

        }catch (SQLException e){
            Log.e("Erro no Insert SQL: ", e.getMessage());
        }
    }

    /**
     * Este método remove uma curtida da publicação no banco de dados.
     * @param idPost
     */
    public void removeCurtidas(Integer idPost){
        SharedPreferences pref = screen.getSharedPreferences("login", Context.MODE_PRIVATE);
        Integer idUsuario = pref.getInt("id", 0);
        String sql = "DELETE FROM tblLikesPorPost WHERE idUsuario="+idUsuario+" AND idPublicacao="+idPost;

        this.con = new Conexao();
        con.connectDB(this.screen);

        try{
                if (con.connect == null || con.connect.isClosed()) {
                    Log.e("Conexão", "Conexão com banco está fechada. Reabra a conexão aqui.");
                    return;
                }

            int res = con.command.executeUpdate(sql);

            if(res == 0)
                Log.e("Erro", "Valor nao foi deletado do Banco");

        }catch (SQLException e){
            Log.e("Erro no Delete SQL: ", e.getMessage());
        }
    }
}
