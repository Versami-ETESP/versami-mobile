package com.example.prjversami.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Publicacao {
    private String title, content;
    private Integer like;
    private Calendar postDate;
    private Livro book;
    private Usuario user;

    private List<Comentario> comments = new ArrayList<>(); // Postagem tem relação com a classe Comentario

    // Contrutor padrão da classe iniciando com valores padrão: like e data. Torna obrigatorio para um post o titulo e o conteudo
    //  Criei objeto Calendar para definir o horario e data do post

    public Publicacao(String title, String content) {
        this.setTitle(title);
        this.setContent(content);
        this.postDate = Calendar.getInstance();
        this.like = 0;
    }

    // metodos getters e setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Calendar getPostDate() {
        return postDate;
    }

    public Livro getBook() {
        return book;
    }

    public void setBook(Livro book) {
        this.book = book;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public List<Comentario> getComments() {
        return comments;
    }

    public void setComments(Comentario comment) {
        this.comments.add(comment);
    }

    // metodos da classe
    // removi o metodo setLike e dividi a funcionalidade em dois para facilitar o uso no código

    public void addLike(boolean isLike){
        if(isLike)
            this.like++;
    }

    public void removeLike(boolean isLike){
        if(!isLike && this.like > 0)
            this.like--;
    }
}
