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

    public Usuario obtemPerfil(Integer idUsuarioVisualizado){
        int idUsuarioLogado = screen.getSharedPreferences("login", Context.MODE_PRIVATE).getInt("id", 0);
        String sql = "SELECT nome, arroba_usuario, fotoUsuario, fotoCapa, bio_usuario," +
                "(SELECT COUNT(*) FROM tblSeguidores s1 WHERE s1.idSeguido = u.idUsuario) AS seguidores," +
                "(SELECT COUNT(*) FROM tblSeguidores s2 WHERE s2.idSeguidor = u.idUsuario) AS seguindo, " +
                "(SELECT COUNT(*) FROM tblSeguidores s3 WHERE s3.idSeguidor = ? AND s3.idSeguido = ?) AS seguiu " +
                "FROM tblUsuario u WHERE u.idUsuario=?";
        Usuario user = null;
        this.con = new Conexao();
        Connection c = this.con.connectDB(this.screen);

        if(c != null){

            try{
                PreparedStatement ps = this.con.connect.prepareStatement(sql);
                ps.setInt(1, idUsuarioLogado);
                ps.setInt(2, idUsuarioVisualizado);
                ps.setInt(3, idUsuarioVisualizado);

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
                    user.setSeguidor(con.result.getInt("seguiu") > 0);
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

    public boolean atualizarDados(Usuario user, int opc){
        boolean resultado = false;
        String sql = "";
        switch (opc){
            case 1:
            sql = "UPDATE tblUsuario SET nome=?, arroba_usuario=?, bio_usuario=?, fotoUsuario=?, fotoCapa=? " +
                    "WHERE idUsuario=?";
            break;
            case 2:
                sql = "UPDATE tblUsuario SET nome=?, arroba_usuario=?, bio_usuario=?, fotoUsuario=? " +
                        "WHERE idUsuario=?";
                break;
            case 3:
                sql = "UPDATE tblUsuario SET nome=?, arroba_usuario=?, bio_usuario=?, fotoCapa=? " +
                        "WHERE idUsuario=?";
                break;
            case 4:
                sql = "UPDATE tblUsuario SET nome=?, arroba_usuario=?, bio_usuario=? " +
                        "WHERE idUsuario=?";
                break;
        }

        this.con = new Conexao();
        Connection c = this.con.connectDB(screen);

        if(c != null){
            try{
               PreparedStatement ps = this.con.connect.prepareStatement(sql);
               ps.setString(1, user.getUserName());
               ps.setString(2, user.getUserLogin());
               ps.setString(3, user.getUserBio());

                switch (opc){
                    case 1:
                        ps.setBytes(4,user.getUserImage());
                        ps.setBytes(5,user.getUserCover());
                        ps.setInt(6,user.getUserID());
                        break;
                    case 2:
                        ps.setBytes(4,user.getUserImage());
                        ps.setInt(5,user.getUserID());
                        break;
                    case 3:
                        ps.setBytes(4,user.getUserCover());
                        ps.setInt(5,user.getUserID());
                        break;
                    case 4:
                        ps.setInt(4,user.getUserID());
                        break;
                }

               resultado = ps.executeUpdate() > 0;

            }catch (SQLException e){
                Log.e("Erro no Update", e.getMessage());
            }
        }else{
            NavigationUtil.activityErro(screen);
        }

        return resultado;
    }
}
