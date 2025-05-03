package com.example.prjversami.entities;

import java.util.ArrayList;
import java.util.List;

public class Usuario extends Perfil{
    // Declaração de atributos

    private String userBirth, userBio, resposta;

    private byte[] userImage, userCover;

    private Integer seguidores, seguindo, pergunta;

    private List<Publicacao> posts = new ArrayList<>(); // esse array armazena no todos os objetos posts relacionados ao usuario

    public Usuario(){

    }

    public Usuario(String name, String login, String email, String birth, String pass){
        this.setUserName(name);
        this.setUserLogin(login);
        this.setUserEmail(email);
        this.setUserPass(pass);
        this.setUserBirth(birth);
    }

    public Usuario(String name, String login, String email, String birth, String pass, String answer, Integer question){
        this.setUserName(name);
        this.setUserLogin(login);
        this.setUserEmail(email);
        this.setUserPass(pass);
        this.setUserBirth(birth);
        this.setPergunta(question);
        this.setResposta(answer);
    }

    // Declaração de métodos
    // metodos getters e setters


    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public Integer getPergunta() {
        return pergunta;
    }

    public void setPergunta(Integer pergunta) {
        this.pergunta = pergunta;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public List<Publicacao> getPosts() {
        return posts;
    }

    public void setPosts(Publicacao post) {
        this.posts.add(post);
    }

    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    public byte[] getUserCover() {
        return userCover;
    }

    public void setUserCover(byte[] userCover) {
        this.userCover = userCover;
    }

    public Integer getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(Integer seguidores) {
        this.seguidores = seguidores;
    }

    public Integer getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(Integer seguindo) {
        this.seguindo = seguindo;
    }
}
