package com.example.prjversami.util;

import com.example.prjversami.entities.Livro;

public class Compartilha {
    private static Livro livro;

    public static Livro getLivro() {
        return livro;
    }

    public static void setLivro(Livro livro) {
        Compartilha.livro = livro;
    }
}
