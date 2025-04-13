package com.example.prjversami.controllers;

import android.content.Context;
import android.util.Log;

import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.Conexao;

import java.sql.SQLException;

public class PerfilController {
    private Conexao con;
    private String table = "tblUsuario";
    private Context screen;

    public PerfilController(Context screen){
        this.screen = screen;
        this.con = new Conexao();
        con.connectDB(this.screen);
    }

    public Usuario obtemPerfil(Integer id){
        String sql = "SELECT nome, arroba_usuario, fotoUsuario, fotoCapa, bio_usuario FROM tblUsuario WHERE idUsuario="+id;
        Usuario user = null;
        try{
            con.result = con.command.executeQuery(sql);

            if(con.result.next()){
                user = new Usuario();
                user.setUserName(con.result.getString("nome"));
                user.setUserLogin(con.result.getString("arroba_usuario"));
                user.setUserBio(con.result.getString("bio_usuario"));
                user.setUserImage(con.result.getBytes("fotoUsuario"));
                user.setUserCover(con.result.getBytes("fotoCapa"));
            }

            sql = "SELECT COUNT(*) AS 'seguidores' FROM tblSeguidores WHERE idSeguido="+id;
            con.result = con.command.executeQuery(sql);

            if(con.result.next())
                user.setSeguidores(con.result.getInt("seguidores"));

            sql = "SELECT COUNT(*) AS 'seguindo' FROM tblSeguidores WHERE idSeguidor="+id;
            con.result = con.command.executeQuery(sql);

            if(con.result.next())
                user.setSeguindo(con.result.getInt("seguindo"));

        }catch(SQLException e){
            Log.e("Erro na consulta SQL: ", e.getMessage());
        }

        return user;
    }
}
