package com.example.prjversami.models;

import java.util.Calendar;

public class Comentario {

    private Usuario user;
    private String content;
    private Integer like;
    private Calendar commentDate;

    public Comentario(String content){
        this.setContent(content);
        commentDate = Calendar.getInstance();
        this.like = 0;
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

    public Integer getLike() {
        return like;
    }

    public Calendar getCommentDate() {
        return commentDate;
    }


    public void addLike(boolean isLike){
        if(isLike)
            this.like++;
    }

    public void removeLike(boolean isLike){
        if(!isLike && this.like > 0)
            this.like--;
    }
}
