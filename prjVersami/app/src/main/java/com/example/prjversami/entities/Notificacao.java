package com.example.prjversami.entities;

public class Notificacao {
    public static final int CURTIDA_POST = 1;
    public static final int CURTIDA_COMENTARIO = 2;
    public static final int COMENTARIO = 3;
    public static final int SEGUIU = 4;
    public static final int FEEDBACK_DENUNCIA = 5;
    public static final int POST_DELETADO = 6;

    private int idNotificacao, tipoNotificacao;
    private boolean visualizado;
    private String msgNotificacao;
    private Usuario user;

    public Notificacao(){

    }

    public Notificacao(int tipoNotificacao, String msgNotificacao, Usuario user) {
        this.tipoNotificacao = tipoNotificacao;
        this.msgNotificacao = msgNotificacao;
        this.user = user;
    }

    public int getIdNotificacao() {
        return idNotificacao;
    }

    public void setIdNotificacao(int idNotificacao) {
        this.idNotificacao = idNotificacao;
    }

    public int getTipoNotificacao() {
        return tipoNotificacao;
    }

    public void setTipoNotificacao(int tipoNotificacao) {
        this.tipoNotificacao = tipoNotificacao;
    }

    public boolean isVisualizado() {
        return visualizado;
    }

    public void setVisualizado(boolean visualizado) {
        this.visualizado = visualizado;
    }

    public String getMsgNotificacao() {
        return msgNotificacao;
    }

    public void setMsgNotificacao(String msgNotificacao) {
        this.msgNotificacao = msgNotificacao;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }
}
