package com.example.prjversami.models;

import java.util.ArrayList;
import java.util.List;

public class Usuario extends Perfil{
    // Declaração de atributos

    private String userBirth, userBio, userImage;

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

    // Declaração de métodos
    // metodos getters e setters

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

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public List<Publicacao> getPosts() {
        return posts;
    }

    public void setPosts(Publicacao post) {
        this.posts.add(post);
    }
}
