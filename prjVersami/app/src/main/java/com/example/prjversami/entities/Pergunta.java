package com.example.prjversami.entities;

public class Pergunta {
    private Integer idPergunta;
    private String pergunta;

    public Pergunta(Integer idPergunta, String pergunta) {
        this.idPergunta = idPergunta;
        this.pergunta = pergunta;
    }

    public Pergunta(){

    }

    public Integer getIdPergunta() {
        return idPergunta;
    }

    public void setIdPergunta(Integer idPergunta) {
        this.idPergunta = idPergunta;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    @Override
    public String toString(){
        return getPergunta();
    }
}
