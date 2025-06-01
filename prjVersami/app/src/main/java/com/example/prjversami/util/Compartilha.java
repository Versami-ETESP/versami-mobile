package com.example.prjversami.util;

import com.example.prjversami.entities.Livro;
import com.example.prjversami.entities.Publicacao;

public class Compartilha {
    private static Livro livro;

    //utilizado para alterar estado do post
    public static boolean publicacaoExcluida = false;
    private static boolean likeAlterado = false;
    private static Publicacao publicacaoAlterada = null;

    //dados para editar perfil

    private static boolean editouPerfil = false;

    //get e set do editar perfil

    public static boolean isEditouPerfil() {
        return editouPerfil;
    }

    public static void setEditouPerfil(boolean editouPerfil) {
        Compartilha.editouPerfil = editouPerfil;
    }

    //getters e setters do post


    public static boolean isPublicacaoExcluida() {
        return publicacaoExcluida;
    }

    public static void setPublicacaoExcluida(boolean publicacaoExcluida) {
        Compartilha.publicacaoExcluida = publicacaoExcluida;
    }

    public static boolean isLikeAlterado() {
        return likeAlterado;
    }

    public static void setLikeAlterado(boolean likeAlterado) {
        Compartilha.likeAlterado = likeAlterado;
    }

    public static Publicacao getPublicacaoAlterada() {
        return publicacaoAlterada;
    }

    public static void setPublicacaoAlterada(Publicacao publicacaoAlterada) {
        Compartilha.publicacaoAlterada = publicacaoAlterada;
    }

    public static void resetDados(){
        likeAlterado = false;
        publicacaoAlterada = null;
    }

    public static Livro getLivro() {
        return livro;
    }

    public static void setLivro(Livro livro) {
        Compartilha.livro = livro;
    }
}
