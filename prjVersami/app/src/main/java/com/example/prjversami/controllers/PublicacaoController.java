package com.example.prjversami.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.prjversami.entities.Comentario;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
                "(SELECT COUNT(*) FROM tblLikesPorPost lp where lp.idPublicacao=p.idPublicacao ) as 'totLike', " +
                "(SELECT COUNT(*) FROM tblComentario c WHERE c.idPublicacao = p.idPublicacao) AS 'totComent' " +
                "FROM tblPublicacao p " +
                "JOIN tblUsuario u ON p.idUsuario = u.idUsuario " +
                "LEFT JOIN tblLivro l ON l.idLivro = p.idLivro " +
                "WHERE p.idUsuario = ? ORDER BY p.dataPublic DESC";
        int idUserLogado = this.screen.getSharedPreferences("login", Context.MODE_PRIVATE).getInt("id", 0);
        this.con = new Conexao();
        Connection c = this.con.connectDB(screen);

        if(c != null){
            try{
                PreparedStatement ps = con.connect.prepareStatement(sql);
                ps.setInt(1, idUserLogado);
                ps.setInt(2, id);

                this.con.result = ps.executeQuery();

                while (this.con.result.next()){

                    Publicacao pub = new Publicacao();
                    pub.setIdPublicacao(con.result.getInt("idPublicacao"));
                    pub.setContent(con.result.getString("conteudo"));
                    pub.setTotalLikes(con.result.getInt("totLike"));
                    pub.setTotalComentarios(con.result.getInt("totComent"));

                    Timestamp timestamp = con.result.getTimestamp("dataPublic");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    pub.setPostDate(sdf.format(timestamp));

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
                        "u.idUsuario, u.nome, u.arroba_usuario, " +
                        "u.fotoUsuario, l.nomeLivro, " +
                        "l.imgCapa, " +
                        "(SELECT COUNT(*) FROM tblLikesPorPost lp WHERE lp.idUsuario= ? AND lp.idPublicacao= p.idPublicacao) AS 'liked', " +
                        "(SELECT COUNT(*) FROM tblLikesPorPost lp where lp.idPublicacao=p.idPublicacao ) as 'totLike'," +
                        "(SELECT COUNT(*) FROM tblComentario c WHERE c.idPublicacao = p.idPublicacao) AS 'totComent' " +
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
                    pub.setTotalLikes(con.result.getInt("totLike"));
                    pub.setTotalComentarios(con.result.getInt("totComent"));

                    Timestamp timestamp = con.result.getTimestamp("dataPublic");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    pub.setPostDate(sdf.format(timestamp));

                    if(con.result.getInt("liked") > 0)
                        pub.addLike();

                    Usuario user = new Usuario();
                    user.setUserID(con.result.getInt("idUsuario"));
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

    public Publicacao obterPublicacao(int idPublicacao){
        Publicacao publicacao = new Publicacao();
        int idUser = screen.getSharedPreferences("login", Context.MODE_PRIVATE).getInt("id", 0);
        String sql =
                "SELECT p.idPublicacao, " +
                        "p.conteudo, p.dataPublic, " +
                        "u.idUsuario, u.nome, u.arroba_usuario, " +
                        "u.fotoUsuario, l.nomeLivro, " +
                        "l.imgCapa, " +
                        "(SELECT COUNT(*) FROM tblLikesPorPost lp WHERE lp.idUsuario= ? AND lp.idPublicacao= p.idPublicacao) AS 'liked', " +
                        "(SELECT COUNT (*) FROM tblLikesPorPost lp WHERE lp.idPublicacao=p.idPublicacao ) AS 'totLike', " +
                        "(SELECT COUNT (*) FROM tblComentario c WHERE c.idPublicacao = p.idPublicacao) AS 'totComent' " +
                        "FROM tblPublicacao p " +
                        "JOIN tblUsuario u ON p.idUsuario = u.idUsuario " +
                        "LEFT JOIN tblLivro l ON l.idLivro = p.idLivro " +
                        "WHERE p.idPublicacao = ?";

        this.con = new Conexao();
        Connection c = this.con.connectDB(screen);
        if(c == null){
            NavigationUtil.activityErro(screen);
            return null;
        }

        try{
            PreparedStatement ps =  con.connect.prepareStatement(sql);
            ps.setInt(1, idUser);
            ps.setInt(2, idPublicacao);

            this.con.result = ps.executeQuery();

            if(!this.con.result.next()) return null;

            publicacao.setIdPublicacao(con.result.getInt("idPublicacao"));
            publicacao.setContent(con.result.getString("conteudo"));
            publicacao.setTotalLikes(con.result.getInt("totLike"));
            publicacao.setTotalComentarios(con.result.getInt("totComent"));
            if(con.result.getInt("liked") > 0) publicacao.addLike();

            // pegando data e hora no banco para exibir nos posts
            Timestamp timestamp = con.result.getTimestamp("dataPublic");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            publicacao.setPostDate(sdf.format(timestamp));

            Usuario user = new Usuario();
            user.setUserID(con.result.getInt("idUsuario"));
            user.setUserName(con.result.getString("nome"));
            user.setUserLogin(con.result.getString("arroba_usuario"));
            user.setUserImage(con.result.getBytes("fotoUsuario"));
            publicacao.setUsuario(user);

            if(con.result.getString("nomeLivro") != null){
                Livro livro = new Livro();
                livro.setTitle(con.result.getString("nomeLivro"));
                livro.setCover(con.result.getBytes("imgCapa"));
                publicacao.setLivro(livro);
            }
            ps.close();
        }catch (SQLException e){
            Log.e("Erro na Consulta", e.getMessage());
        }
     return publicacao;
    }

    public List<Comentario> listarComentarios(int idPublicação, int idUser){
        List<Comentario> comentarios = new ArrayList<>();
        String sql = "SELECT c.idComentario, c.comentario, u.arroba_usuario, u.fotoUsuario, u.idUsuario, " +
                "(SELECT COUNT(*) FROM tblLikesPorComentario lp WHERE lp.idUsuario= ? AND lp.idComentario = c.idComentario) AS 'liked' " +
                "FROM tblComentario c " +
                "JOIN tblUsuario u ON c.idUsuario = u.idUsuario " +
                "WHERE c.idPublicacao = ? ORDER BY c.data_coment DESC";
        this.con = new Conexao();
        Connection c = this.con.connectDB(screen);

        if(c == null){
            NavigationUtil.activityErro(screen);
            return comentarios;
        }

        try{
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, idUser);
            ps.setInt(2, idPublicação);
            this.con.result = ps.executeQuery();

            while (this.con.result.next()){
                Usuario user = new Usuario();
                user.setUserID(this.con.result.getInt("idUsuario"));
                user.setUserLogin(this.con.result.getString("arroba_usuario"));
                if(this.con.result.getBytes("fotoUsuario") != null)
                    user.setUserImage(this.con.result.getBytes("fotoUsuario"));
                String conteudo = this.con.result.getString("comentario");

                Comentario comentario = new Comentario(conteudo, user);
                comentario.setIdComentario(this.con.result.getInt("idComentario"));
                if(this.con.result.getInt("liked") > 0) comentario.addLike();

                comentarios.add(comentario);
            }
            ps.close();
            c.close();
        }catch (SQLException e){
            Log.e("Erro na Consulta", e.getMessage());
        }
        return comentarios;
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

    public boolean adicionarCurtidaComentario(int idComentario, int idUserLogado){
        boolean res = false;
        String sql = "INSERT INTO tblLikesPorComentario (idUsuario, idComentario) VALUES(?,?)";

        this.con = new Conexao();
        Connection c = this.con.connectDB(this.screen);

        if(c == null){
            NavigationUtil.activityErro(screen);
            return false;
        }

        try{
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,idUserLogado);
            ps.setInt(2,idComentario);
            res = ps.executeUpdate() > 0;
            ps.close();
            c.close();

        }catch (SQLException e){
            Log.e("Erro no Insert SQL: ", e.getMessage());
        }
        return res;
    }

    public boolean removeCurtidaComentario(int idComentario, int idUserLogado){
        boolean res = false;
        String sql = "DELETE FROM tblLikesPorComentario WHERE idUsuario = ? AND idComentario = ?";

        this.con = new Conexao();
        Connection c = this.con.connectDB(this.screen);

        if(c == null){
            NavigationUtil.activityErro(screen);
            return false;
        }

        try{
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,idUserLogado);
            ps.setInt(2,idComentario);
            res = ps.executeUpdate() > 0;
            ps.close();
            c.close();

        }catch (SQLException e){
            Log.e("Erro no Insert SQL: ", e.getMessage());
        }
        return res;
    }

}
