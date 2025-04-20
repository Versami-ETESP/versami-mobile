package com.example.prjversami.entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Publicacao {
    private String content;
    private boolean like;
    private String postDate;
    private Integer book;
    private Integer user;
    private Integer idPublicacao;


    private List<Comentario> comments = new ArrayList<>(); // Postagem tem relação com a classe Comentario

    // Contrutor padrão da classe iniciando com valores padrão: like e data. Torna obrigatorio para um post o titulo e o conteudo
    //  Criei objeto Calendar para definir o horario e data do post

    public Publicacao() {

    }

    public Publicacao(String content) {
        this.setContent(content);
        this.postDate = Calendar.getInstance().toString();
        this.like = false;
    }

    // metodos getters e setters

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isLike() {
        return like;
    }

    public String getPostDate() {
        return postDate;
    }

    public Integer getBook() {
        return book;
    }

    public void setBook(Integer book) {
        this.book = book;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public List<Comentario> getComments() {
        return comments;
    }

    public void setComments(Comentario comment) {
        this.comments.add(comment);
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public Integer getIdPublicacao() {
        return idPublicacao;
    }

    public void setIdPublicacao(Integer idPublicacao) {
        this.idPublicacao = idPublicacao;
    }

    // metodos da classe
    // removi o metodo setLike e dividi a funcionalidade em dois para facilitar o uso no código

    public void addLike(boolean isLike) {
        if (isLike)
            this.like = true;
    }

    public void removeLike(boolean isLike) {
        if (!isLike && this.like == true)
            this.like = false;
    }
}
