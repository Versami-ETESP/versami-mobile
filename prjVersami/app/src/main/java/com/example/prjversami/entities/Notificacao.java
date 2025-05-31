package com.example.prjversami.entities;

public class Notificacao {

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
