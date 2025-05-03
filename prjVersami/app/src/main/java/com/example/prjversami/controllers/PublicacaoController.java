package com.example.prjversami.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.prjversami.entities.Livro;
import com.example.prjversami.entities.Publicacao;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.Conexao;
import com.example.prjversami.util.NavigationUtil;

import java.sql.Connection;
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
    private Context screen;

    public PublicacaoController(Context screen){
        this.screen = screen;
    }

    /**
     * Este método resgata todas as publicações do usuario logado no sistema. O resultado podera ser usuado no recycler view
     * @param
     * @return
     */

    public List<Publicacao> listarPublicacoes(int id){

        List<Publicacao> listaPublicaoes = new ArrayList<>();
        String sql =
                "SELECT p.idPublicacao, " +
                "p.conteudo, p.dataPublic, " +
                "u.nome, u.arroba_usuario, " +
                "u.fotoUsuario, l.nomeLivro, " +
                "l.imgCapa, " +
                "(SELECT COUNT(*) FROM tblLikesPorPost lp WHERE lp.idUsuario= ? AND lp.idPublicacao= p.idPublicacao) AS 'liked', " +
                "(SELECT COUNT (*) FROM tblLikesPorPost lp where lp.idPublicacao=p.idPublicacao ) as 'totLike' " +
                "FROM tblPublicacao p " +
                "JOIN tblUsuario u ON p.idUsuario = u.idUsuario " +
                "LEFT JOIN tblLivro l ON l.idLivro = p.idLivro " +
                "WHERE p.idUsuario = ? ORDER BY p.dataPublic DESC";

        this.con = new Conexao();
        Connection c = this.con.connectDB(screen);

        if(c != null){
            try{
                PreparedStatement ps = con.connect.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setInt(2, id);

                this.con.result = ps.executeQuery();

                while (this.con.result.next()){

                    Publicacao pub = new Publicacao();
                    pub.setIdPublicacao(con.result.getInt("idPublicacao"));
                    pub.setContent(con.result.getString("conteudo"));
                    pub.setPostDate(con.result.getDate("dataPublic").toString());
                    pub.setTotalLikes(con.result.getInt("totLike"));

                    if(con.result.getInt("liked") > 0)
                        pub.addLike();

                    Usuario user = new Usuario();
                    user.setUserName(con.result.getString("nome"));
                    user.setUserLogin(con.result.getString("arroba_usuario"));
                    user.setUserImage(con.result.getBytes("fotoUsuario"));
                    pub.setUsuario(user);

                    if(con.result.getString("nomeLivro") != null){
                        Livro livro = new Livro();
                        livro.setTitle(con.result.getString("nomeLivro"));
                        livro.setCover(con.result.getBytes("imgCapa"));
                        pub.setLivro(livro);
                    }
                    listaPublicaoes.add(pub);
                }

                ps.close();

            }catch (SQLException e){
                Log.e("Erro na Consulta", e.getMessage());
            }
        }else{
            NavigationUtil.activityErro(screen);
        }

        return listaPublicaoes;
    }

    /**
     * Este método resgata todas as publicações dos usuarios do aplicativo. O resultado podera ser usuado no recycler view
     * @return
     */

    public List<Publicacao> listarPublicacoes(){

        SharedPreferences pref = screen.getSharedPreferences("login", Context.MODE_PRIVATE);
        int id = pref.getInt("id", 0);

        List<Publicacao> listaPublicaoes = new ArrayList<>();
        String sql =
                "SELECT p.idPublicacao, " +
                        "p.conteudo, p.dataPublic, " +
                        "u.nome, u.arroba_usuario, " +
                        "u.fotoUsuario, l.nomeLivro, " +
                        "l.imgCapa, " +
                        "(SELECT COUNT(*) FROM tblLikesPorPost lp WHERE lp.idUsuario= ? AND lp.idPublicacao= p.idPublicacao) AS 'liked', " +
                        "(SELECT COUNT (*) FROM tblLikesPorPost lp where lp.idPublicacao=p.idPublicacao ) as 'totLike' " +
                        "FROM tblPublicacao p " +
                        "JOIN tblUsuario u ON p.idUsuario = u.idUsuario " +
                        "LEFT JOIN tblLivro l ON l.idLivro = p.idLivro " +
                        "ORDER BY p.dataPublic DESC";

        this.con = new Conexao();
        Connection c = this.con.connectDB(screen);

        if(c != null){
            try{
                PreparedStatement ps = con.connect.prepareStatement(sql);
                ps.setInt(1, id);

                this.con.result = ps.executeQuery();

                while (this.con.result.next()){

                    Publicacao pub = new Publicacao();
                    pub.setIdPublicacao(con.result.getInt("idPublicacao"));
                    pub.setContent(con.result.getString("conteudo"));
                    pub.setPostDate(con.result.getDate("dataPublic").toString());
                    pub.setTotalLikes(con.result.getInt("totLike"));

                    if(con.result.getInt("liked") > 0)
                        pub.addLike();

                    Usuario user = new Usuario();
                    user.setUserName(con.result.getString("nome"));
                    user.setUserLogin(con.result.getString("arroba_usuario"));
                    user.setUserImage(con.result.getBytes("fotoUsuario"));
                    pub.setUsuario(user);

                    if(con.result.getString("nomeLivro") != null){
                        Livro livro = new Livro();
                        livro.setTitle(con.result.getString("nomeLivro"));
                        livro.setCover(con.result.getBytes("imgCapa"));
                        pub.setLivro(livro);
                    }
                    listaPublicaoes.add(pub);
                }

                ps.close();

            }catch (SQLException e){
                Log.e("Erro na Consulta", e.getMessage());
            }
        }else{
            NavigationUtil.activityErro(screen);
        }

        return listaPublicaoes;
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
