package com.example.prjversami.models;

public class Genero {
    private Integer generoID;
    private String type, description;

    public int getGeneroID() {
        return generoID;
    }

    public void setGeneroID(int generoID) {
        this.generoID = generoID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
