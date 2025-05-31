package com.example.prjversami.controllers;

import android.content.Context;
import android.util.Log;

import com.example.prjversami.util.Conexao;
import com.example.prjversami.util.NavigationUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotificacaoController {
    public static final int CURTIDA_POST = 1;
    public static final int CURTIDA_COMENTARIO = 2;
    public static final int COMENTARIO = 3;
    public static final int SEGUIU = 4;

    public static void notificarAcao(int tipoAcao, int idUsuarioAlvo, String arrobaUserAcao, Context context) {

        if (tipoAcao < 1 || tipoAcao > 4) {
            Log.e("Notificação", "Tipo de notificação inválido: " + tipoAcao);
            return;
        }

        String sql = "INSERT INTO tblNotificacao(mensagem,tipoNotificacao,idUsuario) VALUES (?,?,?)";
        String mensagem = arrobaUserAcao;

        switch (tipoAcao){
            case CURTIDA_POST:
                mensagem += " curtiu sua publicação";
                break;
            case CURTIDA_COMENTARIO:
                mensagem += " curtiu seu comentario";
                break;
            case COMENTARIO:
                mensagem += " comentou sua publicação";
                break;
            case SEGUIU:
                mensagem += " seguiu você";
                break;
        }

        Conexao con = new Conexao();
        Connection c = con.connectDB(context);

        if (c == null) {
            NavigationUtil.activityErro(context);
            return;
        }

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,mensagem);
            ps.setInt(2,tipoAcao);
            ps.setInt(3,idUsuarioAlvo);

            if(ps.executeUpdate() < 1)
                Log.e("Erro ao notificar", "Usuário não notificado");

            ps.close();
            c.close();
        }catch (SQLException e){
            Log.e("Erro ao notificar", e.getMessage());
        }
    }
}
