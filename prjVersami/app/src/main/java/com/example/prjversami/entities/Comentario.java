package com.example.prjversami.entities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Comentario {

    private int idComentario;
    private Usuario user;
    private String content;
    private String commentDate;
    private boolean like;

    public Comentario() {

    }

    public Comentario(String content, Usuario user){
        this.setContent(content);
        this.user = user;

        //definindo a data
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date agora = new Date();
        this.commentDate = df.format(agora);
    }


    public int getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(int idComentario) {
        this.idComentario = idComentario;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public boolean isLike() {
        return like;
    }
    public void addLike() {
        this.like = true;
    }

    public void removeLike() {
        this.like = false;
    }

}
