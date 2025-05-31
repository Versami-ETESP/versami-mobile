package com.example.prjversami.controllers;

import android.content.Context;
import android.util.Log;

import com.example.prjversami.entities.Notificacao;
import com.example.prjversami.util.Conexao;
import com.example.prjversami.util.NavigationUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificacaoController {
    public static final int CURTIDA_POST = 1;
    public static final int CURTIDA_COMENTARIO = 2;
    public static final int COMENTARIO = 3;
    public static final int SEGUIU = 4;

    private Conexao con;
    private Context screen;

    public NotificacaoController(Context context){
        this.screen = context;
    }

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

    public List<Notificacao> listarNotificacoes(int idUserLogado){
        String sql = "SELECT idNotificacao, mensagem, tipoNotificacao, visualizada FROM tblNotificacao WHERE idUsuario = ? AND visualizada = 0";
        List<Notificacao> notificacoes = new ArrayList<>();

        this.con = new Conexao();
        Connection c = this.con.connectDB(screen);

        if(c == null){
            NavigationUtil.activityErro(screen);
            return null;
        }

        try{
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, idUserLogado);

            this.con.result = ps.executeQuery();

            while (this.con.result.next()){
                Notificacao notificacao = new Notificacao();
                notificacao.setIdNotificacao(this.con.result.getInt("idNotificacao"));
                notificacao.setTipoNotificacao(this.con.result.getInt("tipoNotificacao"));
                notificacao.setMsgNotificacao(this.con.result.getString("mensagem"));
                notificacao.setVisualizado(this.con.result.getBoolean("visualizada"));

                notificacoes.add(notificacao);
            }

            ps.close();
            c.close();
        }catch (SQLException e){
            Log.e("Erro obter notificacoes", e.getMessage());
        }

        return notificacoes;
    }

    public void marcarVisualizada(Notificacao notificacao){
        String sql = "UPDATE tblNotificacao SET visualizada = 1 WHERE idNotificacao = ?";
        this.con = new Conexao();
        Connection c = this.con.connectDB(screen);

        if(c == null){
            NavigationUtil.activityErro(screen);
            return;
        }

        try{
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, notificacao.getIdNotificacao());

            if(ps.executeUpdate() < 1)
                Log.e("Erro Update", "Não foi possivel marcar a visualização");

            ps.close();
            c.close();
        }catch (SQLException e){
            Log.e("Erro Update", e.getMessage());
        }
    }

}
